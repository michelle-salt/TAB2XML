package musicxml.parsing;

//import models.measure.note.notations.Slur;
//import models.measure.note.notations.technical.PullOff;

public class Note {

	private Pitch pitch; //Includes step, octave, and potentially alter
	private int duration; //The length of each note (used for playing)
	private int voice; //Used if there is more than one instrument. Shouldn't make a difference since everything is supposed to be one instrument anyways
	private char type; //maxima (M), long (L), breve (B), whole (W), half (H), quarter (Q), eighth (I), etc. (S, T, X, O, U, R, C)
			  		  //Z is used for if the type is incorrect
	private int string; //The string the note is on
	private int fret; //The fret of the note
	private boolean isChord; //If true, this note is a chord with the preceding note(s)
	private musicxml.parsing.Slur slur; //Stores attributes of the slur (if found)
	private musicxml.parsing.PullOff pullOff; //Stores attributes of the slur (if found)
	
	//Inside here (the notes method, actually), add a method for each note value/sub-tag	
	public Note(Pitch pitch, int duration, int voice, String noteType, int string, int fret, musicxml.parsing.Slur slur, musicxml.parsing.PullOff pullOff) {
		//isChord defaults to false
		isChord = false;
		//Initialize all given variables
		this.pitch = pitch;
		this.duration = duration;
		this.voice = voice;
		this.string = string;
		this.fret = fret; //Represents the number outputted on the lines
		this.slur = slur;
		this.pullOff = pullOff;
		
		//Initialize the value of the type based on the input string, defaulting to 'Z' if it doesn't work
		switch (noteType.toLowerCase()) {

			case "maxima":	this.type = 'M';
							break;
			case "long":	this.type = 'L';
							break;
			case "breve":	this.type = 'B';
							break;
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
			case "256th":	this.type = 'U';
							break;
			case "512th":	this.type = 'R';
							break;
			case "1024th":	this.type = 'C';
							break;
			default:		this.type = 'Z';

							break;
		}
		
	}
	
	public Note(Pitch pitch, int voice, String noteType, int string, int fret, musicxml.parsing.Slur slur, musicxml.parsing.PullOff pullOff) { // for grace notes
		
		//isChord defaults to false
				isChord = false;
				//Initialize all given variables
				this.pitch = pitch;
				this.voice = voice;
				this.string = string;
				this.fret = fret; //Represents the number outputted on the lines
				this.slur = slur;
				this.pullOff = pullOff;
				this.duration = 0;
				
				//Initialize the value of the type based on the input string, defaulting to 'Z' if it doesn't work
				switch (noteType.toLowerCase()) {

					case "maxima":	this.type = 'M';
									break;
					case "long":	this.type = 'L';
									break;
					case "breve":	this.type = 'B';
									break;
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
					case "256th":	this.type = 'U';
									break;
					case "512th":	this.type = 'R';
									break;
					case "1024th":	this.type = 'C';
									break;
					default:		this.type = 'Z';

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
	
	public musicxml.parsing.Slur getSlur() {
		return slur;
	}

	public musicxml.parsing.PullOff getPullOff() {
		return pullOff;
	}

	public boolean isChord() {
		return isChord;
	}
	
	//Setter to indicate that the note is a chord
	public void setChord() {
		this.isChord = true;
	}
}

