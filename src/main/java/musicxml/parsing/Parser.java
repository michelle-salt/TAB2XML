package musicxml.parsing;

import java.io.File;
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
		//Change this first line so the input is read instead
//		musicXML = new File("C:\\Users\\User\\Documents\\School\\Second Year\\EECS 2311\\Code\\MusicXMLParserAttempt\\src\\guitarTabXMLText.txt");
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			//File() constructor needs the path file; it does not convert a String to a File.
			doc = dBuilder.parse(new File(musicXML));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Problem parsing the file");
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();
	}
	
	private void parseInstrument() {
		instrument = doc.getElementsByTagName("part-name").item(0).getTextContent().trim();
		if (instrument.equalsIgnoreCase("Drumset")) {
			instrument = "STEEL_DRUMS"; //MidiDictionary also allows "TAIKO_DRUM" and "SYNTH_DRUM"
			//This will likely require the parsing of all the instruments within the drumset (e.g. cymbals)
			//Call a method to parse all the instruments within the drumset
		} else if (instrument.equalsIgnoreCase("Bass")) {
			instrument = "ACOUSTIC_BASS"; //MidiDictionary has 8 different types of bass available
		} else if (!instrument.equalsIgnoreCase("Guitar")) {
			instrument = null;
		}
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
