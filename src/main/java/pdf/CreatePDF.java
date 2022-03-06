package pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class CreatePDF {    
   public static void main(String args[]) {
	   Document document = new Document();
	   
	   try {
		   String filename = "sample.pdf";
		   PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filename));
		   document.open();
		   document.add(new Paragraph("This is the first line."));
		   document.add(new Paragraph());
		   document.close();
		   pdfWriter.close();
		   System.out.println(filename + " has been succesfully created!");
	   }
		catch (DocumentException e) {
			e.printStackTrace();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
   }
} 