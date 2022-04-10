package GUI;

import java.util.ArrayList;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import musicxml.parsing.Note;

//Draws all the beams in a measure at once
public class DrawBeams {
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
	
	private Pane pane;
	
	public DrawBeams(Pane pane, ArrayList<Note> notes, ArrayList<NoteLocation> noteLocations) {
		this.pane = pane;
		this.notes = notes;
		this.noteLocations = noteLocations;
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
		int numerator = 0, maxDenom = 0;
		for (int i = 0; i < this.noteWithoutChords.size(); i++) {
			//If the value is even, check if they can be beamed already
//			numerator += getNoteTypeValue(this.noteWithoutChords.get(i).getType());
//			int currDenom = 1024/getNoteTypeValue(this.noteWithoutChords.get(i).getType());
//			if (currDenom > maxDenom)
//				maxDenom = currDenom;
//			int reducedNum = numerator/(1024/maxDenom);
			numerator += getNoteTypeValue(this.noteWithoutChords.get(i).getType());
			notesToBeam.add(this.noteWithoutChords.get(i));
			noteLocationToBeam.add(this.notesWithoutChordsLocations.get(i));
			
			if (numerator/1024.0 == 0.25) {
				beamTogether(notesToBeam, noteLocationToBeam);
				numerator = 0;
				notesToBeam = new ArrayList<Note>();
				noteLocationToBeam = new ArrayList<NoteLocation>();
			} else if (numerator/1024.0 > 0.25) {
//				flagSeparately(notesToBeam, noteLocationToBeam);
				numerator = 0;
				notesToBeam = new ArrayList<Note>();
				noteLocationToBeam = new ArrayList<NoteLocation>();
			}
			
//			if (numerator/maxDenom)
			
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
			
			//Add up values
			//Get max denominator
			//Divide numerator by opposite of GCD
	
			
			
//			1/8 + 1/16 + 1/8 = 5/16 = 320/1024
//			(1024/x)=16 --> 1024/16 = 64
//			(320/x)=320/64 = 5
			
			
			//If the value is odd, beam the first two only and this third one will become the first of the next group
		}
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
	
	private void beamTogether(ArrayList<Note> notes, ArrayList<NoteLocation> noteLocation) {
		if (noteLocation.get(0).getInstrument().equalsIgnoreCase("drumset")) {
//			beamTogetherDrums(notes, noteLocation);
		} else {
			beamTogetherGuitar(notes, noteLocation);
		}
	}
	
	private void beamTogetherGuitar(ArrayList<Note> notes, ArrayList<NoteLocation> noteLocation) {
		for (int i = 0; i < noteLocation.size(); i++) {
			NoteLocation note = noteLocation.get(i);
			Line l = new Line(note.getX(), note.getStaffY() + 80, note.getX(), note.getStaffY() + 100);
			pane.getChildren().add(l);
		}
	}
}
