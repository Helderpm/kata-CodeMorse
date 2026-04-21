package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Main test suite for the Morse decoder using JUnit 5.
 * Organizes tests in a nested structure for better organization.
 */
@DisplayName("Morse Decoder Test Suite")
class MorseDecoderTest {

    @Test
    @DisplayName("Run complete test suite")
    void testCompleteSuite() {
        // This test serves as a validation that all test classes can be instantiated
        // and basic functionality works
        assertDoesNotThrow(() -> {
            new MorseDecoder();
            MorseDecoderConfig.defaultConfig();
        });
        
        // Test basic functionality
        MorseDecoder decoder = new MorseDecoder();
        String result = decoder.decodeMorse(decoder.decodeBitsAdvanced(TestUtilities.SIMPLE_DOTS));
        assertEquals(TestUtilities.EXPECTED_EE, result, "Basic EE test should pass");
    }

}
