package tab2pdf;
import java.io.File;

import javax.swing.filechooser.FileFilter;

//THE BELOW CODE WAS TAKEN FROM http://www.java2s.com/Code/Java/Swing-JFC/CustomizingaJFileChooser.htm and is not claimed to be written by team4.
	public class ExtensionFilter extends FileFilter {
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