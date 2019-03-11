package homework.expression.core;

import homework.expression.core.interfaces.Expression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultExpFactoryTest {
    private Expression var() {
        return DefaultExpFactory.getInstance().var();
    }

    private Expression constant(long val) {
        return DefaultExpFactory.getInstance().constant(val);
    }

    @Test
    public void mulTest() {
        assert constant(3).mul(7) instanceof Constant;
        assert constant(-1).mul(-1) instanceof Constant;
        Expression actual = constant(-1).mul(-1);
        assertEquals(constant(1), actual);
        assertEquals(var().mul(4).diff(),
            constant(4));
        assertEquals(constant(2), var().pow(2).mul(-1).mul(-1).diff().diff());

    }

}