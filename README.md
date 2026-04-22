# Morse Decoder Kata

A Morse code decoder that handles variable transmission speeds through statistical analysis and dynamic threshold calculation.

## Overview

This project decodes binary bit strings into readable text via Morse code. It handles real-world timing variations by analysing signal durations to find the optimal dot/dash and spacing thresholds automatically — no fixed timing ratios required.

## Architecture

### Core Classes

| Class | Role |
|---|---|
| `MorseDecoder` | Main decoder — bit string → Morse → text |
| `MorseDecoderConfig` | Immutable configuration with builder pattern and presets |
| `MorseCode` | Morse-to-ASCII lookup table |

### Algorithm

```
Raw bits → Strip leading/trailing zeros → Split on transitions
         → Analyse ones (dot/dash threshold)
         → Analyse zeros (inter-char / inter-word thresholds)
         → Build Morse string → Decode to text
```

**Dot/dash threshold** — for signals with range > 3 units, uses a frequency-jump approach: finds the steepest upward jump in the value histogram, which marks the start of the dash cluster. For simple signals, uses gap analysis on sorted unique values.

**Spacing thresholds** — derives the inter-char threshold from the zero distribution (frequency jump, constrained by the average dot duration), then sets the inter-word threshold at `low × 7/3`, matching the standard Morse 3:7 inter-char/inter-word ratio.

## Quick Start

### CLI Interface

The project includes an interactive CLI for quick decoding:

```bash
# Run via Maven
mvn exec:java

# Or build and run as JAR
mvn package
java -jar target/kata-decoder-morse-1.0-SNAPSHOT.jar
```

The CLI menu offers:
1. Decode bit strings (e.g., `0000000011011010011100...`)
2. Decode Morse code (e.g., `.... . -.--   .--- ..- -.. .`)
3. Exit

### Programmatic Usage

```java
MorseDecoder decoder = new MorseDecoder();

String morse = decoder.decodeBitsAdvanced("0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000");
// ".... . -.--   .--- ..- -.. ."

String text = decoder.decodeMorse(morse);
// "HEY JUDE"
```

## Configuration

### Presets

```java
new MorseDecoder(MorseDecoderConfig.defaultConfig())
new MorseDecoder(MorseDecoderConfig.forFastTransmission())
new MorseDecoder(MorseDecoderConfig.forSlowTransmission())
new MorseDecoder(MorseDecoderConfig.forNoisyEnvironment())
new MorseDecoder(MorseDecoderConfig.forPrecisionDecoding())
new MorseDecoder(MorseDecoderConfig.forEducationalUse())
```

### Custom Builder

```java
MorseDecoderConfig config = new MorseDecoderConfig.Builder()
    .enableLogging(true)
    .strictMode(true)
    .maxSignalLength(5000)
    .morseTimeUnitMultipliers(2.0, 6.0)
    .build();

MorseDecoder decoder = new MorseDecoder(config);
```

### Builder Options

| Method | Default | Description |
|---|---|---|
| `defaultThresholdOffset(double)` | `0.5` | Fallback threshold when signal is empty |
| `morseTimeUnitMultipliers(low, high)` | `2.0, 6.0` | Multipliers used when zero distribution is empty |
| `thresholdSafetyFactor(double)` | `2.0` | Used to enforce low < high on thresholds |
| `enableLogging(boolean)` | `true` | JUL logging of threshold decisions |
| `strictMode(boolean)` | `false` | Validates input — throws on invalid chars or oversized signals |
| `maxSignalLength(int)` | `1 000 000` | Max allowed bit string length in strict mode |
| `morseSymbols(dot, dash)` | `".", "-"` | Output symbols |
| `morseSeparators(letter, word)` | `" ", "   "` | Output separators |
| `unicodeSymbols(dot, dash)` | `'·', '−'` | Unicode variants normalised on input |

## Running Tests

```bash
mvn test

# Single class
mvn test -Dtest=SpecificTestCaseTest
```

## Test Coverage

| Class | Cases | What it covers |
|---|---|---|
| `BasicFunctionalityTest` | 3 | Empty input, zeros-only, EE, M |
| `MorseDecoderTest` | 1 | Instantiation + basic EE sanity check |
| `SpecificTestCaseTest` | 6 | EE, M (normal & slow), HEY JUDE, THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG, Titanic SOS |

### Validated Messages

```
"1001"              → EE
"1110111"           → M
"11111100111111"    → M  (slow transmission)
bits                → HEY JUDE
bits                → THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG
bits                → MGY CQD CQD SOS TITANIC POSITION 41.44 N 50.24 W.
                       REQUIRE IMMEDIATE ASSISTANCE. COME AT ONCE.
                       WE STRUCK AN ICEBERG. SINKING
```

## Edge Cases

| Input | Result |
|---|---|
| `""` | `""` |
| `"000000"` | `""` |
| `null` (decodeMorse) | `""` |
| Invalid chars in strict mode | `IllegalArgumentException` |
| Signal too long in strict mode | `IllegalArgumentException` |

## Performance

- **Signal analysis**: O(n) — single pass
- **Threshold calculation**: O(k) — where k is the value range
- **Morse construction**: O(n) — linear pass
- **Space**: O(n)

Typical timings: ~0.3 ms for simple messages, ~1–3 ms for long messages like the Titanic SOS.
