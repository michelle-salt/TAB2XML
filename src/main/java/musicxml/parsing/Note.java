package musicxml.parsing;

public class Note {

	private Pitch pitch; //Includes step, octave, and potentially alter
	private int duration; //The length of each note (used for playing)
	private int voice; //Used if there is more than one instrument. Shouldn't make a difference since everything is supposed to be one instrument anyways
	private int type; //maxima (-3), long (-2), breve (-1), whole (1), half (2), quarter (4), eighth (8), etc. until 1024th (1024)
			  		  //0 is used for if the type is incorrect
	private int string; //The string the note is on
	private int fret; //The fret of the note
	private boolean isChord; //If true, this note is a chord with the preceding note(s)
	
	//Inside here (the notes method, actually), add a method for each note value/sub-tag	
	public Note(Pitch pitch, int duration, int voice, String noteType, int string, int fret) {
		//isChord defaults to false
		isChord = false;
		//Initialize all given variables
		this.pitch = pitch;
		this.duration = duration;
		this.voice = voice;
		this.string = string;
		this.fret = fret; //Represents the number outputted on the lines
		
		//Initialize the value of the type based on the input string, defaulting to 0 if it doesn't work
		switch (noteType.toLowerCase()) {
			case "maxima":	this.type = -3;
							break;
			case "long":	this.type = -2;
							break;
			case "breve":	this.type = -1;
							break;
			case "whole":	this.type = 1;
							break;
			case "half":	this.type = 2;
							break;
			case "quarter":	this.type = 4;
							break;
			case "eigth":	this.type = 8;
							break;
			case "16th":	this.type = 16;
							break;
			case "32nd":	this.type = 32;
							break;
			case "64th":	this.type = 64;
							break;
			case "128th":	this.type = 128;
							break;
			case "256th":	this.type = 256;
							break;
			case "512th":	this.type = 512;
							break;
			case "1024th":	this.type = 1024;
							break;
			default:		this.type = 0;
							break;
		}
		
	}
	
	//Public getters to retrieve each field
	public Pitch getPitch() {
		return pitch;
	}

	public int getDuration() {
		return duration;
	}

	public int getVoice() {
		return voice;
	}

	public int getType() {
		return type;
	}

	public int getString() {
		return string;
	}

	public int getFret() {
		return fret;
	}
	
	public boolean isChord() {
		return isChord;
	}
	
	//Setter to indicate that the note is a chord
	public void setChord() {
		this.isChord = true;
	}
}

