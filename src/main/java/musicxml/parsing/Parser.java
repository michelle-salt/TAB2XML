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

	private String instrument, title, artist;
	private ArrayList<Measure> measures = new ArrayList<Measure>();
	
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
		this.parseInstrumentTitleArtist();
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
	private void parseInstrumentTitleArtist() {
		//Get instrument
		instrument = doc.getElementsByTagName("part-name").item(0).getTextContent().trim();
		//Get music title, if it exists
		try {
			title = doc.getElementsByTagName("movement-title").item(0).getTextContent();
		} catch (NullPointerException e) {
			title = null;
		}
		//Get artist, if it exists
		try {
			artist = doc.getElementsByTagName("creator").item(0).getTextContent();
		} catch (NullPointerException e) {
			artist = null;
		}
		if (artist.equals("")) {
			artist = null;
		}
	}

	private void parseMeasures() {
		NodeList noteList = doc.getElementsByTagName("measure");

		for (int i = 0; i < noteList.getLength(); i++) {
			Node currentNode = noteList.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) currentNode;
				
				int measureNumber = Integer.parseInt(eElement.getAttribute("number"));
				measures.add(new Measure(this.doc, measureNumber, this.getInstrument()));
			}
		}
	}
	
	
	public int getNumMeasures() {
		return this.measures.size();
	}
	
	//Accessors
	public ArrayList<Measure> getMeasures() {
		return this.measures;
	}
	
	//Drumset, Bass, or Guitar
	public String getInstrument() {
		return this.instrument.toLowerCase();
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getArtist() {
		return this.artist;
	}
}
