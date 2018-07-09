import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;

class AppTest {
    @Test
    void testAppHasMain() {
        final App classUnderTest = new App();
        assertAll("app should have a main method.",
            () -> classUnderTest.getClass().getMethod("main", String[].class));
    }
}