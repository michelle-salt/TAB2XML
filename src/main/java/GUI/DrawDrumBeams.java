package GUI;

 import java.util.ArrayList;

 import javafx.scene.layout.Pane;
 import javafx.scene.shape.Line;
 import javafx.scene.shape.Rectangle;
 import musicxml.parsing.Note;

 //Draws all the beams in a measure at once
 public class DrawDrumBeams {
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
 	private int noteSpacing;
 	private Pane pane;

 	public DrawDrumBeams(Pane pane, ArrayList<Note> notes, ArrayList<NoteLocation> noteLocations, int noteSpacing) {
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
 			if (!this.notes.get(i).isChord()) {
 				this.noteWithoutChords.add(this.notes.get(i));
 				this.notesWithoutChordsLocations.add(this.noteLocations.get(i));
 			}
 		}	


 		ArrayList<Note> notesToBeam = new ArrayList<Note>();
 		ArrayList<NoteLocation> noteLocationToBeam = new ArrayList<NoteLocation>();
 		int numerator = 0, firstIndex = 0;
 		for (int i = 0; i < this.noteWithoutChords.size(); i++) {
 			if (!this.noteWithoutChords.get(i).isGraceNote()) {
 				numerator += getNoteTypeValue(this.noteWithoutChords.get(i).getType());
 				notesToBeam.add(this.noteWithoutChords.get(i));
 				noteLocationToBeam.add(this.notesWithoutChordsLocations.get(i));

 				if (numerator/1024.0 == 0.25) {
 					beamTogether(notesToBeam, noteLocationToBeam);
 					firstIndex =  i+ 1;
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
 							firstIndex =  i+ 1;
 							numerator = 0;
 							notesToBeam = new ArrayList<Note>();
 							noteLocationToBeam = new ArrayList<NoteLocation>();
 							works = true;
 							break;
 						}
 					}

 					if (!works) {
 						ArrayList<Note> notesToBeamNew = new ArrayList<Note>();
 						ArrayList<NoteLocation> noteLocationToBeamNew = new ArrayList<NoteLocation>();
 						if (notesToBeam.size() != 0) {
 							notesToBeamNew.add(notesToBeam.get(0));
 							noteLocationToBeamNew.add(noteLocationToBeam.get(0));
 						}
 						flagSeparately(notesToBeamNew, noteLocationToBeamNew);
 						numerator = 0;
 						notesToBeam = new ArrayList<Note>();
 						noteLocationToBeam = new ArrayList<NoteLocation>();
 						i = firstIndex;
 					}
 				}
 			} else {
 				notesToBeam.add(this.noteWithoutChords.get(i));
 				noteLocationToBeam.add(this.notesWithoutChordsLocations.get(i));
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
 			double x = note.getX()+9, y = note.getStaffY()-20;
 			//Get number of flags needed
 			int numFlags = 0;
 			switch (note.getNote().getType()) {
 			case 'I':	numFlags = 1;		break;
 			case 'S':	numFlags = 2;		break;
 			case 'T':	numFlags = 3;		break;
 			}

 			//Draw flag
 			for (int j = 0; j < numFlags; j++) {
 				if (!note.getNote().isGraceNote()) {
 					Line flag = new Line(x+7, y-8, x, y-20);
 					pane.getChildren().add(flag);
 					y += 5;
 				}
 			}
 		}
 	}

 	private void beamTogether(ArrayList<Note> notes, ArrayList<NoteLocation> noteLocation) {
 		beamTogetherDrums(notes, noteLocation);
 	}

 	//Method will run for quarter notes or lower
 	private void beamTogetherDrums(ArrayList<Note> notes, ArrayList<NoteLocation> noteLocation) {

 		for (int i = 0; i < noteLocation.size(); i++) {
 			//Add vertical lines underneath notes
 			NoteLocation note = noteLocation.get(i);

 			//Draw beams to connect lines

 			//32nd is the highest example note we have

 			//First note
 			if (i == 0) {
 				if (!note.getNote().isGraceNote())
 					beamFirstConnection(note, note.getX(), note.getStaffY() - 40);
 			} 
 			//Last note
 			else if (i == noteLocation.size() - 1) {
 				if (!note.getNote().isGraceNote())
 					beamLastConnection(note, note.getX()-(noteSpacing/2)-1, note.getStaffY() - 40);
 			} 
 			//Other notes
 			else {
 				if (note.getNote().isGraceNote()) {
 					beamFirstConnection(noteLocation.get(i+1), note.getX(), note.getStaffY() - 40);
 					beamLastConnection(noteLocation.get(i-1), note.getX()-(noteSpacing/2)-1, note.getStaffY() - 40);
 				} else {
 					drawBeamConnection(noteLocation.get(i), noteLocation, i);
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

 	public void beamFirstConnection(NoteLocation note, double x, double y) {
 		//Quarter
 		if (note.getNote().getType() == 'Q' ) {
 			//Don't beam
 			//Don't draw flag
 		} 
 		//Eighth
 		else if (note.getNote().getType() == 'I' ) {
 			draw8thBeam(x, y, noteSpacing/2);
 		}
 		//Sixteenth
 		else if (note.getNote().getType() == 'S' ) {
 			draw16thBeam(x, y, noteSpacing/2);
 		}
 		//Assumed to be 32nd
 		else {
 			draw32ndBeam(x, y, noteSpacing/2);
 		}
 	}

 	public void beamLastConnection(NoteLocation note, double x, double y) {
 		//Quarter
 		if (note.getNote().getType() == 'Q' ) {
 			//Don't beam
 			//Don't draw flag
 		} 
 		//Eighth
 		else if (note.getNote().getType() == 'I' ) {
 			draw8thBeam(x, y, (noteSpacing/2)+1);
 		}
 		//Sixteenth
 		else if (note.getNote().getType() == 'S' ) {
 			draw16thBeam(x, y, (noteSpacing/2)+1);
 		}
 		//Assumed to be 32nd
 		else {
 			draw32ndBeam(x, y, (noteSpacing/2)+1);
 		}
 	}

 	public void drawBeamConnection(NoteLocation note, ArrayList<NoteLocation> noteLocation, int i) {
 		NoteLocation previousNote = null;
 		boolean beamBackwards = true;
 		for (int j = 1; previousNote == null; j++) {
 			if (i-j < 0) {
 				beamBackwards = false;
 				break;
 			}
 			if (!noteLocation.get(i-j).getNote().isGraceNote())
 				previousNote = noteLocation.get(i-j);
 		}
 		//Quarter
 		if (note.getNote().getType() == 'Q' ) {
 			//Don't beam
 			//Don't draw flag
 		} 
 		//Eighth
 		else if (note.getNote().getType() == 'I' ) {
 			//Beam forwards AND backwards always
 			if (beamBackwards)
 				draw8thBeam(note.getX()-(noteSpacing/2)-1, note.getStaffY() - 40, (noteSpacing/2)+1);
 			draw8thBeam(note.getX(), note.getStaffY() - 40, noteSpacing/2);
 		}
 		//Sixteenth
 		else if (note.getNote().getType() == 'S' ) {
 			//Check whether to beam backwards
 			if (beamBackwards) {
 				if (previousNote.getNote().getType() == 'S') {
 					draw16thBeam(note.getX()-(noteSpacing/2)-1, note.getStaffY() - 40, (noteSpacing/2)+1);
 				} else {
 					draw8thBeam(note.getX()-(noteSpacing/2)-1, note.getStaffY() - 40, (noteSpacing/2)+1);
 				}
 			}
 			//Beam forwards always
 			draw16thBeam(note.getX(), note.getStaffY() - 40, noteSpacing/2);
 		}
 		//Check for grace notes
 		else if (previousNote.getNote().isGraceNote()) {
 			//Beam forwards and backwards to cover the gap
 			if (beamBackwards)
 				draw8thBeam(note.getX()-(noteSpacing/2)-1, note.getStaffY() - 40, (noteSpacing/2)+1);
 			draw8thBeam(note.getX(), note.getStaffY() - 40, noteSpacing/2);
 		}
 		//Assumed to be 32nd
 		else {
 			if (beamBackwards) {
 				//Check whether to beam backwards
 				if (previousNote.getNote().getType() == 'T') {
 					draw32ndBeam(note.getX()-(noteSpacing/2)-1, note.getStaffY() - 40, (noteSpacing/2)+1);
 				} else if (previousNote.getNote().getType() == 'S') {
 					draw16thBeam(note.getX()-(noteSpacing/2)-1, note.getStaffY() - 40, (noteSpacing/2)+1);
 				} else {
 					draw8thBeam(note.getX()-(noteSpacing/2)-1, note.getStaffY() - 40, (noteSpacing/2)+1);
 				}
 			}
 			//Beam forwards always
 			draw32ndBeam(note.getX(), note.getStaffY() - 40, noteSpacing/2);
 		}
 	}


 	public void draw8thBeam(double startX, double startY, int length) {
 		Rectangle r = new Rectangle(startX+9, startY-0.5, length, 4);
 		pane.getChildren().add(r);
 	}

 	public void draw16thBeam(double startX, double startY, int length) {
 		draw8thBeam(startX, startY, length);
 		Rectangle r = new Rectangle(startX+9, startY+6.5, length, 4);
 		pane.getChildren().add(r);
 	}

 	public void draw32ndBeam(double startX, double startY, int length) {
 		draw16thBeam(startX, startY, length);
 		Rectangle r = new Rectangle(startX+9, startY+13.5, length, 4);
 		pane.getChildren().add(r);
 	}
 }