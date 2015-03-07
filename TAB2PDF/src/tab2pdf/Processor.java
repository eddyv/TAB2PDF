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
	
	public Processor(ArrayList<Segment> input)
	{
		inputSegments = input;
		checkRepeatNumber();
		checkRepeatSpacing();
		displayInfo();
	
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
		System.out.println(inputSegments.size());
		while (i < inputSegments.size())
		{
			int j = 0;
			System.out.println(inputSegments.get(i).size());
			while (j < inputSegments.get(i).size())
			{
				System.out.printf("%d %s\n", i, inputSegments.get(i).get(j));
				j++;
			}
			System.out.println("");
			i++;
		}
	}
	
	public ArrayList<Segment> getInput()
	{
		return Processor.inputSegments;
	}
}
