package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import musicxml.parsing.*;
import utility.Settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.fxmisc.richtext.CodeArea;

public class PreviewSheetController{

	@FXML private Pane pane;
	private MainViewController mvc;
	public Window convertWindow;

	public File saveFile;
	private static boolean isEditingSavedFile;

	@FXML public CodeArea mainText;

	@FXML Button savePDFButton;
	@FXML Button playButton;
	@FXML Button goToMeasureButton;
	
	public PreviewSheetController() {}

	public void setMainViewController(MainViewController mvcInput) {
		mvc = mvcInput;
	}

	@FXML
	private void handleCurrentSongSettings() {
		mvc.handleCurrentSongSettings();
	}

	@FXML
	private void handleSystemDefaultSettings() {
		mvc.handleSystemDefaultSettings();
	}

	@FXML
	boolean handleSaveAs() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As PDF");
		fileChooser.setInitialDirectory(new File(Settings.getInstance().outputFolder));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
		if (saveFile != null) {
			fileChooser.setInitialFileName(saveFile.getName());
			fileChooser.setInitialDirectory(new File(saveFile.getParent()));
		}

		File newSaveFile = fileChooser.showSaveDialog(MainApp.STAGE);
		if (newSaveFile == null)
			return false;
		try {
			FileWriter myWriter = new FileWriter(newSaveFile.getPath());
			myWriter.write(mainText.getText());
			myWriter.close();

			saveFile = newSaveFile;
			isEditingSavedFile = true;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	//TODO read the file.
	@FXML
	boolean handleSave() {
		if (!isEditingSavedFile || saveFile == null || !saveFile.exists())
			return this.handleSaveAs();
		try {
			FileWriter myWriter = new FileWriter(saveFile.getPath());
			myWriter.write(mainText.getText());
			myWriter.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@FXML
	private void handleCloseWindow() {
		Alert alert = 
				new Alert(
						Alert.AlertType.CONFIRMATION,
						"Choose your option",
						ButtonType.CANCEL, ButtonType.NO, ButtonType.YES);
		
		alert.setTitle("Exit Preview Window");
		alert.setHeaderText("This document is unsaved and will be overwritten. Do you want to save it first?");

		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == ButtonType.CANCEL) {
			/* nothing */
		}
		if (option.get() == ButtonType.NO) {
			mvc.convertWindow.hide();
		}
		if (option.get() == ButtonType.YES) {
			/*
			 * Implement the saving functionality.
			 */
		}
	}

	@FXML
	void savePDFButtonHandle() {
		mvc.savePDFButtonHandle();
	}

	//Implements the "Go To Measure" button on the SheetMusic GUI
	public void handleGotoMeasure() {
		//Implement - Duaa
		//Check if it's a valid measure
	}
	
	public void handlePlayMusic() {
		try {
			mvc.playMusic();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handlePauseMusic() {
		//Implement
	}
	
	public void handleStopMusic() {
		//Implement
	}

	public void handleTempo() {
		//Implement
	}
	
	public void handlePlay() {
		try {
			mvc.playMusic();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	private void clef(String symbol, double x, double y, String instrument) {
		if (symbol.equalsIgnoreCase("TAB")) {
			if (instrument.equalsIgnoreCase("guitar")) {
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
			} 
			//Assumed to be bass
			else {
				y -= 7;
				//Draw onto the pane letter by letter
				for (int i = 0; i < symbol.length(); i++) {
					//Get the letter
					Text t = new Text(x, y, symbol.substring(i, i+1));
					t.setFont(Font.font("times new roman", FontWeight.BLACK, 17));
					//Add letter to pane
					pane.getChildren().add(t);
					//Increment vertical distance for next letter
					y += 12.5;
				}
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

	public void timeSignature(int beats, int beatType, double x, double y, String instrument) {
		if (instrument.equalsIgnoreCase("guitar")) {
			Text beatsText = new Text(x, y, Integer.toString(beats));
			beatsText.setFont(Font.font("cambria", FontWeight.BLACK, 40));
			//Add number to pane
			pane.getChildren().add(beatsText);
			
			//Increment vertical distance for next letter
			y += 31;
			
			Text beatTypeText = new Text(x, y, Integer.toString(beatType));
			beatTypeText.setFont(Font.font("cambria", FontWeight.BLACK, 40));
			//Add number to pane
			pane.getChildren().add(beatTypeText);
		} else if (instrument.equalsIgnoreCase("drumset")) {
			y -= 6;
			
			Text beatsText = new Text(x, y, Integer.toString(beats));
			beatsText.setFont(Font.font("cambria", FontWeight.BLACK, 32));
			//Add number to pane
			pane.getChildren().add(beatsText);
			
			//Increment vertical distance for next letter
			y += 26;
			
			Text beatTypeText = new Text(x, y, Integer.toString(beatType));
			beatTypeText.setFont(Font.font("cambria", FontWeight.BLACK, 32));
			//Add number to pane
			pane.getChildren().add(beatTypeText);
		} 
		//Assumed to be bass
		else {
			y -= 11;
			
			Text beatsText = new Text(x, y, Integer.toString(beats));
			beatsText.setFont(Font.font("cambria", FontWeight.BLACK, 23));
			//Add number to pane
			pane.getChildren().add(beatsText);
			
			//Increment vertical distance for next letter
			y += 19;
			
			Text beatTypeText = new Text(x, y, Integer.toString(beatType));
			beatTypeText.setFont(Font.font("cambria", FontWeight.BLACK, 23));
			//Add number to pane
			pane.getChildren().add(beatTypeText);
		}
	}
	
	public void drawRepeat(double x, double y, char direction, String words, String instrument) {
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
		//Thinn line
		barLines(x, y, instrument);
		double thiccX, dotX;
		if (direction == 'l') {
			thiccX = x - 5;
			dotX = x + 7;
		}
		//Assumed to be right
		else {
			thiccX = x + 5;
			dotX = x - 7;
			
			//Text
			Text t = new Text(x - 25, y - 10, words);
			t.setFont(Font.font("arial", 15));
			//Add letter to pane
			pane.getChildren().add(t);
		}
		//Thicc line
		Line barLine = new Line(thiccX, y+1, thiccX, y + endY - 1);
		barLine.setStrokeWidth(4);
		pane.getChildren().add(barLine);
		//Circles
		Ellipse dot1 = new Ellipse(dotX, (y+endY)/2 - 7, 3, 3);
		Ellipse dot2 = new Ellipse(dotX, (y+endY)/2 + 7, 3, 3);
				
		pane.getChildren().add(dot1);
		pane.getChildren().add(dot2);
	}
	
	//Update the SheetMusic GUI
	public void update() throws IOException { 	
		Parser p = new Parser(mvc.converter.getMusicXML());
		//Get the list of measure from parser
		List<Measure> measureList = p.getMeasures();
		//Initialize x and y coordinates of where to draw notes
		double x = 100.0, xVerify = 100, y = 0, yStaff = 0;			
		//Iterate through each measure
		for (int i = 0; i< measureList.size(); i++, x += 25)
		{
			//Get the current measure
			Measure measure = measureList.get(i);
			//Get the list of notes for each measure
			ArrayList<Note> noteList = measure.getNotes();
			xVerify = x;
			for (int j = 0; j < noteList.size(); j++, xVerify += 25) {
				//Get the current note
				Note note = noteList.get(j);
				//If it's a chord, draw the notes on the same line (x-coordinate)
				if (note.isChord()) {
					xVerify -= 25;
				}
			}
			if (xVerify > this.pane.getMaxWidth()) {
				x = 100.0;
				yStaff += 100;
				placeSheetLines(yStaff, p.getInstrument());
				clef(p.getMeasures().get(0).getAttributes().getClef().getSign(), 6, 18+yStaff, p.getInstrument());
				timeSignature(p.getMeasures().get(0).getAttributes().getTime().getBeats(), p.getMeasures().get(0).getAttributes().getTime().getBeatType(), 35, 28+yStaff, p.getInstrument());
			}
			
			System.out.println(p.getMeasures().get(i).getBarline().getLocation());
			if (p.getMeasures().get(i).getBarline().getLocation() ==  'l')
				drawRepeat(x-25, 0 + yStaff, 'l', "x7", p.getInstrument());
			
			//Loop through all the notes in the current measure
			for (int j = 0; j < noteList.size(); j++, x += 25)
			{
				//Get the current note
				Note note = noteList.get(j);
				//If it's a chord, draw the notes on the same line (x-coordinate)
				if (note.isChord()) {
					x -= 25;
				}
				//Get the y value for each drum note
				if (p.getInstrument().equals("drumset")) {
					//The spacing of each note is based on the octave and step
					//Each value represents one "space" above, with a space either being the line or the spaces between
					//0 is the bottom line (going through the line), 1 is the space between the bottom two lines, etc.
					if (note.getUnpitched().getOctave() == 5) {
						switch (note.getUnpitched().getStep()) {
						case 'A':
							y = 1;
							//This is the only note which will have a strikethrough in it
							Line strikethrough = new Line(x-2, -12+(y-1)*6+yStaff, x+12, -12+(y-1)*6+yStaff);
							pane.getChildren().add(strikethrough); break;
						case 'G':	y = 2;	break;
						case 'F':	y = 3;	break;
						case 'E':	y = 4;	break;
						case 'D':	y = 5;	break;
						case 'C':	y = 6;	break;
						}
					} else if (note.getUnpitched().getOctave() == 4) {
						switch (note.getUnpitched().getStep()) {
						case 'B':	y = 7;	break;
						case 'A':	y = 8;	break;
						case 'G':	y = 9;	break;
						case 'F':	y = 10;	break;
						case 'E':	y = 11;	break;
						}
					}
					//Value retrieved through guess and check :))))
					y = -7+(y-1)*6; 
				} 
				//Get the y value for each guitar or bass note
				else {
					//Figure out which string the note is on
					int string = note.getString();
					//Set the y coordinate based on the line
					y = 5+(string-1)*12; //Each staff line is 12 y-pixels apart
				}
				//Draw the note
				if (x < this.pane.getMaxWidth()) {
					new DrawNotes(pane, x, y + yStaff, note, p.getInstrument());
					placeSheetLines(0, p.getInstrument());
					clef(p.getMeasures().get(0).getAttributes().getClef().getSign(), 6, 18+yStaff, p.getInstrument());
					timeSignature(p.getMeasures().get(0).getAttributes().getTime().getBeats(), p.getMeasures().get(0).getAttributes().getTime().getBeatType(), 35, 28+yStaff, p.getInstrument());
				}
				else {
					x = 100.0;
					yStaff += 100;
					new DrawNotes(pane, x, y + yStaff, note, p.getInstrument());
					placeSheetLines(yStaff, p.getInstrument());
					clef(p.getMeasures().get(0).getAttributes().getClef().getSign(), 6, 18+yStaff, p.getInstrument());
					timeSignature(p.getMeasures().get(0).getAttributes().getTime().getBeats(), p.getMeasures().get(0).getAttributes().getTime().getBeatType(), 35, 28+yStaff, p.getInstrument());
				}
			}
			//Dynamically draw a bar line (after each measure)
			if (p.getMeasures().get(i).getBarline().getLocation() ==  'r') {
				x += 25;
				drawRepeat(x, 0 + yStaff, 'r', "x7", p.getInstrument());
			} else {
				barLines(x, 0 + yStaff, p.getInstrument());
			}
		}
		
	}
}