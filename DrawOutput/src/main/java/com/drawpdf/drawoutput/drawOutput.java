package com.drawpdf.drawoutput;

/*
 * changes:
 * 
 * create separate method for the initial creation of the document
 * and the setting of the document size
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class drawOutput {

    //public String title = null;
    //public String subtitle = null;
	//public String[] lines = null;
    public static final String DEST = "P1.pdf";

    public static final int MARK_LIMIT = 100; // limit use to remember previous empty line, called by mark()
    
    public static final float SIZEX = 612;
    public static final float SIZEY = 792;
    public static final float LINEY = SIZEY / 22;
    public static final float SEGY = LINEY / 5;
    public static final float BEGINX = SIZEX / 15;

    Parser a;
    public drawOutput (String src, String dest) throws IOException, DocumentException
    {
    	a = new Parser(src);
    	displayInfo();
    	createPdf(dest);
    }
    
    //display lines parsed from Parser
    public void displayInfo(){
		int i = 0;
		while (i < a.input.size()) {
			int j = 0;
			while (j < a.input.get(i).size()) {
					System.out.printf("%d %s\n", i, a.input.get(i).get(j));
				j++;
			}
			System.out.println("");
			i++;
		}
    }
    
    
    public void createPdf(String dest) throws DocumentException, IOException
    {
    	//File input = new File(src);
        //Scanner sc = new Scanner(input);
    	Rectangle pageSize = new Rectangle(SIZEX, SIZEY);
        Document document = new Document(pageSize);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        
      //create Symbol object
        Symbol drawSymbol = new Symbol(SIZEY);

        /*
         * Draws title and subtitle to PDF
         * 
         */
        drawSymbol.createTextCenteredAtPosition(writer, a.title, SIZEX / 2, LINEY * 1.5f, 24);
        drawSymbol.createTextCenteredAtPosition(writer, a.subtitle, SIZEX / 2, LINEY * 2f, 12);
        float currY = LINEY * 3f;
        float cX = BEGINX;
        float currX = cX;
        
        //int i = 0;
        //while (i < a.input.size())
        //{
        	
        //}
        //draws beginning starting lines of documents
        for (int i = 0; i < 6; ++i) 
        {
        	drawSymbol.createHLineAtPosition(writer, 0, currY + i * SEGY, BEGINX);
        }
        //while (i < 6)
        //{
        	//for (int k = 0; k < a.segment.get(i).length(); k++)
        	//{
        		//System.out.print(a.input.get(i).charAt(k));
        		//if (a.segment.get(i).charAt(k) == '-') {
                 //   this.createHLineAtPosition(writer, currX + k * a.spacing, currY + i * SEGY, a.spacing);
               // } else if (a.segment.get(i).charAt(k) == 's') {
               // 	this.drawS(writer, currX + k * a.spacing, currY + i * SEGY, a.spacing);
               // } else if (a.segment.get(i).charAt(k) == '*') {
              //  	this.drawStar(writer, currX + k * a.spacing, currY + i * SEGY, a.spacing, a.segment.get(i).charAt(k + 1) == '|');
              //  }
        	//}
        	//currX += a.segment.get(i).length() * a.spacing;
        	//System.out.println("");
        	//i++;
      //  }
        document.close();

    }
 
}




