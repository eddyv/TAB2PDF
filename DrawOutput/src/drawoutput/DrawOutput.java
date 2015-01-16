package drawoutput;

import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class DrawOutput {

  public static void main(String[] args) throws Exception{
      Rectangle pageSize = new Rectangle(100, 100);
      Document document = new Document(pageSize);

      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("DrawOutput.pdf"));
      document.open();

      PdfContentByte cb = writer.getDirectContent();
      cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false), 24);
      
      /*
       * Drawing symbols used in the output
       * Currently the location for each item is coded directly
       * When implementing will need to change so that it can be adjusted dynamically
       */
      
      /*
       * Draw thin vertical line
       */
      cb.setLineWidth((float) 0.5);
      cb.moveTo(20, 100);
      cb.lineTo(20, 50);
      cb.stroke();
      
      /*
       * Draw repeat bar
       */
      
      cb.setLineWidth((float) 0.5);
      cb.moveTo(47, 100);
      cb.lineTo(47, 50);
      cb.stroke();
      
      cb.circle(42, 84, 2);
      cb.circle(42, 68, 2);
      cb.fillStroke();
      
      cb.setLineWidth((float) 1.5);
      cb.moveTo(50, 100);
      cb.lineTo(50, 50);
      cb.stroke();
      
      document.close();
  }
}