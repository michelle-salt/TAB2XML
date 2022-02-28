package musicxml.parsing;

public class Unpitched {
	private char displayStep; //Letter of each note (ranging from 'A' to 'G')
	private int displayOctave; //Octave each note is on

	public Unpitched(char step, int octave) {
		this.displayStep = step;
		this.displayOctave = octave;
	}

	//Public accessors
	public char getStep() {
		return displayStep;
	}

	public int getOctave() {
		return displayOctave;
	}
}
