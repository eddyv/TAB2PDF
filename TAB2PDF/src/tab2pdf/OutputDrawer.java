package tab2pdf;

/*
 * changes:
 * 
 * create separate method for the initial creation of the document
 * and the setting of the document size
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class OutputDrawer
{
	
	/**
	 * Attributes
	 */
	public static final float SIZEX = 612;					// width of the page
	public static final float SIZEY = 792;					// height of the page
	public static final float LINEY = SIZEY / 22;			// height of the segment 
	public static final float SEGY = LINEY / 5;				// height of a single line 
	public static final float BEGINX = SIZEX / 15;			// starting point of x
	public static final int NEW_LINE_THRESHOLD = 570;		// threshold use to determine whether a new line is needed or not
	public static final int NEW_PAGE_THRESHOLD = 750;		// threshold use to determine whether a new page is needed or not
	public static Symbol drawSymbol = new Symbol(SIZEY);	// utility class for drawing element on PDF
	public static final int NUM_SPACE = 5;
	private Parser parser;	// parser of the input txt file
	private float currX;	// value of current X coordinate on the pdf document
	private float currY;	// value of current Y coordinate on the pdf document
	private String dest;
	private static String localDest = "results.pdf"; //if dest isn't provided we should use the local one!
	
	/**
	 * Constructors 
	 */
	/* create new drawOutput object using path string of the txt file */
	public OutputDrawer(String src, String dest, boolean customTitle, boolean customSubtitle, boolean customSpacing, String title, String subtitle, float spacing) throws IOException
	{
		Parser p = new Parser(src);
		this.dest = dest;
		if(customTitle==true)
		{
			p.title=title;
		}
		if(customSpacing==true)
		{
			p.spacing=spacing;
		}
		if(customSubtitle==true)
		{
			p.subtitle=subtitle;
		}
		this.setParser(p);
	}
	
	/* create new drawOutput object using Parser */
	public OutputDrawer(Parser p, String dest){
		this.setParser(p);	
		this.dest=dest;
	}
	
	/* create new drawOutput object using path string of the txt file, does not use any custom features*/
	public OutputDrawer(String src, String dest) throws IOException{
		Parser p = new Parser(src);
		this.dest=dest;
		this.setParser(p);
	}
	
	/**
	 * Getters
	 */
	public Parser getParser(){
		return this.parser;
	}
	
	public float getCurrX(){
		return this.currX;
	}
	
	public float getCurrY(){
		return this.currY;
	}
	
	public String getTitle()
	{
		Parser a = this.getParser();
		return a.title;
	}
	
	public String getSubtitle()
	{
		Parser a = this.getParser();
		return a.subtitle;
	}
	
	public float getSpacing()
	{
		Parser a = this.getParser();
		return a.spacing;
	}
	public String getDest()
	{
		return this.dest;
	}
	public static String getLocalDest()
	{
		return localDest;
	}
	
	/**
	 * Setters
	 */
	public void setParser(Parser p){
		this.parser = p;
	}
	
	public void setCurrX(float x){
		this.currX = x;
	}
	
	public void setCurrY(float y){
		this.currY = y;
	}

	public void setTitle(String title)
	{
		Parser a = this.getParser();
		a.title = title;
	}
	
	public void setSubtitle(String subtitle)
	{
		Parser a = this.getParser();
		a.subtitle = subtitle;
	}
	
	public void setSpacing(float spacing)
	{
		Parser a = this.getParser();
		a.spacing = spacing;
	}
	/**
	 * Methods
	 */
	/*
	 * Initialize a pdf file
	 */
	public PdfContentByte initPDF(boolean useLocalDest) throws FileNotFoundException, DocumentException
	{
		Rectangle pageSize = new Rectangle(SIZEX, SIZEY);
		Document document = new Document(pageSize);
		PdfWriter writer;
		if(useLocalDest == false)
		{
			writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
		}
		else
		{
			writer = PdfWriter.getInstance(document, new FileOutputStream(localDest));
		}
		document.open();
		PdfContentByte canvas = writer.getDirectContent();
		
		return canvas;
	}
	
	/*
	 * Reset the value of currX and currY ready for a new pdf document
	 */
	public void resetXY(){
		this.setCurrX(BEGINX);
		this.setCurrY(LINEY);
	}
	
	/*
	 * check size of current bar and test if it will fit within the page width when drawn,
	 * if it does not fit, then continue in the if statement to end the current line with
	 * a vertical line, then start on a new line
	 */
	public void checkNewLine(float segLength, PdfContentByte canvas)
	{
		if(segLength + currX > NEW_LINE_THRESHOLD)
		{
			// draw ending lines
			for(int k = 0; k < 6; k++)
			{
				drawSymbol.createHLineAtPosition(canvas, currX,currY + k * SEGY, SIZEX - currX);
			}
			
			// update position info
			currX = BEGINX;
			currY += LINEY * 2;
		}
	}
	
	/*
	 * Check to see if a new page is need to be create in the document
	 */
	public void checkNewPage(Document document, int thresh)
	{
		// start on new page if needed
		if (currY > thresh)
		{
			document.newPage();	// create new page
			this.resetXY();		// reset x and y
		}
	}
	
	/*
	 * Draw title of the document
	 */
	public void DrawTitles(PdfContentByte canvas) throws DocumentException, IOException
	{
		Parser a = this.getParser();
		
		drawSymbol.createTextCenteredAtPosition(canvas, a.title, SIZEX / 2, LINEY * 1.5f, 24);
		drawSymbol.createTextCenteredAtPosition(canvas, a.subtitle, SIZEX / 2, LINEY * 2f, 12);
		
		// update currY
		this.setCurrY(LINEY * 3f);
	}
	
	/*
	 * Draw vertical bars on desired position and return the updated vCounter parameter
	 */
	public int DrawVerticalBar(int i, int j, PdfContentByte canvas, float currX, float currY, int vCounter) throws DocumentException, IOException
	{
//		System.out.println("CurrX : " + currX + " CurrY: " + currY +" vCounter: " + vCounter);
		Parser a = this.getParser();
		String vLines = a.vLinesInput.get(i).get(vCounter);

		if (vLines.equals("|||"))
		{
			
			drawSymbol.createVLineAtPosition(canvas, currX, currY,LINEY);
			drawSymbol.createVLineAtPosition(canvas, currX + a.spacing, currY,LINEY);
			drawSymbol.createVLineAtPosition(canvas, currX + 2 * a.spacing, currY,LINEY);
		}
		else if (vLines.equals("||"))
		{
			if (vCounter % 2 == 0)
			{
				if (a.vLinesInput.get(i).get(4).equals("||*"))
				{
					drawSymbol.createLDoubleBar(canvas, currX, currY, LINEY);
				}
				else
				{
					drawSymbol.createVLineAtPosition(canvas, currX, currY,LINEY);
				}
			}
			else
			{
				if (a.vLinesInput.get(i).get(4).equals("*||"))
				{
					drawSymbol.createRDoubleBar(canvas, currX, currY, LINEY);	
				}
				else
				{
					drawSymbol.createVLineAtPosition(canvas, currX, currY,LINEY);
				}
			}
			currX += a.input.get(i).get(j).length() * a.spacing;
		}
		else if (vLines.equals("*||") && vCounter % 2 == 1)
		{
			drawSymbol.createRDoubleBar(canvas, currX, currY, LINEY);
			drawSymbol.createCircle(canvas, currX - 9, currY + j * SEGY, a.spacing); //
			drawSymbol.createHLineAtPosition(canvas, currX, currY + j * SEGY, a.spacing);
			currX += a.input.get(i).get(j).length() * a.spacing;
		}
		else if (vLines.equals("||*") && vCounter % 2 == 0)
		{
			drawSymbol.createLDoubleBar(canvas, currX, currY, LINEY);
			drawSymbol.createHLineAtPosition(canvas, currX, currY + j * SEGY, a.spacing);
			drawSymbol.createCircle(canvas, currX + 4, currY + j * SEGY, a.spacing);
			currX += a.input.get(i).get(j).length() * a.spacing;
		}
		else if (vLines.matches("\\|\\d") && vCounter % 2 == 1)
		{
			drawSymbol.createTextCenteredAtPosition(canvas, "Repeat " + vLines.charAt(1) + " times", currX - 5 * a.spacing, currY - 1.5f * 5, 8);
		}
		else if (vLines.equals("|"))
		{
			drawSymbol.createVLineAtPosition(canvas, currX, currY, LINEY);
		}
		vCounter++;
		return vCounter;
	}
	
	/*
	 * Draw single segment
	 */
	public void DrawSegment(int i, PdfContentByte canvas) throws DocumentException, IOException
	{
		Parser a = this.getParser();
		
		for(int j = 0; j < a.input.get(i).size(); j++) // j = line number in bar
		{	
			//initialize counter for vLinesInput line number
			int vCounter = j * 2;
			
			// draw beginning filler lines if neccessary
			if(currX == BEGINX){
				drawSymbol.createHLineAtPosition(canvas, 0, currY + j * SEGY,BEGINX);
			}
			
			// Draws beginning vertical bars
			vCounter = DrawVerticalBar(i, j, canvas, currX, currY, vCounter);

			
			String tempString = a.input.get(i).get(j);
			
			//checking for accepted symbols, add to regular expression for any new symbols
			//Pattern pattern = Pattern.compile("<\\d+>|\\d+|(-*)(p|h)([-#=]*)|s|(-+)");
			Pattern pattern = Pattern.compile("<\\d+>|\\d+|(\\D*)(p|h)(\\D*)|s|(-+)");
		    Matcher matcher = pattern.matcher(tempString);
	
		    int numberIndex = 0;	// counts number of diamonds or digits in a line
		    List<Integer> startIndex = new ArrayList<Integer>();
		    List<Integer> endIndex = new ArrayList<Integer>();
		    while (matcher.find()) 	// stores start and end position indexes for symbols for each line
		    {
		    	startIndex.add(matcher.start());
		    	endIndex.add(matcher.end());
		        
		        //System.out.println("start " + matcher.start());
		    	//System.out.println("end " + matcher.end());
		    	//System.out.println(matcher.group());
		    }
			
		    //check cases to draw symbols as needed according to the regular expression
			
			for (int l = 0; l < tempString.length();)
			{
				if (numberIndex < startIndex.size())
				{
					if (l == startIndex.get(numberIndex))
					{
						String tempSymbol = tempString.substring(startIndex.get(numberIndex), endIndex.get(numberIndex));
						int difference = endIndex.get(numberIndex) - startIndex.get(numberIndex);
						
						if (tempSymbol.matches("<\\d\\d>"))	// is a 2 digit diamond
						{
							String diamondNumber = tempSymbol.substring(1, tempSymbol.length() - 1);
							drawSymbol.createTextCenteredAtPosition(canvas, diamondNumber, currX + 	(l + 1f)* a.spacing, currY + j * SEGY + 0.5f * 5, 8);
							drawSymbol.createHLineAtPosition(canvas, currX + (l + difference - 2) * a.spacing, currY + j * SEGY, a.spacing);
							drawSymbol.createHLineAtPosition(canvas, currX + (l + difference - 1) * a.spacing, currY + j * SEGY, a.spacing);
							drawSymbol.createDiamond(canvas,  currX + (l + difference - 1) * a.spacing,  currY + j * SEGY - 0.5f * 4f, 2);
						}
						else if (tempSymbol.matches("<\\d>"))	// is a 1 digit diamond
						{
							String diamondNumber = tempSymbol.substring(1, tempSymbol.length() - 1);
							drawSymbol.createTextCenteredAtPosition(canvas, diamondNumber, currX + 	(l + 0.5f)* a.spacing, currY + j * SEGY + 0.5f * 5, 8);
							drawSymbol.createHLineAtPosition(canvas, currX + (l + difference - 2) * a.spacing, currY + j * SEGY, a.spacing);
							drawSymbol.createHLineAtPosition(canvas, currX + (l + difference - 1) * a.spacing, currY + j * SEGY, a.spacing);
							drawSymbol.createDiamond(canvas,  currX + (l + difference - 1) * a.spacing,  currY + j * SEGY - 0.5f * 4f, 2);
						}
						else if (tempSymbol.matches("(\\D*)(p)(\\D*)"))  //(-*)(p)([-#=]*)	//pull off
						{
							drawSymbol.createArc(canvas, currY + (j - 0.75f) * SEGY, currY + (j - 0.25f) * SEGY, currX + (l + 0.50f) * a.spacing, currX + (l + difference - 0.50f) * a.spacing, a.spacing);
							drawSymbol.createHLine(canvas, currX + l * a.spacing, currX + (l + difference - 1) * a.spacing, currY + j * SEGY, a.spacing);
							drawSymbol.createTextCenteredAtPosition(canvas, "p" , ((currX + (l + 0.5f) * a.spacing) + (currX + (l + difference - 0.5f) * a.spacing))/2, currY + (j - 1.25f) * SEGY + 0.5f * 7, 5);
						}
						else if (tempSymbol.matches("(\\D*)(h)(\\D*)"))  //(-*)(h)([-#=]*)	//hammer on
						{
							drawSymbol.createArc(canvas, currY + (j - 0.75f) * SEGY, currY + (j - 0.25f) * SEGY, currX + (l + 0.50f) * a.spacing, currX + (l + difference - 0.50f) * a.spacing, a.spacing);
							drawSymbol.createHLine(canvas, currX + l * a.spacing, currX + (l + difference - 1) * a.spacing, currY + j * SEGY, a.spacing);
							drawSymbol.createTextCenteredAtPosition(canvas, "h" , ((currX + (l + 0.5f) * a.spacing) + (currX + (l + difference - 0.5f) * a.spacing))/2, currY + (j - 1.25f) * SEGY + 0.5f * 7, 5);
						}
						else if (tempSymbol.matches("s")) // is a s
						{
							drawSymbol.createS(canvas, currX + l * a.spacing, currY + j * SEGY, a.spacing);
						}
						else if (tempSymbol.matches("\\d\\d"))	// is a 2 digit number
						{
							drawSymbol.createTextCenteredAtPosition(canvas, tempSymbol, currX + 
									(l + 0.5f)	* a.spacing, currY + j * SEGY + 0.5f * 5, 8);
							drawSymbol.createHLineAtPosition(canvas, currX + (l + 1.3f) * a.spacing, currY + j * SEGY, a.spacing);
						}
						else if (tempSymbol.matches("\\d"))	// is a single digit number
						{
							drawSymbol.createTextCenteredAtPosition(canvas, tempSymbol, currX + 
									(l + 0.5f)	* a.spacing, currY + j * SEGY + 0.5f * 5, 8);
						}
						else if (tempSymbol.matches("(-+)")) // is horizontal line
						{
							drawSymbol.createHLine(canvas, currX + l * a.spacing, currX + (l + difference - 1) * a.spacing, currY + j * SEGY, a.spacing);
						}
						l = l + difference;	// advances character counter past the symbol
						numberIndex++;
					}
					else
					{
						drawSymbol.createHLineAtPosition(canvas, currX + l * a.spacing, currY + j * SEGY, a.spacing);
						l++;
					}
				}
				else
				{
					drawSymbol.createHLineAtPosition(canvas, currX + l * a.spacing, currY + j * SEGY, a.spacing);
					l++;
				}	
				
			}

			// Draws ending vertical bars
			DrawVerticalBar(i, j, canvas, currX + tempString.length() * a.spacing, currY, vCounter);	
		}
		
		// update currX
//		System.out.println("Update currX : " + currX + " -> " + (currX + a.input.get(i).get(5).length() * a.spacing));
		currX = currX + a.input.get(i).get(5).length() * a.spacing;
	}
	
	/*
	 * Create a pdf file and save is to dest
	 */
	public void createPdf(boolean useLocalDest) throws DocumentException, IOException
	{
		Parser a = this.getParser();
		
		// Setup document
		PdfContentByte canvas = this.initPDF(useLocalDest);

		// reset the position of X and Y coordinate
		this.resetXY();
		
		// Draws title and subtitle to PDF
		this.DrawTitles(canvas);
				
		// Loop on parser's segment list
		for (int i = 0; i < a.input.size(); i++)
		{
			// check whether a return is needed or not
			this.checkNewLine(a.input.get(i).get(0).length() * a.spacing, canvas);
			// check whether a new page is need or not
			this.checkNewPage(canvas.getPdfDocument(), NEW_PAGE_THRESHOLD);
			// draw a single segment and update currX
			DrawSegment(i, canvas);		
		}
		
		// Draw ending filler lines
		for(int k = 0; k < 6; k++)
		{
			drawSymbol.createHLineAtPosition(canvas, currX, currY + k * SEGY, SIZEX - currX);
		}
		
		// finish writing, close document
		canvas.getPdfDocument().close();
	}
	
	/**
	 * Debugging tools
	 */
	
	/* DEBUG TOOL: display file size, all the vertical bars in each segment and stars */
	public void displayVerticalBars()
	{	
		Parser a = this.getParser();	// get parser of this instance
		System.out.println(a.vLinesInput.size());
		for (int i = 0; i < a.vLinesInput.size(); i++)
		{
			System.out.println(a.vLinesInput.get(i).size());
			for (int j = 0; j < a.vLinesInput.get(i).size(); j++)
			{
				System.out.println(a.vLinesInput.get(i).get(j));
			}
		}
	}
	
	/* DEBUG TOOL: display lines parsed from Parser */
	public void displayInfo()
	{
		Parser a = this.getParser();	// get parser of this instance
		int i = 0;
		System.out.println(a.input.size());
		while (i < a.input.size())
		{
			int j = 0;
			System.out.println(a.input.get(i).size());
			while (j < a.input.get(i).size())
			{
				System.out.printf("%d %s\n", i, a.input.get(i).get(j));
				j++;
			}
			System.out.println("");
			i++;
		}
	}
}