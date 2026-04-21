package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple test to verify JUnit 5 migration is working.
 * This test doesn't depend on the main application classes.
 */
@DisplayName("JUnit 5 Migration Verification")
class JUnit5MigrationTest {

    @Test
    @DisplayName("JUnit 5 basic assertion test")
    void testBasicAssertion() {
        assertEquals(2 + 2, 4, "Basic math should work");
    }

    @Test
    @DisplayName("JUnit 5 assertTrue test")
    void testTrueAssertion() {
        assertTrue(true, "True should be true");
    }

    @Test
    @DisplayName("JUnit 5 assertNotNull test")
    void testNotNullAssertion() {
        assertNotNull("JUnit 5", "String should not be null");
    }
}
