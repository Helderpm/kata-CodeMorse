package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for basic Morse decoder functionality.
 * Tests core decoding capabilities with simple patterns.
 */
@DisplayName("Basic Functionality Tests")
class BasicFunctionalityTest {

    @Test
    @DisplayName("Test basic Morse patterns")
    void testBasicPatterns() {
        MorseDecoder decoder = new MorseDecoder();
        
        // Test basic patterns
        TestUtilities.TestCase[] basicCases = TestUtilities.createBasicTestCases();
        
        for (TestUtilities.TestCase testCase : basicCases) {
            String morse = decoder.decodeBitsAdvanced(testCase.input());
            String result = decoder.decodeMorse(morse);
            assertEquals(testCase.expected(), result, 
                "Basic pattern test failed: " + testCase.name());
        }
    }

    @Test
    @DisplayName("Test empty and zero-only inputs")
    void testEmptyAndZeroInputs() {
        MorseDecoder decoder = new MorseDecoder();
        
        // Test empty string
        String morse1 = decoder.decodeBitsAdvanced("");
        String result1 = decoder.decodeMorse(morse1);
        assertEquals("", result1, "Empty string should return empty");
        
        // Test zeros only
        String morse2 = decoder.decodeBitsAdvanced("000000");
        String result2 = decoder.decodeMorse(morse2);
        assertEquals("", result2, "Zeros only should return empty");
    }

    @Test
    @DisplayName("Quick validation of basic functionality from kata-exec")
    void quickValidation() {
        MorseDecoder decoder = new MorseDecoder();
        
        // Test EE from kata-exec: "1001" -> "EE"
        String result1 = decoder.decodeMorse(decoder.decodeBitsAdvanced(TestUtilities.SIMPLE_DOTS));
        assertEquals(TestUtilities.EXPECTED_EE, result1, "Simple dots test should pass");
        
        // Test M from kata-exec: "1110111" -> "M"
        String result2 = decoder.decodeMorse(decoder.decodeBitsAdvanced(TestUtilities.SIMPLE_DASHES));
        assertEquals(TestUtilities.EXPECTED_M, result2, "Simple dashes test should pass");
    }
}
