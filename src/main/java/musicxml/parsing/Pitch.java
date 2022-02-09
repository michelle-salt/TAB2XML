package musicxml.parsing;

public class Pitch {
	private char step; //Letter of each note (ranging from 'A' to 'G')
	//Optional
	private int alter; //Represents chromatic alteration in number of semitones (e.g., -1 for flat, 1 for sharp). Decimal values like 0.5 (quarter tone sharp) are used for microtones.
	private int octave; //Octave each note is on

	public Pitch(char step, int octave) {
		this.step = step;
		this.octave = octave;
	}
	
	public Pitch(char step, int octave, int alter) {
		this.step = step;
		this.octave = octave;
		this.alter = alter;
	}

	//Public accessors
	public char getStep() {
		return step;
	}

	public int getAlter() {
		return alter;
	}

	public int getOctave() {
		return octave;
	}
}
