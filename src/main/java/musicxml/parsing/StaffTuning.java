package musicxml.parsing;

public class StaffTuning {
	private int lineNumber;
	private char tuningStep;
	private int tuningOctave;
	//Optional element in MusicXML file
	private int tuningAlter; //Could be negative as well
	
	//Constructor without the tuning alter. Initializes all other attributes
	public StaffTuning(int lineNumber, char step, int octave) {
		this.lineNumber = lineNumber;
		this.tuningStep = step;
		this.tuningOctave = octave;
	}
	
	//Constructor with the tuning alter. Initializes ALL attributes
	public StaffTuning(int lineNumber, char step, int octave, int alter) {
		this.lineNumber = lineNumber;
		this.tuningStep = step;
		this.tuningOctave = octave;
		this.tuningAlter = alter;
	}

	//Public getters used to retrieve all attributes as needed
	public int getLineNumber() {
		return lineNumber;
	}

	public char getTuningStep() {
		return tuningStep;
	}

	public int getTuningOctave() {
		return tuningOctave;
	}

	public int getTuningAlter() {
		return tuningAlter;
	}
	
	
}
