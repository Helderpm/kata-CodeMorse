# Morse Decoder — Usage Examples

## Basic Decoding

```java
MorseDecoder decoder = new MorseDecoder();

// Bits → Morse → Text
String morse = decoder.decodeBitsAdvanced("1001");
String text  = decoder.decodeMorse(morse);
// morse → ". ."   text → "EE"

// One-liner
String result = decoder.decodeMorse(decoder.decodeBitsAdvanced("1110111"));
// "M"
```

## Real Messages

```java
MorseDecoder decoder = new MorseDecoder();

// HEY JUDE
String bits = "0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000";
System.out.println(decoder.decodeMorse(decoder.decodeBitsAdvanced(bits)));
// HEY JUDE

// THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG
String pangram = "00000000000111111100000011010001110111000000001110000000000000000001111111011111100001101111100000111100111100011111100000001011100000011111110010001111100110000011111100101111100000000000000111111100001111010110000011000111110010000011111110001111110011111110000010001111110001111111100000001111111101110000000000000010110000111111110111100000111110111110011111110000000011111001011011111000000000000111011111011111011111000000010001001111100000111110111111110000001110011111100011111010000001100001001000000000000000000111111110011111011111100000010001001000011111000000100000000101111101000000000000011111100000011110100001001100000000001110000000000000001101111101111000100000100001111111110000000001111110011111100011101100000111111000011011111000111111000000000000000001111110000100110000011111101111111011111111100000001111110001111100001000000000000000000000000000000000000000000000000000000000000";
System.out.println(decoder.decodeMorse(decoder.decodeBitsAdvanced(pangram)));
// THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG
```

## Slow / Variable Speed Signals

The decoder adapts automatically — no configuration change needed.

```java
MorseDecoder decoder = new MorseDecoder();

// Normal speed M
decoder.decodeMorse(decoder.decodeBitsAdvanced("1110111"));
// "M"

// Slow speed M (6-unit pulses instead of 3)
decoder.decodeMorse(decoder.decodeBitsAdvanced("11111100111111"));
// "M"
```

## Configuration Presets

```java
// Balanced — general use
MorseDecoder decoder = new MorseDecoder();

// Explicit default config
MorseDecoder decoder = new MorseDecoder(MorseDecoderConfig.defaultConfig());
```

## Custom Configuration

```java
MorseDecoderConfig config = new MorseDecoderConfig.Builder()
    .enableLogging(false)
    .strictMode(true)
    .maxSignalLength(5000)
    .morseTimeUnitMultipliers(2.0, 6.0)
    .build();

MorseDecoder decoder = new MorseDecoder(config);
```

## Strict Mode

Strict mode validates the input and throws `IllegalArgumentException` on bad data.

```java
MorseDecoderConfig strict = new MorseDecoderConfig.Builder()
    .strictMode(true)
    .maxSignalLength(1000)
    .build();

MorseDecoder decoder = new MorseDecoder(strict);

decoder.decodeBitsAdvanced("10102");          // throws — invalid character '2'
decoder.decodeBitsAdvanced("1".repeat(2000)); // throws — exceeds maxSignalLength
```

## Edge Cases

```java
MorseDecoder decoder = new MorseDecoder();

decoder.decodeBitsAdvanced("");        // ""
decoder.decodeBitsAdvanced("000000"); // ""
decoder.decodeMorse(null);            // ""
decoder.decodeMorse("   ");           // ""
```

## Batch Processing

```java
MorseDecoder decoder = new MorseDecoder();

String[] signals = { "1001", "1110111", "11111100111111" };

for (String signal : signals) {
    System.out.println(decoder.decodeMorse(decoder.decodeBitsAdvanced(signal)));
}
// EE
// M
// M
```

## File Decoding

```java
import java.nio.file.*;

MorseDecoder decoder = new MorseDecoder();

String bits   = Files.readString(Path.of("signal.txt")).strip();
String morse  = decoder.decodeBitsAdvanced(bits);
String text   = decoder.decodeMorse(morse);

Files.writeString(Path.of("result.txt"), text);
```
