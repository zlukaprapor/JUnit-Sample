package sumdu.edu.ua;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorsTest {



    @Test
    void equalCheck() {
        System.out.println("* VectorsJUnit5Test: equalCheck()");
        assertTrue(Vectors.equal(new int[] {}, new int[] {}));
        assertTrue(Vectors.equal(new int[] {0}, new int[] {0}));
        assertTrue(Vectors.equal(new int[] {0, 0}, new int[] {0, 0}));
        assertTrue(Vectors.equal(new int[] {0, 0, 0}, new int[] {0, 0, 0}));
        assertTrue(Vectors.equal(new int[] {5, 6, 7}, new int[] {5, 6, 7}));
        assertFalse(Vectors.equal(new int[] {}, new int[] {0}));
        assertFalse(Vectors.equal(new int[] {0}, new int[] {0, 0}));
        assertFalse(Vectors.equal(new int[] {0, 0}, new int[] {0, 0, 0}));
        assertFalse(Vectors.equal(new int[] {0, 0, 0}, new int[] {0, 0}));
        assertFalse(Vectors.equal(new int[] {0, 0}, new int[] {0}));
        assertFalse(Vectors.equal(new int[] {0}, new int[] {}));
        assertFalse(Vectors.equal(new int[] {0, 0, 0}, new int[] {0, 0, 1}));
        assertFalse(Vectors.equal(new int[] {0, 0, 0}, new int[] {0, 1, 0}));
        assertFalse(Vectors.equal(new int[] {0, 0, 0}, new int[] {1, 0, 0}));
        assertFalse(Vectors.equal(new int[] {0, 0, 1}, new int[] {0, 0, 3}));
    }

    @Test
    void scalarMultiplicationCheck() {
        System.out.println("* VectorsJUnit5Test: ScalarMultiplicationCheck()");
        assertEquals( 0, Vectors.scalarMultiplication(new int[] { 0, 0}, new int[] { 0, 0}));
        assertEquals( 39, Vectors.scalarMultiplication(new int[] { 3, 4}, new int[] { 5, 6}));
        assertEquals(-39, Vectors.scalarMultiplication(new int[] {-3, 4}, new int[] { 5,-6}));
        assertEquals( 0, Vectors.scalarMultiplication(new int[] { 5, 9}, new int[] {-9, 5}));
        assertEquals(100, Vectors.scalarMultiplication(new int[] { 6, 8}, new int[] { 6, 8}));
    }
}