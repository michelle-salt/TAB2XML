package musicxml.parsing;

public class Note {
	private int duration; //The length of each note (used for playing)
	private int voice; //Used if there is more than one instrument. Shouldn't make a difference since everything is supposed to be one instrument anyways
	private char type; //maxima (M), long (L), breve (B), whole (W), half (H), quarter (Q), eighth (I), etc. (S, T, X, O, U, R, C)
						//Z is used for if the type is incorrect
	private double bendAlter;
	private int numDots;
	private boolean isChord; //If true, this note is a chord with the preceding note(s)
	private boolean isGraceNote;
	private boolean isRest;
	private Tremolo tremolo;
	
	public Note(int duration, int voice, String noteType, double bendAlter, int numDots, Tremolo tremolo) {
		//isGraceNote defaults to false
		isGraceNote = false;
		//isChord defaults to false
		isChord = false;
		//Set other values based on arguments passed
		this.duration = duration;
		this.voice = voice;
		this.bendAlter = bendAlter;
		this.numDots = numDots;
		this.tremolo = tremolo;
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
	
	//Public getters for common attributes
	public int getDuration() {
		return duration;
	}

	public int getVoice() {
		return voice;
	}

	public char getType() {
		return type;
	}
	
	public double getBendAlter() {
		return bendAlter;
	}
	
	public int getNumDots() {
		return numDots;
	}
	
	public Tremolo getTremolo() {
		return tremolo;
	}
	
	public boolean isChord() {
		return isChord;
	}
	
	public boolean isGraceNote() {
		return isGraceNote;
	}

	public boolean isRest() {
		return isRest;
	}
	
	//Setter to indicate that the note is a chord
	public void setChord() {
		this.isChord = true;
	}
		
	//Setter to indicate that the note is a grace note
	public void setGraceNote() {
		this.isGraceNote = true;
	}
	
	//Setter to indicate that the note is a rest
	public void setRest() {
		this.isRest = true;
	}
	
	//Initialize a GuitarNote
	private Pitch pitch; //Includes step, octave, and potentially alter
	private int string; //The string the note is on
	private int fret; //The fret of the note
	private musicxml.parsing.Slur slur; //Stores attributes of the slur (if found)
	private musicxml.parsing.PullOff pullOff; //Stores attributes of the slur (if found)
	private Tied tied;
	
	//Inside here (the notes method, actually), add a method for each note value/sub-tag	
	public Note(Pitch pitch, int duration, int voice, String noteType, int string, int fret, musicxml.parsing.Slur slur, musicxml.parsing.PullOff pullOff, Tied tied, double bendAlter, int numDots, Tremolo tremolo) {
		this(duration, voice, noteType, bendAlter, numDots, tremolo);
		//Initialize all given variables
		this.pitch = pitch;
		this.string = string;
		this.fret = fret; //Represents the number outputted on the lines
		this.slur = slur;
		this.pullOff = pullOff;
		this.tied = tied;
	}

//	//Used for grace notes (where the duration is 0)
//	public Note(Pitch pitch, int voice, String noteType, int string, int fret, musicxml.parsing.Slur slur, musicxml.parsing.PullOff pullOff) {
//		//Call the other constructor (above) but set the duration to 0 (since it's not given here)
//		this(pitch, 0, voice, noteType, string, fret, slur, pullOff);
//	}

	//Public getters for guitarNotes
	public Pitch getPitch() {
		return pitch;
	}

	public int getString() {
		return string;
	}

	public int getFret() {
		return fret;
	}

	public musicxml.parsing.Slur getSlur() {
		return slur;
	}

	public musicxml.parsing.PullOff getPullOff() {
		return pullOff;
	}
	
	public Tied getTied() {
		return tied;
	}
	
	//DrumNote	
	private Unpitched unpitched; //Includes display-step and display-octave
	private String instrumentID; //The id of each drum
	private int stem; //0 for none, 1 for down, 2 double, 3 for up
	private String notehead; //Stores many possible values; look at the official website to see what they are
	private boolean noteheadParentheses;
	
//	//Idk if I still need this
//	private musicxml.parsing.Slur slur; //Stores attributes of the slur (if found)
//	private musicxml.parsing.PullOff pullOff; //Stores attributes of the slur (if found)

	//Constructor for the DrumNotes
	//Inside here (the notes method, actually), add a method for each note value/sub-tag	
	public Note(Unpitched unpitched, int duration, String instrumentID, int voice, String noteType, String stem, String notehead, double bendAlter, int numDots, Tremolo tremolo) {
		this(duration, voice, noteType, bendAlter, numDots, tremolo);
		//Initialize all given variables
		this.unpitched = unpitched;
		this.instrumentID = instrumentID;
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
	}

	//Public getters for DrumNotes
	public Unpitched getUnpitched() {
		return unpitched;
	}

	public String getInstrumentID() {
		return instrumentID;
	}

	public int getStem() {
		return stem;
	}

	public String getNotehead() {
		return notehead;
	}	
	
	public boolean getNoteheadParentheses() {
		return noteheadParentheses;
	}
	
	//Setter
	public void setNoteheadParentheses() {
		this.noteheadParentheses = true;
	}
}
