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
	private ArrayList<Barline> barlines = new ArrayList<Barline>();
	private Direction direction;

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
			initializeGuitarNotes(); //Method name should be changed if it's also for bass notes
		} else { // Otherwise, it's assumed to be drumset
			initializeDrumNotes();
		}
		//Get repeats, if they exist
		getRepeat();
	}
	
	private void getRepeat() {
		NodeList measureList = musicXML.getElementsByTagName("measure");

		// Get nodes for current measure
		Node currentMeasureParentNode = measureList.item(measureNumber - 1); //Subtract 1 since it starts from 0
		NodeList noteList = currentMeasureParentNode.getChildNodes();
				
		//Loop through every note in the measure
		for (int i = 0; i < noteList.getLength(); i++) {
			Node currentNode = noteList.item(i);
			//Get Barline
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				if (currentNode.getNodeName().equals("barline")) {
					Element eElement = (Element) currentNode;				
					try {
						String location = eElement.getAttribute("location");
						String barStyle = eElement.getElementsByTagName("bar-style").item(0).getTextContent();
						String direction = eElement.getElementsByTagName("repeat").item(0).getAttributes().getNamedItem("direction").getNodeValue();
						int times = 0;
						//times is optional within the "barline" element
						try {
							times = Integer.parseInt(eElement.getElementsByTagName("repeat").item(0).getAttributes().getNamedItem("times").getNodeValue());
						} catch (NullPointerException e) {
							//Do nothing since times is already 0
						}
						barlines.add(new Barline(location, barStyle, direction, times));
					} 
					catch (NullPointerException e) {
						//Do nothing since we don't want to add a null value to the ArrayList
					}
				}
				//Get Direction
				if (currentNode.getNodeName().equals("direction")) {
					Element eElement = (Element) currentNode;
					try {
						String placement = eElement.getAttribute("placement");
						String words = eElement.getElementsByTagName("words").item(0).getTextContent();
						double x = Double.parseDouble(eElement.getElementsByTagName("words").item(0).getAttributes().getNamedItem("relative-x").getNodeValue());
						double y = Double.parseDouble(eElement.getElementsByTagName("words").item(0).getAttributes().getNamedItem("relative-y").getNodeValue());
						direction = new Direction(placement, x, y, words);
					} 
					catch (NullPointerException e) {
						direction = new Direction("", 0, 0, null);
					}
				}
				if (direction == null) {
					direction = new Direction("", 0, 0, null);
				}
			}
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
				Unpitched unpitch;
				try {
					char step = eElement.getElementsByTagName("display-step").item(0).getTextContent().charAt(0);
					int octave = Integer.parseInt(eElement.getElementsByTagName("display-octave").item(0).getTextContent());
					unpitch = new Unpitched(step, octave);
				} catch (NullPointerException e) {
					unpitch = new Unpitched('Z', -1);
				}
				
				// Get the duration (if possible) or set it to 0 (if it's a grace note)
				int duration;
				try {
					duration = Integer.parseInt(eElement.getElementsByTagName("duration").item(0).getTextContent());
				} catch (NullPointerException e) { // If there is no duration, then it is a grace note
					duration = 0;
				}
				
				//Get the instrument id
				String instrumentID = "none";
				try {
					instrumentID = eElement.getElementsByTagName("instrument").item(0).getAttributes().getNamedItem("id").getNodeValue();
				} catch (NullPointerException e) { // If there is no duration, then it is a grace note
					//Default has already been set
				}

				//Get voice, type, and stem
				int voice;
				try {
					voice = Integer.parseInt(eElement.getElementsByTagName("voice").item(0).getTextContent());
				} catch (NullPointerException e) { 
					voice = -1;
				}
				String type, stem;
				try {
					type = eElement.getElementsByTagName("type").item(0).getTextContent();
				} catch (NullPointerException e) { // If there is no duration, then it is a grace note
					type = "non-existent";
				}
				try {
					stem = eElement.getElementsByTagName("stem").item(0).getTextContent();
				} catch (NullPointerException e) { // If there is no duration, then it is a grace note
					stem = "non-existent";
				}
				
				//Get notehead (since this is optional, set default to null)
				String notehead = null;
				try {
					notehead = eElement.getElementsByTagName("notehead").item(0).getTextContent();
				} catch (NullPointerException e) {
					//Do nothing, since the default value is set to null
				}
				
				double bendAlter = 0.0;
				try {
					bendAlter = Double.parseDouble(eElement.getElementsByTagName("bend-alter").item(0).getTextContent());
				} catch (NullPointerException e) {
					//Do nothing, since the default value is already set
				}
				
				int numDots = 0;
				try {
					numDots = eElement.getElementsByTagName("dot").getLength();
				} catch (NullPointerException | IndexOutOfBoundsException e) {
					//Do nothing since the default is already set to 0
				}
				
				this.notes.add(new Note(unpitch, duration, instrumentID, voice, type, stem, notehead, bendAlter, numDots));
				
				//-----------------------------------------------
				try {
					eElement.getElementsByTagName("chord").item(0).getTextContent();
					this.notes.get(noteCounter).setChord();
				} catch (NullPointerException | IndexOutOfBoundsException e) {
					// This means the note is not a chord, and nothing has to be done
				}
				
				try {
					eElement.getElementsByTagName("grace").item(0).getTextContent();
					this.notes.get(noteCounter).setGraceNote();
				} catch (NullPointerException | IndexOutOfBoundsException e) {
					// This means the note is not a grace note, and nothing has to be done
				}
				
				try {
					eElement.getElementsByTagName("rest").item(0).getTextContent();
					this.notes.get(noteCounter).setChord();
				} catch (NullPointerException | IndexOutOfBoundsException e) {
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
				char step = 'Z'; int octave = -1;
				try {
					step = eElement.getElementsByTagName("step").item(0).getTextContent().charAt(0);
				} catch (NullPointerException e) {
					//Default value has already been set
				}
				try {
					octave = Integer.parseInt(eElement.getElementsByTagName("octave").item(0).getTextContent());
				} catch (NullPointerException e) {
					//Default value has already been set
				}
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

				int voice = -1, string = -1, fret = -1;
				String type = "non-existent";
				try {
					voice = Integer.parseInt(eElement.getElementsByTagName("voice").item(0).getTextContent());
				} catch (NullPointerException e) {
					//Default value has already been set
				}
				try {
					fret = Integer.parseInt(eElement.getElementsByTagName("fret").item(0).getTextContent());
				} catch (NullPointerException e) {
					//Default value has already been set
				}
				try {
					string = Integer.parseInt(eElement.getElementsByTagName("string").item(0).getTextContent());
				} catch (NullPointerException e) {
					//Default value has already been set
				}
				try {
					type = eElement.getElementsByTagName("type").item(0).getTextContent();
				} catch (NullPointerException e) {
					//Default value has already been set
				}

				//Get Slur
				Slur slur = new Slur(0, null, null);
				if (eElement.getElementsByTagName("slur").item(0) != null) { //Changed "number" to "slur". Will verify if this works later
					String slurPlace, slurType;
					int slurNum = Integer.parseInt(eElement.getElementsByTagName("slur").item(0).getAttributes().item(0).getNodeValue());
					if (eElement.getElementsByTagName("slur").item(0).getAttributes().item(1).getNodeName().equals("placement")) {
						slurPlace = eElement.getElementsByTagName("slur").item(0).getAttributes().item(1).getNodeValue();
						slurType = eElement.getElementsByTagName("slur").item(0).getAttributes().item(2).getNodeValue();
					} 
					//Placement does not exist and should be set to null
					else {
						slurPlace = null;
						slurType = eElement.getElementsByTagName("slur").item(0).getAttributes().item(1).getNodeValue();
					}
					
					slur = new Slur(slurNum, slurPlace, slurType);
				}

				//Get Pull-Off
				PullOff pullOff = new PullOff(0, null, null);
				if (eElement.getElementsByTagName("pull-off").item(0) != null) {
					int pullOffNum = Integer.parseInt(eElement.getElementsByTagName("pull-off").item(0).getAttributes().item(0).getNodeValue());
					String pullOffType = eElement.getElementsByTagName("pull-off").item(0).getAttributes().item(1).getNodeValue();
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
				
				//Get the tied values, if they exist
				Tied tied = new Tied();
				//There are 4 possible values for tied, so we loop through this 4 times
				for (int a = 0; a < 4; a++) {
					try {
						String tiedVal = eElement.getElementsByTagName("tied").item(a).getAttributes().item(0).getNodeValue();
						switch (tiedVal.trim().toLowerCase()) {
						case "stop": 		tied.setStop(true); 	break;
						case "start": 		tied.setStart(true); 	break;
						case "continue": 	tied.setCont(true); 	break;
						case "let-ring": 	tied.setLetRing(true); 	break;
						}
					} catch (NullPointerException e) {
						//If it doesn't exist, nothing needs to be done
					}
				}
				
				double bendAlter = 0.0;
				try {
					bendAlter = Double.parseDouble(eElement.getElementsByTagName("bend-alter").item(0).getTextContent());
				} catch (NullPointerException e) {
					//Do nothing, since the default value is already set
				}
				
				int numDots = 0;
				try {
					numDots = eElement.getElementsByTagName("dot").getLength();
				} catch (NullPointerException | IndexOutOfBoundsException e) {
					//Do nothing since the default is already set to 0
				}
				
				this.notes.add(new Note(pitch, duration, voice, type, string, fret, slur, pullOff, tied, bendAlter, numDots));
				
				try {
					eElement.getElementsByTagName("chord").item(0).getTextContent();
					this.notes.get(noteCounter).setChord();
				} catch (NullPointerException | IndexOutOfBoundsException e) {
					// This means the note is not a chord, and nothing has to be done
				}
				
				try {
					eElement.getElementsByTagName("grace").item(0).getTextContent();
					this.notes.get(noteCounter).setGraceNote();
				} catch (NullPointerException | IndexOutOfBoundsException e) {
					// This means the note is not a grace note, and nothing has to be done
				}
				
				try {
					eElement.getElementsByTagName("rest").item(0).getTextContent();
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
	
	public Direction getDirection() {
		return direction;
	}
	
	public ArrayList<Barline> getBarlines() {
		return barlines;
	}
}
