package homework.expression;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ComputableDoubleTest {

    @Test
    public void equals() {
        assertEquals(new ComputableDouble(10), new ComputableDouble(10));
        assertEquals(new ComputableDouble(1), new ComputableDouble(1));
        assertEquals(new ComputableDouble(0), new ComputableDouble(0));
        assertNotEquals(new ComputableDouble(0), new ComputableDouble(1));
    }
}