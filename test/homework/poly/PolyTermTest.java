package homework.poly;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PolyTermTest {

    @Test
    public void getDerivative() {
        PolyTerm term = new PolyTerm(10, -2);
        assertEquals(new PolyTerm(9, -20), term.getDerivative());
        term = new PolyTerm(-10, -2);
        assertEquals(new PolyTerm(-11, 20), term.getDerivative());
    }
}