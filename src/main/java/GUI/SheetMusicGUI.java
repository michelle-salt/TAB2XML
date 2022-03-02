package GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import models.measure.note.Note;
import musicxml.parsing.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SheetMusicGUI {

	@FXML 
	private Pane pane;
	private MainViewController mvc;

	public void setMainViewController(MainViewController mvcInput) {
		mvc = mvcInput;
	}

	//Implements the "Save as PDF" button on the SheetMusic GUI
	public void handleSavePDF() {
		//Implement - Mohammad.
	}

	//Implements the "Go To Measure" button on the SheetMusic GUI
	public void handleGotoMeasure() {
		//Implement - Duaa
		//Check if it's a valid measure
	}

	//Draw the bar to mark the end of a Measure
	//Must implement double bar and end bars soon?
	private void barLines(double x, double y, String instrument) {
		//Set base length of the bar
		int endY = 60;
		//Change the vertical length depending on the instrument
		if (instrument.equalsIgnoreCase("Bass")) {
			endY = 36;
		}
		else if (instrument.equalsIgnoreCase("Drumset")) {
			endY = 48;
		}
		else if (instrument.equalsIgnoreCase("Guitar")) {
			endY = 60;
		}

		//Draw the bar line
		Line bar = new Line();
		bar.setStartX(x);
		bar.setEndX(x);
		bar.setStartY(y);
		bar.setEndY(y + endY);

		//Add bar to pane
		pane.getChildren().add(bar);
	}

	//Draw the Clef at the left-end of the Staff
	private void clef(String symbol, double x, double y) {
		if (symbol.equalsIgnoreCase("TAB")) {
			//Draw onto the pane letter by letter
			for (int i = 0; i < symbol.length(); i++) {
				//Get the letter
				Text t = new Text(x, y, symbol.substring(i, i+1));
				t.setFont(Font.font("times new roman", FontWeight.BLACK, 24));
				//Add letter to pane
				pane.getChildren().add(t);
				//Increment vertical distance for next letter
				y += 19;
			}
			//Must still be implemented, this is just temporary code to fill the space
		} else if (symbol.equalsIgnoreCase("percussion")) {
			symbol = "II";
			for (int i = 0; i < symbol.length(); i++) {
				//Get the letter
				Text t = new Text(x, y, symbol.substring(i, i+1));
				t.setFont(Font.font("veranda", FontWeight.BOLD, 20));
				//Add letter to pane
				pane.getChildren().add(t);
				//Increment vertical distance for next letter
				x += 8;
			}
		} 
	}

	//Draws Sheet lines and places them on the GUI
	public void placeSheetLines(double y, String instrument) {	
		if (instrument.equalsIgnoreCase("Bass")) {
			//Draws 4 lines
			for (int i = 1; i <= 4; i++, y+= 12) {
				DrawSheetLines sheetLine = new DrawSheetLines(0.0, y, this.pane.getMaxWidth(), y);
				//Add lines to pane
				pane.getChildren().add(sheetLine.getLine());
			}
		}
		else if (instrument.equalsIgnoreCase("Drumset")) {
			//Draws 5 lines
			for (int i = 1; i <= 5; i++, y+= 12) {
				DrawSheetLines sheetLine = new DrawSheetLines(0.0, y, this.pane.getMaxWidth(), y);
				//Add lines to pane
				pane.getChildren().add(sheetLine.getLine());
			}
		}
		else if (instrument.equalsIgnoreCase("Guitar")) {
			//Draws 6 lines
			for (int i = 1; i <= 6; i++, y+= 12) {
				DrawSheetLines sheetLine = new DrawSheetLines(0.0, y, this.pane.getMaxWidth(), y);
				//Add lines to pane
				pane.getChildren().add(sheetLine.getLine());
			}
		}		
	}

	//Update the SheetMusic GUI
	public void update() throws IOException { 	
		Parser p = new Parser(mvc.converter.getMusicXML());
		//Get the list of measure from parser
		List<Measure> measureList = p.getMeasures();
		//Initialize x and y coordinates of where to draw notes
		double x = 50.0; 
		double y;
		//Iterate through each measure
		for (int i = 0; i< measureList.size(); i++, x += 25)
		{
			//Get the current measure
			Measure measure = measureList.get(i);
			//Get the list of notes for each measure
			ArrayList<GuitarNote> noteList = measure.getNotes();
			//Loop through all the notes in the current measure
			for (int j = 0; j < noteList.size(); j++, x += 25)
			{
				//Get the current note
				GuitarNote note = noteList.get(j);
				//Figure out which string the note is on
				int string = note.getString();
				//Set the y coordinate based on the line
				//Currently only works for the first staff
				y = 5+(string-1)*12; //Each staff line is 12 y-pixels apart
				//Draw the note
				new DrawNotes(pane, x, y, note, p.getInstrument());
				//If it's a chord, draw the notes on the same line (x-coordinate)
				if (note.isChord()) {
					x -= 25;
				}
			}
			//Dynamically draw a bar line (after each measure)
			barLines(x, 0, p.getInstrument());
		}
		for (int a = 1, b = 0; a <= Math.ceil(p.getNumMeasures()/2); a++, b += 100) {
			placeSheetLines(b, p.getInstrument());
			//Dynamically draw clef
			clef("TAB", 6, 18+b);
		}
	}
}