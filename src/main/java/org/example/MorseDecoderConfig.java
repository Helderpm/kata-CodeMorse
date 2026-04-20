package org.example;

/**
 * Configuration class for Morse decoder parameters.
 * Allows customization of timing thresholds, separators, and decoding behavior.
 */
public class MorseDecoderConfig {
    
    // Timing parameters
    private final double defaultThresholdOffset;
    private final double morseTimeUnitMultiplierLow;
    private final double morseTimeUnitMultiplierHigh;
    private final double thresholdSafetyFactor;
    private final int singleDurationThreshold;
    
    // Morse symbols and separators
    private final String morseDot;
    private final String morseDash;
    private final String morseWordSeparator;
    private final String morseLetterSeparator;
    private final char unicodeDot;
    private final char unicodeDash;
    
    // Behavioral settings
    private final boolean enableLogging;
    private final boolean strictMode;
    private final int maxSignalLength;
    
    private MorseDecoderConfig(Builder builder) {
        this.defaultThresholdOffset = builder.defaultThresholdOffset;
        this.morseTimeUnitMultiplierLow = builder.morseTimeUnitMultiplierLow;
        this.morseTimeUnitMultiplierHigh = builder.morseTimeUnitMultiplierHigh;
        this.thresholdSafetyFactor = builder.thresholdSafetyFactor;
        this.singleDurationThreshold = builder.singleDurationThreshold;
        
        this.morseDot = builder.morseDot;
        this.morseDash = builder.morseDash;
        this.morseWordSeparator = builder.morseWordSeparator;
        this.morseLetterSeparator = builder.morseLetterSeparator;
        this.unicodeDot = builder.unicodeDot;
        this.unicodeDash = builder.unicodeDash;
        
        this.enableLogging = builder.enableLogging;
        this.strictMode = builder.strictMode;
        this.maxSignalLength = builder.maxSignalLength;
    }

    // Getters
    public double getDefaultThresholdOffset() { return defaultThresholdOffset; }
    public double getMorseTimeUnitMultiplierLow() { return morseTimeUnitMultiplierLow; }
    public double getMorseTimeUnitMultiplierHigh() { return morseTimeUnitMultiplierHigh; }
    public double getThresholdSafetyFactor() { return thresholdSafetyFactor; }
    public int getSingleDurationThreshold() { return singleDurationThreshold; }
    
    public String getMorseDot() { return morseDot; }
    public String getMorseDash() { return morseDash; }
    public String getMorseWordSeparator() { return morseWordSeparator; }
    public String getMorseLetterSeparator() { return morseLetterSeparator; }
    public char getUnicodeDot() { return unicodeDot; }
    public char getUnicodeDash() { return unicodeDash; }
    
    public boolean isEnableLogging() { return enableLogging; }
    public boolean isStrictMode() { return strictMode; }
    public int getMaxSignalLength() { return maxSignalLength; }

    /**
     * Creates a default configuration.
     */
    public static MorseDecoderConfig defaultConfig() {
        return new Builder().build();
    }

    /**
     * Creates a new builder for configuration.
     */
    public static class Builder {
        // Default values
        private double defaultThresholdOffset = 0.5;
        private double morseTimeUnitMultiplierLow = 2.0;
        private double morseTimeUnitMultiplierHigh = 6.0;
        private double thresholdSafetyFactor = 2.0;
        private int singleDurationThreshold = 3;
        
        private String morseDot = ".";
        private String morseDash = "-";
        private String morseWordSeparator = "   ";
        private String morseLetterSeparator = " ";
        private char unicodeDot = '·';
        private char unicodeDash = '−';
        
        private boolean enableLogging = true;
        private boolean strictMode = false;
        private int maxSignalLength = 1000000;

        // Empty constructor is intentional - default values are set at field declaration level
        public Builder() {}

        // Timing configuration
        public Builder defaultThresholdOffset(double value) {
            this.defaultThresholdOffset = value;
            return this;
        }

        public Builder morseTimeUnitMultipliers(double low, double high) {
            this.morseTimeUnitMultiplierLow = low;
            this.morseTimeUnitMultiplierHigh = high;
            return this;
        }

        public Builder thresholdSafetyFactor(double value) {
            this.thresholdSafetyFactor = value;
            return this;
        }

        public Builder singleDurationThreshold(int value) {
            this.singleDurationThreshold = value;
            return this;
        }

        // Symbol configuration
        public Builder morseSymbols(String dot, String dash) {
            this.morseDot = dot;
            this.morseDash = dash;
            return this;
        }

        public Builder morseSeparators(String letterSeparator, String wordSeparator) {
            this.morseLetterSeparator = letterSeparator;
            this.morseWordSeparator = wordSeparator;
            return this;
        }

        public Builder unicodeSymbols(char dot, char dash) {
            this.unicodeDot = dot;
            this.unicodeDash = dash;
            return this;
        }

        // Behavioral configuration
        public Builder enableLogging(boolean value) {
            this.enableLogging = value;
            return this;
        }

        public Builder strictMode(boolean value) {
            this.strictMode = value;
            return this;
        }

        public Builder maxSignalLength(int value) {
            this.maxSignalLength = value;
            return this;
        }

        public MorseDecoderConfig build() {
            validateConfiguration();
            return new MorseDecoderConfig(this);
        }

        private void validateConfiguration() {
            if (defaultThresholdOffset <= 0) {
                throw new IllegalArgumentException("Default threshold offset must be positive");
            }
            if (morseTimeUnitMultiplierLow >= morseTimeUnitMultiplierHigh) {
                throw new IllegalArgumentException("Low multiplier must be less than high multiplier");
            }
            if (thresholdSafetyFactor <= 1.0) {
                throw new IllegalArgumentException("Threshold safety factor must be greater than 1.0");
            }
            if (singleDurationThreshold <= 0) {
                throw new IllegalArgumentException("Single duration threshold must be positive");
            }
            if (maxSignalLength <= 0) {
                throw new IllegalArgumentException("Max signal length must be positive");
            }
            if (morseDot == null || morseDash == null || 
                morseWordSeparator == null || morseLetterSeparator == null) {
                throw new IllegalArgumentException("Morse symbols and separators cannot be null");
            }
        }
    }

    /**
     * Creates a configuration optimized for fast transmission.
     */
    public static MorseDecoderConfig forFastTransmission() {
        return new Builder()
            .defaultThresholdOffset(0.3)
            .morseTimeUnitMultipliers(1.5, 4.0)
            .singleDurationThreshold(2)
            .build();
    }

    /**
     * Creates a configuration optimized for slow transmission.
     */
    public static MorseDecoderConfig forSlowTransmission() {
        return new Builder()
            .defaultThresholdOffset(1.0)
            .morseTimeUnitMultipliers(3.0, 8.0)
            .singleDurationThreshold(5)
            .build();
    }

    /**
     * Creates a configuration for noisy environments.
     */
    public static MorseDecoderConfig forNoisyEnvironment() {
        return new Builder()
            .defaultThresholdOffset(0.8)
            .morseTimeUnitMultipliers(2.5, 7.0)
            .thresholdSafetyFactor(3.0)
            .strictMode(false)
            .build();
    }

    /**
     * Creates a configuration for precision decoding.
     */
    public static MorseDecoderConfig forPrecisionDecoding() {
        return new Builder()
            .defaultThresholdOffset(0.2)
            .morseTimeUnitMultipliers(2.2, 6.2)
            .thresholdSafetyFactor(1.5)
            .strictMode(true)
            .build();
    }

    /**
     * Creates a configuration for educational purposes.
     */
    public static MorseDecoderConfig forEducationalUse() {
        return new Builder()
            .enableLogging(true)
            .strictMode(false)
            .maxSignalLength(10000)
            .build();
    }

    @Override
    public String toString() {
        return "MorseDecoderConfig{" +
                "defaultThresholdOffset=" + defaultThresholdOffset +
                ", morseTimeUnitMultiplierLow=" + morseTimeUnitMultiplierLow +
                ", morseTimeUnitMultiplierHigh=" + morseTimeUnitMultiplierHigh +
                ", thresholdSafetyFactor=" + thresholdSafetyFactor +
                ", singleDurationThreshold=" + singleDurationThreshold +
                ", morseDot='" + morseDot + '\'' +
                ", morseDash='" + morseDash + '\'' +
                ", enableLogging=" + enableLogging +
                ", strictMode=" + strictMode +
                ", maxSignalLength=" + maxSignalLength +
                '}';
    }
}
