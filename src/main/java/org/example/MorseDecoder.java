package org.example;

import lombok.extern.slf4j.Slf4j;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <h2>Stratégie de décodage pour le test 'HEY JUDE'</h2>
 * <p>
 * Le défi du message 'HEY JUDE' réside dans l'irrégularité de la transmission humaine.
 * Contrairement à un signal machine, les durées des bits ne sont pas des multiples parfaits.
 * </p>
 * <b>Notre approche repose sur trois piliers :</b>
 * <ul>
 * <li><b>Analyse Statistique :</b> Au lieu de ratios fixes (1:3:7), nous collectons toutes les durées
 * rencontrées dans le message pour identifier les extremums.</li>
 * <li><b>Seuils Dynamiques (Clustering) :</b> Pour les signaux (1), nous calculons le pivot au milieu du
 * point le plus court et du trait le plus long.</li>
 * <li><b>Détection de Sauts (Gap Analysis) :</b> Pour les silences (0), nous cherchons les écarts les plus
 * importants entre les durées triées. Cela permet de séparer mathématiquement les pauses intra-caractère,
 * inter-caractère et inter-mot, même si l'opérateur hésite ou ralentit.</li>
 * </ul>
 */
@Slf4j
public class MorseDecoder {

public static final String REGEX = "(^0+)|(0+$)";

/**
     * Transforme un flux de bits bruts en symboles Morse (· et −).
     * Gère les variations de vitesse en analysant les clusters de durées.
     * * @param bits Chaîne de 0 et de 1
     * @return String de symboles Morse avec espaces (1 entre lettres, 3 entre mots)
     */
    public String decodeBitsAdvanced(String bits) {
        log.info("Début du décodage avancé des bits. Longueur brute : {}", bits.length());
        
        bits = bits.replaceAll(REGEX, "");
        if (bits.isEmpty()) return "";
        
        String[] parts = bits.split("(?<=1)(?=0)|(?<=0)(?=1)");
        
        List<Integer> ones = new ArrayList<>();
        List<Integer> zeros = new ArrayList<>();
        for (String p : parts) {
            if (p.contains("1")) ones.add(p.length());
            else zeros.add(p.length());
        }
        
        // --- 1. CALCUL DU SEUIL POUR LES 1 (POINT vs TRAIT) ---
        // On garde minOne car le bloc 2 en a besoin
        int minOne = ones.stream().min(Integer::compare).orElse(1);
        List<Integer> sortedOnes = ones.stream().distinct().sorted().toList();
        double oneThreshold;
        
        if (sortedOnes.size() > 1) {
            // Détection par le plus grand écart (Gap Analysis)
            int maxGapIdx = 0;
            int maxGap = -1;
            for (int i = 0; i < sortedOnes.size() - 1; i++) {
                int gap = sortedOnes.get(i + 1) - sortedOnes.get(i);
                if (gap > maxGap) {
                    maxGap = gap;
                    maxGapIdx = i;
                }
            }
            oneThreshold = (sortedOnes.get(maxGapIdx) + sortedOnes.get(maxGapIdx + 1)) / 2.0;
        } else {
            // Cas d'une seule durée (ex: 1001 ou 1110111)
            oneThreshold = (minOne < 3) ? minOne + 0.5 : minOne - 0.5;
        }
        log.debug("Analyse des '1' : seuil={}", oneThreshold);
        
        // --- 2. CALCUL DES SEUILS POUR LES 0 (PAUSES) ---
        List<Integer> sortedZeros = zeros.stream().distinct().sorted().toList();
        double zeroThresholdLow;
        double zeroThresholdHigh;
        
        if (sortedZeros.size() < 2) {
            // Utilisation de minOne comme unité de référence (basé sur le standard Morse)
            zeroThresholdLow = minOne * 2.0;
            zeroThresholdHigh = minOne * 6.0;
        } else {
            // Analyse des écarts (Gaps) pour trouver les frontières naturelles
            int maxGapIdx = 0;
            int maxGap = -1;
            for (int i = 0; i < sortedZeros.size() - 1; i++) {
                int gap = sortedZeros.get(i + 1) - sortedZeros.get(i);
                if (gap > maxGap) {
                    maxGap = gap;
                    maxGapIdx = i;
                }
            }
            // Seuil inter-mot (le plus grand saut)
            zeroThresholdHigh = (sortedZeros.get(maxGapIdx) + sortedZeros.get(maxGapIdx + 1)) / 2.0;
            
            int secondMaxGap = -1;
            int secondGapIdx = -1;
            // On cherche le saut pour l'espace inter-caractère sous le seuil du mot
            for (int i = 0; i < maxGapIdx; i++) {
                int gap = sortedZeros.get(i + 1) - sortedZeros.get(i);
                if (gap > secondMaxGap) {
                    secondMaxGap = gap;
                    secondGapIdx = i;
                }
            }
            
            if (secondGapIdx != -1) {
                zeroThresholdLow = (sortedZeros.get(secondGapIdx) + sortedZeros.get(secondGapIdx + 1)) / 2.0;
            } else {
                zeroThresholdLow = (sortedZeros.getFirst() + zeroThresholdHigh) / 2.0;
            }
        }
        
        // Sécurité : Low doit être inférieur à High
        if (zeroThresholdLow >= zeroThresholdHigh) {
            zeroThresholdLow = zeroThresholdHigh / 2.0;
        }
        log.debug("Analyse des '0' : seuil bas={}, seuil haut={}", zeroThresholdLow, zeroThresholdHigh);
        
        // --- 3. CONSTRUCTION DE LA CHAÎNE MORSE ---
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            int len = part.length();
            if (part.charAt(0) == '1') {
                // Utilisation de '.' et '-' standards pour éviter les soucis d'encodage
                sb.append(len > oneThreshold ? "-" : ".");
            } else {
                if (len >= zeroThresholdHigh) {
                    sb.append("   "); // Inter-mot
                } else if (len >= zeroThresholdLow) {
                    sb.append(" ");    // Inter-caractère
                }
            }
        }
        
        String result = sb.toString();
        log.info("Décodage bits terminé : {}", result);
        return result;
    }
    
    /**
     * Traduit une chaîne de symboles Morse en texte ASCII.
     * Utilise la classe utilitaire {@link MorseCode}.
     * * @param morseCode Symboles Morse (·, −)
     * @return Texte en clair (ex: "HEY JUDE")
     */
    public String decodeMorse(String morseCode) {
        if (morseCode == null || morseCode.isEmpty()) {
            return "";
        }
        
        log.debug("Traduction Morse vers Texte...");
        
        // Normalisation Unicode (·, −) -> ASCII (., -) pour le dictionnaire
        String normalized = morseCode.replace('·', '.').replace('−', '-');
        
        // Split par 3 espaces pour isoler les mots
        String decoded = Arrays.stream(normalized.trim().split(" {3}"))
                .map(word -> Arrays.stream(word.split(" ")) // Split par 1 espace pour les lettres
                        .map(MorseCode::get)
                        .collect(Collectors.joining()))
                .collect(Collectors.joining(" "));
        
        log.info("Résultat final de la traduction : {}", decoded);
        return decoded;
    }
}