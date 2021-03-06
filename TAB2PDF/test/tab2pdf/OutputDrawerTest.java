package tab2pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class OutputDrawerTest {
	String inURL1 = "sample1.txt"; // url to a input test file
	String inURL2 = "sample2.txt";
	String outURL = "test.pdf"; // url to a output test file
	Parser p1, p2, p3; // different parsers use for test

	@Before
	public void setUp() throws Exception {
		p1 = new Parser("t1.txt");
		p2 = new Parser("t2.txt");	
		p3 = new Parser("t3.txt");
	}

	@Test
	/* Test the functionality of getters and setters */
	public void testGetterAndSetter() throws Exception {
		OutputDrawer d;
		// the exception part here is supposed to be handled in the gui so we do
		// not test it here.
		d = new OutputDrawer(inURL1, outURL);
		// test on parser
		d.setParser(p1);
		assertSame(p1, d.getParser());
		// test on currX
		d.setCurrX(180f);
		assertEquals(180.0, d.getCurrX(), 0.0001);
		// test on currY
		d.setCurrY(10f);
		assertEquals(10.0, d.getCurrY(), 0.0001);
		
		assertEquals("Moonlight Sonata",d.getTitle());
		assertEquals("Ludwig van Beethoven",d.getSubtitle());
		assertEquals(5,d.getSpacing(),0.0001);
		assertEquals(outURL,d.getDest());

		d = new OutputDrawer(inURL2, outURL);
		// test on currX
		d.setCurrX(180f);
		assertEquals(180.0, d.getCurrX(), 0.0001);
		// test on currY
		d.setCurrY(10f);
		assertEquals(10.0, d.getCurrY(), 0.0001);
		
		assertEquals("Remembering Rain",d.getTitle());
		assertEquals("Jim Matheos",d.getSubtitle());
		assertEquals(4.4,d.getSpacing(),0.0001);
		assertEquals(outURL,d.getDest());
		
		d.resetXY();
		assertEquals(d.LINEY, d.getCurrY(), 0.0001);
		assertEquals(d.BEGINX, d.getCurrX(), 0.0001);
		
		d.setTitle("Remember");
		d.setSubtitle("Jim");
		d.setSpacing(5f);
		assertEquals("Remember", d.getTitle());
		assertEquals("Jim", d.getSubtitle());
		assertEquals(5f, d.getSpacing(), 0.0001);
		
	}

	@Test
	/* Test the functionality of drawOuput.initPDF() */
	public void testInitPDF() throws DocumentException, IOException {
		// Delete test file if it already exist
		File fileTemp = new File(outURL);
		if (fileTemp.exists()) {
			fileTemp.delete();
		}

		// Init new pdf file
		OutputDrawer d = new OutputDrawer(p1, outURL);
		PdfContentByte canvas = d.initPDF(false);
		d.resetXY();
		d.DrawTitles(canvas);
		canvas.getPdfDocument().close();

		// test on the existence of the created file
		fileTemp = new File(outURL);
		assertTrue(fileTemp.exists());
	}
	@Test
	/* Test the functionality of drawOuput.checkNewLine() */
	public void testCheckNewLine() throws FileNotFoundException,
			DocumentException {
		OutputDrawer d = new OutputDrawer(p1, outURL);
		PdfContentByte canvas = d.initPDF(true);
		d.resetXY();

		// test on new line is required
		float beforeTestY = d.getCurrY();
		d.checkNewLine(d.NEW_LINE_THRESHOLD, canvas);
		assertEquals(d.BEGINX, d.getCurrX(), 0.0001);
		assertEquals(beforeTestY + d.LINEY * 2, d.getCurrY(), 0.0001);

		// test on new line is not required
		float beforeTestX = d.getCurrX();
		beforeTestY = d.getCurrY();
		d.checkNewLine(0, canvas);
		assertEquals(beforeTestX, d.getCurrX(), 0.0001);
		assertEquals(beforeTestY, d.getCurrY(), 0.0001);
	}

	@Test
	/* Test the functionality of drawOuput.checkNewPage() */
	public void testCheckNewPage() throws DocumentException, IOException {
		OutputDrawer d = new OutputDrawer(p1, outURL);
		PdfContentByte canvas = d.initPDF(false);
		d.resetXY();

		// test on new page is required
		d.DrawTitles(canvas);
		d.setCurrY(751);
		float beforeTestY = d.getCurrY();
		float beforeTestX = d.getCurrX();
		d.checkNewPage(canvas.getPdfDocument(), d.NEW_PAGE_THRESHOLD);
		assertEquals(d.LINEY, d.getCurrY(), 0.0001);
		assertEquals(d.BEGINX, d.getCurrX(), 0.0001);

		// test on new page is not required
		d.DrawTitles(canvas);
		d.setCurrY(570);
		beforeTestY = d.getCurrY();
		beforeTestX = d.getCurrX();
		d.checkNewPage(canvas.getPdfDocument(), d.NEW_PAGE_THRESHOLD);
		assertEquals(beforeTestY, d.getCurrY(), 0.0001);
		assertEquals(beforeTestX, d.getCurrX(), 0.0001);

		// test total page number
		canvas.getPdfDocument().close();
		PdfReader reader = new PdfReader(outURL);
		assertEquals(2, reader.getNumberOfPages());
		reader.close();
	}

	@Test
	/* Test the functionality of drawOuput.DrawTitles() */
	public void testDrawTitles() throws DocumentException, IOException {
		OutputDrawer d = new OutputDrawer(p1, outURL);
		PdfContentByte canvas = d.initPDF(true);
		d.resetXY();
		d.DrawTitles(canvas);
		canvas.getPdfDocument().close();

		// test position value
		float beforeTestX = d.getCurrX();
		assertEquals(d.LINEY * 3f, d.getCurrY(), 0.0001);
		assertEquals(beforeTestX, d.getCurrX(), 0.0001);

		// test text value
		PdfReader reader = new PdfReader(outURL);
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		TextExtractionStrategy strategy = parser.processContent(1,
				new SimpleTextExtractionStrategy());
		String[] title = strategy.getResultantText().split("\n");
		assertEquals(p1.title, title[0]);
		assertEquals(p1.subtitle, title[1]);

		reader.close();
	}

	@Test
	/* Test the functionality of drawOuput.DrawVerticalBar() */
	public void testDrawVerticalBar() throws DocumentException, IOException { // t3.txt
		OutputDrawer d = new OutputDrawer(p3, outURL);
		PdfContentByte canvas = d.initPDF(true);
		d.resetXY();
		d.DrawTitles(canvas);

		float beforeTestY = d.getCurrY();
		float beforeTestX = d.getCurrX();
		int vCounter = 0;
		int new_vCounter = d.DrawVerticalBar(0, 0, canvas, d.getCurrX(),
				d.getCurrY(), vCounter);

		// test on return value
		assertEquals(vCounter + 1, new_vCounter);
		canvas.getPdfDocument().close();

		// test on x, y position values
		assertEquals(beforeTestY, d.getCurrY(), 0.0001);
		assertEquals(beforeTestX, d.getCurrX(), 0.0001);

	}

	@Test
	/* Test the functionality of drawOuput.DrawSegment(x) */
	public void testDrawSegment() throws DocumentException, IOException {
		OutputDrawer d = new OutputDrawer(p3, outURL);
		PdfContentByte canvas = d.initPDF(true);
		d.resetXY();
		d.DrawTitles(canvas);

		float beforeTestY = d.getCurrY();
		float beforeTestX = d.getCurrX();
		int i = 4;
		d.DrawSegment(i, canvas);

		// test on position value change
		assertEquals(beforeTestY, d.getCurrY(), 0.0001);
		assertEquals(
				beforeTestX + p3.input.get(i).get(0).length() * p3.spacing,
				d.getCurrX(), 0.0001);

	}

	@Test
	/* Test the functionality of drawOuput.createPdf() */
	public void testCreatePdf() throws DocumentException, IOException {
		Parser p = p3;
		OutputDrawer d = new OutputDrawer(inURL2, outURL);
		d.setParser(p3);
		d.createPdf(false);

		// Get expect value of x and y
		float ex = d.BEGINX, ey = d.LINEY * 3f;
		for (int i = 0; i < p.input.size(); i++) {
			ex += p.input.get(i).get(0).length() * p.spacing;

			if (ex > d.NEW_LINE_THRESHOLD) {
				// reset x on new line and add the width of the segment
				ex = d.BEGINX + p.input.get(i).get(0).length() * p.spacing;
				// update y to move to next line
				ey += d.LINEY * 2;
				if (ey > OutputDrawer.NEW_PAGE_THRESHOLD) { // need update y for
															// new page
					ey = d.LINEY;
				}
			}
		}

		// test on position value at end of file
		assertEquals(ex, d.getCurrX(), 0.0001);
		assertEquals(ey, d.getCurrY(), 0.0001);
	}
}
