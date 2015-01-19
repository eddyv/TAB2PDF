package textfiles;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class FileData {
	/**
	 * 
	 * Line 1 of the text file must read TITLE=titlename.
	 * Line 2 of the text file must read SUBTITLE=subtitlename.
	 * 
	 * 
	 * @param args
	 * @throws IOException
	 */
	
	public static void main(String[] args) throws IOException {
		
		String file_name = "results/InputSample.txt";
		try {
			ReadFile file = new ReadFile(file_name);
			String[] textfile = file.OpenFile();
			StringBuffer title = new StringBuffer(textfile[0]);
			StringBuffer subtitle = new StringBuffer(textfile[1]);
			
			
			
			
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
