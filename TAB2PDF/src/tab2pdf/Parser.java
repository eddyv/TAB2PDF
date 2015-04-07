package tab2pdf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	Segment tempSegment;
	Segment vLinesTempSegment;
	ArrayList<Segment> tempList;
	ArrayList<Segment> vLinesTempList;
	public ArrayList<Segment> input;
	ArrayList<Segment> vLinesInput;
	String prevLine;
	Integer prevSegmentAmount;
	int lineNumber = 0;
	public String title = null;
	public String subtitle = null;
	int tempVLines = 0;
	float spacing = 0;
	ArrayList<Integer> repeatTimes;

	public Parser(String filePath) throws IOException {
		tempList = new ArrayList<Segment>();
		vLinesTempList = new ArrayList<Segment>();
		input = new ArrayList<Segment>();
		vLinesInput = new ArrayList<Segment>();
		repeatTimes = new ArrayList<Integer>();
		fileIn(filePath);
		Processor processor = new Processor(input, vLinesInput, title, subtitle, spacing);
		input = processor.getInput();
		spacing = processor.getSpacing();
	}

	public void fileIn(String filePath) throws IOException {
		try {
			BufferedReader in = new BufferedReader(new FileReader(filePath));
			String line;

			// accounts for more than 3 segments?
			String regex = "(\\|+|-|[0-9ebgdashEBGDA])(.+)(\\|*)(.*)(\\|+|-|[0-9ebgdashEBGDA])";

			for (line = in.readLine(); line != null; line=in.readLine()) {
				if (line.split("=")[0].equals("TITLE")) {
					title = line.split("=")[1];
				}
				
				if (line.split("=")[0].equals("SUBTITLE")) {
					subtitle = line.split("=")[1];
				}
				
				if (line.split("=")[0].equals("SPACING")) {
					spacing = new Float(line.split("=")[1]);
				}
				
				if (line.matches(regex)) {
					//vLines ArrayList stores the vertical lines that exist in the current line
					ArrayList<String> vLines = new ArrayList<String>();
					
					//if the line starts with a number, then it gets removed from the line
					//and in its place, a vertical bar is appended at the beginning of the line
					
					//The number that was removed gets recorded in the repeatTimes ArrayList
					
					//NEED TO WORK ON REGEX AND ON STORING THE REPEAT VALUES IN THE CASE WHERE THE LINE DOES NOT START WITH IT
					//BUT A SEGMENT DOES
					String repeatNum = "([0-9]-)(.+)(\\|*)(.*)(\\|+|-|[0-9ebgdashEBGDA])";
					if (line.matches(repeatNum)){
						repeatTimes.add(new Integer(line.charAt(0)));
						String result = line.substring(0, 0) + line.substring(0 + 1);
						line = result;
						line = "|" + line;
					}

					Pattern ptn = Pattern.compile("(\\|\\|\\|)|(\\|\\d)|(\\*\\|\\|)|(\\|\\|\\*)|(\\|\\|)|(\\|)");
					Matcher mtr = ptn.matcher(line);

					// the loop finds how many segments are in the line and adds
					// them to the vLines arraylist
					while (mtr.find()) {
						String s = mtr.group();
						//System.out.println(s);
						vLines.add(s);
					}
					
					//System.out.printf("temp list size = %d segments = %d\n", tempList.size(), vLines.size() - 1);
					
					// lineAfterSplit is an array of strings that contains the
					// current line of every segment that is on the same level
					String lineAfterSplit[] = line.split("((?<!\\|)\\|(?!\\|))|(\\|\\|)");

					// if this is the first line of the segment, record it as
					// the previous line
					if (prevLine == null) {
						prevLine = line;
					}

					// if this is the first line of the segment, record the amount of
					// segments it has
					if (prevSegmentAmount == null) {
						prevSegmentAmount = vLines.size() - 1;
					}

					/** System.out.printf("%s %s \n", prevLine, line); */
					// if the current line is the same length as the previous
					// line and has the same amount of segments as the previous
					// line
					if (prevLine.length() == line.length()) {
						if (prevSegmentAmount == (vLines.size() - 1))
						{
							this.addToTempList(vLines, lineAfterSplit, line);
						}
						//if the top line has fewer segments than the rest of the lines, add the correct amount of segments to the tempList and restructure the data amongst them
						
						//
						else if (prevSegmentAmount < (vLines.size() - 1)){
							
							//add new segments to the templist and the vLinesTempList
							for (int i = 0; i < ((vLines.size() - 1) - prevSegmentAmount); i++){
								tempList.add(new Segment());
								vLinesTempList.add(new Segment());
							}
							
							//create a String array temp, which starts at index 1 (similar to vLinesTempList) to feed into the addToTempList method later
							String temp[] = new String [vLines.size()];
							temp[0] = "";
							
							//create String tempLine, which corresponds to the first line of each segment in the tempList as the loop goes on
							String tempLine = "";
							//create a String fullLine, (similar to line) to feed into the addToTempList method later
							String fullLine = vLines.get(0);
							boolean isDownsized = false;
							
							//diff variable keeps track of the amount of times the tempLine was downsized to not lose track
							int diff = 0;
							
							//the following loop goes through each segment's second line and compares its length to the corresponding line's length in the first segment that was previously added
							//every iteration it puts each segment's first line into the tempLine variable
							//if the first line's segment is the same length as the second line's corresponding segment, then it stores the first line's segment in an Array (temp[])
							//if the first line's segment is longer than the second line's corresponding segment, then it chops (downsizes) the first line's segment (tempLine) and stores the difference in a separate Array (temp[])
							
							for (int i = 0; i < (vLines.size() - 1); i++)
							{	
								if (isDownsized == false)
								{
									tempLine = tempList.get(i - diff).get(0);
								}
								
								//if first is longer than the corresponding second
								if (lineAfterSplit[i + 1].length() < tempLine.length())
								{
									//stores the difference in the temp array
									temp[i + 1] = tempLine.substring(0, lineAfterSplit[i + 1].length());
									if (tempLine != temp[i + 1])
									{
										//chops tempLine to move on
										tempLine = tempLine.substring(lineAfterSplit[i + 1].length() + vLines.get(i + 1).length(), tempLine.length());
										diff++;
										isDownsized = true;
									} else {
										isDownsized = false;
									}
								} else if (lineAfterSplit[i + 1].length() == tempLine.length()) 
								{
									//if lines first and second lines are of same length, simply store the first one in the temp array
									temp[i + 1] = tempLine;
									isDownsized = false;
								}
								//the fullLine is built as the loop goes on
								fullLine = fullLine + temp[i + 1] + vLines.get(i + 1);
							}
		
							//remove the info from every segment in the tempList (first line only)
							for (int i = 0; i < prevSegmentAmount; i++){
								tempList.get(i).remove(0);
							}
							
							//remove each pair of bars from every segment in the vLinesTempList (first line only)
							for (int i = 0; i < prevSegmentAmount; i++){
								vLinesTempList.get(i).remove(0);
								vLinesTempList.get(i).remove(0);
							}
							
							
						
							
							
							//after clearing the space, correctly add the first line by feeding in the usual vLines array, then the temp arrayList and the fullLine we built earlier
							this.addToTempList(vLines, temp, fullLine);
							prevLine = fullLine;
							prevSegmentAmount = tempList.size();
							
							//add the next(second) line to the segment
							this.addToTempList(vLines, lineAfterSplit, line);

			
						}
					} else {
						// this is for the case where there are two 6 line
						// pieces of different length, one after another,
						// without a blank line

						// as if does not match regex: add previous (filled in)
						// segments to main arrayList, instantiate a new
						// tempList
						
						this.addToFullList();
						this.newTempList();
						// add segments to new tempList
						this.addToTempList(vLines, lineAfterSplit, line);
					}
					// System.out.println("");
				} else {
					// if the first segment in the temporary list is 6 lines
					// long, add it to the input arraylist (the main arraylist)
					this.addToFullList();
					// if the temporary list was used by the current line,
					// instantiate a new temporary list for the next line
					this.newTempList();
				}
				lineNumber++;
			}

			// if there is a leftover segment and it is valid (of length 6), add
			// it to the input arraylist
			if (tempList.size() > 0 && tempList.get(0).size() == 6) {
				input.addAll(tempList);
				vLinesInput.addAll(vLinesTempList);
			}
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	// methods----------------------------------------------------------------------------------------------------------
	public void addToFullList() {
		// if the first segment in the temporary list is 6 lines long, add it to
		// the input arraylist (the main arraylist)
		if (tempList.size() > 0 && tempList.get(0).size() == 6) {
			// if the input arrayList is empty, repoint it to the temporary arraylist of Segments
			if (input.size() == 0) {
				input = tempList;
				vLinesInput = vLinesTempList;
			}
			// if the input array already contains elements, append the
			// temporary arraylist to the end of it
			else if (input.size() > 0) {
				input.addAll(tempList);
				vLinesInput.addAll(vLinesTempList);
			}
		}
		
	}

	public void newTempList() {
		// if the temporary list was used by the current line, instantiate a new
		// temporary list for the next line
		if (tempList.size() > 0) {
			tempList = new ArrayList<Segment>();
			vLinesTempList = new ArrayList<Segment>();
			// make prevLine null, for the next segment
			prevLine = null;
			prevSegmentAmount = null;
		}
	}

	public void addToTempList(ArrayList<String> verticalLinesArrayList, String[] splitLineArray, String currentLine) {
		
		/**if (tempList.size() == 0 && tempVLines == 0)
		{
			tempVLines = verticalLinesArrayList.size() - 1;
		} 
		else if (tempList.size() == 0 && tempVLines != 0)
		{
			if (tempVLines != (verticalLinesArrayList.size() - 1))
			{
				System.out.println("flag");
			}
			tempVLines = 0;
		}
		System.out.println(tempVLines);**/
		
		/*if (tempList.size() != (verticalLinesArrayList.size() - 1))
		{
			System.out.printf("temp list size = %d segments = %d\n", tempList.size(), verticalLinesArrayList.size() - 1);
		}*/
		
		// loop goes through all the segments in the current level
		for (int k = 1; k < verticalLinesArrayList.size(); k++) {
			// if condition checks whether the current size of the temporary
			// list contains all the segments
			// if not, then create a new segment and add it to the temporary
			// list
			if (tempList.size() < (verticalLinesArrayList.size() - 1)) {
				
				tempSegment = new Segment();
				tempList.add(tempSegment);

				vLinesTempSegment = new Segment();
				vLinesTempList.add(vLinesTempSegment);
			}
			// add the line (lineAfterSplit[k]) to the segment surrounded by the
			// vertical bars vLines.get(k - 1) and vLines.get(k)
			// tempList.get(k - 1).add(vLines.get(k - 1) + lineAfterSplit[k] + vLines.get(k));
			tempList.get(k - 1).add(splitLineArray[k]);
			
			//add the left and right bars of the segment to the separate ArrayList called vLinesTempList
			vLinesTempList.get(k - 1).add(verticalLinesArrayList.get(k - 1));
			vLinesTempList.get(k - 1).add(verticalLinesArrayList.get(k));
		}

		// after inputting the line into the segment, assign the previous line
		// into the current line, for the next iteration
		prevLine = currentLine;
		// after inputting the line into the segment, assign the current amount
		// of segments into the previous amount of segments, for the next
		// iteration
		prevSegmentAmount = verticalLinesArrayList.size() - 1;
	}
}