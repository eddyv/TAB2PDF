/*
 * this project can't distinguish simple bar and repeat bar
 * 
 */
package com.drawpdf.drawoutput;

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
import com.itextpdf.text.pdf.PdfWriter;

public class App {
	
	public static final String SRC = "sample1.txt";
	public static final String DEST = "P1.pdf";
	
	public static final int MARK_LIMIT = 100;	// limit use to remember previous empty line, called by mark()
	
	public static final float SIZEX = 612;	// width of document
	public static final float SIZEY = 792;	// length of document
	public static final float LINEY = SIZEY / 22;	//default height spacing 
	public static final float SEGY = LINEY / 5;
	public static final float BEGINX = SIZEX / 15;
	
	
	public static void main(String[] args) throws IOException, DocumentException {
        new App().createPdf(SRC, DEST);
    }
	
	public void createPdf(String src, String dest) throws IOException, DocumentException {
		
		// File reader classes and attributes
		File input = new File(src);
		FileReader fileReader = new FileReader(input);
		BufferedReader reader = new BufferedReader(fileReader);
		String line;	// line container
		
        // PDF classes
		Rectangle pageSize = new Rectangle(SIZEX, SIZEY);
        Document document = new Document(pageSize);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        
        // create title at position size SIZEX / 2, LINEY * 1.5
        line = reader.readLine();
        String title = line.split("=")[1];
        this.createTextCenteredAtPosition(writer, title, SIZEX / 2, LINEY * 1.5f, 24);
        
        // create subtitle at position size SIZEX / 2, LINEY * 2
        line = reader.readLine();
        String subtitle = line.split("=")[1];
        this.createTextCenteredAtPosition(writer, subtitle, SIZEX / 2, LINEY * 2f, 12);
        
        // get spacing
        line = reader.readLine();
        float spacing = Float.parseFloat(line.split("=")[1]);
        System.out.println("Title = " + title);
        System.out.println("Subtitle = " + subtitle);
        System.out.println("Spacing = " + spacing);
        
        float currY = LINEY * 3f;
        
        while ((line = reader.readLine()) != null) {
        	// create beginning lines of each row
        	for (int i = 0; i < 6; ++i) {
        		this.createHLineAtPosition(writer, 0, currY + i * SEGY, BEGINX);
        	}
        	
        	// consume blank input lines, and get first non-empty line
            while(line.isEmpty()){
            	reader.mark(MARK_LIMIT);
            	line = reader.readLine();
            }
            reader.reset(); // found new non-empty, go back one line for reading
            
        	//store each lines bar in lines
        	String[] lines = new String[6];
        	
        	for (int i = 0; i < 6; ++i) {
        		lines[i] = reader.readLine();
        		System.out.println(lines[i]);
        	}
        	
        	for (int i = 0; i < 6; ++i) {
        		
        		float currX = BEGINX;
        		//separate each bar by split 
        		//store music bars in segments without vertical bars
        		Pattern ptn = Pattern.compile("((?<!\\|)\\|(?!\\|))|(\\|\\|\\*)|(\\*\\|\\|)|((?<!\\*)\\|\\|(?!\\*))");
        		
        		Matcher mtr = ptn.matcher(lines[i]);
        		
        		List<String> vlines = new ArrayList<String>();
     
        		while (mtr.find()) {
        			
        			String s = mtr.group();
        			vlines.add(s);
        			System.out.println(s);
        		}
        		
        		
        		String[] segments = lines[i].split("((?<!\\|)\\|(?!\\|))|(\\|\\|)");
        		int count = 1;
        		for (int j = 0; j < segments.length; ++j) {
        			System.out.println(segments[j] + " " + count);
        			count++;
        		}
        		
        		for (int j = 0; j < segments.length; ++j) {
        			//check if turn to newline
        			
        			
        			
        			//draw vertical bars
        			if (i == 2) {
        				if (vlines.get(j).equals("|"))
        					this.createVLineAtPosition(writer, currX, currY, LINEY);
        				else if (vlines.get(j).equals("||*")) {
        					//draw two vertical bar
        					this.createLDoubleBar(writer, currX, currY, LINEY);
        					
        				}else if(vlines.get(j).equals("*||")){
        					this.createRDoubleBar(writer, currX, currY, LINEY);
        				}
        			}
        			//draw the 6 horizontal lines and numbers
        			for (int k = 0; k < segments[j].length(); ++k) {
        				if (segments[j].charAt(k) == '-') {
        					this.createHLineAtPosition(writer, currX + k * spacing, currY + i * SEGY, spacing);
        				} else {
        					this.createTextCenteredAtPosition(writer, "" + segments[j].charAt(k), currX + (k + 0.5f) * spacing, currY + i * SEGY + 0.3f * spacing, spacing);
        				}
        			}
        			
        			currX += segments[j].length() * spacing;
        			
        			//break;
        		}
        	}
        	
        	currY += LINEY * 2;
        	
        	//break;
        }
        
        // clean up
        document.close();
        fileReader.close();
    }
	//draw text
	public void createTextCenteredAtPosition(PdfWriter writer, String text, float x, float y, float size) throws DocumentException, IOException {
		PdfContentByte cb = writer.getDirectContent();
		cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false), size);
        cb.beginText();
        cb.showTextAligned(Element.ALIGN_CENTER, text, x, SIZEY - y, 0);
        cb.endText();
        cb.stroke();
	}
	//draw horizontal line
	public void createHLineAtPosition(PdfWriter writer, float x, float y, float length) {
		PdfContentByte cb = writer.getDirectContent();
		cb.setLineWidth(0.5f);
		cb.moveTo(x, SIZEY - y);
        cb.lineTo(x + length, SIZEY - y);
        cb.stroke();
	}
	//draw vertical
	public void createVLineAtPosition(PdfWriter writer, float x, float y, float length) {
		PdfContentByte cb = writer.getDirectContent();
		cb.setLineWidth(0.5f);
		cb.moveTo(x, SIZEY - y);
        cb.lineTo(x, SIZEY - (y + length));
        cb.stroke();
	}
	//draw circle for repeat bar
	public void createCircle(PdfWriter writer, float x, float y, float length){
		PdfContentByte cb = writer.getDirectContent();
		cb.circle(x + 2f, SIZEY - y, 1);
		cb.stroke();
	}
	public void createRDoubleBar(PdfWriter writer, float x, float y, float length){
		PdfContentByte cbThin = writer.getDirectContent();
		cbThin.setLineWidth(0.5f);
		cbThin.moveTo(x - 1, SIZEY - y);
        cbThin.lineTo(x - 1, SIZEY - (y + length));
		PdfContentByte cbBold = writer.getDirectContent();
		cbThin.stroke();
        
		cbBold.setLineWidth(1.2f);
		cbBold.moveTo(x + 1, SIZEY - y);
        cbBold.lineTo(x + 1, SIZEY - (y + length));
        cbBold.stroke();

	}public void createLDoubleBar(PdfWriter writer, float x, float y, float length){
		PdfContentByte cbBold = writer.getDirectContent();
        cbBold.setLineWidth(1.2f);
		cbBold.moveTo(x + 1, SIZEY - y);
        cbBold.lineTo(x + 1, SIZEY - (y + length));
        cbBold.stroke();
        
        PdfContentByte cbThin = writer.getDirectContent();
		cbThin.setLineWidth(0.5f);
		cbThin.moveTo(x - 1, SIZEY - y);
        cbThin.lineTo(x - 1, SIZEY - (y + length));
		cbThin.stroke();
	}
	
	
}
