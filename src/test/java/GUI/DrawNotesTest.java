package GUI;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import musicxml.parsing.*;
import GUI.DrawNotes;
import javafx.scene.layout.Pane;

@SuppressWarnings("unused")
public class DrawNotesTest {

	private MainViewController mvc;
	
	public void setMainViewController(MainViewController mvcInput) {
		mvc = mvcInput; // Set main view controller
	}
	
	@Test
	public void testDrawNotes() {
		//To do: Test measure list is correct from input(Parser and measureList are initialized already)
		Parser p = new Parser(mvc.converter.getMusicXML());
		List<Measure> measureList = p.getMeasures();
		
		//Testing if DrawNotes methods are correct
		Note guitarNote = new Note(2, 2, "eighth", 4.0, 5, new Tremolo(null, 0), new TimeModification(-1, -1), new Slide(null, -1), new HammerOn(-1, null, null)); // Initialized guitar note with random values for parameters
		Unpitched pitch = new Unpitched('2', 5); // Initialized random pitch with random parameters for drum
		Note drumNote = new Note(pitch, 2, "A2", 4, "eighth", "down", "B2", 5, 5, new Tremolo(null, 0), new TimeModification(-1, -1), new Slide(null, -1), new HammerOn(-1, null, null)); // Initialized drum note with random values for parameters
		
		Pane pane = new Pane(); // Initialized dummy pane
		DrawNotes drawGuitarNote = new DrawNotes(pane, 40.0, 50.0, guitarNote, "guitar", 15); // Initialized dummy guitar note
		DrawNotes drawDrumSetNote = new DrawNotes(pane, 60.0, 70.0, drumNote, "drumset", 15); // Initialized dummy drum note
		
		//Testing if attributes for the draw notes methods are correct
		assertEquals(40.0, drawGuitarNote.getX());
		assertEquals(50.0, drawGuitarNote.getY());
		assertEquals(guitarNote, drawGuitarNote.getNote());
		assertEquals(pane, drawGuitarNote.getPane());
		assertEquals(pane, drawDrumSetNote.getPane());
		assertEquals(drumNote, drawDrumSetNote.getNote());
		assertEquals(60.0, drawDrumSetNote.getX());
		assertEquals(70.0, drawDrumSetNote.getY());
		
	}
	
}
