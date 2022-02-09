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
	private ArrayList<Note> notes;
	private ArrayList<StaffTuning> lines; //Includes tuning-step and tuning-octave

	private File musicXML; //Might not need this stored, assuming this parser will only ever run once. Good to have just in case
	private DocumentBuilderFactory dbFactory;	
	private DocumentBuilder dBuilder;
	private Document doc;

	public Parser(File musicXML) throws SAXException, IOException {
		//Initialize and standardize MusicXML file
		this.prepareDocumentForReading();
		//Get instrument
		this.parseInstrument();
		//Create list for all notes
		this.parseNotes(); //Inside here, add a method for each note value/sub-tag
		//Initialize attributes
			
		//Staff details (within attributes) is only needed for guitars (and maybe bass??)
			//These include a <staff-lines> tag which indicates how many lines need to be drawn on the sheet musicx
	}

	//Parser (private) helper methods
	private void prepareDocumentForReading() {
		//Change this first line so the input is read instead
		musicXML = new File("C:\\Users\\User\\Documents\\School\\Second Year\\EECS 2311\\Code\\MusicXMLParserAttempt\\src\\guitarTabXMLText.txt");
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(musicXML);
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
		}
	}

	private void parseNotes() {
		NodeList noteList = doc.getElementsByTagName("note");

		for (int i = 0; i < noteList.getLength(); i++) {
			Node currentNode = noteList.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) currentNode;
				char step = eElement.getElementsByTagName("step").item(0).getTextContent().charAt(0);
				int octave = Integer.parseInt(eElement.getElementsByTagName("octave").item(0).getTextContent());
				int duration = Integer.parseInt(eElement.getElementsByTagName("duration").item(0).getTextContent());
				int voice = Integer.parseInt(eElement.getElementsByTagName("voice").item(0).getTextContent());
				String type = eElement.getElementsByTagName("type").item(0).getTextContent();
				int string = Integer.parseInt(eElement.getElementsByTagName("string").item(0).getTextContent());
				int fret = Integer.parseInt(eElement.getElementsByTagName("fret").item(0).getTextContent());

				this.notes.add(new Note(step, octave, duration, voice, type, string, fret));
			}
		}
	}

	private void parseAttributes() {
		int divisions;
		int fifths;
		
		String sign;
		int line;
		Clef clef;
		
		NodeList attributes = doc.getElementsByTagName("attributes");

		for (int i = 0; i < attributes.getLength(); i++) {
			Node currentNode = attributes.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				Element currElement = (Element) currentNode;
//				currElement.getElementsByTagName("step").item(0).getTextContent();
				
				divisions = Integer.parseInt(currElement.getElementsByTagName("divisions").item(0).getTextContent());
				fifths = Integer.parseInt(currElement.getElementsByTagName("fifths").item(0).getTextContent());
				
				sign = currElement.getElementsByTagName("fifths").item(0).getTextContent();
				line = Integer.parseInt(currElement.getElementsByTagName("fifths").item(0).getTextContent());
				clef = new Clef(sign, line);
				
			}
		}		
	}
	
	//Graphics helper methods
	public String getStaffDetails() {
		//Used for drawing the graphics
		return null;
	}
	
	//Accessors
	public ArrayList<Note> getNotes() {
		return this.notes;
	}

	public String getInstrument() {
		return this.instrument;
	}

}
