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
        
        // Nettoyage des zéros initiaux et finaux
        bits = bits.replaceAll(REGEX, "");
        if (bits.isEmpty()) {
            log.warn("Le message reçu est vide ou ne contient que des zéros.");
            return "";
        }
        
        // Découpage en blocs de bits identiques (000, 11, etc.)
        String[] parts = bits.split("(?<=1)(?=0)|(?<=0)(?=1)");
        
        List<Integer> ones = new ArrayList<>();
        List<Integer> zeros = new ArrayList<>();
        for (String p : parts) {
            if (p.contains("1")) ones.add(p.length());
            else zeros.add(p.length());
        }
        
        // --- 1. CALCUL DU SEUIL POUR LES 1 (POINT vs TRAIT) ---
        int minOne = ones.stream().min(Integer::compare).orElse(1);
        int maxOne = ones.stream().max(Integer::compare).orElse(1);
        
        double oneThreshold;
        
        if (minOne == maxOne) {
            // Nous n'avons qu'une seule durée de '1'.
            // Si cette durée est petite (ex: 1 ou 2), c'est très probablement un point.
            // Si elle est grande (ex: >= 3), c'est probablement un trait.
            if (minOne < 3) {
                oneThreshold = minOne + 1; // Le signal sera < au seuil -> Point
            } else {
                oneThreshold = minOne - 1; // Le signal sera > au seuil -> Trait
            }
        } else {
            // Cas normal : on prend la moyenne entre le point le plus court
            // et le trait le plus long pour trouver la frontière.
            oneThreshold = (minOne + maxOne) / 2.0;
        }
        
        // --- 2. CALCUL DES SEUILS POUR LES 0 (PAUSES) ---
        List<Integer> sortedZeros = zeros.stream().distinct().sorted().toList();
        double zeroThresholdLow;
        double zeroThresholdHigh;
        
        if (sortedZeros.size() < 2) {
            // Si l'opérateur est trop régulier ou le message trop court
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
            
            // Seuil inter-caractère (le deuxième plus grand saut)
            int secondGapIdx = -1;
            int secondMaxGap = -1;
            for (int i = 0; i < sortedZeros.size() - 1; i++) {
                if (i == maxGapIdx) continue;
                int gap = sortedZeros.get(i + 1) - sortedZeros.get(i);
                if (gap > secondMaxGap) {
                    secondMaxGap = gap;
                    secondGapIdx = i;
                }
            }
            
            if (secondGapIdx != -1) {
                zeroThresholdLow = (sortedZeros.get(secondGapIdx) + sortedZeros.get(secondGapIdx + 1)) / 2.0;
            } else {
                zeroThresholdLow = zeroThresholdHigh / 2.0;
            }
        }
        log.debug("Analyse des '0' : seuil bas={}, seuil haut={}", zeroThresholdLow, zeroThresholdHigh);
        
        // --- 3. CONSTRUCTION DE LA CHAÎNE MORSE ---
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            int len = part.length();
            if (part.charAt(0) == '1') {
                sb.append(len > oneThreshold ? "−" : "·");
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
        String decoded = Arrays.stream(normalized.trim().split("   "))
                .map(word -> Arrays.stream(word.split(" ")) // Split par 1 espace pour les lettres
                        .map(MorseCode::get)
                        .collect(Collectors.joining()))
                .collect(Collectors.joining(" "));
        
        log.info("Résultat final de la traduction : {}", decoded);
        return decoded;
    }
}