package org.example;

/**
 * Main test runner for the Morse decoder test suite.
 * Orchestrates all test classes in a structured tree hierarchy.
 * 
 * Test Tree Structure:
 * ├── BasicFunctionalityTest
 * ├── ConfigurationTest
 * ├── PerformanceTest
 * ├── EdgeCaseTest
 * ├── ComplexMessageTest
 * └── SpecificTestCaseTest
 */
public class MorseDecoderTest {

    public static void main(String[] args) {
        System.out.println("=== Morse Decoder Test Suite ===");
        System.out.println("Unified test tree structure with comprehensive coverage\n");
        
        boolean allTestsPassed = true;
        
        try {
            // Run all test categories
            allTestsPassed &= runTestCategory("Basic Functionality", BasicFunctionalityTest::runAllTests);
            allTestsPassed &= runTestCategory("Configuration", ConfigurationTest::runAllTests);
            allTestsPassed &= runTestCategory("Performance", PerformanceTest::runAllTests);
            allTestsPassed &= runTestCategory("Edge Cases", EdgeCaseTest::runAllTests);
            allTestsPassed &= runTestCategory("Complex Messages", ComplexMessageTest::runAllTests);
            allTestsPassed &= runTestCategory("Specific Test Cases", SpecificTestCaseTest::runAllTests);
            
            // Run additional validation tests
            allTestsPassed &= runValidationTests();
            
        } catch (Exception e) {
            System.err.println("Test suite execution failed: " + e.getMessage());
            allTestsPassed = false;
        }
        
        // Print final summary
        printFinalSummary(allTestsPassed);
        
        // Exit with appropriate code
        System.exit(allTestsPassed ? 0 : 1);
    }

    private static boolean runTestCategory(String categoryName, Runnable testRunner) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("RUNNING: " + categoryName.toUpperCase());
        System.out.println("=".repeat(50));
        
        try {
            testRunner.run();
            System.out.println("✓ " + categoryName + " tests completed successfully");
            return true;
        } catch (Exception e) {
            System.err.println("✗ " + categoryName + " tests failed: " + e.getMessage());
            return false;
        }
    }

    private static boolean runValidationTests() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("RUNNING: VALIDATION TESTS");
        System.out.println("=".repeat(50));
        
        boolean allValidationsPassed = true;
        
        // Quick validation tests
        System.out.println("Running quick validations:");
        
        boolean basicValidation = BasicFunctionalityTest.quickValidation();
        System.out.printf("  Basic functionality: %s%n", basicValidation ? "PASSED" : "FAILED");
        allValidationsPassed &= basicValidation;
        
        boolean accuracyValidation = ComplexMessageTest.testAccuracy();
        System.out.printf("  Message accuracy: %s%n", accuracyValidation ? "PASSED" : "FAILED");
        allValidationsPassed &= accuracyValidation;
        
        // Configuration validation
        try {
            ConfigurationTest.testConfigurationValidation();
            System.out.println("  Configuration validation: PASSED");
        } catch (Exception e) {
            System.out.println("  Configuration validation: FAILED");
            allValidationsPassed = false;
        }
        
        // Performance benchmark
        System.out.println("Running performance benchmark:");
        PerformanceTest.quickBenchmark();
        
        return allValidationsPassed;
    }

    private static void printFinalSummary(boolean allTestsPassed) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FINAL TEST SUMMARY");
        System.out.println("=".repeat(60));
        
        if (allTestsPassed) {
            System.out.println("🎉 ALL TESTS PASSED SUCCESSFULLY! 🎉");
            System.out.println("\nTest Tree Coverage:");
            System.out.println("  ✓ Basic Functionality Tests");
            System.out.println("  ✓ Configuration Tests");
            System.out.println("  ✓ Performance Tests");
            System.out.println("  ✓ Edge Case Tests");
            System.out.println("  ✓ Complex Message Tests");
            System.out.println("  ✓ Specific Test Case Tests");
            System.out.println("  ✓ Validation Tests");
            System.out.println("\nThe Morse decoder implementation is working correctly!");
        } else {
            System.out.println("❌ SOME TESTS FAILED ❌");
            System.out.println("\nPlease check the test output above for details.");
            System.out.println("Review the failed test cases and fix any issues.");
        }
        
        System.out.println("=".repeat(60));
    }

    /**
     * Runs a specific test category.
     * Useful for targeted testing during development.
     */
    public static void runSpecificCategory(String category) {
        switch (category.toLowerCase()) {
            case "basic":
                BasicFunctionalityTest.runAllTests();
                break;
            case "config":
                ConfigurationTest.runAllTests();
                break;
            case "performance":
                PerformanceTest.runAllTests();
                break;
            case "edge":
                EdgeCaseTest.runAllTests();
                break;
            case "complex":
                ComplexMessageTest.runAllTests();
                break;
            case "specific":
                SpecificTestCaseTest.runAllTests();
                break;
            default:
                System.err.println("Unknown test category: " + category);
                System.out.println("Available categories: basic, config, performance, edge, complex, specific");
        }
    }

    /**
     * Quick test run for development validation.
     */
    public static void quickTest() {
        System.out.println("=== Quick Test Run ===");
        
        boolean basicPassed = BasicFunctionalityTest.quickValidation();
        boolean accuracyPassed = ComplexMessageTest.testAccuracy();
        
        System.out.printf("Quick test result: %s%n", 
            (basicPassed && accuracyPassed) ? "PASSED" : "FAILED");
    }
}
