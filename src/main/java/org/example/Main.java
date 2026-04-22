package org.example;

import java.util.Scanner;

public class Main {

    private static final String MENU = """

            === Morse Decoder ===
            1. Decode bit string  (e.g. 0000000011011010011100...)
            2. Decode Morse code  (e.g. .... . -.--   .--- ..- -.. .)
            3. Exit
            Choice:\s""";

    private static final MorseDecoder DECODER = new MorseDecoder(
        new MorseDecoderConfig.Builder().enableLogging(false).build()
    );

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print(MENU);
                switch (scanner.nextLine().trim()) {
                    case "1" -> handleBits(scanner);
                    case "2" -> handleMorse(scanner);
                    case "3" -> { System.out.println("Goodbye!"); return; }
                    default  -> System.out.println("Invalid choice. Please enter 1, 2 or 3.");
                }
            }
        }
    }

    private static void handleBits(Scanner scanner) {
        System.out.print("Enter bit string: ");
        var bits = scanner.nextLine().trim();
        if (bits.isBlank()) { System.out.println("Input is empty."); return; }
        try {
            var morse = DECODER.decodeBitsAdvanced(bits);
            var text  = DECODER.decodeMorse(morse);
            System.out.println("Morse : %s".formatted(morse.isBlank() ? "(empty)" : morse));
            System.out.println("Text  : %s".formatted(text.isBlank()  ? "(empty)" : text));
        } catch (IllegalArgumentException e) {
            System.out.println("Error : %s".formatted(e.getMessage()));
        }
    }

    private static void handleMorse(Scanner scanner) {
        System.out.print("Enter Morse code: ");
        var morse = scanner.nextLine().trim();
        if (morse.isBlank()) { System.out.println("Input is empty."); return; }
        var text = DECODER.decodeMorse(morse);
        System.out.println("Text  : %s".formatted(text.isBlank() ? "(empty)" : text));
    }
}
