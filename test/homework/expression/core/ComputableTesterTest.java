package homework.expression.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ComputableTesterTest {

    @Test
    public void equals() {
        assertEquals(new ComputableTester(10), new ComputableTester(10));
        assertEquals(new ComputableTester(1), new ComputableTester(1));
        assertEquals(new ComputableTester(0), new ComputableTester(0));
        assertNotEquals(new ComputableTester(0), new ComputableTester(1));
    }
}