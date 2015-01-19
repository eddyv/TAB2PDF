/*Date created: 1/15/2015
 *Edited: 1/15/2015
 *Author:Edward Vaisman
 *Team 4
 *Description: creates a gui that will be able to handle multiple options such as opening a text file that can be later converted to a pdf
 *TODO:: MODIFY CODE SO THAT EVERYTHING IS NOT STATIC AND CAN BE USED IN A SEPERATE CLASS
 *TODO:: STUFF
 */

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

public class Main {
	static JMenuItem open;
	static JMenuItem exit;
	static JMenuItem convert;
	static JMenu file;
	static JMenu edit;
	static JMenuBar bar;
	static File in;
	static String out;

	public static void main(String[] args) {
		//sets up the frame
		JFrame frame = new JFrame("TAB2PDF");
		frame.setLayout(new FlowLayout());
		frame.setVisible(true);
		frame.setSize(800,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		bar = new JMenuBar();
		frame.setJMenuBar(bar);
		
		file = new JMenu("File");
		bar.add(file);
		
		edit = new JMenu("Edit");
		bar.add(edit);
		
		open = new JMenuItem("Open");
		file.add(open);
		
		exit = new JMenuItem("Exit");
		file.add(exit);
		
		convert = new JMenuItem("Convert to PDF");
		file.add(convert);
		
		
		
		class Handler implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				if(e.getSource()== exit)
				{
					System.exit(0);
				}
				else if(e.getSource() == open)
				{
					JFileChooser fc = new JFileChooser();
					FileFilter textFilter = new ExtensionFilter("Text file", ".txt");
					//fc.addChoosableFileFilter(textFilter);
					fc.setFileFilter(textFilter);
					int status = fc.showOpenDialog(null);
					if(status == JFileChooser.APPROVE_OPTION)
					{
						in = fc.getSelectedFile();
						System.out.println(in.getPath());
					}
				}
				else if(e.getSource() == convert)
				{
					JFileChooser fc = new JFileChooser();
					FileFilter pdfFilter = new ExtensionFilter("Pdf file", ".pdf");
					fc.setFileFilter(pdfFilter);
					int status = fc.showSaveDialog(null);
					if(status == JFileChooser.APPROVE_OPTION)
					{
						//ugly maybe i can fix this....
						out = fc.getSelectedFile().getPath().concat(((ExtensionFilter) pdfFilter).getExtension());
						System.out.println(out);
					}
					//converting stuff to pdf.
				}
			}
		}
		
		exit.addActionListener(new Handler());
		open.addActionListener(new Handler());
		convert.addActionListener(new Handler());
	}
	
	//THE BELOW CODE WAS TAKEN FROM http://www.java2s.com/Code/Java/Swing-JFC/CustomizingaJFileChooser.htm and is not claimed to be written by team4.
	public static class ExtensionFilter extends FileFilter {
	    private String extensions[];

	    private String description;

	    public ExtensionFilter(String description, String extension) {
	      this(description, new String[] { extension });
	    }

	    public ExtensionFilter(String description, String extensions[]) {
	      this.description = description;
	      this.extensions = (String[]) extensions.clone();
	    }

	    public boolean accept(File file) {
	      if (file.isDirectory()) {
	        return true;
	      }
	      int count = extensions.length;
	      String path = file.getAbsolutePath();
	      for (int i = 0; i < count; i++) {
	        String ext = extensions[i];
	        if (path.endsWith(ext)
	            && (path.charAt(path.length() - ext.length()) == '.')) {
	          return true;
	        }
	      }
	      return false;
	    }

	    public String getDescription() {
	      return (description == null ? extensions[0] : description);
	    }
	    //this method i created myself.
	    public String getExtension()
	    {
	    	return extensions[0];
	    }
	  }

}

