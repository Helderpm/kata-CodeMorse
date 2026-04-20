# Morse Decoder Usage Examples

This document provides comprehensive examples of how to use the refactored Morse decoder in various scenarios.

## 📚 Table of Contents

1. [Basic Usage](#basic-usage)
2. [Advanced Scenarios](#advanced-scenarios)
3. [Error Handling](#error-handling)
4. [Performance Considerations](#performance-considerations)
5. [Integration Examples](#integration-examples)
6. [Custom Extensions](#custom-extensions)

## 🚀 Basic Usage

### Simple Decoding

```java
import org.example.MorseDecoderClean;

public class BasicExample {
    public static void main(String[] args) {
        MorseDecoderClean decoder = new MorseDecoderClean();
        
        // Example 1: Simple dots and dashes
        String bits1 = "1001";
        String morse1 = decoder.decodeBitsAdvanced(bits1);
        String text1 = decoder.decodeMorse(morse1);
        System.out.println("Input: " + bits1 + " → Morse: " + morse1 + " → Text: " + text1);
        // Output: Input: 1001 → Morse: . . → Text: EE
        
        // Example 2: Simple dashes
        String bits2 = "1110111";
        String morse2 = decoder.decodeBitsAdvanced(bits2);
        String text2 = decoder.decodeMorse(morse2);
        System.out.println("Input: " + bits2 + " → Morse: " + morse2 + " → Text: " + text2);
        // Output: Input: 1110111 → Morse: -- → Text: M
    }
}
```

### Complete Message Decoding

```java
public class MessageExample {
    public static void main(String[] args) {
        MorseDecoderClean decoder = new MorseDecoderClean();
        
        // The classic "HEY JUDE" message
        String heyJudeBits = "0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000";
        
        // Step-by-step decoding
        String morse = decoder.decodeBitsAdvanced(heyJudeBits);
        System.out.println("Morse code: " + morse);
        // Output: .... . -.--   .--- ..- -.. .
        
        String text = decoder.decodeMorse(morse);
        System.out.println("Decoded text: " + text);
        // Output: HEY JUDE
        
        // One-line decoding
        String oneLineResult = decoder.decodeMorse(decoder.decodeBitsAdvanced(heyJudeBits));
        System.out.println("One-line result: " + oneLineResult);
        // Output: HEY JUDE
    }
}
```

## 🔧 Advanced Scenarios

### Variable Transmission Speeds

```java
public class VariableSpeedExample {
    public static void main(String[] args) {
        MorseDecoderClean decoder = new MorseDecoderClean();
        
        // Fast transmission - short dots and dashes
        String fastSignal = "101010"; // EE (fast)
        String fastResult = decoder.decodeMorse(decoder.decodeBitsAdvanced(fastSignal));
        System.out.println("Fast signal: " + fastResult);
        
        // Slow transmission - long dots and dashes
        String slowSignal = "111111000111111000"; // EE (slow)
        String slowResult = decoder.decodeMorse(decoder.decodeBitsAdvanced(slowSignal));
        System.out.println("Slow signal: " + slowResult);
        
        // Mixed speeds in same message
        String mixedSignal = "101111110001111110101"; // EE with varying speeds
        String mixedResult = decoder.decodeMorse(decoder.decodeBitsAdvanced(mixedSignal));
        System.out.println("Mixed speed signal: " + mixedResult);
    }
}
```

### Complex Message Processing

```java
public class ComplexMessageExample {
    public static void main(String[] args) {
        MorseDecoderClean decoder = new MorseDecoderClean();
        
        // The famous "THE QUICK BROWN FOX" message
        String complexBits = "00000000000111111100000011010001110111000000001110000000000000000001111111011111100001101111100000111100111100011111100000001011100000011111110010001111100110000011111100101111100000000000000111111100001111010110000011000111110010000011111110001111110011111110000010001111110001111111100000001111111101110000000000000010110000111111110111100000111110111110011111110000000011111001011011111000000000000111011111011111011111000000010001001111100000111110111111110000001110011111100011111010000001100001001000000000000000000111111110011111011111100000010001001000011111000000100000000101111101000000000000011111100000011110100001001100000000001110000000000000001101111101111000100000100001111111110000000001111110011111100011101100000111111000011011111000111111000000000000000001111110000100110000011111101111111011111111100000001111110001111100001000000000000000000000000000000000000000000000000000000000000";
        
        String result = decoder.decodeMorse(decoder.decodeBitsAdvanced(complexBits));
        System.out.println("Complex message: " + result);
        // Output: THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG
    }
}
```

### Real-time Processing Simulation

```java
public class RealTimeExample {
    public static void main(String[] args) {
        MorseDecoderClean decoder = new MorseDecoderClean();
        
        // Simulate receiving chunks of data
        String[] chunks = {
            "000000001101101001110000011000000111111010011111001111110000000000",
            "0111011111111011111011111000000101100011111100000111110011101100000100000"
        };
        
        StringBuilder receivedSignal = new StringBuilder();
        
        for (String chunk : chunks) {
            receivedSignal.append(chunk);
            
            // Try to decode what we have so far
            String currentMorse = decoder.decodeBitsAdvanced(receivedSignal.toString());
            String currentText = decoder.decodeMorse(currentMorse);
            
            System.out.println("Received chunk, current text: " + currentText);
        }
        
        // Final complete message
        String finalResult = decoder.decodeMorse(decoder.decodeBitsAdvanced(receivedSignal.toString()));
        System.out.println("Final message: " + finalResult);
    }
}
```

## ⚠️ Error Handling

### Robust Error Handling

```java
public class ErrorHandlingExample {
    public static void main(String[] args) {
        MorseDecoderClean decoder = new MorseDecoderClean();
        
        // Handle null input
        String nullResult = decoder.decodeMorse(null);
        System.out.println("Null input result: '" + nullResult + "'");
        // Output: Null input result: ''
        
        // Handle empty strings
        String emptyBitsResult = decoder.decodeBitsAdvanced("");
        String emptyMorseResult = decoder.decodeMorse("");
        System.out.println("Empty bits result: '" + emptyBitsResult + "'");
        System.out.println("Empty morse result: '" + emptyMorseResult + "'");
        // Output: Empty bits result: ''
        // Output: Empty morse result: ''
        
        // Handle zeros-only input
        String zerosResult = decoder.decodeBitsAdvanced("000000");
        System.out.println("Zeros-only result: '" + zerosResult + "'");
        // Output: Zeros-only result: ''
        
        // Handle invalid Morse patterns
        String invalidMorse = decoder.decodeMorse(".... --- ... ...");
        System.out.println("Invalid Morse result: '" + invalidMorse + "'");
        // Output: Invalid Morse result: 'HOSS' (or partial decoding)
    }
}
```

### Input Validation

```java
public class ValidationExample {
    public static void main(String[] args) {
        MorseDecoderClean decoder = new MorseDecoderClean();
        
        String[] testInputs = {
            "1001",           // Valid: EE
            "1110111",        // Valid: M
            "",               // Empty
            "000000",         // Zeros only
            "101010101010",   // Long pattern
            "1",              // Single bit
            "0",              // Single zero
            "101",            // Odd length
            "1100110011"      // Repeated pattern
        };
        
        for (String input : testInputs) {
            try {
                String morse = decoder.decodeBitsAdvanced(input);
                String text = decoder.decodeMorse(morse);
                System.out.printf("Input: %-12s → Morse: %-20s → Text: %s%n", 
                    "\"" + input + "\"", "\"" + morse + "\"", "\"" + text + "\"");
            } catch (Exception e) {
                System.out.printf("Input: %-12s → Error: %s%n", "\"" + input + "\"", e.getMessage());
            }
        }
    }
}
```

## ⚡ Performance Considerations

### Batch Processing

```java
public class BatchProcessingExample {
    public static void main(String[] args) {
        MorseDecoderClean decoder = new MorseDecoderClean();
        
        // Process multiple messages efficiently
        String[] messages = {
            "1001",                    // EE
            "1110111",                 // M
            "0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000", // HEY JUDE
            "101010",                  // EE (fast)
            "111111000111111000"       // EE (slow)
        };
        
        long startTime = System.nanoTime();
        
        for (String message : messages) {
            String result = decoder.decodeMorse(decoder.decodeBitsAdvanced(message));
            System.out.println("Decoded: " + result);
        }
        
        long endTime = System.nanoTime();
        double totalTime = (endTime - startTime) / 1_000_000.0;
        
        System.out.printf("Processed %d messages in %.2f ms (avg: %.2f ms per message)%n",
            messages.length, totalTime, totalTime / messages.length);
    }
}
```

### Memory Optimization

```java
public class MemoryOptimizationExample {
    public static void main(String[] args) {
        MorseDecoderClean decoder = new MorseDecoderClean();
        
        // Process large signals without memory leaks
        for (int i = 0; i < 1000; i++) {
            String largeSignal = generateLargeSignal(10000);
            String result = decoder.decodeMorse(decoder.decodeBitsAdvanced(largeSignal));
            
            // Clear references to help garbage collection
            largeSignal = null;
            result = null;
            
            if (i % 100 == 0) {
                System.gc(); // Suggest garbage collection periodically
                System.out.println("Processed " + i + " signals");
            }
        }
    }
    
    private static String generateLargeSignal(int length) {
        // Generate a large but realistic signal
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            sb.append(random.nextBoolean() ? '1' : '0');
        }
        
        return sb.toString();
    }
}
```

## 🔌 Integration Examples

### Integration with File I/O

```java
import java.io.*;
import java.nio.file.*;

public class FileIntegrationExample {
    public static void main(String[] args) throws IOException {
        MorseDecoderClean decoder = new MorseDecoderClean();
        
        // Read binary signal from file
        String signal = Files.readString(Paths.get("input_signal.txt"));
        
        // Decode the signal
        String morse = decoder.decodeBitsAdvanced(signal);
        String text = decoder.decodeMorse(morse);
        
        // Write results to files
        Files.writeString(Paths.get("output_morse.txt"), morse);
        Files.writeString(Paths.get("output_text.txt"), text);
        
        System.out.println("Decoding complete. Results saved to files.");
        System.out.println("Morse: " + morse);
        System.out.println("Text: " + text);
    }
}
```

### Integration with Network Communication

```java
import java.net.*;
import java.io.*;

public class NetworkIntegrationExample {
    public static void main(String[] args) {
        MorseDecoderClean decoder = new MorseDecoderClean();
        
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Morse decoder server listening on port 8080");
            
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    
                    // Read binary signal from client
                    String signal = in.readLine();
                    
                    // Decode and send back result
                    String result = decoder.decodeMorse(decoder.decodeBitsAdvanced(signal));
                    out.println(result);
                    
                    System.out.println("Decoded signal: " + signal + " → " + result);
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
```

### Integration with GUI Applications

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIIntegrationExample {
    private static MorseDecoderClean decoder = new MorseDecoderClean();
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIIntegrationExample::createAndShowGUI);
    }
    
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Morse Decoder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        
        // Input area for binary signal
        JTextArea inputArea = new JTextArea(10, 50);
        JScrollPane inputScroll = new JScrollPane(inputArea);
        
        // Output area for decoded text
        JTextArea outputArea = new JTextArea(5, 50);
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        
        // Decode button
        JButton decodeButton = new JButton("Decode");
        decodeButton.addActionListener(e -> {
            String signal = inputArea.getText();
            String result = decoder.decodeMorse(decoder.decodeBitsAdvanced(signal));
            outputArea.setText(result);
        });
        
        // Layout
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Binary Signal:"), BorderLayout.NORTH);
        panel.add(inputScroll, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(decodeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Decoded Text:"), BorderLayout.NORTH);
        outputPanel.add(outputScroll, BorderLayout.CENTER);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel, outputPanel);
        frame.add(splitPane);
        
        frame.setVisible(true);
    }
}
```

## 🔧 Custom Extensions

### Custom Threshold Configuration

```java
public class CustomThresholdExample {
    // Extend the decoder with custom thresholds
    static class CustomMorseDecoder extends MorseDecoderClean {
        private final double customLowMultiplier;
        private final double customHighMultiplier;
        
        public CustomMorseDecoder(double lowMultiplier, double highMultiplier) {
            this.customLowMultiplier = lowMultiplier;
            this.customHighMultiplier = highMultiplier;
        }
        
        @Override
        protected ThresholdPair calculateDefaultZeroThresholds(List<Integer> ones) {
            int minOne = ones.stream().min(Integer::compare).orElse(1);
            double lowThreshold = minOne * customLowMultiplier;
            double highThreshold = minOne * customHighMultiplier;
            return new ThresholdPair(lowThreshold, highThreshold);
        }
    }
    
    public static void main(String[] args) {
        // Create decoder with custom thresholds
        CustomMorseDecoder customDecoder = new CustomMorseDecoder(1.5, 4.0);
        
        String signal = "1001";
        String result = customDecoder.decodeMorse(customDecoder.decodeBitsAdvanced(signal));
        System.out.println("Custom decoder result: " + result);
    }
}
```

### Statistical Analysis Extension

```java
public class StatisticalAnalysisExample {
    static class AnalyzingMorseDecoder extends MorseDecoderClean {
        private int totalDecodings = 0;
        private long totalDecodingTime = 0;
        
        @Override
        public String decodeBitsAdvanced(String bits) {
            long startTime = System.nanoTime();
            String result = super.decodeBitsAdvanced(bits);
            long endTime = System.nanoTime();
            
            totalDecodings++;
            totalDecodingTime += (endTime - startTime);
            
            return result;
        }
        
        public void printStatistics() {
            double avgTime = totalDecodingTime / (double) totalDecodings / 1_000_000.0;
            System.out.printf("Total decodings: %d, Average time: %.2f ms%n", 
                totalDecodings, avgTime);
        }
    }
    
    public static void main(String[] args) {
        AnalyzingMorseDecoder analyzer = new AnalyzingMorseDecoder();
        
        // Process some signals
        String[] signals = {"1001", "1110111", "101010"};
        for (String signal : signals) {
            analyzer.decodeMorse(analyzer.decodeBitsAdvanced(signal));
        }
        
        analyzer.printStatistics();
    }
}
```

## 📝 Best Practices

1. **Always validate input** before processing
2. **Handle null and empty inputs** gracefully
3. **Use batch processing** for multiple messages
4. **Monitor memory usage** with large signals
5. **Consider caching** for repeated patterns
6. **Log important operations** for debugging
7. **Test with various transmission speeds**
8. **Implement proper error handling** in production code

## 🎯 Common Use Cases

- **Educational tools** for learning Morse code
- **Historical document analysis** for old telegraph messages
- **Radio communication** software
- **Signal processing** research
- **Accessibility tools** for visually impaired users
- **Emergency communication** systems
- **Amateur radio** applications
- **Cryptography and puzzles** involving Morse code

---

These examples demonstrate the versatility and robustness of the refactored Morse decoder. The clean architecture makes it easy to integrate into various applications while maintaining high performance and reliability.
