package org.example;

/**
 * Test class for basic Morse decoder functionality.
 * Tests core decoding capabilities with simple patterns.
 */
public class BasicFunctionalityTest {

    public static void runAllTests() {
        TestUtilities.printSectionHeader("Basic Functionality Tests");
        
        MorseDecoder decoder = new MorseDecoder();
        
        // Test basic patterns
        TestUtilities.TestCase[] basicCases = TestUtilities.createBasicTestCases();
        
        System.out.println("Testing basic Morse patterns:");
        for (TestUtilities.TestCase testCase : basicCases) {
            TestUtilities.executeTestCase(decoder, testCase);
        }
        
        // Additional specific tests
        testSpecificPatterns(decoder);
        
        System.out.println("Basic functionality tests completed.");
    }

    private static void testSpecificPatterns(MorseDecoder decoder) {
        System.out.println("\nTesting specific patterns:");
        
        // Test letter by letter
        String[] letterTests = {
            "1",              // E
            "111",            // M  
            "101",            // I
            "110011",         // K
            "1011"            // L
        };
        
        String[] expectedLetters = {"E", "M", "I", "K", "L"};
        
        for (int i = 0; i < letterTests.length; i++) {
            TestUtilities.TestCase testCase = new TestUtilities.TestCase(
                "Letter " + expectedLetters[i], letterTests[i], expectedLetters[i]
            );
            TestUtilities.executeTestCase(decoder, testCase);
        }
    }

    /**
     * Quick validation of basic functionality.
     */
    public static boolean quickValidation() {
        MorseDecoder decoder = new MorseDecoder();
        
        try {
            String result1 = decoder.decodeMorse(decoder.decodeBitsAdvanced(TestUtilities.SIMPLE_DOTS));
            String result2 = decoder.decodeMorse(decoder.decodeBitsAdvanced(TestUtilities.SIMPLE_DASHES));
            
            return TestUtilities.EXPECTED_EE.equals(result1) && TestUtilities.EXPECTED_M.equals(result2);
        } catch (Exception e) {
            return false;
        }
    }
}
