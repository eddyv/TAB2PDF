package tab2pdf;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class Main_Test {
	String src;
	String dest;
	Main main;

	@Before
	public void setUp() throws Exception {
		// hard coded values for src and dest strings
		src = "C:\\Users\\Edward\\Documents\\test.txt";
		src = "C:\\Users\\Edward\\Documents\\test.pdf";
		main = new Main();
	}

	@Test
	public void testSaveFile() {
		assertEquals(src, main.saveFile());
	}

	@Test
	public void testOpenFile() {
		assertEquals(dest, main.openFile());
	}

}
