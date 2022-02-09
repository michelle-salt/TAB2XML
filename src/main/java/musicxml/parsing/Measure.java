package musicxml.parsing;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Measure {
	private Document musicXML;
	
	private int measureNumber;
	private Attributes attributes;
	private ArrayList<Note> notes = new ArrayList<Note>();
	
	public Measure(Document musicXML, int measureNumber) {
		//Set musicXML
		this.musicXML = musicXML;
		//Set measureNumber
		this.measureNumber = measureNumber;
		
		//Initialize attributes
		this.attributes = new Attributes(musicXML);
		//Get a list of notes
		initializeNotes();
	}
	
	//Create two methods based on parser class methods
	private void initializeNotes() {
		NodeList noteList = musicXML.getElementsByTagName("note");

		for (int i = 0; i < noteList.getLength(); i++) {
			Node currentNode = noteList.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) currentNode;
				
				Pitch pitch;
				char step = eElement.getElementsByTagName("step").item(0).getTextContent().charAt(0);
				int octave = Integer.parseInt(eElement.getElementsByTagName("octave").item(0).getTextContent());
				try {
					int alter = Integer.parseInt(eElement.getElementsByTagName("alter").item(0).getTextContent()); //Optional
					pitch = new Pitch(step, octave, alter);
				} catch (NullPointerException e) {
					//This means an alter has not been provided
					pitch = new Pitch(step, octave);
				}

				int duration = Integer.parseInt(eElement.getElementsByTagName("duration").item(0).getTextContent());
				int voice = Integer.parseInt(eElement.getElementsByTagName("voice").item(0).getTextContent());
				String type = eElement.getElementsByTagName("type").item(0).getTextContent();
				int string = Integer.parseInt(eElement.getElementsByTagName("string").item(0).getTextContent());
				int fret = Integer.parseInt(eElement.getElementsByTagName("fret").item(0).getTextContent());

				this.notes.add(new Note(pitch, duration, voice, type, string, fret));
			}
		}
	}

	
	//Methods for GUI
	public int getNumNotes() {
		return this.notes.size();
	}
	
	//Public accessors
	public int getMeasureNumber() {
		return measureNumber;
	}
	

	public Attributes getAttributes() {
		return attributes;
	}
	

	public ArrayList<Note> getNotes() {
		return notes;
	}
}
