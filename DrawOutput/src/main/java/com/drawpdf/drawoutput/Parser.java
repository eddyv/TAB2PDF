package com.drawpdf.drawoutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Will need to further break down the class into smaller methods
 */
public class Parser
{

	Segment segment;
	ArrayList<Segment> input;
	int lineNumber = 0;
	String title = null;
	String subtitle = null;
	float spacing = 0;

	public Parser(String filePath) throws IOException
	{
		segment = new Segment();
		input = new ArrayList<Segment>();
		fileIn(filePath);
	}

	public void fileIn(String filePath) throws IOException
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(filePath));
			String line = "";

			// read the first line
			line = in.readLine();
			// System.out.println(line);

			// Skip blank lines.
			while (line != null && line.trim().equals(""))
			{
				line = in.readLine();
				lineNumber++;
				// System.out.println(line);
			}

			// if the first readable line is the title then set the title
			// variable
			if (line.split("=")[0].equals("TITLE"))
			{
				title = line.split("=")[1];
			}
			System.out.println(title);

			// read the second line
			line = in.readLine();
			// System.out.println(line);

			// Skip blank lines.
			while (line != null && line.trim().equals(""))
			{
				line = in.readLine();
				lineNumber++;
				// System.out.println(line);
			}

			// if the first readable line is the subtitle then set the subtitle
			// variable
			if (line.split("=")[0].equals("SUBTITLE"))
			{
				subtitle = line.split("=")[1];
			}
			System.out.println(subtitle);

			// read the third line
			line = in.readLine();
			// System.out.println(line);

			// Skip blank lines.
			while (line != null && line.trim().equals(""))
			{
				line = in.readLine();
				lineNumber++;
				// System.out.println(line);
			}

			// if the first readable line is the spacing then set the spacing
			// variable
			if (line.split("=")[0].equals("SPACING"))
			{
				spacing = new Float(line.split("=")[1]);
			}
			System.out.println(spacing);

			// read the fourth line
			line = in.readLine();
			System.out.println(line);

			// Skip blank lines.
			while (line != null && line.trim().equals(""))
			{
				line = in.readLine();
				lineNumber++;
				// System.out.println(line);
			}

			// String regex =
			// "((\\|+|[EBGDA0-9ebgda-]|-).*(\\|*|-*)(.+)(-|\\|+|[A-Z0-9a-z]))";
			// |||||||
			// A
			// -------
			// one or more bars or a number/letter or dash
			// anything any amount of times
			// any amount of bars or any amount of dashes
			// anything one or more times
			// one or more bars or a number/letter or dash
			// |||||||||||||||||
			// -
			// A
			// String regex= "(\\||-|[A-Za-z0-9]).*(\\|*).*([A-Za-z0-9]|-|\\|)";
			String regex = "(\\|+|-|[EBGDAebgda0-9])(.+)(\\|*)(.*)(\\|+|-|[EBGDAebgda0-9])";

			while (line != null)
			{
				if (line.matches(regex))
				{
					// add the current line to the segment
					segment.add(line);
					System.out.println("Segment: " + line);
				} else
				{
					// if the segment is 6 lines long, add it to the input
					// arraylist (of segments)
					if (segment.size() == 6)
					{
						input.add(segment);
						System.out.println("add segment into input");
					}
					// instantiate a new segment
					segment = new Segment();
				}
				line = in.readLine();
			}
			// if there is a leftover segment and it is valid (of length 6), add
			// it to the input arraylist
			if (segment.size() == 6)
			{
				input.add(segment);
				System.out.println("Leftover segment");
			}

			in.close();

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
