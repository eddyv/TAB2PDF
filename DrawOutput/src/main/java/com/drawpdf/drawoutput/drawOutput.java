package com.drawpdf.drawoutput;

/*
 * changes:
 * 
 * create separate method for the initial creation of the document
 * and the setting of the document size
 * 
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class drawOutput
{

	// public String title = null;
	// public String subtitle = null;
	// public String[] lines = null;
	public static final String DEST = "P1.pdf";

	public static final int MARK_LIMIT = 100; // limit use to remember previous
												// empty line, called by mark()

	public static final float SIZEX = 612;
	public static final float SIZEY = 792;
	public static final float LINEY = SIZEY / 22;
	public static final float SEGY = LINEY / 5;
	public static final float BEGINX = SIZEX / 15;

	Parser a;

	public drawOutput(String src, String dest) throws IOException,
			DocumentException
	{
		a = new Parser(src);
		//displayInfo();
		//createPdf(dest);
	}

	// display lines parsed from Parser
	public void displayInfo()
	{
		int i = 0;
		while (i < a.input.size())
		{
			int j = 0;
			while (j < a.input.get(i).size())
			{
				System.out.printf("%d %s\n", i, a.input.get(i).get(j));
				j++;
			}
			System.out.println("");
			i++;
		}
	}

	public void createPdf(String dest) throws DocumentException, IOException
	{
		Rectangle pageSize = new Rectangle(SIZEX, SIZEY);
		Document document = new Document(pageSize);
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(dest));
		document.open();

		// create Symbol object and canvas to draw on
		Symbol drawSymbol = new Symbol(SIZEY);
		PdfContentByte canvas = writer.getDirectContent();

		/*
		 * Draws title and subtitle to PDF
		 */
		drawSymbol.createTextCenteredAtPosition(canvas, a.title, SIZEX / 2, LINEY * 1.5f, 24);
		drawSymbol.createTextCenteredAtPosition(canvas, a.subtitle, SIZEX / 2, LINEY * 2f, 12);

		float currY = LINEY * 3f;
		float cX = BEGINX;
		float currX = cX;

		for (int i = 0; i < a.input.size(); i++)
		 // to test only first n rows of music 
		//for(int i = 0; i < 6; i++)	// i = bar #
		{
			for(int j = 0; j < a.input.get(i).size(); j++) // j = line number in bar
			{
				currX = cX;
				drawSymbol.createHLineAtPosition(canvas, 0, currY + j * SEGY,BEGINX);
				System.out.println(i + " " + a.input.get(i).get(j));
				drawSymbol.createVLineAtPosition(canvas, currX, currY,LINEY);
				
				/*
				 * check size of current bar and test if it will fit within the page width when drawn
				 * if does not fit then continue in the if statement to end the current line with
				 * a vertical line, then start on a new line
				 */
				if(a.input.get(i).get(j).length() * a.spacing + currX > 570)
				{
					drawSymbol.createVLineAtPosition(canvas, currX, currY,LINEY);
					
					for(int k = 0; k < 6; k++)
					{
						drawSymbol.createHLineAtPosition(canvas, currX,currY + k * SEGY, SIZEX - currX);
					}
					cX = BEGINX;
					currX = cX;
					currY += LINEY * 2;
				}
				
				/*
				 * check cases to draw symbols as needed
				 */
				
				for (int l = 0; l < a.input.get(i).get(j).length(); l++)
				{
					if (a.input.get(i).get(j).charAt(l) == '-')
					{
						drawSymbol.createHLineAtPosition(canvas, currX + l * a.spacing, currY + j * SEGY, a.spacing);
					}
					else if (a.input.get(i).get(j).charAt(l) == '*')
					{
						drawSymbol.createCircle(canvas, currX + l * a.spacing, currY + j * SEGY, a.spacing);
					}
					else
					{
						drawSymbol.createTextCenteredAtPosition(canvas, "" + a.input.get(i).get(j).charAt(l), currX + 
								(l + 0.5f)	* a.spacing, currY + j * SEGY + 0.3f
								* a. spacing, 7);
					}
				}
				currX += a.input.get(i).get(j).length() * a.spacing;
			}
			cX = currX;
			System.out.println(i + " " + a.input.get(i).get(1).length()); //length of bar
		}
		document.close();
	}
}
