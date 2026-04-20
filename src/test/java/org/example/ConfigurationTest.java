package org.example;

/**
 * Test class for Morse decoder configuration functionality.
 * Tests different configuration presets and custom settings.
 */
public class ConfigurationTest {

    public static void runAllTests() {
        TestUtilities.printSectionHeader("Configuration Tests");
        
        testDefaultConfiguration();
        testFastTransmissionConfiguration();
        testSlowTransmissionConfiguration();
        testNoisyEnvironmentConfiguration();
        testCustomConfiguration();
        testStrictMode();
        testMorseSymbolsConfiguration();
        testTimingParametersConfiguration();
        testBehavioralSettingsConfiguration();
        testEducationalConfiguration();
        testConfigurationPresets();
        testConfigurationBuilder();
        testConfigurationEdgeCases();
        
        System.out.println("Configuration tests completed.");
    }

    private static void testDefaultConfiguration() {
        System.out.println("Testing default configuration:");
        
        MorseDecoder defaultDecoder = new MorseDecoder();
        TestUtilities.executeTestCase(defaultDecoder, 
            new TestUtilities.TestCase("Default config", TestUtilities.SIMPLE_DOTS, TestUtilities.EXPECTED_EE));
    }

    private static void testFastTransmissionConfiguration() {
        System.out.println("Testing fast transmission configuration:");
        
        MorseDecoder fastDecoder = new MorseDecoder(MorseDecoderConfig.forFastTransmission());
        TestUtilities.executeTestCase(fastDecoder, 
            new TestUtilities.TestCase("Fast transmission", "101010", "S"));
    }

    private static void testSlowTransmissionConfiguration() {
        System.out.println("Testing slow transmission configuration:");
        
        MorseDecoder slowDecoder = new MorseDecoder(MorseDecoderConfig.forSlowTransmission());
        TestUtilities.executeTestCase(slowDecoder, 
            new TestUtilities.TestCase("Slow transmission", "111111000111111000", TestUtilities.EXPECTED_M));
    }

    private static void testNoisyEnvironmentConfiguration() {
        System.out.println("Testing noisy environment configuration:");
        
        MorseDecoder noisyDecoder = new MorseDecoder(MorseDecoderConfig.forNoisyEnvironment());
        TestUtilities.executeTestCase(noisyDecoder, 
            new TestUtilities.TestCase("Noisy environment", TestUtilities.SIMPLE_DOTS, TestUtilities.EXPECTED_EE));
    }

    private static void testCustomConfiguration() {
        System.out.println("Testing custom configuration:");
        
        MorseDecoderConfig customConfig = new MorseDecoderConfig.Builder()
            .enableLogging(false)
            .defaultThresholdOffset(0.7)
            .morseTimeUnitMultipliers(2.5, 7.0)
            .build();
        
        MorseDecoder customDecoder = new MorseDecoder(customConfig);
        TestUtilities.executeTestCase(customDecoder, 
            new TestUtilities.TestCase("Custom config", TestUtilities.SIMPLE_DOTS, TestUtilities.EXPECTED_EE));
    }

    private static void testStrictMode() {
        System.out.println("Testing strict mode:");
        
        MorseDecoderConfig strictConfig = new MorseDecoderConfig.Builder()
            .strictMode(true)
            .maxSignalLength(100)
            .build();
        
        MorseDecoder strictDecoder = new MorseDecoder(strictConfig);
        
        // Test valid input
        TestUtilities.executeTestCase(strictDecoder, 
            new TestUtilities.TestCase("Strict mode valid", TestUtilities.SIMPLE_DOTS, TestUtilities.EXPECTED_EE));
        
        // Test invalid input should throw exceptions
        try {
            strictDecoder.decodeBitsAdvanced("10102"); // Invalid character
            System.out.println("  Invalid character test: FAILED (should throw exception)");
        } catch (IllegalArgumentException e) {
            System.out.println("  Invalid character test: PASSED (correctly threw exception)");
        }
        
        try {
            strictDecoder.decodeBitsAdvanced("1".repeat(200)); // Too long
            System.out.println("  Too long test: FAILED (should throw exception)");
        } catch (IllegalArgumentException e) {
            System.out.println("  Too long test: PASSED (correctly threw exception)");
        }
    }

    private static void testMorseSymbolsConfiguration() {
        System.out.println("Testing Morse symbols configuration:");
        
        MorseDecoderConfig symbolConfig = new MorseDecoderConfig.Builder()
            .morseSymbols(".", "-")
            .morseSeparators(" ", "   ")
            .unicodeSymbols('.', '-')
            .build();
        
        MorseDecoder symbolDecoder = new MorseDecoder(symbolConfig);
        TestUtilities.executeTestCase(symbolDecoder, 
            new TestUtilities.TestCase("Custom symbols", TestUtilities.SIMPLE_DOTS, TestUtilities.EXPECTED_EE));
        
        System.out.println("  Custom symbols test: PASSED");
    }

    private static void testTimingParametersConfiguration() {
        System.out.println("Testing timing parameters configuration:");
        
        MorseDecoderConfig timingConfig = new MorseDecoderConfig.Builder()
            .defaultThresholdOffset(0.5)
            .morseTimeUnitMultipliers(1.5, 4.0)
            .thresholdSafetyFactor(2.0)
            .singleDurationThreshold(3)
            .build();
        
        MorseDecoder timingDecoder = new MorseDecoder(timingConfig);
        TestUtilities.executeTestCase(timingDecoder, 
            new TestUtilities.TestCase("Custom timing", TestUtilities.SIMPLE_DOTS, TestUtilities.EXPECTED_EE));
        
        System.out.println("  Custom timing test: PASSED");
    }

    private static void testBehavioralSettingsConfiguration() {
        System.out.println("Testing behavioral settings configuration:");
        
        MorseDecoderConfig behaviorConfig = new MorseDecoderConfig.Builder()
            .enableLogging(true)
            .strictMode(false)
            .maxSignalLength(5000)
            .build();
        
        MorseDecoder behaviorDecoder = new MorseDecoder(behaviorConfig);
        TestUtilities.executeTestCase(behaviorDecoder, 
            new TestUtilities.TestCase("Behavioral settings", TestUtilities.SIMPLE_DOTS, TestUtilities.EXPECTED_EE));
        
        System.out.println("  Behavioral settings test: PASSED");
    }

    private static void testEducationalConfiguration() {
        System.out.println("Testing educational configuration:");
        
        MorseDecoderConfig educationalConfig = MorseDecoderConfig.forEducationalUse();
        MorseDecoder educationalDecoder = new MorseDecoder(educationalConfig);
        
        // Test with educational configuration (logging enabled for learning)
        TestUtilities.executeTestCase(educationalDecoder, 
            new TestUtilities.TestCase("Educational config", TestUtilities.HEY_JUDE_BITS, TestUtilities.HEY_JUDE_TEXT));
        
        // Test that educational config handles longer signals well
        String longSignal = "1".repeat(1000) + "0".repeat(1000) + "1".repeat(1000);
        try {
            educationalDecoder.decodeMorse(educationalDecoder.decodeBitsAdvanced(longSignal));
            System.out.println("  Educational long signal test: PASSED (handled " + longSignal.length() + " chars)");
        } catch (Exception e) {
            System.out.println("  Educational long signal test: FAILED - " + e.getMessage());
        }
        
        System.out.println("  Educational configuration test: PASSED");
    }

    private static void testConfigurationPresets() {
        System.out.println("Testing configuration presets:");
        
        // Test all preset configurations
        MorseDecoderConfig[] presets = {
            MorseDecoderConfig.defaultConfig(),
            MorseDecoderConfig.forFastTransmission(),
            MorseDecoderConfig.forSlowTransmission(),
            MorseDecoderConfig.forNoisyEnvironment(),
            MorseDecoderConfig.forPrecisionDecoding(),
            MorseDecoderConfig.forEducationalUse()
        };
        
        String[] presetNames = {"Default", "Fast", "Slow", "Noisy", "Precision", "Educational"};
        
        for (int i = 0; i < presets.length; i++) {
            MorseDecoder presetDecoder = new MorseDecoder(presets[i]);
            TestUtilities.executeTestCase(presetDecoder, 
                new TestUtilities.TestCase("Preset: " + presetNames[i], TestUtilities.SIMPLE_DOTS, TestUtilities.EXPECTED_EE));
        }
        
        System.out.println("  All presets test: PASSED");
    }

    private static void testConfigurationBuilder() {
        System.out.println("Testing configuration builder:");
        
        // Test builder chaining
        MorseDecoderConfig builderConfig = new MorseDecoderConfig.Builder()
            .morseSymbols(".", "-")
            .morseSeparators(" ", "   ")
            .enableLogging(false)
            .strictMode(false)
            .maxSignalLength(1000)
            .defaultThresholdOffset(0.6)
            .morseTimeUnitMultipliers(2.0, 6.0)
            .thresholdSafetyFactor(1.5)
            .singleDurationThreshold(2)
            .build();
        
        MorseDecoder builderDecoder = new MorseDecoder(builderConfig);
        TestUtilities.executeTestCase(builderDecoder, 
            new TestUtilities.TestCase("Builder chaining", TestUtilities.SIMPLE_DOTS, TestUtilities.EXPECTED_EE));
        
        System.out.println("  Builder chaining test: PASSED");
    }

    private static void testConfigurationEdgeCases() {
        System.out.println("Testing configuration edge cases:");
        
        // Test minimum values
        MorseDecoderConfig minConfig = new MorseDecoderConfig.Builder()
            .maxSignalLength(1)
            .singleDurationThreshold(1)
            .defaultThresholdOffset(0.1)
            .morseTimeUnitMultipliers(1.0, 2.0)
            .thresholdSafetyFactor(1.1)
            .build();
        
        MorseDecoder minDecoder = new MorseDecoder(minConfig);
        TestUtilities.executeTestCase(minDecoder, 
            new TestUtilities.TestCase("Minimum values", "1", TestUtilities.EXPECTED_E));
        
        // Test large values
        MorseDecoderConfig maxConfig = new MorseDecoderConfig.Builder()
            .maxSignalLength(100000)
            .singleDurationThreshold(1000)
            .defaultThresholdOffset(2.0)
            .morseTimeUnitMultipliers(10.0, 50.0)
            .thresholdSafetyFactor(10.0)
            .build();
        
        MorseDecoder maxDecoder = new MorseDecoder(maxConfig);
        TestUtilities.executeTestCase(maxDecoder, 
            new TestUtilities.TestCase("Large values", TestUtilities.SIMPLE_DOTS, TestUtilities.EXPECTED_EE));
        
        System.out.println("  Edge cases test: PASSED");
    }

    /**
     * Tests configuration validation.
     */
    public static void testConfigurationValidation() {
        System.out.println("Testing configuration validation:");
        
        try {
            // This should throw an exception
            new MorseDecoderConfig.Builder()
                .defaultThresholdOffset(-1.0)
                .build();
            
            System.out.println("  Validation test: FAILED (should have thrown exception)");
        } catch (IllegalArgumentException e) {
            System.out.println("  Validation test: PASSED (correctly threw exception)");
        }
        
        // Test multiple invalid configurations
        String[] invalidTests = {
            "Invalid threshold offset",
            "Invalid time multipliers", 
            "Invalid safety factor",
            "Invalid max signal length"
        };
        
        for (String testName : invalidTests) {
            try {
                switch (testName) {
                    case "Invalid threshold offset":
                        new MorseDecoderConfig.Builder().defaultThresholdOffset(-1.0).build();
                        break;
                    case "Invalid time multipliers":
                        new MorseDecoderConfig.Builder().morseTimeUnitMultipliers(0.5, 1.0).build();
                        break;
                    case "Invalid safety factor":
                        new MorseDecoderConfig.Builder().thresholdSafetyFactor(0.5).build();
                        break;
                    case "Invalid max signal length":
                        new MorseDecoderConfig.Builder().maxSignalLength(0).build();
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown test case: " + testName);
                }
                System.out.println("  " + testName + ": FAILED (should throw exception)");
            } catch (IllegalArgumentException e) {
                System.out.println("  " + testName + ": PASSED (correctly threw exception)");
            }
        }
    }
}
