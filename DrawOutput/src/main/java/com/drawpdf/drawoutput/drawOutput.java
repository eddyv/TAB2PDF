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
    	createPdf(src, dest);
    }
    
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
    
    
    public void createPdf(String src, String dest) throws DocumentException, IOException
    {
    	//File input = new File(src);
        //Scanner sc = new Scanner(input);
    	Rectangle pageSize = new Rectangle(SIZEX, SIZEY);
        Document document = new Document(pageSize);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        
        document.open();
        this.createTextCenteredAtPosition(writer, a.title, SIZEX / 2, LINEY * 1.5f, 24);
        this.createTextCenteredAtPosition(writer, a.subtitle, SIZEX / 2, LINEY * 2f, 12);
        float currY = LINEY * 3f;
        float cX = BEGINX;
        float currX = cX;
        
        for (int i = 0; i < 6; ++i) {
        	this.createHLineAtPosition(writer, 0, currY + i * SEGY, BEGINX);
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
    }
    
    public void createLDoubleBar(PdfWriter writer, float x, float y, float length){
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
    
    //draw S
    public void drawS(PdfWriter writer, float x, float y, float length) {
    	PdfContentByte cb = writer.getDirectContent();
		
    	float tiltAngle = (float)(-45 * (Math.PI / 180));
		float scaleX = (float)(Math.cos(tiltAngle)); 		
		float scaleY = (float)(Math.cos(tiltAngle)); 
		float tiltX  = (float)(-Math.sin(tiltAngle)); 
		float tiltY  = (float)(Math.sin(tiltAngle)); 

		PdfTemplate diagonal = null;

		diagonal = cb.createTemplate(12/2.0f, 12/2.0f);
		diagonal.moveTo(0f, 12/4.0f);
		diagonal.lineTo(12/2.0f, 12/4.0f);
		diagonal.stroke();
		
		//adds the diagonal line to the PdfContentByte object
		cb.addTemplate(diagonal, scaleX, tiltX, scaleY, tiltY, x, SIZEY - y);
		
		//adds the horizontal line to the PdfContentByte object
		cb.moveTo(x, SIZEY - y);
        cb.lineTo(x + length, SIZEY - y);
	}
    
	public void drawStar (PdfWriter writer, float x, float y, float length, boolean nextCharBar) {
		PdfContentByte cb = writer.getDirectContent();
		if (nextCharBar) {
			cb.circle(x + (length - 5), SIZEY - y, 7/5f);
		}
		else {
			cb.circle(x - (length - 5), SIZEY - y, 7/5f);
		}
		cb.fillStroke();
	}
 
}




