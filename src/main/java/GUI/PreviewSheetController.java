package GUI;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.Printer.MarginType;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import musicxml.parsing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.*;

import org.fxmisc.richtext.CodeArea;
import org.jfugue.player.ManagedPlayer;
import org.jfugue.player.Player;

import custom_exceptions.*;

public class PreviewSheetController {

	private int noteSpacing;
	private int staffSpacing;

	@FXML private Pane pane;
	@FXML private AnchorPane anchorPane;
	@FXML private Canvas canvas;

	private MainViewController mvc;
	public Window convertWindow;

	@FXML public CodeArea mainText;

	@FXML Button printButton;
	@FXML Button playButton;
	@FXML Button pauseButton;
	@FXML TextField tempoField;
	@FXML TextField goToMeasureField;
	@FXML Button goToMeasureButton;

	BooleanProperty printButtonPressed = new SimpleBooleanProperty(false);

	private Player player;
	private ManagedPlayer mplayer;
	private String currentTempoDetails;

	public PreviewSheetController() { 
		/* Set player */
		player = new Player();
		mplayer = player.getManagedPlayer();
		currentTempoDetails = "null";

		/* Set default noteSpacing to 25 and staffSpacing to 100 */
		noteSpacing = 25;
		staffSpacing = 100;
	}

	public void setMainViewController(MainViewController mvcInput) {
		mvc = mvcInput;
	}

	@FXML
	private <printButtonPressed> void printHandle() {
		//Set up a printer
		Printer p = Printer.getDefaultPrinter();
		if (p == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "You do not have a printer setup. Please set up a printer on this device to continue.");
			alert.setTitle("Print");
			alert.setHeaderText("Printing Error!");
			alert.show();
		} else {
			//Set up Page Dialog
			PrinterJob pj = PrinterJob.createPrinterJob();
			PageLayout pl = p.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, MarginType.DEFAULT);
			//Get the image of the GUI contents
			WritableImage wi = anchorPane.snapshot(null, null);
			//Load the image
			ImageView v = new ImageView(wi);
			//Dimensions of the Printable area
			double w = pl.getPrintableWidth()/wi.getWidth();
			double h = pl.getPrintableHeight()/wi.getHeight();
			Scale s = new Scale(w, w);
			v.getTransforms().add(s);
			Window window = pane.getScene().getWindow();
			//Print
			if (pj != null && pj.showPrintDialog(window)) {
				Translate t = new Translate(0, 0);
				v.getTransforms().add(t);
				for (int i = 0; i < Math.ceil(w/h); i++) {
					t.setY((pl.getPrintableHeight()/w) * (-i));
					pj.printPage(pl, v);
				}
				pj.endJob();
			}
		}
	}

	@FXML
	private void handleGotoMeasure() throws IOException {
		String os = System.getProperty("os.name").toLowerCase();
		String path = new File("").getAbsolutePath();
		if (os.contains("win")) {
			path = path.concat("\\");
		}
		else {
			path = path.concat("/");
		}
		Parser p = new Parser(Files.readString(Paths.get(path.concat("musicXML.txt"))));

		int measureNum = Integer.parseInt(goToMeasureField.getText());
		int measureCount = p.getNumMeasures();
		if (measureNum < 1 || measureNum > measureCount) {
			Alert alert = new Alert(Alert.AlertType.ERROR, 
					"The measure you entered is outside the valid range. Enter a measure betweeen 1 and " + measureCount + ".");
			alert.setTitle("Go-To Measure");
			alert.setHeaderText("Invalid Measure!");
			alert.show();
		}
		else {
			goToMeasure(measureNum);
		}
	}

	private void goToMeasure(int number) {

	}

	@FXML
	private void handleEditInput() {
		mvc.convertWindow.hide();
	}

	@FXML
	private void handlePlayMusic() throws IOException, InvalidInputException, UnrecognizedInstrumentException {
		/*
		 * The visibility of the buttons should toggle after each button press.
		 * 
		 * The play button either starts a new `player`, or resumes from where it was last paused.
		 * The pause button only pauses the `player`. 
		 */
		if (playButton.isVisible()) {
			if (mplayer.isStarted()) {
				mplayer.resume();
			} else {
				player = new Player();
				mplayer = player.getManagedPlayer();
				try { 
					/* This retrieves the list of measures from the musicXML file, 
					 * and information about the inputed tempo and instrument,
					 * outputs it into a String format, then plays the String.
					 */
					currentTempoDetails = this.getTempoDetails(); // record the currrent tempoDetails
					String recording = this.getMeasureDetails(currentTempoDetails, this.getMeasureList());
					play(recording);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			playButton.setVisible(false);
			pauseButton.setVisible(true);
		} else {
			mplayer.pause();
			playButton.setVisible(true);
			pauseButton.setVisible(false);
		}
	}

	@FXML
	private void handleStopMusic() {
		mplayer.reset();
		player = new Player();
		mplayer = player.getManagedPlayer();

		playButton.setVisible(true);
		pauseButton.setVisible(false);
	}

	/*
	 * Check if the user has entered a new `tempo` value.
	 * 
	 * ONLY returns true when the `tempo` value has been changed. 
	 * 
	 */
	@FXML
	private boolean atTempoKeyPressed() throws InvalidInputException, UnrecognizedInstrumentException {
		if (!this.getTempoDetails().equals(currentTempoDetails)) {
			currentTempoDetails = this.getTempoDetails();
			return true;
		}
		else {
			return false;
		}
	}

	private String getMeasureDetails(String tmp, ArrayList<String> list) {
		String result = tmp;
		for (String s: list) {
			result += s;
		}
		return result;
	}

	private String getTempoDetails() throws InvalidInputException, UnrecognizedInstrumentException {
		String result = "";

		/* 
		 * default 
		 */
		if (tempoField.getText().isEmpty()) {
			if (getInstrument().equals("guitar") || getInstrument().equals("bass")) {
				result += "T100 V0 I[" + this.getParser().getInstrument() + "] ";
			}
			else if (this.getInstrument().equals("drumset")) {
				result += "T100 V9 ";
			}
			else {
				throw new UnrecognizedInstrumentException("Error: Instrument not supported");
			}
		} 
		else {
			if (tempoField.getText().charAt(0) == '-') {
				throw new InvalidInputException("Error: negative number not allowed!");
			}
			if (Integer.parseInt(tempoField.getText()) > 0) {
				if (getInstrument().equals("guitar") || getInstrument().equals("bass")) {
					result += "T" + tempoField.getText() + " V0 I[" + this.getParser().getInstrument() + "] ";
				}
				else if (this.getInstrument().equals("drumset")) {
					result += "T" + tempoField.getText() + " V9 ";
				}
				else {
					throw new UnrecognizedInstrumentException("Error: Instrument not supported");
				}
			}
			else { /* <= 0 */
				throw new InvalidInputException("Error: Invalid BPM, Do not proceed to play!");
			}
		}
		return result;
	}

	private void play(String record) {
		System.out.println(record);
		new Thread(() -> {
			player.play(record);
		}).start();
	}

	private Parser getParser() {
		return new Parser(mvc.getMusicXML());
	}

	private String getInstrument() {
		return getParser().getInstrument();
	}

	private ArrayList<String> GuitarBass() throws IOException {
		ArrayList<String> measureList = new ArrayList<>(); // split measures into array

		int numOfMeasures = this.getParser().getNumMeasures();
		ArrayList<Measure> measures = this.getParser().getMeasures();

		for(int i = 0; i < numOfMeasures; i++) {
			String measure = "";

			int numOfNotes = measures.get(i).getNumNotes();
			ArrayList<Note> notes = measures.get(i).getNotes();

			for (int j = 0; j < numOfNotes; j++) {
				String altervalue = "";

				Pitch pitch = notes.get(j).getPitch();
				char type = notes.get(j).getType();

				char step = pitch.getStep();
				int octave = pitch.getOctave();

				// Check alter.
				if (pitch.getAlter() == 1) {
					altervalue = "#";
				}
				else if (pitch.getAlter() == -1) {
					altervalue = "b";
				}
				else {
					altervalue = "";
				}

				// Check if note is a grace note.
				if (notes.get(j).getDuration() == 0) {
					measure += step + altervalue + octave + "o- ";
					measure += step + altervalue + octave + "-o";
				} 
				else {
					measure += step + altervalue + octave + type;
				}

				// 
				if (numOfNotes-j != 1) {
					if (notes.get(j + 1).isChord()) { // if next note is also part of the chord
						measure += "+";
					} 
					else {
						measure += " "; // add a space to split up notes
					}
				} 
				else { // add the tie thing around here i think

				}
			} // inner loop ends

			if (numOfMeasures-i != 1) {
				measure += " | "; // add space between notes to indicate measures
			}
			measureList.add(measure);
		} // outer loop ends
		return measureList;
	}

	private ArrayList<String> Drum() throws IOException {
		ArrayList<String> measureList = new ArrayList<>(); // split measures into array

		int numOfMeasures = this.getParser().getNumMeasures();
		ArrayList<Measure> measures = this.getParser().getMeasures();

		for (int i = 0; i < numOfMeasures; i++) { // go through every measure
			String measure = "";

			int numOfNotes = measures.get(i).getNumNotes();
			ArrayList<Note> notes = measures.get(i).getNotes();

			for (int j = 0; j < measures.get(i).getNumNotes(); j++) { // go through all notes in specific measure
				String instrumentID = notes.get(j).getInstrumentID();
				char type = notes.get(j).getType();

				/*
				   P1-I46 = Low Tom
		      	   P1-I43 = Closed Hi-Hat
		      	   P1-I42 = Low Floor Tom
		      	   P1-I48 = Low-Mid Tom
		      	   P1-I45 = Pedal Hi-Hat
		      	   P1-I47 = Open Hi-Hat
		      	   P1-I50 = Crash Cymbal 1
		      	   P1-I44 = High Floor Tom
		      	   P1-I39 = Snare
		      	   P1-I54 = Ride Bell
		      	   P1-I53 = Chinese Cymbal 1
		      	   P1-I36 = Bass Drum 1
		      	   P1-I52 = Ride Cymbal 1
				 */

				switch(instrumentID) {
				case "P1-I46":	instrumentID = "[LO_TOM]"; break;
				case "P1-I43":	instrumentID = "[CLOSED_HI_HAT]"; break;
				case "P1-I42":	instrumentID = "[LO_FLOOR_TOM"; break;
				case "P1-I48":	instrumentID = "[LO_MID_TOM]"; break;
				case "P1-I45":	instrumentID = "[PEDAL_HI_HAT]"; break;
				case "P1-I47":	instrumentID = "[OPEN_HI_HAT]"; break;
				case "P1-I50":	instrumentID = "[CRASH_CYMBAL_1]"; break;
				case "P1-I44":	instrumentID = "[HIGH_FLOOR_TOM]]"; break;
				case "P1-I39":	instrumentID = "[ACOUSTIC_SNARE]"; break; // or [ELECTRIC_SNARE]
				case "P1-I54":	instrumentID = "[RIDE_BELL]"; break;
				case "P1-I53":	instrumentID = "[CHINESE_CYMBAL]"; break;
				case "P1-I36":	instrumentID = "[BASS_DRUM]"; break;
				case "P1-I52":	instrumentID = "[RIDE_CYMBAL_1]"; break;
				}

				// Check if note is a grace note.
				if (notes.get(j).getDuration() == 0) {
					measure += instrumentID + "o-";
					measure += " ";
					measure += instrumentID + "-o";
				} 
				else {
					measure += instrumentID + type;
				}

				// 
				if (measures.get(i).getNumNotes() - j != 1) {
					if (measures.get(i).getNotes().get(j + 1).isChord()) { // if next note is also part of the chord
						measure += "+";
					} 
					else {
						measure += " "; // add a space to split up notes
					}
				} 
				else { // add the tie thing around here i think

				}
			} // end of inner loop

			if (numOfMeasures - i != 1) {
				measure += "| "; // add space between notes to indicate measures
			}
			measureList.add(measure);
		} // end of outer loop
		return measureList;
	}	

	private ArrayList<String> getMeasureList() {
		ArrayList<String> result = new ArrayList<>();
		try {
			String instrument = this.getParser().getInstrument();
			if (instrument.equals("guitar") || instrument.equals("bass")) {
				result = GuitarBass();
			}
			else {
				result = Drum();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	//Draw the bar to mark the end of a Measure
	//Must implement double bar and end bars soon?
	private void barLines(double x, double y, String instrument) {
		//Set base length of the bar
		int endY;
		//Change the vertical length depending on the instrument
		if (instrument.equalsIgnoreCase("Bass")) {
			endY = 36;
		} else if (instrument.equalsIgnoreCase("Drumset")) {
			endY = 48;
		}
		//Assumed to be guitar
		else {
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
		int endY;
		//Change the vertical length depending on the instrument
		if (instrument.equalsIgnoreCase("Bass")) {
			endY = 36;
		} else if (instrument.equalsIgnoreCase("Drumset")) {
			endY = 48;
		}
		//Assumed to be guitar
		else {
			endY = 60;
		}

		double thinX, dotX;
		if (direction == 'l') {
			thinX = x + 5;
			dotX = x + 12;
		}
		//Assumed to be right
		else {
			thinX = x - 5;
			dotX = x - 12;

			//Text
			Text t = new Text(x - 25, y - 10, words);
			t.setFont(Font.font("arial", 15));
			//Add letter to pane
			pane.getChildren().add(t);
		}
		//Thinn line
		barLines(thinX, y, instrument);
		//Thicc line
		Line barLine = new Line(x, y+1, x, y + endY - 1);
		barLine.setStrokeWidth(4);
		pane.getChildren().add(barLine);
		//Circles
		Ellipse dot1 = new Ellipse(dotX, (2*y+endY)/2 - 7, 3, 3);
		Ellipse dot2 = new Ellipse(dotX, (2*y+endY)/2 + 7, 3, 3);

		pane.getChildren().add(dot1);
		pane.getChildren().add(dot2);
	}

	public void drawMeasureNumber(double y, int measureNumber) {
		Text t = new Text(7, y - 10, Integer.toString(measureNumber));
		t.setFont(Font.font("arial", FontPosture.ITALIC, 15));
		pane.getChildren().add(t);
	}

	//Update the SheetMusic GUI
	public void update() throws IOException { 
		this.pane.getChildren().clear();
		Parser p = new Parser(mvc.converter.getMusicXML());
		//Get the list of measure from parser
		List<Measure> measureList = p.getMeasures();
		//Initialize x and y coordinates of where to draw notes
		double x = 100.0, xVerify = 100, y = 0, yStaff = 0;			
		//Iterate through each measure
		for (int i = 0; i< measureList.size(); i++, x += noteSpacing)
		{
			//Get the current measure
			Measure measure = measureList.get(i);
			//Get the list of notes for each measure
			ArrayList<Note> noteList = measure.getNotes();
			xVerify = x;
			for (int j = 0; j < noteList.size(); j++, xVerify += noteSpacing) {
				//Get the current note
				Note note = noteList.get(j);
				//If it's a chord, draw the notes on the same line (x-coordinate)
				if (note.isChord()) {
					xVerify -= noteSpacing;
				}
			}
			if (xVerify > this.pane.getMaxWidth()) {
				x = 100.0;
				yStaff += staffSpacing;
				drawMeasureNumber(yStaff, p.getMeasures().get(i).getMeasureNumber());
				placeSheetLines(yStaff, p.getInstrument());
				clef(p.getMeasures().get(0).getAttributes().getClef().getSign(), 6, 18+yStaff, p.getInstrument());
				timeSignature(p.getMeasures().get(0).getAttributes().getTime().getBeats(), p.getMeasures().get(0).getAttributes().getTime().getBeatType(), 35, 28+yStaff, p.getInstrument());
			}

			//First one is always left
			if (p.getMeasures().get(i).getBarlines().size() > 0 && p.getMeasures().get(i).getBarlines().get(0).getLocation() ==  'l')
				drawRepeat(x-25, 0 + yStaff, 'l', p.getMeasures().get(i).getDirection().getWords(), p.getInstrument());

			//Loop through all the notes in the current measure
			for (int j = 0; j < noteList.size(); j++, x += noteSpacing)
			{
				//Get the current note
				Note note = noteList.get(j);
				//If it's a chord, draw the notes on the same line (x-coordinate)
				if (note.isChord()) {
					x -= noteSpacing;
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
				if (i == 0 && j == 0) {
					placeSheetLines(yStaff, p.getInstrument());
				}
				//Draw the note
				if (x < this.pane.getMaxWidth()) {
					new DrawNotes(pane, x, y + yStaff, note, p.getInstrument());
					clef(p.getMeasures().get(0).getAttributes().getClef().getSign(), 6, 18+yStaff, p.getInstrument());
					timeSignature(p.getMeasures().get(0).getAttributes().getTime().getBeats(), p.getMeasures().get(0).getAttributes().getTime().getBeatType(), 35, 28+yStaff, p.getInstrument());
				}
				else {
					x = 100.0;
					yStaff += staffSpacing;
					placeSheetLines(yStaff, p.getInstrument());
					new DrawNotes(pane, x, y + yStaff, note, p.getInstrument());
					drawMeasureNumber(yStaff, p.getMeasures().get(i).getMeasureNumber());
					clef(p.getMeasures().get(0).getAttributes().getClef().getSign(), 6, 18+yStaff, p.getInstrument());
					timeSignature(p.getMeasures().get(0).getAttributes().getTime().getBeats(), p.getMeasures().get(0).getAttributes().getTime().getBeatType(), 35, 28+yStaff, p.getInstrument());
				}
			}
			//Dynamically draw a bar line (after each measure)
			//Either first or second is right
			if ((p.getMeasures().get(i).getBarlines().size() == 1 && p.getMeasures().get(i).getBarlines().get(0).getLocation() ==  'r') 
					|| (p.getMeasures().get(i).getBarlines().size() > 1 && p.getMeasures().get(i).getBarlines().get(1).getLocation() ==  'r')) {
				x += noteSpacing;
				drawRepeat(x, 0 + yStaff, 'r', p.getMeasures().get(i).getDirection().getWords(), p.getInstrument());
			} else {
				barLines(x, 0 + yStaff, p.getInstrument());
			}
		}

	}

	private Window openNewWindow(Parent root, String windowName) {
		Stage stage = new Stage();
		stage.setTitle(windowName);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(MainApp.STAGE);
		stage.setResizable(false);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		return scene.getWindow();
	}

	@FXML
	private void customizeHandle() throws IOException {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/customizeGUI.fxml"));
			root = loader.load();
			CustomizeController controller = loader.getController();
			controller.setPreviewSheetController(this);
			convertWindow = this.openNewWindow(root, "Customize");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}

	public int getNoteSpacing() {
		return noteSpacing;
	}

	public int getStaffSpacing() {
		return staffSpacing;
	}

	// Setters for dynamic spacing
	public void setNoteSpacing(int noteSpacing) {
		this.noteSpacing = noteSpacing;
	}

	public void setStaffSpacing(int staffSpacing) {
		this.staffSpacing = staffSpacing;
	}

}