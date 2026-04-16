package org.example;


import java.util.HashMap;
import java.util.Map;

/**
 * Classe utilitaire pour la traduction du code Morse vers l'alphabet ASCII.
 */
public class MorseCode {

    private MorseCode() {
        // This utility class should not be instantiated
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final Map<String, String> DICTIONARY = new HashMap<>();
    
    static {
        // Lettres
        DICTIONARY.put(".-", "A");
        DICTIONARY.put("-...", "B");
        DICTIONARY.put("-.-.", "C");
        DICTIONARY.put("-..", "D");
        DICTIONARY.put(".", "E");
        DICTIONARY.put("..-.", "F");
        DICTIONARY.put("--.", "G");
        DICTIONARY.put("....", "H");
        DICTIONARY.put("..", "I");
        DICTIONARY.put(".---", "J");
        DICTIONARY.put("-.-", "K");
        DICTIONARY.put(".-..", "L");
        DICTIONARY.put("--", "M");
        DICTIONARY.put("-.", "N");
        DICTIONARY.put("---", "O");
        DICTIONARY.put(".--.", "P");
        DICTIONARY.put("--.-", "Q");
        DICTIONARY.put(".-.", "R");
        DICTIONARY.put("...", "S");
        DICTIONARY.put("-", "T");
        DICTIONARY.put("..-", "U");
        DICTIONARY.put("...-", "V");
        DICTIONARY.put(".--", "W");
        DICTIONARY.put("-..-", "X");
        DICTIONARY.put("-.--", "Y");
        DICTIONARY.put("--..", "Z");
        
//        // Chiffres
//        DICTIONARY.put("-----", "0");
//        DICTIONARY.put(".----", "1");
//        DICTIONARY.put("..---", "2");
//        DICTIONARY.put("...--", "3");
//        DICTIONARY.put("....-", "4");
//        DICTIONARY.put(".....", "5");
//        DICTIONARY.put("-....", "6");
//        DICTIONARY.put("--...", "7");
//        DICTIONARY.put("---..", "8");
//        DICTIONARY.put("----.", "9");
//
//        // Ponctuation & Spécial
//        DICTIONARY.put(".-.-.-", ".");
//        DICTIONARY.put("--..--", ",");
//        DICTIONARY.put("---...", ":");
//        DICTIONARY.put("..--..", "?");
//        DICTIONARY.put(".----.", "'");
//        DICTIONARY.put("-....-", "-");
//        DICTIONARY.put("-..-.", "/");
//        DICTIONARY.put(".-..-.", "\"");
//        DICTIONARY.put(".--.-.", "@");
//        DICTIONARY.put("-...-", "=");
//        DICTIONARY.put("...---...", "SOS");
    }
    
    /**
     * Récupère le caractère correspondant au code Morse fourni.
     * @param code Le code Morse (ex: ".-")
     * @return La lettre ASCII ou une chaîne vide si non trouvé.
     */
    public static String get(String code) {
        return DICTIONARY.getOrDefault(code, "");
    }
}
