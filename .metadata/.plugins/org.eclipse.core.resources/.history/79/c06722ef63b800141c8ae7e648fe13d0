package com.drawpdf.drawoutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	
	Segment tempSegment;
	Segment vLinesTempSegment;
	ArrayList<Segment> tempList;
	ArrayList<Segment> vLinesTempList;
	ArrayList<Segment> input;
	ArrayList<Segment> vLinesInput;
	String prevLine;
	int lineNumber = 0;
	String title = null;
	String subtitle = null;
	float spacing = 0;
	
	public Parser (String filePath) throws IOException{
		tempList = new ArrayList<Segment>();
		vLinesTempList = new ArrayList<Segment>();
		input = new ArrayList<Segment>();
		vLinesInput = new ArrayList<Segment>();
		fileIn(filePath);
	}
	
	public void fileIn(String filePath) throws IOException
	{
		try {
			BufferedReader in = new BufferedReader(new FileReader(filePath));
			String line = "";
			
			//read the first line
			line = in.readLine();
			
			//Skip blank lines.
			while(line != null && line.trim().equals("")) {
				line = in.readLine();
				lineNumber++;
			}
			
			//if the first readable line is the title then set the title variable
			if (line.split("=")[0].equals("TITLE"))
			{
				title = line.split("=")[1];
			}
			//System.out.println(title);
			
			//read the second line
			line = in.readLine();
			
			//Skip blank lines.
			while(line != null && line.trim().equals("")) {
				line = in.readLine();
				lineNumber++;
			}
			
			//if the first readable line is the subtitle then set the subtitle variable
			if (line.split("=")[0].equals("SUBTITLE"))
			{
				subtitle = line.split("=")[1];
			}
			//System.out.println(subtitle);
			
			//read the third line
			line = in.readLine();
			
			//Skip blank lines.
			while(line != null && line.trim().equals("")) {
				line = in.readLine();
				lineNumber++;
			}
			
			//if the first readable line is the spacing then set the spacing variable
			if (line.split("=")[0].equals("SPACING"))
			{
				spacing = new Float(line.split("=")[1]);
			}
			//System.out.println(spacing);
			
			//read the fourth line
			line = in.readLine();
			
			//Skip blank lines.
			while(line != null && line.trim().equals("")) {
				line = in.readLine();
				lineNumber++;
			}
			
			//accounts for more than 3 segments?
			String regex= "(\\|+|-|[0-9ebgdashEBGDA])(.+)(\\|*)(.*)(\\|+|-|[0-9ebgdashEBGDA])";

			while(line != null)
			{
				if (line.matches(regex))
				{
					Pattern ptn = Pattern.compile("(\\|\\|\\|)|(\\|\\d)|(\\*\\|\\|)|(\\|\\|\\*)|(\\|\\|)|(\\|)");
							//("((?<!\\|)\\|(?!\\|))|(\\|\\|\\*)|(\\*\\|\\|)|((?<!\\*)\\|\\|(?!\\*))");
					Matcher mtr = ptn.matcher(line);
					
					//the loop finds how many segments are in the line and adds them to the vLines arraylist
					ArrayList<String> vLines = new ArrayList<String>();
		 			while (mtr.find())
		 			{
		 				String s = mtr.group();
		 				//System.out.println(s);
		 				vLines.add(s);
		 			}
		 			
		 			//lineAfterSplit is an array of strings that contains the current line of every segment that is on the same level
		 			String lineAfterSplit[] = line.split("((?<!\\|)\\|(?!\\|))|(\\|\\|)");
		 			
		 			//if this is the first line of the segment, record it as the previous line
		 			if (prevLine == null){
		 				prevLine = line;
		 			}
		 			
		 			/**System.out.printf("%s %s \n", prevLine, line);*/
		 			//if the current line is the same length as the previous line
		 			if (prevLine.length() == line.length()) {
		 				//loop goes through all the segments in the current level
		 				for (int k = 1; k < vLines.size(); k++)
		 				{
		 					//if condition checks whether the current size of the temporary list contains all the segments
		 					//if not, then create a new segment and add it to the temporary list
		 					if (tempList.size() < (vLines.size() - 1)) {
		 						tempSegment = new Segment();
		 						tempList.add(tempSegment);
		 						
		 						vLinesTempSegment = new Segment();
		 						vLinesTempList.add(vLinesTempSegment);
		 					}
		 					//add the line (lineAfterSplit[k]) to the segment surrounded by the vertical bars vLines.get(k - 1) and vLines.get(k)
		 					//tempList.get(k - 1).add(vLines.get(k - 1) + lineAfterSplit[k] + vLines.get(k));
		 					tempList.get(k - 1).add(lineAfterSplit[k]);
		 					
		 					vLinesTempList.get(k - 1).add(vLines.get(k - 1));
		 					vLinesTempList.get(k - 1).add(vLines.get(k));
		 					
		 					//after inputting the line into the segment, make the current line into the previous line, for the ext iteration
		 					prevLine = line;
		 				}
		 			} else {
		 				//this is for the case where there are two 6 line pieces of different length, one after another, without a blank line
		 				
		 				//as if does not match regex: add previous stuff to main arrayList, instantiate a new tempList
						//add segments to new tempList 
		 				
		 				///create above 2 functions
		 				
		 			}
		 			//System.out.println("");
				}
				else {
					//if the first segment in the temporary list is 6 lines long, add it to the input arraylist (the main arraylist)
					if (tempList.size() > 0 && tempList.get(0).size() == 6){
						//if the input arrayList is empty, repoint it to the temporary arraylist of Segments
						if (input.size() == 0)
						{	
							input = tempList;
							vLinesInput = vLinesTempList;
						}
						//if the input array already contains elements, append the temporary arraylist to the end of it
						else if (input.size() > 0)
						{
							input.addAll(tempList);
							vLinesInput.addAll(vLinesTempList);
						}
					}
					//if the temporary list was used by the current line, instantiate a new temporary list for the next line
					if (tempList.size() > 0) {
						tempList = new ArrayList<Segment>();
						vLinesTempList = new ArrayList<Segment>();
						//make prevLine null, for the next segment
						prevLine = null;
					}
	 			}
				//read the next line
				line = in.readLine();
			}
			
			//if there is a leftover segment and it is valid (of length 6), add it to the input arraylist
			if (tempList.size() > 0 && tempList.get(0).size() == 6)
			{
				input.addAll(tempList);
				vLinesInput.addAll(vLinesTempList);
			}

			in.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
