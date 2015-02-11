package com.drawpdf.drawoutput;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class Symbol {

	public static float SIZEY;
	
	public Symbol(float pageHeight)
	{
		SIZEY = pageHeight;
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
	
	public static void createDiamond(PdfContentByte canvas, float x, float y, float size)
	{
		canvas.moveTo(x, y);
		canvas.lineTo(x - size, y - size);
		canvas.lineTo(x, y - (size * 2));
		canvas.lineTo(x + size, y - size);
		canvas.lineTo(x - 0.35f, y + 0.35f);
		canvas.setColorFill(new BaseColor(255, 255, 255));
		canvas.fillStroke();
		//canvas.stroke();
	}
	
	public static void createArc(PdfContentByte canvas, float bottom, float top, float leftend, float rightend, float size)
	{
		canvas.arc(leftend - size, bottom, leftend + size, top, 0, 180);
	}
}
