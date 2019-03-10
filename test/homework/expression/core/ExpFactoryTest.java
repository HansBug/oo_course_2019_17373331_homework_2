package homework.expression.core;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class ExpFactoryTest {
    private Expression constant(long val) {
        return ExpFactory.constant(val);
    }

    private Expression mul(long a, Expression b) {
        return ExpFactory.mul(a, b);
    }

    private Expression mul(long a, long b) {
        return mul(a, constant(b));
    }

    private Expression pow(Expression base, BigInteger index) {
        return ExpFactory.pow(base, index);
    }

    private Expression pow(Expression base, long index) {
        return pow(base, BigInteger.valueOf(index));
    }

    @Test
    public void mulTest() {
        assert mul(3, 7) instanceof Constant;
        assert mul(-1, -1) instanceof Constant;
        Constant actual = (Constant) mul(-1, -1);
        assertEquals(constant(1), actual);
        assertEquals(mul(4, ExpFactory.var()).diff(), constant(4));
        assertEquals(constant(2),
            mul(-1, mul(-1, pow(ExpFactory.var(), 2))).diff().diff());

    }

}