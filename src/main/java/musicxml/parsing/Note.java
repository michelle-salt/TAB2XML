package musicxml.parsing;

public class Note {

	private char step; //Letter of each note (ranging from 'A' to 'G')
	private int alter; //Represents chromatic alteration in number of semitones (e.g., -1 for flat, 1 for sharp). Decimal values like 0.5 (quarter tone sharp) are used for microtones.
	private int octave; //Octave each note is on
	private int duration; //The length of each note (used for playing)
	private int voice; //Used if there is more than one instrument. Shouldn't make a difference since everything is supposed to be one instrument anyways
	private int type; //maxima (-3), long (-2), breve (-1), whole (1), half (2), quarter (4), eighth (8), etc. until 1024th (1024)
			  		  //0 is used for if the type is incorrect
	private int string; //The string the note is on
	private int fret; //The fret of the note
	
	//Inside here (the notes method, actually), add a method for each note value/sub-tag	
	public Note(char step, int alter, int octave, int duration, int voice, String noteType, int string, int fret) {
		//Initialize all variables
		this.step = step;
		this.alter = alter;
		this.octave = octave;
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
	public char getStep() {
		return step;
	}

	public int getAlter() {
		return alter;
	}
	
	public int getOctave() {
		return octave;
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

	//Public setters to modify each field value
	//Likely won't be used but will be placed here for now, just in case
	public void setStep(char step) {
		this.step = step;
	}

	public void setOctave(int octave) {
		this.octave = octave;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setVoice(int voice) {
		this.voice = voice;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setString(int string) {
		this.string = string;
	}

	public void setFret(int fret) {
		this.fret = fret;
	}

}
