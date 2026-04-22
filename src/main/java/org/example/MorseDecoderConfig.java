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
    /** Gets the default threshold offset.
     * @return the default threshold offset */
    public double getDefaultThresholdOffset() { return defaultThresholdOffset; }
    /** Gets the Morse time unit multiplier low.
     * @return the Morse time unit multiplier low */
    public double getMorseTimeUnitMultiplierLow() { return morseTimeUnitMultiplierLow; }
    /** Gets the Morse time unit multiplier high.
     * @return the Morse time unit multiplier high */
    public double getMorseTimeUnitMultiplierHigh() { return morseTimeUnitMultiplierHigh; }
    /** Gets the threshold safety factor.
     * @return the threshold safety factor */
    public double getThresholdSafetyFactor() { return thresholdSafetyFactor; }
    /** Gets the single duration threshold.
     * @return the single duration threshold */
    public int getSingleDurationThreshold() { return singleDurationThreshold; }

    /** Gets the Morse dot symbol.
     * @return the Morse dot symbol */
    public String getMorseDot() { return morseDot; }
    /** Gets the Morse dash symbol.
     * @return the Morse dash symbol */
    public String getMorseDash() { return morseDash; }
    /** Gets the Morse word separator.
     * @return the Morse word separator */
    public String getMorseWordSeparator() { return morseWordSeparator; }
    /** Gets the Morse letter separator.
     * @return the Morse letter separator */
    public String getMorseLetterSeparator() { return morseLetterSeparator; }
    /** Gets the Unicode dot character.
     * @return the Unicode dot character */
    public char getUnicodeDot() { return unicodeDot; }
    /** Gets the Unicode dash character.
     * @return the Unicode dash character */
    public char getUnicodeDash() { return unicodeDash; }

    /** Checks if logging is enabled.
     * @return true if logging is enabled */
    public boolean isEnableLogging() { return enableLogging; }
    /** Checks if strict mode is enabled.
     * @return true if strict mode is enabled */
    public boolean isStrictMode() { return strictMode; }
    /** Gets the maximum signal length.
     * @return the maximum signal length */
    public int getMaxSignalLength() { return maxSignalLength; }

    /**
     * Creates a default configuration.
     * @return a default MorseDecoderConfig
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
        /** Creates a new Builder with default values. */
        public Builder() {}

        // Timing configuration
        /** Sets the default threshold offset.
         * @param value the default threshold offset
         * @return this Builder instance */
        public Builder defaultThresholdOffset(double value) {
            this.defaultThresholdOffset = value;
            return this;
        }

        /** Sets the Morse time unit multipliers for low and high thresholds.
         * @param low the low multiplier
         * @param high the high multiplier
         * @return this Builder instance */
        public Builder morseTimeUnitMultipliers(double low, double high) {
            this.morseTimeUnitMultiplierLow = low;
            this.morseTimeUnitMultiplierHigh = high;
            return this;
        }

        /** Sets the threshold safety factor.
         * @param value the threshold safety factor
         * @return this Builder instance */
        public Builder thresholdSafetyFactor(double value) {
            this.thresholdSafetyFactor = value;
            return this;
        }

        /** Sets the single duration threshold.
         * @param value the single duration threshold
         * @return this Builder instance */
        public Builder singleDurationThreshold(int value) {
            this.singleDurationThreshold = value;
            return this;
        }

        // Symbol configuration
        /** Sets the Morse symbols for dot and dash.
         * @param dot the dot symbol
         * @param dash the dash symbol
         * @return this Builder instance */
        public Builder morseSymbols(String dot, String dash) {
            this.morseDot = dot;
            this.morseDash = dash;
            return this;
        }

        /** Sets the Morse separators for letters and words.
         * @param letterSeparator the letter separator
         * @param wordSeparator the word separator
         * @return this Builder instance */
        public Builder morseSeparators(String letterSeparator, String wordSeparator) {
            this.morseLetterSeparator = letterSeparator;
            this.morseWordSeparator = wordSeparator;
            return this;
        }

        /** Sets the Unicode symbols for dot and dash.
         * @param dot the dot character
         * @param dash the dash character
         * @return this Builder instance */
        public Builder unicodeSymbols(char dot, char dash) {
            this.unicodeDot = dot;
            this.unicodeDash = dash;
            return this;
        }

        // Behavioral configuration
        /** Enables or disables logging.
         * @param value true to enable logging, false to disable
         * @return this Builder instance */
        public Builder enableLogging(boolean value) {
            this.enableLogging = value;
            return this;
        }

        /** Enables or disables strict mode.
         * @param value true to enable strict mode, false to disable
         * @return this Builder instance */
        public Builder strictMode(boolean value) {
            this.strictMode = value;
            return this;
        }

        /** Sets the maximum signal length.
         * @param value the maximum signal length
         * @return this Builder instance */
        public Builder maxSignalLength(int value) {
            this.maxSignalLength = value;
            return this;
        }

        /** Builds the MorseDecoderConfig.
         * @return the built MorseDecoderConfig */
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
     * @return a configuration for fast transmission
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
     * @return a configuration for slow transmission
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
     * @return a configuration for noisy environments
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
     * @return a configuration for precision decoding
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
     * @return a configuration for educational use
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
