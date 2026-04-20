package org.example;

/**
 * Test class for Morse decoder performance.
 * Tests execution time with various signal sizes and configurations.
 */
public class PerformanceTest {

    public static void runAllTests() {
        TestUtilities.printSectionHeader("Performance Tests");
        
        testScalability();
        testConfigurationPerformance();
        testMemoryUsage();
        
        System.out.println("Performance tests completed.");
    }

    private static void testScalability() {
        System.out.println("Testing scalability with increasing signal sizes:");
        
        MorseDecoder decoder = new MorseDecoder();
        int[] sizes = {100, 500, 1000, 5000, 10000};
        
        for (int size : sizes) {
            String signal = TestUtilities.generateRandomSignal(size);
            
            long totalTime = TestUtilities.measureExecutionTime(() -> {
                String morse = decoder.decodeBitsAdvanced(signal);
                decoder.decodeMorse(morse);
            });
            
            double timeMs = TestUtilities.formatNanosToMillis(totalTime);
            System.out.printf("  Size %6d: %8.2fms%n", size, timeMs);
        }
    }

    private static void testConfigurationPerformance() {
        System.out.println("Testing performance with different configurations:");
        
        MorseDecoderConfig[] configs = {
            MorseDecoderConfig.defaultConfig(),
            MorseDecoderConfig.forFastTransmission(),
            MorseDecoderConfig.forSlowTransmission(),
            MorseDecoderConfig.forPrecisionDecoding()
        };
        
        String[] configNames = {"Default", "Fast", "Slow", "Precision"};
        String testSignal = TestUtilities.generateRandomSignal(1000);
        
        for (int i = 0; i < configs.length; i++) {
            MorseDecoder decoder = new MorseDecoder(configs[i]);
            
            long totalTime = TestUtilities.measureExecutionTime(() -> {
                String morse = decoder.decodeBitsAdvanced(testSignal);
                decoder.decodeMorse(morse);
            });
            
            double timeMs = TestUtilities.formatNanosToMillis(totalTime);
            System.out.printf("  %-12s: %8.2fms%n", configNames[i], timeMs);
        }
    }

    private static void testMemoryUsage() {
        System.out.println("Testing memory usage patterns:");
        
        MorseDecoder decoder = new MorseDecoder();
        Runtime runtime = Runtime.getRuntime();
        
        // Force garbage collection
        System.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        
        // Process multiple signals
        for (int i = 0; i < 100; i++) {
            String signal = TestUtilities.generateRandomSignal(1000);
            String morse = decoder.decodeBitsAdvanced(signal);
            decoder.decodeMorse(morse);
        }
        
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;
        
        System.out.printf("  Memory used for 100 iterations: %d KB%n", memoryUsed / 1024);
        System.out.printf("  Average memory per operation: %.2f KB%n", memoryUsed / 1024.0 / 100);
    }

    /**
     * Runs a quick performance benchmark.
     */
    public static void quickBenchmark() {
        MorseDecoder decoder = new MorseDecoder();
        String signal = TestUtilities.HEY_JUDE_BITS;
        
        long time = TestUtilities.measureExecutionTime(() -> {
            String morse = decoder.decodeBitsAdvanced(signal);
            decoder.decodeMorse(morse);
        });
        
        System.out.printf("Quick benchmark: %.2fms%n", TestUtilities.formatNanosToMillis(time));
    }
}
