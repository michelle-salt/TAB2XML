package GUI;

import musicxml.parsing.Note;

public class NoteLocation {
	private double x, y;
	private Note note;
	private String instrument;
	
	public NoteLocation(double x, double y, Note note, String instrument) {
		this.x = x;
		this.y = y;
		this.note = note;
		this.instrument = instrument;
	}
	
	//Public getters
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Note getNote() {
		return note;
	}

	public String instrument() {
		return instrument;
	}
}
