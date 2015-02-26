package tab2pdf;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(
{ DrawOutputDebugTest.class, drawOutputTest.class, ParserTest.class,
		SegmentTest.class })
public class AllTests
{

}
