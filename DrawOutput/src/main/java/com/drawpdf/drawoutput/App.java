package com.drawpdf.drawoutput;

/*
 * this project can't distinguish simple bar and repeat bar
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class App
{

	public static final String SRC = "sample2.txt";
	public static final String DEST = "P2Full.pdf";

	public static final int MARK_LIMIT = 100; // limit use to remember previous
												// empty line, called by mark()

	public static final float SIZEX = 612;
	public static final float SIZEY = 792;
	public static final float LINEY = SIZEY / 22;
	public static final float SEGY = LINEY / 5;
	public static final float BEGINX = SIZEX / 15;

	public static void main(String[] args) throws IOException,
			DocumentException
	{
		new App().createPdf(SRC, DEST);
	}

	public void createPdf(String src, String dest) throws IOException,
			DocumentException
	{

		// File reader classes and attributes
		File input = new File(src);
		FileReader fileReader = new FileReader(input);
		BufferedReader reader = new BufferedReader(fileReader);
		String line; // line container

		// PDF classes
		Rectangle pageSize = new Rectangle(SIZEX, SIZEY);
		Document document = new Document(pageSize);
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(dest));
		document.open();

		// create Symbol object
		Symbol drawSymbol = new Symbol(SIZEY);
		PdfContentByte canvas = writer.getDirectContent();
		// create title at position size SIZEX / 2, LINEY * 1.5
		line = reader.readLine();
		String title = line.split("=")[1];
		drawSymbol.createTextCenteredAtPosition(canvas, title, SIZEX / 2,
				LINEY * 1.5f, 24);

		// create subtitle at position size SIZEX / 2, LINEY * 2
		line = reader.readLine();
		String subtitle = line.split("=")[1];
		drawSymbol.createTextCenteredAtPosition(canvas, subtitle, SIZEX / 2,
				LINEY * 2f, 12);

		// get spacing
		line = reader.readLine();
		float spacing = Float.parseFloat(line.split("=")[1]);
		System.out.println("Title=" + title);
		System.out.println("Subtitle = " + subtitle);
		System.out.println("Spacing = " + spacing);

		float currY = LINEY * 3f;
		float cX = BEGINX;
		float currX = cX;

		while ((line = reader.readLine()) != null)
		{

			// create beginning lines of each row
			for (int i = 0; i < 6; ++i)
			{
				drawSymbol.createHLineAtPosition(canvas, 0, currY + i * SEGY,
						BEGINX);
			}

			// consume blank input lines, and get first non-empty line
			while (line.isEmpty())
			{
				reader.mark(MARK_LIMIT);
				line = reader.readLine();
			}
			reader.reset(); // found new non-empty, go back one line for reading

			// store each lines bar in lines
			String[] lines = new String[6];

			for (int i = 0; i < 6; ++i)
			{
				lines[i] = reader.readLine();
				System.out.println(lines[i]);
			}

			for (int i = 0; i < 6; ++i)
			{

				currX = cX;
				// separate each bar by split
				// store music bars in segments without vertical bars

				Pattern ptn = Pattern.compile("((?<!\\|)\\|(?!\\|))|(\\|\\|\\*)|(\\*\\|\\|)|((?<!\\*)\\|\\|(?!\\*))");
				Matcher mtr = ptn.matcher(lines[i]);

				List<String> vlines = new ArrayList<String>();

				while (mtr.find())
				{
					String s = mtr.group();
					vlines.add(s);
					System.out.println(s);
				}

				String[] segments = lines[i].split("((?<!\\|)\\|(?!\\|))|(\\|\\|)");

				for (int j = 0; j < segments.length; ++j)
				{

					if (segments.length * spacing + currX > 450)
					{
						if (i == 0)
						{
							drawSymbol.createVLineAtPosition(canvas, currX,
									currY, LINEY);
							for (int k = 0; k < 6; ++k)
							{
								drawSymbol.createHLineAtPosition(canvas, currX,
										currY + k * SEGY, SIZEX - currX);
							}
						}

						cX = BEGINX;
						currX = cX;
						currY += LINEY * 2;

					}

					// draw vertical bars
					if (i == 2)
					{
						// check if turn to newline

						if (vlines.get(j).equals("|"))
							drawSymbol.createVLineAtPosition(canvas, currX,
									currY, LINEY);
						else if (vlines.get(j).equals("||*"))
						{
							// draw two vertical bar
							drawSymbol.createLDoubleBar(canvas, currX, currY,
									LINEY);

						} else if (vlines.get(j).equals("*||"))
						{
							drawSymbol.createRDoubleBar(canvas, currX, currY,
									LINEY);
						}
					}
					// draw the 6 horizontal lines and numbers
					for (int k = 0; k < segments[j].length(); ++k)
					{
						if (segments[j].charAt(k) == '-')
						{
							drawSymbol.createHLineAtPosition(canvas, currX + k
									* spacing, currY + i * SEGY, spacing);
						} else
						{
							drawSymbol.createTextCenteredAtPosition(canvas, ""
									+ segments[j].charAt(k), currX + (k + 0.5f)
									* spacing, currY + i * SEGY + 0.3f
									* spacing, spacing);
						}
					}

					currX += segments[j].length() * spacing;

					// break;
				}

			}
			cX = currX;
			// currY += LINEY * 2;

			// break;
		}

		// clean up
		document.close();
		fileReader.close();
	}

}
