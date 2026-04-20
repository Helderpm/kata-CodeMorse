package org.example;

import java.util.Random;

/**
 * Utility class for Morse decoder tests.
 * Provides common test data, helper methods, and signal generation.
 */
public class TestUtilities {

    private static final Random RANDOM = new Random(42); // Fixed seed for reproducible tests

    // Test data constants
    public static final String SIMPLE_DOTS = "1001";
    public static final String SIMPLE_DASHES = "1110111";
    public static final String REPEATED_DOTS = "101010";
    public static final String SLOW_DASHES = "11111100111111";
    public static final String SINGLE_BIT = "1";
    public static final String SINGLE_ZERO = "0";
    public static final String EMPTY_STRING = "";
    public static final String ZEROS_ONLY = "000000";
    
    public static final String HEY_JUDE_BITS = "0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000";
    public static final String HEY_JUDE_TEXT = "HEY JUDE";
    
    public static final String COMPLEX_BITS = "00000000000111111100000011010001110111000000001110000000000000000001111111011111100001101111100000111100111100011111100000001011100000011111110010001111100110000011111100101111100000000000000111111100001111010110000011000111110010000011111110001111110011111110000010001111110001111111100000001111111101110000000000000010110000111111110111100000111110111110011111110000000011111001011011111000000000000111011111011111011111000000010001001111100000111110111111110000001110011111100011111010000001100001001000000000000000000111111110011111011111100000010001001000011111000000100000000101111101000000000000011111100000011110100001001100000000001110000000000000001101111101111000100000100001111111110000000001111110011111100011101100000111111000011011111000111111000000000000000001111110000100110000011111101111111011111111100000001111110001111100001000000000000000000000000000000000000000000000000000000000000";

    // Expected results
    public static final String EXPECTED_EE = "EE";
    public static final String EXPECTED_M = "M";
    public static final String EXPECTED_EEE = "EEE";
    public static final String EXPECTED_E = "E";
    public static final String EXPECTED_EMPTY = "";

    /**
     * Test case data structure.
     */
    public record TestCase(String name, String input, String expected) {}

    /**
     * Generates a random binary signal for testing.
     */
    public static String generateRandomSignal(int length) {
        StringBuilder sb = new StringBuilder(length);
        
        // Add some leading zeros
        int leadingZeros = RANDOM.nextInt(10);
        sb.append("0".repeat(leadingZeros));
        
        // Generate random signal pattern
        boolean lastBit = false;
        for (int i = leadingZeros; i < length - 10; i++) {
            if (RANDOM.nextDouble() < 0.3 || (i > leadingZeros + 5 && lastBit && sb.charAt(i-1) == '1')) {
                sb.append(lastBit ? '0' : '1');
                lastBit = !lastBit;
            } else {
                char bit = RANDOM.nextBoolean() ? '1' : '0';
                sb.append(bit);
                lastBit = bit == '1';
            }
        }
        
        // Add trailing zeros
        while (sb.length() < length) {
            sb.append('0');
        }
        
        return sb.toString();
    }

    /**
     * Executes a test case and prints the result.
     */
    public static void executeTestCase(MorseDecoder decoder, TestCase testCase) {
        try {
            String morse = decoder.decodeBitsAdvanced(testCase.input());
            String result = decoder.decodeMorse(morse);
            
            boolean passed = testCase.expected().equals(result);
            System.out.printf("  %-20s: %s %s%n", testCase.name(), result, passed ? "✓" : "✗");
            
            if (!passed) {
                System.out.printf("    Expected: %s, Got: %s%n", testCase.expected(), result);
                System.out.printf("    Morse: %s%n", morse);
            }
        } catch (Exception e) {
            System.out.printf("  %-20s: ERROR %s%n", testCase.name(), e.getMessage());
        }
    }

    /**
     * Creates basic test cases.
     */
    public static TestCase[] createBasicTestCases() {
        return new TestCase[] {
            new TestCase("Simple dots", SIMPLE_DOTS, EXPECTED_EE),
            new TestCase("Simple dashes", SIMPLE_DASHES, EXPECTED_M),
            new TestCase("Repeated dots", REPEATED_DOTS, EXPECTED_EEE),
            new TestCase("Slow dashes", SLOW_DASHES, EXPECTED_M),
            new TestCase("Single bit", SINGLE_BIT, EXPECTED_E),
            new TestCase("Single zero", SINGLE_ZERO, EXPECTED_EMPTY)
        };
    }

    /**
     * Measures execution time of a runnable task.
     */
    public static long measureExecutionTime(Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        return System.nanoTime() - startTime;
    }

    /**
     * Formats nanoseconds to milliseconds.
     */
    public static double formatNanosToMillis(long nanos) {
        return nanos / 1_000_000.0;
    }

    /**
     * Prints a test section header.
     */
    public static void printSectionHeader(String sectionName) {
        System.out.println("\n" + sectionName);
        System.out.println("=".repeat(sectionName.length()));
    }
    
}
