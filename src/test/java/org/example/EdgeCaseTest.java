package org.example;

/**
 * Test class for Morse decoder edge cases and error conditions.
 * Tests boundary conditions and invalid inputs.
 */
public class EdgeCaseTest {

    public static void runAllTests() {
        TestUtilities.printSectionHeader("Edge Case Tests");
        
        testEmptyInputs();
        testNullInputs();
        testSingleBits();
        testInvalidPatterns();
        testBoundaryConditions();
        
        System.out.println("Edge case tests completed.");
    }

    private static void testEmptyInputs() {
        System.out.println("Testing empty inputs:");
        
        MorseDecoder decoder = new MorseDecoder();
        
        // Test empty bits
        String emptyBitsResult = decoder.decodeBitsAdvanced(TestUtilities.EMPTY_STRING);
        boolean emptyBitsPassed = emptyBitsResult.isEmpty();
        System.out.printf("  Empty bits: '%s' %s%n", emptyBitsResult, emptyBitsPassed ? "✓" : "✗");
        
        // Test empty morse
        String emptyMorseResult = decoder.decodeMorse(TestUtilities.EMPTY_STRING);
        boolean emptyMorsePassed = emptyMorseResult.isEmpty();
        System.out.printf("  Empty morse: '%s' %s%n", emptyMorseResult, emptyMorsePassed ? "✓" : "✗");
        
        // Test spaces only
        String spacesResult = decoder.decodeMorse("   ");
        boolean spacesPassed = spacesResult.isEmpty();
        System.out.printf("  Spaces only: '%s' %s%n", spacesResult, spacesPassed ? "✓" : "✗");
    }

    private static void testNullInputs() {
        System.out.println("Testing null inputs:");
        
        MorseDecoder decoder = new MorseDecoder();
        
        try {
            String nullResult = decoder.decodeMorse(null);
            boolean nullPassed = nullResult.isEmpty();
            System.out.printf("  Null input: '%s' %s%n", nullResult, nullPassed ? "✓" : "✗");
        } catch (Exception e) {
            System.out.printf("  Null input: ERROR %s ✗%n", e.getMessage());
        }
        
        try {
            String nullBitsResult = decoder.decodeBitsAdvanced(null);
            System.out.printf("  Null bits: '%s' %s%n", nullBitsResult, "✗"); // Should not reach here
        } catch (Exception e) {
            System.out.printf("  Null bits: Handled gracefully ✓%n");
        }
    }

    private static void testSingleBits() {
        System.out.println("Testing single bits:");
        
        MorseDecoder decoder = new MorseDecoder();
        
        TestUtilities.TestCase[] singleBitCases = {
            new TestUtilities.TestCase("Single bit", TestUtilities.SINGLE_BIT, TestUtilities.EXPECTED_E),
            new TestUtilities.TestCase("Single zero", TestUtilities.SINGLE_ZERO, TestUtilities.EXPECTED_EMPTY)
        };
        
        for (TestUtilities.TestCase testCase : singleBitCases) {
            TestUtilities.executeTestCase(decoder, testCase);
        }
    }

    private static void testInvalidPatterns() {
        System.out.println("Testing invalid patterns:");
        
        MorseDecoder decoder = new MorseDecoder();
        
        // Test zeros only
        TestUtilities.executeTestCase(decoder, 
            new TestUtilities.TestCase("Zeros only", TestUtilities.ZEROS_ONLY, TestUtilities.EXPECTED_EMPTY));
        
        // Test very short patterns
        TestUtilities.executeTestCase(decoder, 
            new TestUtilities.TestCase("Very short", "10", "EE"));
        
        // Test alternating pattern
        TestUtilities.executeTestCase(decoder, 
            new TestUtilities.TestCase("Alternating", "101", "IE"));
    }

    private static void testBoundaryConditions() {
        System.out.println("Testing boundary conditions:");
        
        MorseDecoder decoder = new MorseDecoder();
        
        // Test very long string of zeros
        String longZeros = "0".repeat(1000);
        String longZerosResult = decoder.decodeBitsAdvanced(longZeros);
        System.out.printf("  Long zeros (1000): '%s' %s%n", longZerosResult, 
            longZerosResult.isEmpty() ? "✓" : "✗");
        
        // Test very short valid signal
        TestUtilities.executeTestCase(decoder, 
            new TestUtilities.TestCase("Minimal signal", "1", TestUtilities.EXPECTED_E));
        
        // Test signal with only leading/trailing zeros
        TestUtilities.executeTestCase(decoder, 
            new TestUtilities.TestCase("Only padding", "000111000", "E"));
    }
    
}
