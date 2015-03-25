package tab2pdf;

import java.util.ArrayList;

/**
 * Class to process the Parser class information
 * 
 * Will change blank spaces to 
 * @author Jennifer
 *
 */
	
public class Processor
{
	private static ArrayList<Segment> inputSegments;
	private static ArrayList<Segment> vLinesSegments;
	private static String title;
	private static String subtitle;
	private static float spacing;
	
	public Processor(ArrayList<Segment> input, ArrayList<Segment> vLinesInput, String p_title, String p_subtitle, float p_spacing)
	{
		inputSegments = input;
		vLinesSegments = vLinesInput;
		title = p_title;
		subtitle = p_subtitle;
		spacing = p_spacing;
		checkRepeatNumber();
		checkRepeatSpacing();
		checkParseInfo();
		//displayInfo();
		//displayVerticalBars();
	
	}
	
	/**
	 * Realign string due to bug in parser, occurs on parsed lines with vertical bars for repeat n times 
	 */
	public void checkRepeatNumber()
	{
		for (int i = 0; i < inputSegments.size(); i++) // i = segment number
		{	
			if (inputSegments.get(i).get(0).length() != inputSegments.get(i).get(1).length() && inputSegments.get(i).get(0).substring(0, 1).matches("[0-9]"))
			{
				Segment tempSegment = new Segment();
				for (int j = 0; j < 6; j++)
				{
					if (j == 0)
					{
						tempSegment.add(inputSegments.get(i).get(j).substring(1));
					}
					else
					{
						tempSegment.add(inputSegments.get(i).get(j));
					}
				}
				inputSegments.set(i, tempSegment);
			}
		}
	}
	
	/**
	 * If the bar has a repeat measure, will add another dash to give distance between bar and numbers
	 */
	public void checkRepeatSpacing()
	{
		for (int i = 0; i < inputSegments.size(); i++)
		{
			if(inputSegments.get(i).get(2).charAt(0) == '*')
			{
				Segment tempSegment = new Segment();
				
				for (int j = 0; j < 6; j++)
				{
					if (j == 2 || j == 3)	
						tempSegment.add("*-" + inputSegments.get(i).get(j).substring(1));
					else
						tempSegment.add("-" + inputSegments.get(i).get(j));
				}
				inputSegments.set(i, tempSegment);
			}
			
			if(inputSegments.get(i).get(2).charAt(inputSegments.get(i).get(2).length() - 1) == '*')
			{
				Segment tempSegment = new Segment();
				
				for (int j = 0; j < 6; j++)
				{
					if (j == 2 || j == 3)	
						tempSegment.add(inputSegments.get(i).get(j).substring(0, inputSegments.get(i).get(j).length() - 1) + "-*");
					else
						tempSegment.add(inputSegments.get(i).get(j) + "-");
				}
				inputSegments.set(i, tempSegment);
			}
		}
	}
	
	public void displayInfo()
	{
		int i = 0;
		System.out.println("# of segments " + inputSegments.size());
		while (i < inputSegments.size())
		{
			int j = 0;
			System.out.println("Size of segment " + inputSegments.get(i).size());
			while (j < inputSegments.get(i).size())
			{
				System.out.printf("%d %s\n", i, inputSegments.get(i).get(j));
				j++;
			}
			System.out.println("");
			i++;
		}
	}

	public void displayVerticalBars()
	{	
		System.out.println(vLinesSegments.size());
		for (int i = 0; i < vLinesSegments.size(); i++)
		{
			System.out.println(vLinesSegments.get(i).size());
			for (int j = 0; j < vLinesSegments.get(i).size(); j++)
			{
				System.out.println(vLinesSegments.get(i).get(j));
			}
		}
	}
	
	public void checkParseInfo()
	{
		System.out.println("Title " + title);
		System.out.println("Subtitle " + subtitle);
		System.out.println("Spacing " + spacing);
		
		if (spacing == 0)
		{
			spacing = 5;
		}
		System.out.println("Spacing " + spacing);
	}
		
		public ArrayList<Segment> getInput()
		{
			return Processor.inputSegments;
		}
		
		public float getSpacing()
		{
			return Processor.spacing;
		}
		
}
	
