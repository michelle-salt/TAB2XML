package musicxml.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Attributes {

	public static final int DEFAULT_FIFTHS = -100000;
	private int divisions;
	private int fifths;
	private Clef clef;
	private Time time;
	
	/*
	 * Staff details method is only needed (only for guitars (and maybe bass??))
	 * These include a <staff-lines> tag which indicates how many lines need to be drawn on the sheet music
	 */
	public Attributes(Document doc, int measureNumber) {
		NodeList measureList = doc.getElementsByTagName("measure");

		/*
		 * Get the nodes for the current measure
		 * NOTE: item should start from  0 
		 */
		Node currentMeasureParentNode = measureList.item(measureNumber - 1);
		NodeList attributesInMeasure = currentMeasureParentNode.getChildNodes();
		
		for (int i = 0; i < attributesInMeasure.getLength(); i++) {	
			
			// Get Node at position i.
			Node currentNode = attributesInMeasure.item(i);
			
			// Get Element of the current node.
			if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName().equals("attributes")) {
				Element currentElement = (Element) currentNode;
				
				divisions = Integer.parseInt(currentElement.getElementsByTagName("divisions").item(0).getTextContent());
				try {
					fifths = Integer.parseInt(currentElement.getElementsByTagName("fifths").item(0).getTextContent());
				} 
				catch (NullPointerException e) {
					fifths = DEFAULT_FIFTHS; // Random value that's probably not a valid number
				}

				// Clef will only exist for the first measure.
				try {
					String sign = currentElement.getElementsByTagName("sign").item(0).getTextContent();
					int line = Integer.parseInt(currentElement.getElementsByTagName("line").item(0).getTextContent());
					clef = new Clef(sign, line);
				} 
				catch (NullPointerException e) {
					clef = new Clef(null, 0);
				}
				
				// Time only exists in some MusicXML files
				try {
					int beats = Integer.parseInt(currentElement.getElementsByTagName("beats").item(0).getTextContent());
					int beatType = Integer.parseInt(currentElement.getElementsByTagName("beat-type").item(0).getTextContent());
					time = new Time(beats, beatType);
				} 
				catch (NullPointerException e) {
					//Default is 4, 4
					time = new Time(4, 4);
				}
				
				break; // Since there's only one attribute tag
			}
			
		}
	}

	public int getDivisions() {
		return divisions;
	}

	public int getFifths() {
		return fifths;
	}
	
	public Clef getClef() {
		return clef;
	}
	
	public Time getTime() {
		return time;
	}	
}
