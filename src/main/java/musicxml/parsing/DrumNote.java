package musicxml.parsing;

public class DrumNote {
	private Unpitched unpitched; //Includes display-step and display-octave
	private int duration; //The length of each note (used for playing)
	private String instrumentID; //The id of each drum
	private int voice; //Used if there is more than one instrument. Shouldn't make a difference since everything is supposed to be one instrument anyways
	private char type; //maxima (M), long (L), breve (B), whole (W), half (H), quarter (Q), eighth (I), etc. (S, T, X, O, U, R, C)
	//Z is used for if the type is incorrect
	private int stem; //0 for none, 1 for down, 2 double, 3 for up
	private String notehead; //Stores many possible values; look at the official website to see what they are
	
	//Idk if I still need this
//	private boolean isChord; //If true, this note is a chord with the preceding note(s)
//	private musicxml.parsing.Slur slur; //Stores attributes of the slur (if found)
//	private musicxml.parsing.PullOff pullOff; //Stores attributes of the slur (if found)
	

	//Inside here (the notes method, actually), add a method for each note value/sub-tag	
	public DrumNote(Unpitched unpitched, int duration, String instrumentID, int voice, String noteType, String stem, String notehead) {
		//isChord defaults to false
//		isChord = false;
		//Initialize all given variables
		this.unpitched = unpitched;
		this.duration = duration;
		this.instrumentID = instrumentID;
		this.voice = voice;
		this.notehead = notehead;
		
//		this.slur = slur;
//		this.pullOff = pullOff;

		//0 for none, 1 for down, 2 double, 3 for up. Default is -1 if no other match is found
		switch (stem.toLowerCase()) {
		case "none":	this.stem = 0; break;
		case "down":	this.stem = 1; break;
		case "double":	this.stem = 2; break;
		case "up":		this.stem = 3; break;
		default:		this.stem = -1; break;
		}
		
		
		//Initialize the value of the type based on the input string, defaulting to 'Z' if it doesn't work
		switch (noteType.toLowerCase()) {
		case "maxima":	this.type = 'M'; break;
		case "long":	this.type = 'L'; break;
		case "breve":	this.type = 'B'; break;
		case "whole":	this.type = 'W'; break;
		case "half":	this.type = 'H'; break;
		case "quarter":	this.type = 'Q'; break;
		case "eighth":	this.type = 'I'; break;
		case "16th":	this.type = 'S'; break;
		case "32nd":	this.type = 'T'; break;
		case "64th":	this.type = 'X'; break;
		case "128th":	this.type = 'O'; break;
		case "256th":	this.type = 'U'; break;
		case "512th":	this.type = 'R'; break;
		case "1024th":	this.type = 'C'; break;
		default:		this.type = 'Z'; break;
		}
	}
}
