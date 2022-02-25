package musicxml.parsing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {

	private String instrument;
	private ArrayList<Measure> measures = new ArrayList<Measure>();
	//StaffTuning lines are currently not retrieved in this method
	//They should be retrieved in the Attributes method instead (remove the variable + methods from here)
	private ArrayList<StaffTuning> lines; //Includes tuning-step and tuning-octave

	private String musicXML; //Might not need this stored, assuming this parser will only ever run once. Good to have just in case
	private File musicXMLFile;
	private DocumentBuilderFactory dbFactory;	
	private DocumentBuilder dBuilder;
	private Document doc;

	public Parser(String musicXML) {
		this.musicXML = musicXML;
		//Initialize and standardize MusicXML file
		this.prepareDocumentForReading();
		//Get instrument
		this.parseInstrument();
		//Create list to store each measure
		this.parseMeasures();
	}

	//Parser (private) helper methods
	private void prepareDocumentForReading() {
		//Write musicXML string to file
		FileWriter myWriter;
		try {
			myWriter = new FileWriter("musicXML.txt");
			myWriter.write(musicXML);
			myWriter.close();
		} catch (IOException e1) {
			System.out.println("Problem converting MusicXML to file. What a hard life.");
			e1.printStackTrace();
		}
		//Create file object to store newly created file
		musicXMLFile = new File("musicXML.txt");
		//Prepare file for reading
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(musicXMLFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.out.println("Problem parsing the file. How devastating.");
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();
	}
	
	//Drumset, Bass, or Guitar
	private void parseInstrument() {
		instrument = doc.getElementsByTagName("part-name").item(0).getTextContent().trim();
	}

	private void parseMeasures() {
		NodeList noteList = doc.getElementsByTagName("measure");

		for (int i = 0; i < noteList.getLength(); i++) {
			Node currentNode = noteList.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) currentNode;
				
				int measureNumber = Integer.parseInt(eElement.getAttribute("number"));
				measures.add(new Measure(this.doc, measureNumber));
			}
		}
	}
	
	//Graphics helper methods	
	public int getNumLines() {
		return this.lines.size();
	}
	
	public int getNumMeasures() {
		return this.measures.size();
	}
	
	//Accessors
	public ArrayList<Measure> getMeasures() {
		return this.measures;
	}

	//Line 6 would be the bottom one, line 1 would be the top (applicable for note retreival, not form StaffTuning)
	public ArrayList<StaffTuning> getLines() {
		return this.lines;
	}
	
	public String getInstrument() {
		return this.instrument;
	}
}
