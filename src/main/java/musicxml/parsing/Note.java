package musicxml.parsing;

public class Note {

	private Pitch pitch; //Includes step, octave, and potentially alter
	private int duration; //The length of each note (used for playing)
	private int voice; //Used if there is more than one instrument. Shouldn't make a difference since everything is supposed to be one instrument anyways
	private char type; //maxima (-3), long (-2), breve (-1), whole (1), half (2), quarter (4), eighth (8), etc. until 1024th (1024)
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
//			case "maxima":	this.type = -3;
//							break;
//			case "long":	this.type = -2;
//							break;
//			case "breve":	this.type = -1;
//							break;
			case "whole":	this.type = 'W';
							break;
			case "half":	this.type = 'H';
							break;
			case "quarter":	this.type = 'Q';
							break;
			case "eighth":	this.type = 'I';
							break;
			case "16th":	this.type = 'S';
							break;
			case "32nd":	this.type = 'T';
							break;
			case "64th":	this.type = 'X';
							break;
			case "128th":	this.type = 'O';
							break;
//			case "256th":	this.type = 256;
//							break;
//			case "512th":	this.type = 512;
//							break;
//			case "1024th":	this.type = 1024;
//							break;
			default:		this.type = ' ';
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

	public char getType() {
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

