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
    public static final String SLOW_DASHES = "11111100111111";
    public static final String EMPTY_STRING = "";
    public static final String ZEROS_ONLY = "000000";
    
    public static final String HEY_JUDE_BITS = "0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000";
    public static final String HEY_JUDE_TEXT = "HEY JUDE";
    
    public static final String COMPLEX_BITS = "00000000000111111100000011010001110111000000001110000000000000000001111111011111100001101111100000111100111100011111100000001011100000011111110010001111100110000011111100101111100000000000000111111100001111010110000011000111110010000011111110001111110011111110000010001111110001111111100000001111111101110000000000000010110000111111110111100000111110111110011111110000000011111001011011111000000000000111011111011111011111000000010001001111100000111110111111110000001110011111100011111010000001100001001000000000000000000111111110011111011111100000010001001000011111000000100000000101111101000000000000011111100000011110100001001100000000001110000000000000001101111101111000100000100001111111110000000001111110011111100011101100000111111000011011111000111111000000000000000001111110000100110000011111101111111011111111100000001111110001111100001000000000000000000000000000000000000000000000000000000000000";

    // Expected results
    public static final String EXPECTED_EE = "EE";
    public static final String EXPECTED_M = "M";
    public static final String EXPECTED_E = "E";
    public static final String EXPECTED_EMPTY = "";
    public static final String EXPECTED_S = "S";
    public static final String EXPECTED_O = "O";

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
     * Creates basic test cases matching kata-exec requirements.
     * These are the core test cases from kata-exec:
     * - "1001" decodes to "EE"
     * - "1110111" decodes to "M"
     * - "11111100111111" decodes to "M"
     */
    public static TestCase[] createBasicTestCases() {
        return new TestCase[] {
            new TestCase("Simple dots (EE)", SIMPLE_DOTS, EXPECTED_EE),
            new TestCase("Simple dashes (M)", SIMPLE_DASHES, EXPECTED_M),
            new TestCase("Slow dashes (M)", SLOW_DASHES, EXPECTED_M)
        };
    }

        
}
