package tab2pdf;

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

public class drawOutput
{
	private static final float SIZEX = 612;
	private static final float SIZEY = 792;
	private static final float LINEY = SIZEY / 22;
	private static final float SEGY = LINEY / 5;
	private static final float BEGINX = SIZEX / 15;
	private static int vCounter;
	private static Symbol drawSymbol = new Symbol(SIZEY);
	Parser a;

	public drawOutput(String src, String dest) throws IOException,
			DocumentException
	{
		a = new Parser(src);
		//displayInfo();
		//displayVerticalBars();
		createPdf(dest);
	}

	public void displayVerticalBars()
	{
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
	
	// display lines parsed from Parser
	public void displayInfo()
	{
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

	public void createPdf(String dest) throws DocumentException, IOException
	{
		Rectangle pageSize = new Rectangle(SIZEX, SIZEY);
		Document document = new Document(pageSize);
		PdfWriter writer = PdfWriter.getInstance(document,
		new FileOutputStream(dest));
		document.open();

		// create Symbol object and canvas to draw on
		//Symbol drawSymbol = new Symbol(SIZEY);
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
		//for(int i = 0; i < 2; i++)	// i = bar #
		{
			
			for(int j = 0; j < a.input.get(i).size(); j++) // j = line number in bar
			{
				//initialize counter for vLinesInput line number
				vCounter = j * 2;
				
				currX = cX;
				
				// draw beginning filler lines
				drawSymbol.createHLineAtPosition(canvas, 0, currY + j * SEGY,BEGINX);

				/*
				 * check size of current bar and test if it will fit within the page width when drawn
				 * if does not fit then continue in the if statement to end the current line with
				 * a vertical line, then start on a new line
				 */
				if(a.input.get(i).get(j).length() * a.spacing + currX > 570)
				{
					//drawSymbol.createVLineAtPosition(canvas, currX, currY,LINEY);
					
					for(int k = 0; k < 6; k++)
					{
						drawSymbol.createHLineAtPosition(canvas, currX,currY + k * SEGY, SIZEX - currX);
					}
					cX = BEGINX;
					currX = cX;
					currY += LINEY * 2;
				}
				
				// start on new page if needed
				if (currY > 750)
				{
					document.newPage();
					currX = BEGINX;
					currY = LINEY;
				}
				
				// Draws beginning vertical bars
				DrawVerticalBar(i, j, canvas, currX, currY);

				/*
				 * check cases to draw symbols as needed
				 */
				String tempString = a.input.get(i).get(j);
				
				
				//checking for diamond in the line
				Boolean hasDiamond = false;
				Pattern pattern = Pattern.compile("<\\d+>");
			    Matcher matcher = pattern.matcher(tempString);
			    List<Integer> diamondIndex = new ArrayList<Integer>(); // stores start and end indexes of diamonds
			    int diamondIndexCount = 0;	// counts number of diamonds in a line
			    
			    while (matcher.find()) 	// stores indexes of diamond start and end position for each line
			    {
			    	diamondIndex.add(matcher.start());
			    	diamondIndex.add(matcher.end());
			        hasDiamond = true;
			    }
			    
				for (int l = 0; l < tempString.length(); l++)
				{
					// realign string due to bug in parser, occurs on parsed lines with vertical bars for repeat n times 
					// ex. |4, the 4 gets parsed into the line making it 1 length longer than the rest in the segment
					if (l == 0 && j ==0 && a.input.get(i).get(j).substring(l, l+1).matches("[0-9]"))
					{
						tempString = tempString.substring(1);
					}
					
					// replaces blank space with a dash
					if (a.input.get(i).get(j).substring(l, l+1).matches("\\s"))
					{
						tempString = tempString.replaceAll("\\s", "-");
					}
					
					//check case for diamond symbol
					if (hasDiamond == true && diamondIndexCount < diamondIndex.size())
					{
						if (l == diamondIndex.get(diamondIndexCount))
						{
							System.out.println("Testing diamond");
							
							//get number value
							String diamondnum = tempString.substring(diamondIndex.get(diamondIndexCount) + 1, diamondIndex.get(diamondIndexCount + 1) - 1);
							System.out.println(diamondnum);
							int difference = (diamondIndex.get(diamondIndexCount + 1) - diamondIndex.get(diamondIndexCount));
							drawSymbol.createTextCenteredAtPosition(canvas, diamondnum, currX + 
									(l + 1f)	* a.spacing, currY + j * SEGY + 0.5f * a. spacing, 8);
							drawSymbol.createDiamond(canvas,  currX + (l + difference - 1) * a.spacing,  currY + j * SEGY - 0.5f * a. spacing, 2);
							//drawSymbol.createHLineAtPosition(canvas, currX + (l + difference) * a.spacing, currY + j * SEGY, a.spacing);
							
							l = l + difference;	// advances character counter past the diamond symbol
							diamondIndexCount = diamondIndexCount + 2;
						}
					}
					
					if (tempString.charAt(l) == '-')
					{
						drawSymbol.createHLineAtPosition(canvas, currX + l * a.spacing, currY + j * SEGY, a.spacing);
					}
					else if (tempString.charAt(l) == '*')
					{
						if (l == 0)
						{
							drawSymbol.createHLineAtPosition(canvas, currX + l * a.spacing, currY + j * SEGY, a.spacing);
							drawSymbol.createCircle(canvas, currX + a.spacing, currY + j * SEGY, a.spacing);
						}
						else
						{
							drawSymbol.createCircle(canvas, (currX + a.spacing * (l - 1)), currY + j * SEGY, a.spacing);
							drawSymbol.createHLineAtPosition(canvas, currX + l * a.spacing, currY + j * SEGY, a.spacing);
						}
					}
					else if (tempString.charAt(l) == 's')
					{
						drawSymbol.createS(canvas, currX + l * a.spacing, currY + j * SEGY, a.spacing);
					}
					else if (tempString.charAt(l) == 'h' || tempString.charAt(l) == 'p')
					{
						drawSymbol.createArc(canvas, currY + (j - 0.75f) * SEGY, currY + (j - 0.25f) * SEGY, currX + (l + 0.50f) * a.spacing, currX + (l + 5) * a.spacing, a.spacing);
						drawSymbol.createHLineAtPosition(canvas, currX + l * a.spacing, currY + j * SEGY, a.spacing);
						drawSymbol.createTextCenteredAtPosition(canvas, "" + tempString.charAt(l), currX + 
								(l + 0.5f)	* a.spacing, currY + (j - 1.25f) * SEGY + 0.5f * a. spacing, 5);
					}
					else
					{
						drawSymbol.createTextCenteredAtPosition(canvas, "" + tempString.charAt(l), currX + 
								(l + 0.5f)	* a.spacing, currY + j * SEGY + 0.5f * a. spacing, 8);
					}	
				}
				currX += tempString.length() * a.spacing;	
				// Draws ending vertical bars
				DrawVerticalBar(i, j, canvas, currX, currY);	
			}
			cX = currX;	
		}
		
		// Draw ending filler lines
		for(int k = 0; k < 6; k++)
		{
			drawSymbol.createHLineAtPosition(canvas, currX,currY + k * SEGY, SIZEX - currX);
		}
		document.close();
	}
	
	public void DrawVerticalBar(int i, int j, PdfContentByte canvas, float currX, float currY) throws DocumentException, IOException
	{
		String vLines = a.vLinesInput.get(i).get(vCounter);
		//System.out.println(a.vLinesInput.get(i).get(vCounter));
		//System.out.println("j = " + j + " vCounter = " + vCounter);
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
				if (!a.vLinesInput.get(i).get(4).equals("*||"))
				{
					drawSymbol.createLDoubleBar(canvas, currX, currY, LINEY);
				}	
			}
			else
			{
				if (!a.vLinesInput.get(i).get(4).equals("||*"))
				{
					drawSymbol.createRDoubleBar(canvas, currX, currY, LINEY);	
				}
			}
			currX += a.input.get(i).get(j).length() * a.spacing;
		}
		else if (vLines.equals("*||") && vCounter % 2 == 1)
		{
			drawSymbol.createRDoubleBar(canvas, currX, currY, LINEY);
			currX += a.input.get(i).get(j).length() * a.spacing;
		}
		else if (vLines.equals("||*") && vCounter % 2 == 0)
		{
			drawSymbol.createLDoubleBar(canvas, currX, currY, LINEY);
			currX += a.input.get(i).get(j).length() * a.spacing;
		}
		else if (vLines.matches("\\|\\d") && vCounter % 2 == 1)
		{
			drawSymbol.createTextCenteredAtPosition(canvas, "Repeat " + vLines.charAt(1) + " times", currX - 5 * a.spacing, currY - 1.5f * a. spacing, 8);
		}
		else if (vLines.equals("|"))
		{
			drawSymbol.createVLineAtPosition(canvas, currX, currY, LINEY);
		}
		vCounter++;
	}
}