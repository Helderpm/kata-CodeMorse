package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.logging.Logger;

/**
 * Unified Morse decoder with configurable parameters.
 * Handles variable transmission speeds through statistical analysis and dynamic thresholds.
 */
public class MorseDecoder {

    private static final Logger LOGGER = Logger.getLogger(MorseDecoder.class.getName());

    private final MorseDecoderConfig config;

    // Average dot duration cached from ones analysis, used for zero threshold calculation
    private double avgDot = 1.0;

    private static final String LEADING_TRAILING_ZEROS_REGEX = "(^0+)|(0+$)";
    private static final String BIT_SPLIT_REGEX = "(?<=1)(?=0)|(?<=0)(?=1)";

    public MorseDecoder() {
        this(MorseDecoderConfig.defaultConfig());
    }

    public MorseDecoder(MorseDecoderConfig config) {
        this.config = Objects.requireNonNull(config, "Configuration cannot be null");
    }

    /**
     * Decodes a binary bit string into Morse code symbols.
     *
     * @param bits binary string of 0s and 1s representing a Morse signal
     * @return Morse code string with dots, dashes, and spacing
     */
    public String decodeBitsAdvanced(String bits) {
        if (config.isLoggingEnabled()) {
            LOGGER.info("Starting advanced bit decoding. Raw length: %d".formatted(bits.length()));
        }

        if (config.isStrictMode()) {
            validateBitInput(bits);
        }

        var cleanedBits = cleanBitString(bits);
        if (cleanedBits.isEmpty()) {
            return "";
        }

        var signalParts = cleanedBits.split(BIT_SPLIT_REGEX);
        var durations   = extractSignalDurations(signalParts);

        var oneThreshold   = calculateOneThreshold(durations.ones());
        var zeroThresholds = calculateZeroThresholds(durations.zeros());
        var morseResult    = buildMorseString(signalParts, oneThreshold, zeroThresholds);

        if (config.isLoggingEnabled()) {
            LOGGER.info("Bit decoding completed: %s".formatted(morseResult));
        }

        return morseResult;
    }

    /**
     * Converts Morse code symbols to readable text.
     *
     * @param morseCode Morse code string with dots, dashes, and spacing
     * @return decoded text string
     */
    public String decodeMorse(String morseCode) {
        if (morseCode == null || morseCode.isBlank()) {
            return "";
        }

        if (config.isLoggingEnabled()) {
            LOGGER.info("Converting Morse to text...");
        }

        var decodedText = decodeMorseWords(normalizeUnicodeCharacters(morseCode));

        if (config.isLoggingEnabled()) {
            LOGGER.info("Final translation result: %s".formatted(decodedText));
        }

        return decodedText;
    }

    // === Private Implementation ===

    private void validateBitInput(String bits) {
        if (bits == null)
            throw new IllegalArgumentException("Input cannot be null in strict mode");
        if (bits.length() > config.getMaxSignalLength())
            throw new IllegalArgumentException(
                "Input signal too long: %d (max: %d)".formatted(bits.length(), config.getMaxSignalLength()));
        for (char c : bits.toCharArray()) {
            if (c != '0' && c != '1')
                throw new IllegalArgumentException(
                    "Invalid character in input: '%c' (only '0' and '1' allowed in strict mode)".formatted(c));
        }
    }

    private String cleanBitString(String bits) {
        return bits.replaceAll(LEADING_TRAILING_ZEROS_REGEX, "");
    }

    private SignalDurations extractSignalDurations(String[] parts) {
        List<Integer> ones  = new ArrayList<>();
        List<Integer> zeros = new ArrayList<>();
        for (var part : parts) {
            (part.contains("1") ? ones : zeros).add(part.length());
        }
        return new SignalDurations(ones, zeros);
    }

    private double calculateOneThreshold(List<Integer> ones) {
        if (ones.isEmpty()) {
            return config.getDefaultThresholdOffset();
        }

        var sortedOnes = ones.stream().sorted().toList();
        int median = sortedOnes.get(sortedOnes.size() / 2);
        int range  = sortedOnes.getLast() - sortedOnes.getFirst();

        if (config.isLoggingEnabled()) {
            LOGGER.info("Ones durations: %s".formatted(ones));
            LOGGER.info("Median: %d, range: %d".formatted(median, range));
        }

        double threshold = range > 3
            ? findThresholdByFrequencyJump(ones)
            : findThresholdByGapAnalysis(sortedOnes);

        if (config.isLoggingEnabled()) {
            LOGGER.info("One threshold: %s".formatted(threshold));
        }

        // Cache average dot duration for zero threshold calculation
        this.avgDot = ones.stream()
            .filter(v -> v <= threshold)
            .mapToInt(Integer::intValue)
            .average()
            .orElse(median);

        return threshold;
    }

    /**
     * Finds the threshold at the steepest upward frequency jump — the start of the dash cluster.
     */
    private double findThresholdByFrequencyJump(List<Integer> values) {
        var freq    = values.stream().collect(Collectors.groupingBy(v -> v, Collectors.counting()));
        int min     = values.stream().mapToInt(Integer::intValue).min().orElse(1);
        int max     = values.stream().mapToInt(Integer::intValue).max().orElse(1);
        int bestV   = min;
        long bestScore = Long.MIN_VALUE;
        for (int v = min; v < max; v++) {
            long score = freq.getOrDefault(v + 1, 0L) - freq.getOrDefault(v, 0L);
            if (score > bestScore) {
                bestScore = score;
                bestV     = v;
            }
        }
        return bestV + 0.5;
    }

    private double findThresholdByGapAnalysis(List<Integer> sortedValues) {
        int maxGapIndex = findMaxGapIndex(sortedValues);
        int maxGap      = sortedValues.get(maxGapIndex + 1) - sortedValues.get(maxGapIndex);

        if (maxGap == 0) {
            double avg = sortedValues.stream().mapToInt(Integer::intValue).average().orElse(1.0);
            return avg > 2 ? avg * 0.5 : avg + 1;
        }

        if (maxGap <= 2) {
            return sortedValues.get(sortedValues.size() / 2);
        }

        return (sortedValues.get(maxGapIndex) + sortedValues.get(maxGapIndex + 1)) / 2.0;
    }

    private int findMaxGapIndex(List<Integer> sortedValues) {
        int maxGapIndex = 0;
        int maxGap      = -1;
        for (int i = 0; i < sortedValues.size() - 1; i++) {
            int gap = sortedValues.get(i + 1) - sortedValues.get(i);
            if (gap > maxGap) {
                maxGap      = gap;
                maxGapIndex = i;
            }
        }
        return maxGapIndex;
    }

    private ThresholdPair calculateZeroThresholds(List<Integer> zeros) {
        if (config.isLoggingEnabled()) {
            LOGGER.info("Zeros durations: %s".formatted(zeros));
        }

        if (zeros.isEmpty()) {
            return new ThresholdPair(config.getMorseTimeUnitMultiplierLow(), config.getMorseTimeUnitMultiplierHigh());
        }

        double low;
        if (zeros.stream().distinct().count() <= 1) {
            // Single zero value: all gaps are inter-char
            low = avgDot * 1.5;
        } else {
            low = findThresholdByFrequencyJump(zeros);
            if (low < avgDot * 1.5 || low > avgDot * 4.0) {
                low = avgDot * 2.5;
            }
        }

        // Inter-word/inter-char ratio in Morse is 7:3
        double high = low * (7.0 / 3.0);

        if (config.isLoggingEnabled()) {
            LOGGER.info("Zero thresholds (avgDot=%s): low=%s, high=%s".formatted(avgDot, low, high));
        }

        return new ThresholdPair(low, high);
    }

    private String buildMorseString(String[] parts, double oneThreshold, ThresholdPair zeroThresholds) {
        var sb = new StringBuilder();
        for (var part : parts) {
            if (part.charAt(0) == '1') {
                sb.append(part.length() > oneThreshold ? config.getMorseDash() : config.getMorseDot());
            } else {
                appendMorseSpacing(sb, part.length(), zeroThresholds);
            }
        }
        return sb.toString();
    }

    private void appendMorseSpacing(StringBuilder builder, int length, ThresholdPair thresholds) {
        if (length >= thresholds.high()) {
            builder.append(config.getMorseWordSeparator());
        } else if (length >= thresholds.low()) {
            builder.append(config.getMorseLetterSeparator());
        }
    }

    private String normalizeUnicodeCharacters(String morseCode) {
        return morseCode
            .replace(config.getUnicodeDot(),  config.getMorseDot().charAt(0))
            .replace(config.getUnicodeDash(), config.getMorseDash().charAt(0));
    }

    private String decodeMorseWords(String normalizedMorse) {
        return Arrays.stream(normalizedMorse.trim().split(config.getMorseWordSeparator()))
            .map(this::decodeMorseWord)
            .collect(Collectors.joining(" "));
    }

    private String decodeMorseWord(String word) {
        return Arrays.stream(word.split(config.getMorseLetterSeparator()))
            .map(MorseCode::get)
            .collect(Collectors.joining());
    }

    private record SignalDurations(List<Integer> ones, List<Integer> zeros) {}

    private record ThresholdPair(double low, double high) {}
}
