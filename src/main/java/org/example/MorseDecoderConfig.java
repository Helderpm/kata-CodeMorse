package org.example;

/**
 * Immutable configuration for {@link MorseDecoder}.
 *
 * <p>Instances are created exclusively through the {@link Builder} or one of the
 * static factory presets. All fields are set at construction time and cannot be
 * changed afterwards, making this class safe to share across threads.
 *
 * <h2>Quick start</h2>
 * <pre>{@code
 * // Default settings
 * MorseDecoderConfig config = MorseDecoderConfig.defaultConfig();
 *
 * // Custom settings via builder
 * MorseDecoderConfig config = new MorseDecoderConfig.Builder()
 *     .enableLogging(false)
 *     .strictMode(true)
 *     .maxSignalLength(5_000)
 *     .build();
 * }</pre>
 *
 * @see MorseDecoder
 * @see Builder
 */
public class MorseDecoderConfig {

    // === Timing ===

    /**
     * Fallback dot/dash threshold returned when the ones list is empty.
     * Must be strictly positive.
     */
    private final double defaultThresholdOffset;

    /**
     * Lower multiplier applied to the base time unit when the zero distribution
     * is empty and default thresholds are used.
     */
    private final double morseTimeUnitMultiplierLow;

    /**
     * Upper multiplier applied to the base time unit when the zero distribution
     * is empty and default thresholds are used.
     * Must be strictly greater than {@link #morseTimeUnitMultiplierLow}.
     */
    private final double morseTimeUnitMultiplierHigh;

    // === Morse symbols ===

    /** Symbol emitted for a dot signal (default {@code "."}). */
    private final String morseDot;

    /** Symbol emitted for a dash signal (default {@code "-"}). */
    private final String morseDash;

    /** Separator inserted between words (default three spaces {@code "   "}). */
    private final String morseWordSeparator;

    /** Separator inserted between letters within a word (default one space {@code " "}). */
    private final String morseLetterSeparator;

    /**
     * Unicode middle-dot character ({@code U+00B7 ·}) normalised to {@link #morseDot}
     * before decoding Morse input.
     */
    private final char unicodeDot;

    /**
     * Unicode minus character ({@code U+2212 −}) normalised to {@link #morseDash}
     * before decoding Morse input.
     */
    private final char unicodeDash;

    // === Behaviour ===

    /** Whether {@link java.util.logging.Logger} messages are emitted during decoding. */
    private final boolean loggingEnabled;

    /**
     * Whether strict input validation is active.
     * When {@code true}, {@link MorseDecoder#decodeBitsAdvanced(String)} throws
     * {@link IllegalArgumentException} on invalid characters or oversized signals.
     */
    private final boolean strictMode;

    /**
     * Maximum accepted bit-string length when {@link #strictMode} is {@code true}.
     * Must be strictly positive.
     */
    private final int maxSignalLength;

    /**
     * Private — use {@link Builder#build()} or a static factory preset.
     *
     * @param builder validated builder instance
     */
    private MorseDecoderConfig(Builder builder) {
        this.defaultThresholdOffset    = builder.defaultThresholdOffset;
        this.morseTimeUnitMultiplierLow  = builder.morseTimeUnitMultiplierLow;
        this.morseTimeUnitMultiplierHigh = builder.morseTimeUnitMultiplierHigh;
        this.morseDot             = builder.morseDot;
        this.morseDash            = builder.morseDash;
        this.morseWordSeparator   = builder.morseWordSeparator;
        this.morseLetterSeparator = builder.morseLetterSeparator;
        this.unicodeDot           = builder.unicodeDot;
        this.unicodeDash          = builder.unicodeDash;
        this.loggingEnabled       = builder.loggingEnabled;
        this.strictMode           = builder.strictMode;
        this.maxSignalLength      = builder.maxSignalLength;
    }

    /**
     * Returns the fallback dot/dash threshold used when the signal contains no ones.
     *
     * @return default threshold offset (always &gt; 0)
     */
    public double getDefaultThresholdOffset()     { return defaultThresholdOffset; }

    /**
     * Returns the lower time-unit multiplier used when no zero durations are present.
     *
     * @return low multiplier
     */
    public double getMorseTimeUnitMultiplierLow()  { return morseTimeUnitMultiplierLow; }

    /**
     * Returns the upper time-unit multiplier used when no zero durations are present.
     *
     * @return high multiplier (always &gt; {@link #getMorseTimeUnitMultiplierLow()})
     */
    public double getMorseTimeUnitMultiplierHigh() { return morseTimeUnitMultiplierHigh; }

    /**
     * Returns the symbol used to represent a dot in the Morse output.
     *
     * @return dot symbol (default {@code "."})
     */
    public String getMorseDot()                    { return morseDot; }

    /**
     * Returns the symbol used to represent a dash in the Morse output.
     *
     * @return dash symbol (default {@code "-"})
     */
    public String getMorseDash()                   { return morseDash; }

    /**
     * Returns the separator inserted between words in the Morse output.
     *
     * @return word separator (default three spaces)
     */
    public String getMorseWordSeparator()          { return morseWordSeparator; }

    /**
     * Returns the separator inserted between letters within a word.
     *
     * @return letter separator (default one space)
     */
    public String getMorseLetterSeparator()        { return morseLetterSeparator; }

    /**
     * Returns the Unicode dot character that is normalised to {@link #getMorseDot()}
     * before Morse decoding.
     *
     * @return unicode dot ({@code U+00B7 ·})
     */
    public char getUnicodeDot()                    { return unicodeDot; }

    /**
     * Returns the Unicode dash character that is normalised to {@link #getMorseDash()}
     * before Morse decoding.
     *
     * @return unicode dash ({@code U+2212 −})
     */
    public char getUnicodeDash()                   { return unicodeDash; }

    /**
     * Returns whether JUL logging is active during decoding.
     *
     * @return {@code true} if logging is enabled
     */
    public boolean isLoggingEnabled()              { return loggingEnabled; }

    /**
     * Returns whether strict input validation is active.
     *
     * @return {@code true} if strict mode is enabled
     */
    public boolean isStrictMode()                  { return strictMode; }

    /**
     * Returns the maximum accepted bit-string length in strict mode.
     *
     * @return max signal length (always &gt; 0)
     */
    public int getMaxSignalLength()                { return maxSignalLength; }

    /**
     * Creates a configuration with balanced defaults suitable for most signals.
     *
     * @return default configuration
     */
    public static MorseDecoderConfig defaultConfig() {
        return new Builder().build();
    }

    /**
     * Returns a human-readable summary of this configuration.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return """
                MorseDecoderConfig{defaultThresholdOffset=%s, morseTimeUnitMultiplierLow=%s,\
                 morseTimeUnitMultiplierHigh=%s, morseDot='%s', morseDash='%s',\
                 loggingEnabled=%s, strictMode=%s, maxSignalLength=%s}"""
            .formatted(defaultThresholdOffset, morseTimeUnitMultiplierLow, morseTimeUnitMultiplierHigh,
                       morseDot, morseDash, loggingEnabled, strictMode, maxSignalLength);
    }

    /**
     * Fluent builder for {@link MorseDecoderConfig}.
     *
     * <p>All setter methods return {@code this} to allow chaining.
     * Call {@link #build()} to obtain an immutable {@link MorseDecoderConfig}.
     *
     * <pre>{@code
     * MorseDecoderConfig config = new MorseDecoderConfig.Builder()
     *     .enableLogging(false)
     *     .strictMode(true)
     *     .maxSignalLength(5_000)
     *     .build();
     * }</pre>
     */
    public static class Builder {

        private double  defaultThresholdOffset    = 0.5;
        private double  morseTimeUnitMultiplierLow  = 2.0;
        private double  morseTimeUnitMultiplierHigh = 6.0;
        private String  morseDot             = ".";
        private String  morseDash            = "-";
        private String  morseWordSeparator   = "   ";
        private String  morseLetterSeparator = " ";
        private char    unicodeDot           = '\u00B7'; // ·
        private char    unicodeDash          = '\u2212'; // −
        private boolean loggingEnabled       = true;
        private boolean strictMode           = false;
        private int     maxSignalLength      = 1_000_000;

        /**
         * Sets the fallback dot/dash threshold used when the signal contains no ones.
         *
         * @param value threshold offset — must be &gt; 0
         * @return this builder
         */
        public Builder defaultThresholdOffset(double value) {
            this.defaultThresholdOffset = value;
            return this;
        }

        /**
         * Sets the low and high time-unit multipliers used when no zero durations
         * are present in the signal.
         *
         * @param low  lower multiplier — must be &lt; {@code high}
         * @param high upper multiplier — must be &gt; {@code low}
         * @return this builder
         */
        public Builder morseTimeUnitMultipliers(double low, double high) {
            this.morseTimeUnitMultiplierLow  = low;
            this.morseTimeUnitMultiplierHigh = high;
            return this;
        }

        /**
         * Enables or disables JUL logging of threshold decisions during decoding.
         *
         * @param value {@code true} to enable logging (default), {@code false} to silence it
         * @return this builder
         */
        public Builder enableLogging(boolean value) {
            this.loggingEnabled = value;
            return this;
        }

        /**
         * Enables or disables strict input validation.
         * When enabled, {@link MorseDecoder#decodeBitsAdvanced(String)} throws
         * {@link IllegalArgumentException} on invalid characters or signals that
         * exceed {@link #maxSignalLength(int)}.
         *
         * @param value {@code true} to enable strict mode
         * @return this builder
         */
        public Builder strictMode(boolean value) {
            this.strictMode = value;
            return this;
        }

        /**
         * Sets the maximum accepted bit-string length when strict mode is active.
         *
         * @param value maximum length — must be &gt; 0
         * @return this builder
         */
        public Builder maxSignalLength(int value) {
            this.maxSignalLength = value;
            return this;
        }

        /**
         * Validates the current builder state and returns an immutable
         * {@link MorseDecoderConfig}.
         *
         * @return new immutable configuration
         * @throws IllegalArgumentException if any field value is invalid
         */
        public MorseDecoderConfig build() {
            validate();
            return new MorseDecoderConfig(this);
        }

        /**
         * Checks that all field values are within their valid ranges.
         *
         * @throws IllegalArgumentException on the first invalid value found
         */
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
