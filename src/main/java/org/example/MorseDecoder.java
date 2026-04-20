package org.example;

import java.util.*;
import java.util.stream.Collectors;
import java.util.logging.Logger;

/**
 * Unified Morse decoder with configurable parameters.
 * Handles variable transmission speeds through statistical analysis and dynamic thresholds.
 * </p>
 * This implementation eliminates code duplication by providing a single, flexible decoder
 * that can be configured for different use cases.
 */
public class MorseDecoder {

    private static final Logger LOGGER = Logger.getLogger(MorseDecoder.class.getName());

    // Configuration
    private final MorseDecoderConfig config;
    
    // Pre-compiled regex patterns
    private static final String LEADING_TRAILING_ZEROS_REGEX = "(^0+)|(0+$)";
    private static final String BIT_SPLIT_REGEX = "(?<=1)(?=0)|(?<=0)(?=1)";

    /**
     * Creates a decoder with default configuration.
     */
    public MorseDecoder() {
        this(MorseDecoderConfig.defaultConfig());
    }

    /**
     * Creates a decoder with custom configuration.
     */
    public MorseDecoder(MorseDecoderConfig config) {
        this.config = Objects.requireNonNull(config, "Configuration cannot be null");
    }

    /**
     * Main entry point for decoding binary bits to Morse symbols.
     * 
     * @param bits Binary string of 0s and 1s representing Morse signal
     * @return Morse code string with dots, dashes, and appropriate spacing
     */
    public String decodeBitsAdvanced(String bits) {
        if (config.isEnableLogging()) {
            LOGGER.info("Starting advanced bit decoding. Raw length: " + bits.length());
        }
        
        // Validate input in strict mode
        if (config.isStrictMode()) {
            validateBitInput(bits);
        }
        
        String cleanedBits = cleanBitString(bits);
        if (cleanedBits.isEmpty()) {
            return "";
        }
        
        String[] signalParts = cleanedBits.split(BIT_SPLIT_REGEX);
        SignalDurations durations = extractSignalDurations(signalParts);
        
        double oneThreshold = calculateOneThreshold(durations.ones());
        ThresholdPair zeroThresholds = calculateZeroThresholds(durations.zeros(), durations.ones());
        
        String morseResult = buildMorseString(signalParts, oneThreshold, zeroThresholds);
        
        if (config.isEnableLogging()) {
            LOGGER.info("Bit decoding completed: " + morseResult);
        }
        
        return morseResult;
    }

    /**
     * Converts Morse code symbols to readable text.
     * 
     * @param morseCode Morse code string with dots, dashes, and spacing
     * @return Decoded text string
     */
    public String decodeMorse(String morseCode) {
        if (morseCode == null || morseCode.trim().isEmpty()) {
            return "";
        }
        
        if (config.isEnableLogging()) {
            LOGGER.info("Converting Morse to text...");
        }
        
        String normalizedMorse = normalizeUnicodeCharacters(morseCode);
        String decodedText = decodeMorseWords(normalizedMorse);
        
        if (config.isEnableLogging()) {
            LOGGER.info("Final translation result: " + decodedText);
        }
        
        return decodedText;
    }

    
    // === Private Implementation Methods ===

    private void validateBitInput(String bits) {
        if (bits == null) {
            throw new IllegalArgumentException("Input cannot be null in strict mode");
        }
        
        if (bits.length() > config.getMaxSignalLength()) {
            throw new IllegalArgumentException("Input signal too long: " + bits.length() + 
                " (max: " + config.getMaxSignalLength() + ")");
        }
        
        // Check for invalid characters
        for (char c : bits.toCharArray()) {
            if (c != '0' && c != '1') {
                throw new IllegalArgumentException("Invalid character in input: '" + c + 
                    "' (only '0' and '1' allowed in strict mode)");
            }
        }
    }

    private String cleanBitString(String bits) {
        return bits.replaceAll(LEADING_TRAILING_ZEROS_REGEX, "");
    }

    private SignalDurations extractSignalDurations(String[] parts) {
        List<Integer> ones = new ArrayList<>();
        List<Integer> zeros = new ArrayList<>();
        
        for (String part : parts) {
            if (part.contains("1")) {
                ones.add(part.length());
            } else {
                zeros.add(part.length());
            }
        }
        
        return new SignalDurations(ones, zeros);
    }

    private double calculateOneThreshold(List<Integer> ones) {
        if (ones.isEmpty()) {
            return config.getDefaultThresholdOffset();
        }
        
        int minOne = ones.stream().min(Integer::compare).orElse(1);
        List<Integer> sortedUniqueOnes = ones.stream().distinct().sorted().toList();
        
        if (sortedUniqueOnes.size() > 1) {
            return findThresholdByGapAnalysis(sortedUniqueOnes);
        } else {
            return calculateSingleDurationThreshold(minOne);
        }
    }

    private double findThresholdByGapAnalysis(List<Integer> sortedValues) {
        int maxGapIndex = findMaxGapIndex(sortedValues);
        return (sortedValues.get(maxGapIndex) + sortedValues.get(maxGapIndex + 1)) / 2.0;
    }

    private int findMaxGapIndex(List<Integer> sortedValues) {
        int maxGapIndex = 0;
        int maxGap = -1;
        
        for (int i = 0; i < sortedValues.size() - 1; i++) {
            int currentGap = sortedValues.get(i + 1) - sortedValues.get(i);
            if (currentGap > maxGap) {
                maxGap = currentGap;
                maxGapIndex = i;
            }
        }
        
        return maxGapIndex;
    }

    private double calculateSingleDurationThreshold(int duration) {
        return (duration < config.getSingleDurationThreshold()) 
            ? duration + config.getDefaultThresholdOffset() 
            : duration - config.getDefaultThresholdOffset();
    }

    private ThresholdPair calculateZeroThresholds(List<Integer> zeros, List<Integer> ones) {
        if (zeros.isEmpty()) {
            return new ThresholdPair(config.getMorseTimeUnitMultiplierLow(), config.getMorseTimeUnitMultiplierHigh());
        }
        
        if (zeros.size() < 2) {
            return calculateDefaultZeroThresholds(ones);
        }
        
        return calculateZeroThresholdsByGapAnalysis(zeros);
    }

    private ThresholdPair calculateDefaultZeroThresholds(List<Integer> ones) {
        int minOne = ones.stream().min(Integer::compare).orElse(1);
        double lowThreshold = minOne * config.getMorseTimeUnitMultiplierLow();
        double highThreshold = minOne * config.getMorseTimeUnitMultiplierHigh();
        return new ThresholdPair(lowThreshold, highThreshold);
    }

    private ThresholdPair calculateZeroThresholdsByGapAnalysis(List<Integer> zeros) {
        List<Integer> sortedUniqueZeros = zeros.stream().distinct().sorted().toList();
        
        if (sortedUniqueZeros.size() < 2) {
            return calculateDefaultZeroThresholds(List.of(1));
        }
        
        int maxGapIndex = findMaxGapIndex(sortedUniqueZeros);
        
        if (maxGapIndex >= sortedUniqueZeros.size() - 1) {
            maxGapIndex = sortedUniqueZeros.size() - 2;
        }
        
        double highThreshold = (sortedUniqueZeros.get(maxGapIndex) + sortedUniqueZeros.get(maxGapIndex + 1)) / 2.0;
        double lowThreshold = findSecondGapThreshold(sortedUniqueZeros, maxGapIndex, highThreshold);
        
        return ensureValidThresholdOrder(lowThreshold, highThreshold);
    }

    private double findSecondGapThreshold(List<Integer> sortedZeros, int maxGapIndex, double highThreshold) {
        if (maxGapIndex <= 0) {
            return (sortedZeros.getFirst() + highThreshold) / 2.0;
        }
        
        int secondMaxGap = -1;
        int secondGapIndex = -1;
        
        for (int i = 0; i < maxGapIndex; i++) {
            int gap = sortedZeros.get(i + 1) - sortedZeros.get(i);
            if (gap > secondMaxGap) {
                secondMaxGap = gap;
                secondGapIndex = i;
            }
        }
        
        if (secondGapIndex != -1) {
            return (sortedZeros.get(secondGapIndex) + sortedZeros.get(secondGapIndex + 1)) / 2.0;
        } else {
            return (sortedZeros.getFirst() + highThreshold) / 2.0;
        }
    }

    private ThresholdPair ensureValidThresholdOrder(double lowThreshold, double highThreshold) {
        if (lowThreshold >= highThreshold) {
            lowThreshold = highThreshold / config.getThresholdSafetyFactor();
        }
        return new ThresholdPair(lowThreshold, highThreshold);
    }

    private String buildMorseString(String[] parts, double oneThreshold, ThresholdPair zeroThresholds) {
        StringBuilder morseBuilder = new StringBuilder();
        
        for (String part : parts) {
            int length = part.length();
            
            if (part.charAt(0) == '1') {
                morseBuilder.append(length > oneThreshold ? config.getMorseDash() : config.getMorseDot());
            } else {
                appendMorseSpacing(morseBuilder, length, zeroThresholds);
            }
        }
        
        return morseBuilder.toString();
    }

    private void appendMorseSpacing(StringBuilder builder, int length, ThresholdPair thresholds) {
        if (length >= thresholds.high()) {
            builder.append(config.getMorseWordSeparator());
        } else if (length >= thresholds.low()) {
            builder.append(config.getMorseLetterSeparator());
        }
    }

    private String normalizeUnicodeCharacters(String morseCode) {
        return morseCode.replace(config.getUnicodeDot(), config.getMorseDot().charAt(0))
                       .replace(config.getUnicodeDash(), config.getMorseDash().charAt(0));
    }

    private String decodeMorseWords(String normalizedMorse) {
        return Arrays.stream(normalizedMorse.trim().split(config.getMorseWordSeparator()))
                .map(this::decodeMorseWord)
                .collect(Collectors.joining(config.getMorseLetterSeparator()));
    }

    private String decodeMorseWord(String word) {
        return Arrays.stream(word.split(config.getMorseLetterSeparator()))
                .map(MorseCode::get)
                .collect(Collectors.joining());
    }

    // === Inner Classes ===

    /**
     * Record to hold signal duration data.
     */
    private record SignalDurations(List<Integer> ones, List<Integer> zeros) {}

    /**
     * Record to hold threshold pair data.
     */
    private record ThresholdPair(double low, double high) {}
}
