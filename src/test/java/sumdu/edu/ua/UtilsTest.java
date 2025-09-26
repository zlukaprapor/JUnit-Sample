package sumdu.edu.ua;

import org.junit.jupiter.api.*;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @BeforeAll
    public static void setUpClass() throws Exception {
        System.out.println("* UtilsJUnit5Test: @BeforeAll method");
    }
    @AfterAll
    public static void tearDownClass() throws Exception {
        System.out.println("* UtilsJUnit5Test: @AfterAll method");
    }
    @BeforeEach
    public void setUp() {
        System.out.println("* UtilsJUnit5Test: @BeforeEach method");
    }
    @AfterEach
    public void tearDown() {
        System.out.println("* UtilsJUnit5Test: @AfterEach method");
    }
    @Test
    void helloWorldCheck() {
        System.out.println("* UtilsJUnit5Test:test method 1 - helloWorldCheck()");
        assertEquals("Hello, world!",
                Utils.concatWords("Hello", ", ", "world", "!"),
                "* Test method 1 is failed");
    }
    @Test
    void testWithTimeout() {
        System.out.println("* UtilsJUnit5Test: test method 2 - testWithTimeout()");
        assertTimeout(
                ofSeconds(1),
                () -> {
                    final int factorialOf = 1 + (int) (30000 * Math.random());
                    System.out.println("computing " + factorialOf + '!');
                },
                "* Test method 2 is failed"
        );
    }
    @Test
    public void checkExpectedException() {
        System.out.println("* UtilsJUnit5Test: test method 3 – checkExpectedException()");
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    final int factorialOf = -5;
                    System.out.println(factorialOf + "! = " +
                            Utils.computeFactorial(factorialOf));
                },
                "* Test method 3 is failed"
        );
    }
    @Disabled("Do not run")
    @Test
    public void temporarilyDisabledTest() throws Exception {
        System.out.println("* UtilsJUnit4Test: test method 4 – checkExpectedException()");
        assertEquals("Malm\u00f6", Utils.normalizeWord("Malmo\u0308"));
    }


}