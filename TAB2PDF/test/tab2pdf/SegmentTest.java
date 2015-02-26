package tab2pdf;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SegmentTest
{
	private Segment s;
	
	@Before
	public void setUp() throws Exception
	{
		s = new Segment();
	}

	@Test
	public void testIsEmpty()
	{
		// test empty
		assertTrue(s.isEmpty());
	}
	
	@Test
	public void testNotEmpty()
	{
		s.add("This is a test string");
		s.add("This is 2nd test string");
		s.add("This is final test string");
		
		// test size
		assertEquals(3, s.size());
		// test get
		assertEquals("This is final test string", s.get(2));
	}

}
