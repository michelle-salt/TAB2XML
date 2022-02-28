package musicxml.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Attributes {
	private int divisions;
	private int fifths;
	private Clef clef;
	
	public static final int DEFAULT_FIFTHS = -100000;

	//Staff details method is only needed (only for guitars (and maybe bass??))
		//These include a <staff-lines> tag which indicates how many lines need to be drawn on the sheet music
	public Attributes(Document doc, int measureNumber) {
		NodeList measureList = doc.getElementsByTagName("measure");

		// Get nodes for current measure
		Node currentMeasureParentNode = measureList.item(measureNumber - 1); //Subtract 1 since it starts from 0
		NodeList attributesInMeasure = currentMeasureParentNode.getChildNodes();

		int counter = 0;
		for (int i = 0; i < attributesInMeasure.getLength(); i++, counter++) {
			Node currentNode = attributesInMeasure.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName().equals("attributes")) {
				Element currElement = (Element) currentNode;
				
				divisions = Integer.parseInt(currElement.getElementsByTagName("divisions").item(0).getTextContent());
				try {
					fifths = Integer.parseInt(currElement.getElementsByTagName("fifths").item(0).getTextContent());
				} catch (NullPointerException e) {
					fifths = DEFAULT_FIFTHS; //Random value that's probably not a valid number
				}
				

				//Clef will only exist for the first measure
				try {
					String sign = currElement.getElementsByTagName("sign").item(0).getTextContent();
					int line = Integer.parseInt(currElement.getElementsByTagName("line").item(0).getTextContent());
					clef = new Clef(sign, line);
				} catch (NullPointerException e) {
					clef = new Clef(null, 0);
				}
				break; //Since there's only one attribute tag
			}
		}
	}

	
	//Public accessors
	public int getDivisions() {
		return divisions;
	}
	

	public int getFifths() {
		return fifths;
	}
	

	public Clef getClef() {
		return clef;
	}
}
