package tab2pdf;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GUI_Main_Test {
	String src;
	String dest;
	GUI_Main main;
	@Before
	public void setUp() throws Exception {
		//hard coded values for src and dest strings
		src="C:\\Users\\Edward\\Documents\\test.txt";
		src="C:\\Users\\Edward\\Documents\\test.pdf";
		main = new GUI_Main();
	}

	@Test
	public void testSaveFile() {
		assertEquals(src,main.saveFile());
	}

	@Test
	public void testOpenFile() {
		assertEquals(dest,main.openFile());
	}

}
