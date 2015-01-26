import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class GUI_MainTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testOpenFile() {
		assertEquals("/cs/home/eddyv/a6.txt",new GUI_Main().OpenFile());
	}

	@Test
	public void testSaveFile() {
		assertEquals("/cs/home/eddyv/Assignment_1.pdf",new GUI_Main().SaveFile());
	}
	
	
	

}
