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
		s.add("This is a test string");
		s.add("This is 2nd test string");
		s.add("This is final test string");
	}

	@Test
	public void testIsEmpty()
	{
		// test empty
		assertFalse(s.isEmpty());
	}
	
	@Test
	public void testGet()
	{

		// test get
		assertEquals("This is final test string", s.get(2));
	}
	
	@Test
	public void testSize()
	{
		// test size
		assertEquals(3, s.size());
	}
	@Test
	public void testRemove()
	{
		//removes the first line
		s.remove(0);
		assertEquals("This is 2nd test string",s.get(0));
		assertEquals(2,s.size());
		
	}

}
