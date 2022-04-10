package GUI;

import musicxml.parsing.Note;

public class NoteLocation {
	private double x, y, staffY;
	private Note note;
	private String instrument;
	
	public NoteLocation(double x, double y, double staffY, Note note, String instrument) {
		this.x = x;
		this.y = y;
		this.staffY = staffY;
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
	
	public double getStaffY() {
		return staffY;
	}

	public Note getNote() {
		return note;
	}

	public String getInstrument() {
		return instrument;
	}
}
