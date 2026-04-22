package org.example;

/**
 * Configuration for the Morse decoder.
 * Use the {@link Builder} or one of the static factory presets to create instances.
 */
public class MorseDecoderConfig {

    private final double defaultThresholdOffset;
    private final double morseTimeUnitMultiplierLow;
    private final double morseTimeUnitMultiplierHigh;

    private final String morseDot;
    private final String morseDash;
    private final String morseWordSeparator;
    private final String morseLetterSeparator;
    private final char unicodeDot;
    private final char unicodeDash;

    private final boolean loggingEnabled;
    private final boolean strictMode;
    private final int maxSignalLength;

    private MorseDecoderConfig(Builder builder) {
        this.defaultThresholdOffset    = builder.defaultThresholdOffset;
        this.morseTimeUnitMultiplierLow  = builder.morseTimeUnitMultiplierLow;
        this.morseTimeUnitMultiplierHigh = builder.morseTimeUnitMultiplierHigh;
        this.morseDot            = builder.morseDot;
        this.morseDash           = builder.morseDash;
        this.morseWordSeparator  = builder.morseWordSeparator;
        this.morseLetterSeparator = builder.morseLetterSeparator;
        this.unicodeDot          = builder.unicodeDot;
        this.unicodeDash         = builder.unicodeDash;
        this.loggingEnabled      = builder.loggingEnabled;
        this.strictMode          = builder.strictMode;
        this.maxSignalLength     = builder.maxSignalLength;
    }

    public double  getDefaultThresholdOffset()     { return defaultThresholdOffset; }
    public double  getMorseTimeUnitMultiplierLow()  { return morseTimeUnitMultiplierLow; }
    public double  getMorseTimeUnitMultiplierHigh() { return morseTimeUnitMultiplierHigh; }
    public String  getMorseDot()                    { return morseDot; }
    public String  getMorseDash()                   { return morseDash; }
    public String  getMorseWordSeparator()          { return morseWordSeparator; }
    public String  getMorseLetterSeparator()        { return morseLetterSeparator; }
    public char    getUnicodeDot()                  { return unicodeDot; }
    public char    getUnicodeDash()                 { return unicodeDash; }
    public boolean isLoggingEnabled()               { return loggingEnabled; }
    public boolean isStrictMode()                   { return strictMode; }
    public int     getMaxSignalLength()             { return maxSignalLength; }

    public static MorseDecoderConfig defaultConfig()        { return new Builder().build(); }

    public static MorseDecoderConfig forFastTransmission() {
        return new Builder()
            .defaultThresholdOffset(0.3)
            .morseTimeUnitMultipliers(1.5, 4.0)
            .build();
    }

    public static MorseDecoderConfig forSlowTransmission() {
        return new Builder()
            .defaultThresholdOffset(1.0)
            .morseTimeUnitMultipliers(3.0, 8.0)
            .build();
    }

    public static MorseDecoderConfig forNoisyEnvironment() {
        return new Builder()
            .defaultThresholdOffset(0.8)
            .morseTimeUnitMultipliers(2.5, 7.0)
            .strictMode(false)
            .build();
    }

    public static MorseDecoderConfig forPrecisionDecoding() {
        return new Builder()
            .defaultThresholdOffset(0.2)
            .morseTimeUnitMultipliers(2.2, 6.2)
            .strictMode(true)
            .build();
    }

    public static MorseDecoderConfig forEducationalUse() {
        return new Builder()
            .enableLogging(true)
            .strictMode(false)
            .maxSignalLength(10_000)
            .build();
    }

    @Override
    public String toString() {
        return "MorseDecoderConfig{defaultThresholdOffset=%s, morseTimeUnitMultiplierLow=%s, morseTimeUnitMultiplierHigh=%s, morseDot='%s', morseDash='%s', loggingEnabled=%s, strictMode=%s, maxSignalLength=%s}"
            .formatted(defaultThresholdOffset, morseTimeUnitMultiplierLow, morseTimeUnitMultiplierHigh,
                       morseDot, morseDash, loggingEnabled, strictMode, maxSignalLength);
    }

    public static class Builder {
        private double  defaultThresholdOffset    = 0.5;
        private double  morseTimeUnitMultiplierLow  = 2.0;
        private double  morseTimeUnitMultiplierHigh = 6.0;
        private String  morseDot            = ".";
        private String  morseDash           = "-";
        private String  morseWordSeparator  = "   ";
        private String  morseLetterSeparator = " ";
        private char    unicodeDot          = '\u00B7'; // ·
        private char    unicodeDash         = '\u2212'; // −
        private boolean loggingEnabled      = true;
        private boolean strictMode          = false;
        private int     maxSignalLength     = 1_000_000;

        public Builder defaultThresholdOffset(double value) {
            this.defaultThresholdOffset = value;
            return this;
        }

        public Builder morseTimeUnitMultipliers(double low, double high) {
            this.morseTimeUnitMultiplierLow  = low;
            this.morseTimeUnitMultiplierHigh = high;
            return this;
        }

        public Builder morseSymbols(String dot, String dash) {
            this.morseDot  = dot;
            this.morseDash = dash;
            return this;
        }

        public Builder morseSeparators(String letterSeparator, String wordSeparator) {
            this.morseLetterSeparator = letterSeparator;
            this.morseWordSeparator   = wordSeparator;
            return this;
        }

        public Builder unicodeSymbols(char dot, char dash) {
            this.unicodeDot  = dot;
            this.unicodeDash = dash;
            return this;
        }

        public Builder enableLogging(boolean value) {
            this.loggingEnabled = value;
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
            validate();
            return new MorseDecoderConfig(this);
        }

        private void validate() {
            if (defaultThresholdOffset <= 0)
                throw new IllegalArgumentException("Default threshold offset must be positive");
            if (morseTimeUnitMultiplierLow >= morseTimeUnitMultiplierHigh)
                throw new IllegalArgumentException("Low multiplier must be less than high multiplier");
            if (maxSignalLength <= 0)
                throw new IllegalArgumentException("Max signal length must be positive");
            if (morseDot == null)
                throw new IllegalArgumentException("Morse dot symbol cannot be null");
            if (morseDash == null)
                throw new IllegalArgumentException("Morse dash symbol cannot be null");
            if (morseLetterSeparator == null)
                throw new IllegalArgumentException("Morse letter separator cannot be null");
            if (morseWordSeparator == null)
                throw new IllegalArgumentException("Morse word separator cannot be null");
        }
    }
}
