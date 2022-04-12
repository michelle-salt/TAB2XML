package GUI;

import java.util.ArrayList;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import musicxml.parsing.Note;

//Draws all the beams in a measure at once
public class DrawGuitarBeams {
	/*
	 * Logic
	 * Beam if there are enough of the same note
	 * If you have 3 notes (e.g. 8, 16, 16) where first two are different, check if the third note makes the numerator even.
	 * 		THEN CHECK FOR QUARTER
	 * 		If it does, beam them. 
	 * 		If it doesn't, beam the first two and start with the third one being the first note of the next set of beams.
	 * 
	 * ONLY BEAM IF YOU'RE == QUARTER
	 * IF YOU'RE < QUARTER, CONTINUE ADDING AND CHECKING
	 * IF > QUARTER, FLAGS INSTEAD OF BEAMS
	 * 
	 * 
	 * Notes between measures aren't beamed
	 * Grace notes aren't beamed (they just have flags + are smaller)
	 */
	private ArrayList<Note> notes, noteWithoutChords;
	private ArrayList<NoteLocation> noteLocations, notesWithoutChordsLocations;
	private double noteSpacing;
	private Pane pane;
	
	public DrawGuitarBeams(Pane pane, ArrayList<Note> notes, ArrayList<NoteLocation> noteLocations, double noteSpacing) {
		this.pane = pane;
		this.notes = notes;
		this.noteLocations = noteLocations;
		this.noteSpacing = noteSpacing;
		//Calculate which beams to draw
		
		//Get a list of all "notes" (exclude all chords) and their respective note locations
//		Are chorded note guaranteed to have the same type value (e.g. both be 16th notes?)
		this.noteWithoutChords = new ArrayList<Note>();
		this.notesWithoutChordsLocations = new ArrayList<NoteLocation>();
		for (int i = 0; i < this.notes.size(); i++) {
			if (!this.notes.get(i).isChord() && !this.notes.get(i).isGraceNote()) {
				this.noteWithoutChords.add(this.notes.get(i));
				this.notesWithoutChordsLocations.add(this.noteLocations.get(i));
			} 
		}	
		
		
		ArrayList<Note> notesToBeam = new ArrayList<Note>();
		ArrayList<NoteLocation> noteLocationToBeam = new ArrayList<NoteLocation>();
		int numerator = 0;
		int numActual = 0;
		for (int i = 0; i < this.noteWithoutChords.size(); i++) {
			if (this.noteWithoutChords.get(i).getType() == 'H') {
				//Flag and reset ArrayLists
				flagSeparately(notesToBeam, noteLocationToBeam);
				numerator = 0;
				notesToBeam = new ArrayList<Note>();
				noteLocationToBeam = new ArrayList<NoteLocation>();
				//Add note to ArrayList
				notesToBeam.add(this.noteWithoutChords.get(i));
				noteLocationToBeam.add(this.notesWithoutChordsLocations.get(i));
				//Draw half line
				NoteLocation note = noteLocationToBeam.get(0);
				Line l = new Line(note.getX(), note.getStaffY() + 95, note.getX(), note.getStaffY() + 110);
				pane.getChildren().add(l);
				//Reset ArrayLists
				notesToBeam = new ArrayList<Note>();
				noteLocationToBeam = new ArrayList<NoteLocation>();
				
				double dotX = l.getEndX() + 7;
				for (int j = 0; j < this.notesWithoutChordsLocations.get(i).getNote().getNumDots(); j++) {
					Ellipse dot = new Ellipse(dotX, l.getStartY() + 3, 2, 2);
					pane.getChildren().add(dot);
					dotX += 6;
				}				
			} else {
				//If it's an actual actual note
				if (this.noteWithoutChords.get(i).getTimeModification().getActualNotes() != -1) {
					if (numActual == 2) {
						numActual = 0;
					} else {
						numActual ++;
						if (numActual == 1) {
							this.notesWithoutChordsLocations.get(i).setActualNote(this.noteWithoutChords.get(i).getTimeModification().getActualNotes());
						}
					}
				}
				
				if (this.noteWithoutChords.get(i).getTimeModification().getActualNotes() == -1 || numActual != 1) {
						numerator += getNoteTypeValue(this.noteWithoutChords.get(i).getType());
				}
				notesToBeam.add(this.noteWithoutChords.get(i));
				noteLocationToBeam.add(this.notesWithoutChordsLocations.get(i));
				
				if (numerator/1024.0 == 0.25) {
					beamTogether(notesToBeam, noteLocationToBeam);
					numerator = 0;
					notesToBeam = new ArrayList<Note>();
					noteLocationToBeam = new ArrayList<NoteLocation>();
				} else if (numerator/1024.0 > 0.25) {
					//Check that each value (getting rid of first n values) doesn't work
					boolean works = false;
					for (int j = 0; j < notesToBeam.size(); j++) {
						//Remove beginning note
						numerator -= getNoteTypeValue(this.noteWithoutChords.get(j).getType());
						notesToBeam.remove(this.noteWithoutChords.get(j));
						noteLocationToBeam.remove(this.notesWithoutChordsLocations.get(j));
						//Check if new set works
						if (numerator/1024.0 == 0.25) {
							beamTogether(notesToBeam, noteLocationToBeam);
							numerator = 0;
							notesToBeam = new ArrayList<Note>();
							noteLocationToBeam = new ArrayList<NoteLocation>();
							works = true;
							break;
						}
					}
					
					if (!works) {
						flagSeparately(notesToBeam, noteLocationToBeam);
						numerator = 0;
						notesToBeam = new ArrayList<Note>();
						noteLocationToBeam = new ArrayList<NoteLocation>();
					}
				}
			}
			
			
			/*
			 * For loop through every note
			 * 		Have counter for numerator
			 * 		If counter == 1/4, beam + reset counter + reset ArrayList
			 * 		Else if counter < 1/4, add numerator to counter
			 * 			Add note x + y + Note value to arrayList to figure out where to beam
			 * 		Else, draw flags on previous notes + reset counter + reset ArrayList
			 * 
			 * Also have a checker to make sure it doesn't draw beams if it's just one quarter/half/whole note
			 */
		}
		flagSeparately(notesToBeam, noteLocationToBeam);
	}
	
	//Returns the numerator of the value, assuming a denominator of 1024 (since that's the biggest value)
	private int getNoteTypeValue(char type) {
		switch (type) {
		case 'W':	return 1024;
		case 'H':	return 512;
		case 'Q':	return 256;
		case 'I':	return 128;
		case 'S':	return 64;
		case 'T':	return 32;
		case 'X':	return 16;
		case 'O':	return 8;
		case 'U':	return 4;
		case 'R':	return 2;
		case 'C':	return 1;
		}
		return 0;
	}
	
	private void flagSeparately(ArrayList<Note> notes, ArrayList<NoteLocation> noteLocation) {
		for (int i = 0; i < noteLocation.size(); i++) {
			//Add vertical lines underneath notes
			NoteLocation note = noteLocation.get(i);
			double x = note.getX(), y = note.getStaffY()+80;
			Line l = new Line(x, y, x, y + 30);
			pane.getChildren().add(l);
			
			double dotX = l.getEndX() + 7;
			for (int j = 0; j < noteLocation.get(i).getNote().getNumDots(); j++) {
				Ellipse dot = new Ellipse(dotX, l.getStartY() + 3, 2, 2);
				pane.getChildren().add(dot);
				dotX += 6;
			}
			
			//Get number of flags needed
			int numFlags = 0;
			switch (note.getNote().getType()) {
				case 'I':	numFlags = 1;		break;
				case 'S':	numFlags = 2;		break;
				case 'T':	numFlags = 3;		break;
			}
			
			//Draw flag
			y += 38; x -= 9;
			for (int j = 0; j < numFlags; j++) {
				Line flag = new Line(x+9, y-8, x+17, y-22);
				pane.getChildren().add(flag);
				y -= 10;
			}
		}
	}
	
	private void beamTogether(ArrayList<Note> notes, ArrayList<NoteLocation> noteLocation) {
		if (noteLocation.get(0).getInstrument().equalsIgnoreCase("drumset")) {
//			beamTogetherDrums(notes, noteLocation);
		} else {
			beamTogetherGuitar(notes, noteLocation);
		}
	}
	
	//Method will run for quarter notes or lower
	private void beamTogetherGuitar(ArrayList<Note> notes, ArrayList<NoteLocation> noteLocation) {
		
		for (int i = 0; i < noteLocation.size(); i++) {
			//Add vertical lines underneath notes
			NoteLocation note = noteLocation.get(i);
			Line l = new Line(note.getX(), note.getStaffY() + 80, note.getX(), note.getStaffY() + 110);
			pane.getChildren().add(l);
			
			double dotX = l.getEndX() + 7;
			for (int j = 0; j < noteLocation.get(i).getNote().getNumDots(); j++) {
				Ellipse dot = new Ellipse(dotX, l.getStartY() + 3, 2, 2);
				pane.getChildren().add(dot);
				dotX += 6;
			}
			
			//Draw actual notes
			if (noteLocation.get(i).getActualNote() != -1) {
				String actual = Integer.toString(noteLocation.get(i).getActualNote());
				Text t = new Text(note.getX() + 21, l.getEndY() + 15, actual);
				t.setFont(Font.font("arial", FontWeight.BLACK, FontPosture.ITALIC, 13));
				pane.getChildren().add(t);
			}
			
			//Draw beams to connect lines
			
			//32nd is the highest example note we have
			
			//First note
			if (i == 0) {
				//Quarter
				if (note.getNote().getType() == 'Q' ) {
					//Don't beam
					//Don't draw flag
				} 
				//Eighth
				else if (note.getNote().getType() == 'I' ) {
					draw8thBeam(l.getStartX(), note.getStaffY() + 107, noteSpacing/2);
				}
				//Sixteenth
				else if (note.getNote().getType() == 'S' ) {
					draw16thBeam(l.getStartX(), note.getStaffY() + 107, noteSpacing/2);
				}
				//Assumed to be 32nd
				else {
					draw32ndBeam(l.getStartX(), note.getStaffY() + 107, noteSpacing/2);
				}
			} 
			//Last note
			else if (i == noteLocation.size() - 1) {
				//Quarter
				if (note.getNote().getType() == 'Q' ) {
					//Don't beam
					//Don't draw flag
				} 
				//Eighth
				else if (note.getNote().getType() == 'I' ) {
					draw8thBeam(l.getStartX()-(noteSpacing/2)-1, note.getStaffY() + 107, (noteSpacing/2)+1);
				}
				//Sixteenth
				else if (note.getNote().getType() == 'S' ) {
					draw16thBeam(l.getStartX()-(noteSpacing/2)-1, note.getStaffY() + 107, (noteSpacing/2)+1);
				}
				//Assumed to be 32nd
				else {
					draw32ndBeam(l.getStartX()-(noteSpacing/2)-1, note.getStaffY() + 107, (noteSpacing/2)+1);
				}
			} 
			//Other notes
			else {
				//Quarter
				if (note.getNote().getType() == 'Q' ) {
					//Don't beam
					//Don't draw flag
				} 
				//Eighth
				else if (note.getNote().getType() == 'I' ) {
					//Beam forwards AND backwards always
					draw8thBeam(l.getStartX()-(noteSpacing/2)-1, note.getStaffY() + 107, (noteSpacing/2)+1);
					draw8thBeam(l.getStartX(), note.getStaffY() + 107, noteSpacing/2);
				}
				//Sixteenth
				else if (note.getNote().getType() == 'S' ) {
					//Check whether to beam backwards
					if (noteLocation.get(i-1).getNote().getType() == 'S') {
						draw16thBeam(l.getStartX()-(noteSpacing/2)-1, note.getStaffY() + 107, (noteSpacing/2)+1);
					} else {
						draw8thBeam(l.getStartX()-(noteSpacing/2)-1, note.getStaffY() + 107, (noteSpacing/2)+1);
					}
					
					//Beam forwards always
					draw16thBeam(l.getStartX(), note.getStaffY() + 107, noteSpacing/2);
				}
				//Assumed to be 32nd
				else {
					//Check whether to beam backwards
					if (noteLocation.get(i-1).getNote().getType() == 'T') {
						draw32ndBeam(l.getStartX()-(noteSpacing/2)-1, note.getStaffY() + 107, (noteSpacing/2)+1);
					} else if (noteLocation.get(i-1).getNote().getType() == 'S') {
						draw16thBeam(l.getStartX()-(noteSpacing/2)-1, note.getStaffY() + 107, (noteSpacing/2)+1);
					} else {
						draw8thBeam(l.getStartX()-(noteSpacing/2)-1, note.getStaffY() + 107, (noteSpacing/2)+1);
					}
					
					//Beam forwards always
					draw32ndBeam(l.getStartX(), note.getStaffY() + 107, noteSpacing/2);
				}
			}
			
					
			
			
			/*
			 * Given 8, 32
			 * Draw 8th beam half way (noteSpacing/2)
			 * Move to next note
			 * Draw 8th beam half way both ways (forward _and_ back)
			 * Draw 16th + 32nd beams forwards only
			 * 
			 * Draw things backwards instead of forwards for last note in beam only
			 */
			
		}
	}
	

	public void draw8thBeam(double startX, double startY, double length) {
		Rectangle r = new Rectangle(startX, startY, length, 4);
		pane.getChildren().add(r);
	}
	
	public void draw16thBeam(double startX, double startY, double length) {
		draw8thBeam(startX, startY, length);
		Rectangle r = new Rectangle(startX, startY-7, length, 4);
		pane.getChildren().add(r);
	}
	
	public void draw32ndBeam(double startX, double startY, double length) {
		draw16thBeam(startX, startY, length);
		Rectangle r = new Rectangle(startX, startY-14, length, 4);
		pane.getChildren().add(r);
	}
}
