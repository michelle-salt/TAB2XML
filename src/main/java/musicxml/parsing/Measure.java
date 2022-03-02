package musicxml.parsing;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import models.measure.note.Note;

public class Measure {
	private Document musicXML;

	private int measureNumber;
	private Attributes attributes;
	private ArrayList<Note> notes = new ArrayList<Note>();
//	boolean isGuitar;
//	private ArrayList<GuitarNote> guitarNotes = new ArrayList<GuitarNote>();
//	private ArrayList<DrumNote> drumNotes = new ArrayList<DrumNote>();

	public Measure(Document musicXML, int measureNumber, String instrument) {
		// Set musicXML
		this.musicXML = musicXML;
		// Set measureNumber
		this.measureNumber = measureNumber;

		// Initialize attributes
		this.attributes = new Attributes(musicXML, measureNumber);
		// Get a list of notes, based on the instrument (since they're parsed differently)
		// I don't know how bass musicxml differs from guitar musicxml (yet) so I'll assume they're the same for now
		if (instrument.equals("guitar") || instrument.equals("bass")) {
//			isGuitar = true;
			initializeGuitarNotes(); //Method name should be changed if it's also for bass notes
		} else { // Otherwise, it's assumed to be drumset
//			isGuitar = false;
			initializeDrumNotes();
		}
	}

	private void initializeDrumNotes() {
		NodeList measureList = musicXML.getElementsByTagName("measure");

		// Get nodes for current measure
		Node currentMeasureParentNode = measureList.item(measureNumber - 1); //Subtract 1 since it starts from 0
		NodeList noteList = currentMeasureParentNode.getChildNodes();

		int noteCounter = 0;
		
		//Loop through every note in the measure
		for (int i = 0; i < noteList.getLength(); i++) {
			Node currentNode = noteList.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName().equals("note")) {
				Element eElement = (Element) currentNode;

				/*
				 * pull off hammer slur dotted grace time modification slide key
				 */
				
				//Get the step and octave from the 'unpitched' tag
				char step = eElement.getElementsByTagName("display-step").item(0).getTextContent().charAt(0);
				int octave = Integer.parseInt(eElement.getElementsByTagName("display-octave").item(0).getTextContent());
				Unpitched unpitch = new Unpitched(step, octave);
				
				// Get the duration (if possible) or set it to 0 (if it's a grace note)
				int duration;
				try {
					duration = Integer.parseInt(eElement.getElementsByTagName("duration").item(0).getTextContent());
				} catch (NullPointerException e) { // If there is no duration, then it is a grace note
					duration = 0;
				}
				
				//Get the instrument id
				String instrumentID = eElement.getElementsByTagName("instrument").item(0).getAttributes().getNamedItem("id").getNodeValue();

				//Get voice, type, and stem
				int voice = Integer.parseInt(eElement.getElementsByTagName("voice").item(0).getTextContent());
				String type = eElement.getElementsByTagName("type").item(0).getTextContent();
				String stem = eElement.getElementsByTagName("stem").item(0).getTextContent();
				
				//Get notehead (since this is optional, set default to null)
				String notehead = null;
				try {
					notehead = eElement.getElementsByTagName("notehead").item(0).getTextContent();
				} catch (NullPointerException e) {
					//Do nothing, since the default value is set to null
				}
				
				this.notes.add(new Note(unpitch, duration, instrumentID, voice, type, stem, notehead));
				System.out.println("sjfn");
				
//				//Get Slur
//				Slur slur = new Slur(0, null, null);
//				if (eElement.hasAttribute("slur")) {
//					int slurNum = Integer.parseInt(eElement.getAttribute("number"));
//					String slurPlace = eElement.getAttribute("placement");
//					String slurType = eElement.getAttribute("type");
//					slur = new Slur(slurNum, slurPlace, slurType);
//				}
//
//				//Get Pull-Off
//				PullOff pullOff = new PullOff(0, null, null);
//				if (eElement.hasAttribute("pull-off")) {
//					int pullOffNum = Integer.parseInt(eElement.getAttribute("number"));
//					String pullOffType = eElement.getAttribute("type");
//					String pullOffVal;
//					try {
//						pullOffVal = eElement.getElementsByTagName("pull-off").item(0).getTextContent();
//					} catch (NullPointerException e) {
//						pullOffVal = null;
//					}
//					pullOff = new PullOff(pullOffNum, pullOffType, pullOffVal);
//				} 
//
				try {
					eElement.getElementsByTagName("chord").item(0).getTextContent();
					this.notes.get(noteCounter).setChord();
				} catch (NullPointerException e) {
					// This means the note is not a chord, and nothing has to be done
				}
				noteCounter++;
			}
		}
	}

	private void initializeGuitarNotes() {
		NodeList measureList = musicXML.getElementsByTagName("measure");

		// Get nodes for current measure
		Node currentMeasureParentNode = measureList.item(measureNumber - 1); //Subtract 1 since it starts from 0
		NodeList noteList = currentMeasureParentNode.getChildNodes();

		int noteCounter = 0;

		for (int i = 0; i < noteList.getLength(); i++) {
			Node currentNode = noteList.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName().equals("note")) {
				Element eElement = (Element) currentNode;

				Pitch pitch;
				char step = eElement.getElementsByTagName("step").item(0).getTextContent().charAt(0);
				int octave = Integer.parseInt(eElement.getElementsByTagName("octave").item(0).getTextContent());
				try {
					int alter = Integer.parseInt(eElement.getElementsByTagName("alter").item(0).getTextContent()); // Optional
					pitch = new Pitch(step, octave, alter);
				} catch (NullPointerException e) {
					// This means an alter has not been provided
					pitch = new Pitch(step, octave);
				}

				/*
				 * pull off hammer slur dotted grace time modification slide key
				 */

				int voice = Integer.parseInt(eElement.getElementsByTagName("voice").item(0).getTextContent());
				String type = eElement.getElementsByTagName("type").item(0).getTextContent();
				int string = Integer.parseInt(eElement.getElementsByTagName("string").item(0).getTextContent());
				int fret = Integer.parseInt(eElement.getElementsByTagName("fret").item(0).getTextContent());

				//Get Slur
				Slur slur = new Slur(0, null, null);
				if (eElement.hasAttribute("slur")) { //Changed "number" to "slur". Will verify if this works later
					int slurNum = Integer.parseInt(eElement.getAttribute("number"));
					String slurPlace = eElement.getAttribute("placement");
					String slurType = eElement.getAttribute("type");
					slur = new Slur(slurNum, slurPlace, slurType);
				}

				//Get Pull-Off
				PullOff pullOff = new PullOff(0, null, null);
				if (eElement.hasAttribute("pull-off")) {
					int pullOffNum = Integer.parseInt(eElement.getAttribute("number"));
					String pullOffType = eElement.getAttribute("type");
					String pullOffVal;
					try {
						pullOffVal = eElement.getElementsByTagName("pull-off").item(0).getTextContent();
					} catch (NullPointerException e) {
						pullOffVal = null;
					}
					pullOff = new PullOff(pullOffNum, pullOffType, pullOffVal);
				} 

				// Get the duration (if possible) or set it to 0 (if it's a grace note)
				int duration;
				try {
					duration = Integer.parseInt(eElement.getElementsByTagName("duration").item(0).getTextContent());
				} catch (NullPointerException e) { // If there is no duration, then it is a grace note
					duration = 0;
				}
				this.notes.add(new Note(pitch, duration, voice, type, string, fret, slur, pullOff));

				try {
					eElement.getElementsByTagName("chord").item(0).getTextContent();
					this.notes.get(noteCounter).setChord();
				} catch (NullPointerException | IndexOutOfBoundsException e) {
					// This means the note is not a chord, and nothing has to be done
				}
				noteCounter++;
			}
		}
	}
	
	// Methods for GUI
	public int getNumNotes() {
		return this.notes.size();
	}

	// Public accessors
	public int getMeasureNumber() {
		return measureNumber;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public ArrayList<Note> getNotes() {
		return notes;
	}
	
//	public ArrayList<GuitarNote> getDrumNotes() {
//		return guitarNotes;
//	}
}
