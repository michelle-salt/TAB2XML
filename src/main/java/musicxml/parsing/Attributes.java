package musicxml.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Attributes {
	private int divisions;
	private int fifths;
	private Clef clef;

	//Staff details method is only needed (only for guitars (and maybe bass??))
		//These include a <staff-lines> tag which indicates how many lines need to be drawn on the sheet music
	public Attributes(Document doc) {
		NodeList attributes = doc.getElementsByTagName("attributes");

		for (int i = 0; i < attributes.getLength(); i++) {
			Node currentNode = attributes.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				Element currElement = (Element) currentNode;

				divisions = Integer.parseInt(currElement.getElementsByTagName("divisions").item(0).getTextContent());
				fifths = Integer.parseInt(currElement.getElementsByTagName("fifths").item(0).getTextContent());

				String sign = currElement.getElementsByTagName("sign").item(0).getTextContent();
				int line = Integer.parseInt(currElement.getElementsByTagName("line").item(0).getTextContent());
				clef = new Clef(sign, line);

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
