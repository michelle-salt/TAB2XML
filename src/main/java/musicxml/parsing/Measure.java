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
//		NodeList measureList = musicXML.getChildNodes();
		NodeList measureList = musicXML.getElementsByTagName("measure");
//		NodeList noteList = measureList.getElementsByTagName("note");
		
		//Get nodes for current measure
		Node currentMeasureParentNode = measureList.item(measureNumber - 1);
		NodeList noteList = currentMeasureParentNode.getChildNodes();
		
//		for (int nodeIndex = 0; nodeIndex < measureList.getLength(); nodeIndex++) {
//			Node curr = measureList.item(nodeIndex);
//			
//			NodeList noteList = curr.getChildNodes();
//			System.out.println("test");
//		}
		
		int noteCounter = 0;
		
		for (int i = 0; i < noteList.getLength(); i++) {
			Node currentNode = noteList.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName().equals("note")) {
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
				
				/*
				 pull off
				hammer
				slur
				dotted
				grace
				time modification
				slide
				key
				 */


				int duration = Integer.parseInt(eElement.getElementsByTagName("duration").item(0).getTextContent());
				int voice = Integer.parseInt(eElement.getElementsByTagName("voice").item(0).getTextContent());
				String type = eElement.getElementsByTagName("type").item(0).getTextContent();
				int string = Integer.parseInt(eElement.getElementsByTagName("string").item(0).getTextContent());
				int fret = Integer.parseInt(eElement.getElementsByTagName("fret").item(0).getTextContent());

//				I still need to verify that getting an attribute that doesn't exist won't cause an error
				//Get Slur
				Slur slur = new Slur(0, null, null);
				
				if (eElement.hasAttribute("number")) {
					int slurNum = Integer.parseInt(eElement.getAttribute("number"));
					String slurPlace = eElement.getAttribute("placement");
					String slurType = eElement.getAttribute("type");
					slur = new Slur(slurNum, slurPlace, slurType);
				}
				
				//Gets to pull-off tag, if it exists
//				eElement.getElementsByTagName("notations").item(0).getChildNodes().item(3).getChildNodes().item(5)
				
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
//				
//				int pullOffNum = Integer.parseInt(eElement.getAttribute("number"));
//				String pullOffType = eElement.getAttribute("type");
//				//Since pitch might not exist, this might return an error
////				String pullOffVal = eElement.getElementsByTagName("pull-off").item(0).getTextContent();
//				//^^ is being replaced with
//				String pullOffVal;
//				try {
//					pullOffVal = eElement.getElementsByTagName("pull-off").item(0).getTextContent();
//				} catch (NullPointerException e) {
//					pullOffVal = null;
//				}
//				PullOff pullOff = new PullOff(pullOffNum, pullOffType, pullOffVal);
				
				this.notes.add(new Note(pitch, duration, voice, type, string, fret, slur, pullOff));
				
				try {
					eElement.getElementsByTagName("chord").item(0).getTextContent();
					this.notes.get(noteCounter).setChord();
				} catch (NullPointerException e) {
					//This means the note is not a chord, and nothing has to be done
				}
				noteCounter++;
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
