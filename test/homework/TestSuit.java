package homework;

import homework.poly.PolyExpTest;
import homework.poly.PolyTermTest;
import homework.poly.parse.PolyParserTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    MainTest.class,
    PolyTermTest.class,
    PolyExpTest.class,
    PolyParserTest.class
})
public class TestSuit {
    // the class remains empty,
    // used only as a holder for the above annotations
}