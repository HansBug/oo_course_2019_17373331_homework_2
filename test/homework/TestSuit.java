package homework;

import homework.expression.ComputableDoubleTest;
import homework.expression.core.DefaultExpFactoryTest;
import homework.tri.TriExpTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ComputableDoubleTest.class,
    DefaultExpFactoryTest.class,
    TriExpTest.class,
    MainTest.class,
})
public class TestSuit {
    // the class remains empty,
    // used only as a holder for the above annotations
}