package tab2pdf;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ParserTest
{
	
	@Before
	public void setUp() throws Exception{
	}
	
	@Test
	public void testFile1() throws IOException
	{
		Parser p = new Parser("test1.txt");
		// test title
		assertEquals("Moonlight Sonata", p.title);
		// test subtitle
		assertEquals("Ludwig van Beethoven", p.subtitle);
		// test space
		assertEquals(5.0, p.spacing, 0.0001);
		// test segments
		assertEquals("-2-----2-----2-----2-----", p.input.get(0).get(3));	// input 
		assertEquals("|", p.vLinesInput.get(0).get(0));					// vertical input
	}
	
	@Test
	public void testFile2() throws IOException
	{
		Parser p = new Parser("test2.txt");
		// test title
		assertEquals("Remembering Rain", p.title);
		// test subtitle
		assertEquals("Jim Matheos", p.subtitle);
		// test space
		assertEquals(4.4, p.spacing, 0.0001);
		// test segments
		assertEquals("------------------------------<12>----------", p.input.get(0).get(0));	// input 
		assertEquals("*||", p.vLinesInput.get(2).get(7));	// vertical input
	}
	
	@Test
	public void testFile3() throws IOException
	{
		Parser p = new Parser("test3.txt");
		// test title
		assertEquals("This is 3rd test", p.title);
		// test subtitle
		assertEquals("and the subtitle", p.subtitle);
		// test space
		assertEquals(2.45, p.spacing, 0.0001);
		// test segments
		assertEquals("-3-----3-----3-----3------2-----2-----2-----0-----", p.input.get(0).get(3));	// input 
		assertEquals("|", p.vLinesInput.get(0).get(11));	// vertical input
	}
	
	// test array list values
}
