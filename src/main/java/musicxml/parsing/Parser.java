package musicxml.parsing;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Parser {

	public static void main(String[] args) {

	      try {
	         File inputFile = new File("C:\\Users\\User\\Documents\\School\\Second Year\\EECS 2311\\Code\\MusicXMLParserAttempt\\src\\guitarTabXMLText.txt");
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         System.out.println("Instrument :" + doc.getElementsByTagName("part-name").item(0).getTextContent());
	         NodeList nList = doc.getElementsByTagName("note");
//	         System.out.println("----------------------------");
	         
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            System.out.println("\nCurrent Element :" + nNode.getNodeName());
	            
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               System.out.println("Pitch:");
	               System.out.println("\tStep: " 
	 	                  + eElement.getElementsByTagName("step")
		                  .item(0)
		                  .getTextContent()); 
	               System.out.println("\tOctave: " 
	 	   	              + eElement.getElementsByTagName("octave")
		                  .item(0)
		                  .getTextContent());
	               System.out.println("Duration: " 
	                  + eElement
	                  .getElementsByTagName("duration")
	                  .item(0)
	                  .getTextContent());
	               System.out.println("Voice: " 
	                  + eElement
	                  .getElementsByTagName("voice")
	                  .item(0)
	                  .getTextContent());
	               System.out.println("Type: " 
	                  + eElement
	                  .getElementsByTagName("type")
	                  .item(0)
	                  .getTextContent());
	               System.out.println("Notations:");
	               System.out.println("\tString: " 
	 	                  + eElement.getElementsByTagName("string")
		                  .item(0)
		                  .getTextContent()); 
	               System.out.println("\tFret: " 
	 	   	              + eElement.getElementsByTagName("fret")
		                  .item(0)
		                  .getTextContent());
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }

}
