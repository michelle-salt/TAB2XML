package GUI;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
//import models.measure.note.Note;
import musicxml.parsing.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;

public class SheetMusicGUI{

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
	
	public void handlePlayMusic() {
		
		//Plays music corresponding to the inputted tablature
		
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
        } else if (symbol.equalsIgnoreCase("percussion")) {
        	symbol = "II";
        	//Percussion symbol starts lower on the staff than TAB
        	y += 18;
        	for (int i = 0; i < symbol.length(); i++) {
                //Get the letter
                Text t = new Text(x, y, symbol.substring(i, i+1));
                t.setFont(Font.font("veranda", FontWeight.BLACK, 34));
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
			ArrayList<Note> noteList = measure.getNotes();
			//Loop through all the notes in the current measure
			for (int j = 0; j < noteList.size(); j++, x += 25)
			{
				//Get the current note
				Note note = noteList.get(j);
				//If it's a chord, draw the notes on the same line (x-coordinate)
				if (note.isChord()) {
					x -= 25;
				}
				//Figure out which string the note is on
				int string = note.getString();
				//Set the y coordinate based on the line
				//Currently only works for the first staff
				y = 5+(string-1)*12; //Each staff line is 12 y-pixels apart
				//Draw the note
				new DrawNotes(pane, x, y, note, p.getInstrument());
			}
			//Dynamically draw a bar line (after each measure)
			barLines(x, 0, p.getInstrument());
		}
		//Dynamically draw the Sheet lines on the SheetMusic GUI
      	for (int i = 1, y2 = 0; i <= Math.ceil(p.getNumMeasures()/2); i++, y2 += 100) {
      		placeSheetLines(y2, p.getInstrument());
      		//Dynamically draw clef
          	clef(p.getMeasures().get(0).getAttributes().getClef().getSign(), 6, 18+y2);
      	}
	}
	
}
