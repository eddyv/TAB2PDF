package tab2pdf;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;


public class Symbol {

	public static float SIZEY;
	
	public Symbol(float pageHeight)
	{
		SIZEY = pageHeight;
	}
	
    public void createTextCenteredAtPosition(PdfContentByte canvas, String text, float x, float y, float size) throws DocumentException, IOException {
    	BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1257, BaseFont.EMBEDDED);
        canvas.setFontAndSize(bf, size);
        canvas.setLineWidth(0.5f);
        canvas.beginText();
        canvas.showTextAligned(Element.ALIGN_CENTER, text, x, SIZEY - y, 0);
        canvas.endText();
        canvas.stroke();
    }
    
    public void createHLineAtPosition(PdfContentByte canvas, float x, float y, float length) {
        canvas.setLineWidth(0.5f);
        canvas.moveTo(x, SIZEY - y);
        canvas.lineTo(x + length, SIZEY - y);
        canvas.stroke();
    }

    public void createVLineAtPosition(PdfContentByte canvas, float x, float y, float length) {
        canvas.setLineWidth(0.5f);
        canvas.moveTo(x, SIZEY - y);
        canvas.lineTo(x, SIZEY - (y + length));
        canvas.stroke();
    }

	//draw circle for repeat bar
    public void createCircle(PdfContentByte canvas, float x, float y, float length){
    		canvas.circle(x + 2.5f, SIZEY - y, 1.25f);
    		canvas.fillStroke();
    		createHLineAtPosition(canvas, x, y, length);
    		
    }
    
    public void createRDoubleBar(PdfContentByte canvas, float x, float y, float length){
    	
    	canvas.setLineWidth(1.8f);
    	canvas.moveTo(x, SIZEY - y);
    	canvas.lineTo(x, SIZEY - (y + length));
    	canvas.stroke();
    	
    	canvas.setLineWidth(0.5f);
    	canvas.moveTo(x - 3, SIZEY - y);
    	canvas.lineTo(x - 3, SIZEY - (y + length));
    	canvas.stroke();
    	
    }
    
    public void createLDoubleBar(PdfContentByte canvas, float x, float y, float length){
    	canvas.setLineWidth(1.8f);
    	canvas.moveTo(x, SIZEY - y);
    	canvas.lineTo(x, SIZEY - (y + length));
    	canvas.stroke();
    	
    	canvas.setLineWidth(0.5f);
    	canvas.moveTo(x + 3, SIZEY - y);
    	canvas.lineTo(x + 3, SIZEY - (y + length));
    	canvas.stroke();
    }
    
    //draw S
    public void createS(PdfContentByte canvas, float x, float y, float length) {
    	//PdfContentByte canvas = writer.getDirectContent();
		
    	float tiltAngle = (float)(-45 * (Math.PI / 180));
		float scaleX = (float)(Math.cos(tiltAngle)); 		
		float scaleY = (float)(Math.cos(tiltAngle)); 
		float tiltX  = (float)(-Math.sin(tiltAngle)); 
		float tiltY  = (float)(Math.sin(tiltAngle)); 

		PdfTemplate diagonal = null;

		diagonal = canvas.createTemplate(12/2.0f, 12/2.0f);
		diagonal.moveTo(0f, 12/4.0f);
		diagonal.lineTo(12/2.0f, 12/4.0f);
		diagonal.stroke();
		
		//adds the diagonal line to the PdfContentByte object
		canvas.addTemplate(diagonal, scaleX, tiltX, scaleY, tiltY, x - (length/2), SIZEY - y);
		
		//adds the horizontal line to the PdfContentByte object
		canvas.moveTo(x, SIZEY - y);
        canvas.lineTo(x + length, SIZEY - y);
	}
    
	public void createStar (PdfContentByte canvas, float x, float y, float length, boolean nextCharBar) {
		//PdfContentByte canvas = writer.getDirectContent();
		if (nextCharBar) {
			canvas.circle(x + (length - 5), SIZEY - y, 7/5f);
		}
		else {
			canvas.circle(x - (length - 5), SIZEY - y, 7/5f);
		}
		canvas.fillStroke();
	}
	
	public void createDiamond(PdfContentByte canvas, float x, float y, float size)
	{
		canvas.setLineWidth(0.5f);
		//canvas.moveTo(x, SIZEY - y);
		canvas.moveTo(x - size, SIZEY - y - size);
		canvas.lineTo(x, SIZEY - y);
		canvas.lineTo(x + size, SIZEY - y - size);
		canvas.lineTo(x, SIZEY - y - (size * 2));
		canvas.lineTo(x - size, SIZEY - y - size);
		//canvas.lineTo(x - size, SIZEY - y - size);
		//canvas.lineTo(x, SIZEY - y - (size * 2));
		//canvas.lineTo(x + size, SIZEY - y - size);
		//canvas.lineTo(x, SIZEY - y);
		
		canvas.setColorFill(new BaseColor(255, 255, 255));
		canvas.fillStroke();
		canvas.setColorFill(new BaseColor(0, 0, 0));
		//canvas.stroke();
	}
	
	public void createArc(PdfContentByte canvas, float bottom, float top, float leftend, float rightend, float size)
	{
		canvas.arc(leftend - size, SIZEY - bottom, leftend + size, SIZEY - top, 0, 180);
	}
}
