package tab2pdf;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ OutputDrawerTest.class,
		ParserTest.class, SegmentTest.class, Main_Test.class })
public class AllTests {

}
