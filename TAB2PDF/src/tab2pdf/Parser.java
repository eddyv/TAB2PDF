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

	public Parser(String filePath) throws IOException {
		tempList = new ArrayList<Segment>();
		vLinesTempList = new ArrayList<Segment>();
		input = new ArrayList<Segment>();
		vLinesInput = new ArrayList<Segment>();
		fileIn(filePath);
		Processor processor = new Processor(input, vLinesInput, title,
				subtitle, spacing);
		input = processor.getInput();
		spacing = processor.getSpacing();
	}

	public void fileIn(String filePath) throws IOException {
		try {
			BufferedReader in = new BufferedReader(new FileReader(filePath));
			String line;

			// accounts for more than 3 segments?
			String regex = "(\\|+|-|[0-9ebgdashEBGDA])(?=.*[-])(\\|*)(.*)(\\|+|-|[0-9ebgdashEBGDA])";

			for (line = in.readLine(); line != null; line = in.readLine()) {
				if (line.split("=")[0].equals("TITLE")) {
					title = line.split("=")[1];
				}

				if (line.split("=")[0].equals("SUBTITLE")) {
					subtitle = line.split("=")[1];
				}

				if (line.split("=")[0].equals("SPACING")) {
					spacing = new Float(line.split("=")[1]);
				}

				Pattern preRegexPattern = Pattern
						.compile("(\\|\\|\\|)|(\\|\\d)|(\\*\\|\\|)|(\\|\\|\\*)|(\\|\\|)|(\\|)");
				Matcher preRegexmatcher = preRegexPattern.matcher(line);

				// the loop finds how many segments are in the line and adds
				// them to the vLines arraylist
				int tempLastBarPosition = 0;
				while (preRegexmatcher.find()) {
					String s = preRegexmatcher.group();

					// System.out.printf("%s %d %d\n", line,
					// preRegexmatcher.start(), preRegexmatcher.end());
					tempLastBarPosition = preRegexmatcher.end();
				}

				// System.out.printf("%s %d %d\n", line, line.length(),
				// tempLastBarPosition);
				if (tempLastBarPosition > 0) {
					line = line.substring(0, tempLastBarPosition);
				}

				// if line matches regex
				if (line.matches(regex)) {
					System.out.println("");
					System.out.printf("Line = %s\n", line);

					// vLines ArrayList stores the vertical lines that exist in
					// the current line
					ArrayList<String> vLines = new ArrayList<String>();

					// if the line starts with a number, then it gets removed
					// from the line
					// and in its place, a vertical bar is appended at the
					// beginning of the line

					String repeatNum = "(-|[0-9ebgdashEBGDA])(?=.*[-])(\\|*)(.*)(\\|+|-|[0-9ebgdashEBGDA])";
					if (line.matches(repeatNum)) {

						String result = line.substring(0, 0)
								+ line.substring(0 + 1);
						line = result;
						line = "|" + line;
					}

					Pattern ptn = Pattern
							.compile("(\\|\\|\\|)|(\\|\\d)|(\\*\\|\\|)|(\\|\\|\\*)|(\\|\\|)|(\\|)");
					Matcher mtr = ptn.matcher(line);

					// CHANGE 0 TO VARIABLE WHEN IN GREATER LOOP
					/**
					 * for (int i = 0; i < tempList.size(); i++){ if
					 * (tempList.get(0).size() > 0){
					 * System.out.printf("tempList size = %d\n",
					 * tempList.size()); } }
					 */

					// the following while-loop finds how many vertical lines
					// there are in the line and adds them to vLines
					// also counts how many vertical bars from the previous line
					// and the current line are in the same position

					int countMatchingPositions = 0;
					int fullLength = 0;
					int starAmount = 0;

					if (tempList.size() > 0 && tempList.get(0).size() > 0) {
						if (vLinesTempList.get(0)
								.get(((tempList.get(0).size()) * 2) - 2)
								.equals("||*")) {
							starAmount++;
							fullLength = vLinesTempList.get(0)
									.get(((tempList.get(0).size()) * 2) - 2)
									.length();

						} else if (vLinesTempList.get(0)
								.get(((tempList.get(0).size()) * 2) - 2)
								.equals("*||")) {
							fullLength = vLinesTempList.get(0)
									.get(((tempList.get(0).size()) * 2) - 2)
									.length() - 1;

						} else if (vLinesTempList.get(0)
								.get(((tempList.get(0).size()) * 2) - 2)
								.equals("*||*")) {
							starAmount++;
							fullLength = vLinesTempList.get(0)
									.get(((tempList.get(0).size()) * 2) - 2)
									.length() - 1;
						} else {
							fullLength = vLinesTempList.get(0)
									.get(((tempList.get(0).size()) * 2) - 2)
									.length();
						}
						System.out
								.printf("FullLength test= %d starAmount = %d symbol = %s\n",
										fullLength,
										starAmount,
										vLinesTempList
												.get(0)
												.get(((tempList.get(0).size()) * 2) - 2));
					}

					while (mtr.find()) {
						String s = mtr.group();
						for (int i = 0; i < tempList.size(); i++) {
							if (tempList.get(0).size() > 0 && vLines.size() > 0) {
								// System.out.println("Flag STAR");
								if (vLinesTempList.get(i).get(1).equals("||*")) {
									fullLength = fullLength
											+ tempList
													.get(i)
													.get(tempList.get(0).size() - 1)
													.length()
											+ vLinesTempList.get(i).get(1)
													.length() - 1;

								} else if (vLinesTempList.get(i).get(1)
										.equals("*||")) {
									starAmount++;
									fullLength = fullLength
											+ tempList
													.get(i)
													.get(tempList.get(0).size() - 1)
													.length()
											+ vLinesTempList.get(i).get(1)
													.length();

								} else if (vLinesTempList.get(i).get(1)
										.equals("*||*")) {
									starAmount++;
									fullLength = fullLength
											+ tempList
													.get(i)
													.get(tempList.get(0).size() - 1)
													.length()
											+ vLinesTempList.get(i).get(1)
													.length() - 1;

								} else {
									fullLength = fullLength
											+ tempList
													.get(i)
													.get(tempList.get(0).size() - 1)
													.length()
											+ vLinesTempList.get(i).get(1)
													.length();
								}

								if ((fullLength == (mtr.end() - starAmount))
										&& (mtr.end() != line.length())) {
									countMatchingPositions++;
									System.out.println(countMatchingPositions);
								}

								// System.out.printf("line length = %d\n",
								// line.length());
							}
						}
						vLines.add(s);
						if (tempList.size() > 0 && tempList.get(0).size() > 0) {
							fullLength = vLinesTempList.get(0)
									.get(((tempList.get(0).size()) * 2) - 2)
									.length();
						}
					}

					// lineAfterSplit is an array of strings that contains the
					// current line of every segment that is on the same level
					String lineAfterSplit[] = line
							.split("((?<!\\|)\\|(?!\\|))|(\\|\\|)");

					// if this is the first line of the segment, record it as
					// the previous line
					if (prevLine == null) {
						prevLine = line;
					}

					// if this is the first line of the segment, record the
					// amount of
					// segments it has
					if (prevSegmentAmount == null) {
						prevSegmentAmount = vLines.size() - 1;
					}

					System.out
							.printf("vLines.size() - 1 = %d countMatchingPositions = %d\n",
									vLines.size() - 1, countMatchingPositions);

					// vLines ArrayList stores the vertical lines that exist in
					// the current line plus the vertical bars from the line
					// above it
					ArrayList<String> newVLines = new ArrayList<String>();
					newVLines.add(vLines.get(0));

					// if the current line is the same length as the previous
					// line and has the same amount of segments as the previous
					// line
					if (prevLine.length() == line.length()) {
						if (prevSegmentAmount == (vLines.size() - 1)) {
							this.addToTempList(vLines, lineAfterSplit, line);
						}

						// if the top line has fewer segments than the rest of
						// the lines, add the correct amount of segments to the
						// tempList and restructure the data amongst them
						else if (prevSegmentAmount < (vLines.size() - 1)) {
							System.out.println("First less than second");
							// add new segments to the templist and the
							// vLinesTempList

							this.restructure(vLines, newVLines,
									countMatchingPositions, lineAfterSplit);

						} else if (prevSegmentAmount > (vLines.size() - 1)) {
							System.out.println("");
							System.out.println("Second less than first");

							this.restructure(vLines, newVLines,
									countMatchingPositions, lineAfterSplit);

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
			// if the input arrayList is empty, repoint it to the temporary
			// arraylist of Segments
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

	public void addToTempList(ArrayList<String> verticalLinesArrayList,
			String[] splitLineArray, String currentLine) {
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
			// tempList.get(k - 1).add(vLines.get(k - 1) + lineAfterSplit[k] +
			// vLines.get(k));
			tempList.get(k - 1).add(splitLineArray[k]);

			// add the left and right bars of the segment to the separate
			// ArrayList called vLinesTempList
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
	/**
	 * //the following loop goes through each segment's
	 * second line and compares its length to the
	 * corresponding line's length in the first segment
	 * that was previously added //every iteration it
	 * puts each segment's first line into the tempLine
	 * variable //if the first line's segment is the
	 * same length as the second line's corresponding
	 * segment, then it stores the first line's segment
	 * in an Array (temp[]) //if the first line's
	 * segment is longer than the second line's
	 * corresponding segment, then it chops (downsizes)
	 * the first line's segment (tempLine) and stores
	 * the difference in a separate Array (temp[])
	 */
	public void restructure(ArrayList<String> verticalLinesArrayList,
			ArrayList<String> newVerticalLinesArrayList, int countMatch,
			String[] splitLineArray) {
		// add new segments to the templist and the vLinesTempList
		for (int i = 0; i < (verticalLinesArrayList.size() - 1)
				+ prevSegmentAmount - 1 - countMatch - prevSegmentAmount; i++) {
			tempList.add(new Segment());
			vLinesTempList.add(new Segment());
		}
		System.out.printf("Total segments in tempList = %d\n", tempList.size());
		// create a String array temp1, which starts at index 1 (similar to
		// vLinesTempList) to feed into the addToTempList method later
		String temp1[] = new String[verticalLinesArrayList.size()
				+ prevSegmentAmount - 1 - countMatch];
		temp1[0] = "";
		// create a String array temp2, which starts at index 1 (similar to
		// vLinesTempList) to feed into the addToTempList method later
		String temp2[] = new String[verticalLinesArrayList.size()
				+ prevSegmentAmount - 1 - countMatch];
		temp2[0] = "";

		// create String tempLine, which corresponds to the first line of each
		// segment in the tempList as the loop goes on
		String tempLine = "";
		// create a String fullLine, (similar to line) to feed into the
		// addToTempList method later
		String fullLine1 = vLinesTempList.get(0).get(
				((tempList.get(0).size()) * 2) - 2);
		String fullLine2 = verticalLinesArrayList.get(0);
		// String fullLine2 = vLines.get(0);
		boolean isDownsized = false;

		// diff variable keeps track of the amount of times the tempLine was
		// downsized to not lose track
		int diff1 = 0;
		int diff2 = 0;
		boolean topBottom = false;

		for (int i = 0; i < (verticalLinesArrayList.size() - 1)
				+ prevSegmentAmount - 1 - countMatch; i++) {

			System.out.printf("tempLine = %s\n", tempLine);
			System.out.printf("i=1: %s tempLine = %s\n", temp1[1], tempLine);
			System.out.printf("i=1: %s %s\n", temp2[1], tempList.get(i - diff1)
					.get(tempList.get(0).size() - 1));
			System.out.println(isDownsized);
			if (isDownsized == false) {
				if (tempList.get(i - diff1).get(tempList.get(0).size() - 1)
						.length() > splitLineArray[i + 1 - diff2].length()) {
					tempLine = tempList.get(i - diff1).get(
							tempList.get(0).size() - 1);
					topBottom = false;
				} else if (tempList.get(i - diff1)
						.get(tempList.get(0).size() - 1).length() < splitLineArray[i
						+ 1 - diff2].length()) {
					tempLine = splitLineArray[i + 1 - diff2];
					topBottom = true;
				} else if (tempList.get(i - diff1)
						.get(tempList.get(0).size() - 1).length() == splitLineArray[i
						+ 1 - diff2].length()) {
					tempLine = splitLineArray[i + 1 - diff2];
					topBottom = true;
				}
			}

			System.out.printf("tempLine = %s\n", tempLine);
			System.out.printf("i=1: tempLine = %s %s\n", tempLine,
					splitLineArray[i + 1 - diff2]);
			if ((tempLine.length() > splitLineArray[i + 1 - diff2].length())
					&& (topBottom == false)) {
				System.out.printf(
						"%s %s %s\n",
						"flag1",
						tempLine.substring(0,
								splitLineArray[i + 1 - diff2].length()),
						splitLineArray[i + 1 - diff2]);
				temp1[i + 1] = tempLine.substring(0, splitLineArray[i + 1
						- diff2].length());
				temp2[i + 1] = splitLineArray[i + 1 - diff2];
				if (tempLine != temp1[i + 1]) {
					// chops tempLine to move on
					tempLine = tempLine.substring(
							splitLineArray[i + 1 - diff2].length()
									+ verticalLinesArrayList.get(i + 1 - diff2)
											.length(), tempLine.length());
					diff1++;
					isDownsized = true;
				} else {
					isDownsized = false;
				}
				newVerticalLinesArrayList.add(verticalLinesArrayList.get(i + 1
						- diff2));

			} else if ((tempList.get(i - diff1).get(tempList.get(0).size() - 1)
					.length() < tempLine.length())
					&& (topBottom == true)) {
				System.out.println("flag2");
				temp1[i + 1] = tempList.get(i - diff1).get(
						tempList.get(0).size() - 1);
				temp2[i + 1] = tempLine.substring(0, tempList.get(i - diff1)
						.get(tempList.get(0).size() - 1).length());
				if (tempLine != temp2[i + 1]) {
					// chops tempLine to move on
					tempLine = tempLine.substring(
							tempList.get(i - diff1)
									.get(tempList.get(0).size() - 1).length()
									+ vLinesTempList.get(i - diff1).get(1)
											.length(), tempLine.length());
					diff2++;
					isDownsized = true;
				} else {
					isDownsized = false;
				}
				newVerticalLinesArrayList.add(vLinesTempList.get(i - diff1)
						.get(1));

			} else if ((tempLine.length() < splitLineArray[i + 1 - diff2]
					.length()) && (topBottom == false)) {
				System.out.println("flag3");
				temp1[i + 1] = tempLine;
				tempLine = splitLineArray[i + 1 - diff2];
				temp2[i + 1] = tempLine.substring(0, temp1[i + 1].length());
				tempLine = tempLine.substring(temp1[i + 1].length()
						+ vLinesTempList.get(i - diff1).get(1).length(),
						tempLine.length());
				diff2++;
				isDownsized = true;
				topBottom = true;
				System.out.printf("%s %s %d\n", vLinesTempList.get(0).get(1),
						vLinesTempList.get(1).get(1), diff1);
				newVerticalLinesArrayList.add(vLinesTempList.get(i - diff1)
						.get(1));

			} else if ((tempList.get(i - diff1).get(tempList.get(0).size() - 1)
					.length() > tempLine.length())
					&& (topBottom == true)) {
				System.out.println("flag4");
				temp2[i + 1] = tempLine;
				tempLine = tempList.get(i - diff1).get(
						tempList.get(0).size() - 1);
				temp1[i + 1] = tempLine.substring(0, temp2[i + 1].length());
				tempLine = tempLine.substring(temp2[i + 1].length()
						+ verticalLinesArrayList.get(i + 1 - diff2).length(),
						tempLine.length());
				diff1++;
				isDownsized = true;
				topBottom = false;
				newVerticalLinesArrayList.add(verticalLinesArrayList.get(i + 1
						- diff2));

			} else if ((tempLine.length() == splitLineArray[i + 1 - diff2]
					.length()) && (topBottom == false)) {
				System.out.println("flag5");
				temp1[i + 1] = tempLine;
				temp2[i + 1] = splitLineArray[i + 1 - diff2];
				isDownsized = false;
				newVerticalLinesArrayList.add(verticalLinesArrayList.get(i + 1
						- diff2));

			} else if ((tempLine.length() == tempList.get(i - diff1)
					.get(tempList.get(0).size() - 1).length())
					&& (topBottom == true)) {
				System.out.println("flag6");
				temp1[i + 1] = tempList.get(i - diff1).get(
						tempList.get(0).size() - 1);
				temp2[i + 1] = tempLine;
				isDownsized = false;
				newVerticalLinesArrayList.add(vLinesTempList.get(i - diff1)
						.get(1));
			}
			System.out.printf("fullLine1 = %s, temp2[i + 1] = %s, %s %s %d\n",
					fullLine1, temp2[i + 1], newVerticalLinesArrayList.get(0),
					newVerticalLinesArrayList.get(1),
					newVerticalLinesArrayList.size());
			for (int j = 0; j < newVerticalLinesArrayList.size(); j++) {
				System.out.printf("newVLines.get(%d) = %s\n", j,
						newVerticalLinesArrayList.get(j));
			}
			fullLine1 = fullLine1 + temp1[i + 1]
					+ newVerticalLinesArrayList.get(i + 1);
			fullLine2 = fullLine2 + temp2[i + 1]
					+ newVerticalLinesArrayList.get(i + 1);
			System.out.printf("fullLine1 = %s\n", fullLine1);
			System.out.printf("fullLine2 = %s\n", fullLine2);
		}

		System.out.printf("tempLine = %s\n", tempLine);
		System.out.printf("i=2 again: %s %s\n", temp1[1], tempLine);
		System.out.printf("i=2 again: %s %s\n", temp2[1],
				tempList.get(2 - diff1).get(tempList.get(0).size() - 1));

		// REMOVING STARTS HERE

		// remove the info from every segment in the tempList (first line only)
		for (int i = 0; i < prevSegmentAmount; i++) {
			tempList.get(i).remove(tempList.get(i).size() - 1);
		}

		// remove each pair of bars from every segment in the vLinesTempList
		// (first line only)
		for (int i = 0; i < prevSegmentAmount; i++) {
			vLinesTempList.get(i)
					.remove(((tempList.get(i).size() + 1) * 2) - 1);
			vLinesTempList.get(i)
					.remove(((tempList.get(i).size() + 1) * 2) - 2);
		}

		// after clearing the space, correctly add the first line by feeding in
		// the usual vLines array, then the temp arrayList and the fullLine we
		// built earlier
		this.addToTempList(newVerticalLinesArrayList, temp1, fullLine1);

		// add the next(second) line to the segment
		this.addToTempList(newVerticalLinesArrayList, temp2, fullLine2);

		System.out.println(prevSegmentAmount);
		for (int i = 0; i < tempList.get(0).size(); i++) {
			System.out.printf("Segments: %s\n", tempList.get(0).get(i));
		}

	}
}
