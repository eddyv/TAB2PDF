package com.drawpdf.drawoutput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
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
	
	public static final float SIZEX = 612;
	public static final float SIZEY = 792;
	public static final float LINEY = SIZEY / 22;
	public static final float SEGY = LINEY / 5;
	public static final float BEGINX = SIZEX / 15;
	
	
	public static void main(String[] args) throws IOException, DocumentException {
        new App().createPdf(SRC, DEST);
    }
	
	public void createPdf(String src, String dest) throws IOException, DocumentException {
		
		File input = new File(src);
        Scanner sc = new Scanner(input);
		
		Rectangle pageSize = new Rectangle(SIZEX, SIZEY);
        Document document = new Document(pageSize);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        
        // create title at position size SIZEX / 2, LINEY * 1.5
        String line = sc.nextLine();
        String title = line.split("=")[1];
        this.createTextCenteredAtPosition(writer, title, SIZEX / 2, LINEY * 1.5f, 24);
        
        // create subtitle at position size SIZEX / 2, LINEY * 2
        line = sc.nextLine();
        String subtitle = line.split("=")[1];
        this.createTextCenteredAtPosition(writer, subtitle, SIZEX / 2, LINEY * 2f, 12);
        
        // get spacing
        line = sc.nextLine();
        float spacing = Float.parseFloat(line.split("=")[1]);
        
        // consume a blank input line
        sc.nextLine();
        
        float currY = LINEY * 3f;
        
        while (sc.hasNextLine()) {
        	
        	// create beginning lines of each row
        	for (int i = 0; i < 6; ++i) {
        		this.createHLineAtPosition(writer, 0, currY + i * SEGY, BEGINX);
        	}
        	
        	String[] lines = new String[6];
        	
        	for (int i = 0; i < 6; ++i) {
        		lines[i] = sc.nextLine();
        	}
        	
        	for (int i = 0; i < 6; ++i) {
        		
        		float currX = BEGINX;
        		
        		String[] segments = lines[i].split("(\\|)|(\\|\\|)");
        		for (int j = 0; j < segments.length; ++j) {
        			
        			if (i == 0) {
        				this.createVLineAtPosition(writer, currX, currY, LINEY);
        			}
        			
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
        	
        	break;
        }
        
        
        
        document.close();
        
        sc.close();
    }
	
	public void createTextCenteredAtPosition(PdfWriter writer, String text, float x, float y, float size) throws DocumentException, IOException {
		PdfContentByte cb = writer.getDirectContent();
		cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false), size);
        cb.beginText();
        cb.showTextAligned(Element.ALIGN_CENTER, text, x, SIZEY - y, 0);
        cb.endText();
        cb.stroke();
	}
	
	public void createHLineAtPosition(PdfWriter writer, float x, float y, float length) {
		PdfContentByte cb = writer.getDirectContent();
		cb.setLineWidth(0.5f);
		cb.moveTo(x, SIZEY - y);
        cb.lineTo(x + length, SIZEY - y);
        cb.stroke();
	}
	
	public void createVLineAtPosition(PdfWriter writer, float x, float y, float length) {
		PdfContentByte cb = writer.getDirectContent();
		cb.setLineWidth(0.5f);
		cb.moveTo(x, SIZEY - y);
        cb.lineTo(x, SIZEY - (y + length));
        cb.stroke();
	}
}
