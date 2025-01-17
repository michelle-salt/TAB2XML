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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
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
import java.io.IOException;
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
import org.jfugue.player.SequencerManager;

import custom_exceptions.*;

public class PreviewSheetController {
	private MainViewController mvc;
	public Window convertWindow;

	@FXML private Pane pane;
	@FXML private AnchorPane anchorPane;
	@FXML private Canvas canvas;
	@FXML public CodeArea mainText;

	@FXML private Button playButton;
	@FXML private Button pauseButton;
	@FXML private Button stepForwardButton;
	@FXML private Button stepBackwardButton;
	@FXML private Button printButton;
	@FXML private Button goToMeasureButton;
	@FXML private TextField goToMeasureField;
	@FXML private TextField tempoField;

	private Parser parser;
	private String instrument;
	private String tempo;
	private ArrayList<String> measureList;
	private String musicSequence;

	private int noteSpacing;
	private int staffSpacing;
	private boolean justify;

	private Player player;
	private ManagedPlayer mplayer;

	BooleanProperty printButtonPressed;

	private ArrayList<ArrayList<NoteLocation>> noteLocation;
	private ArrayList<MeasureLocation> measureLocation;
	private ArrayList<ArrayList<String>> noteValues;

	/*
	 * Set default preview spacings. Initialize the player.
	 */
	public PreviewSheetController() {
		noteSpacing = 25;
		staffSpacing = 150;

		player = new Player();
		mplayer = player.getManagedPlayer();

		printButtonPressed = new SimpleBooleanProperty(false);

		noteLocation = new ArrayList<ArrayList<NoteLocation>>();
		measureLocation = new ArrayList<MeasureLocation>();
		noteValues = new ArrayList<ArrayList<String>>();
	}

	public void setMainViewController(MainViewController mvcInput) {
		mvc = mvcInput;
	}

	@FXML
	private <printButtonPressed> void printHandle() {
		// Set up a printer
		Printer p = Printer.getDefaultPrinter();
		if (p == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR,
					"You do not have a printer setup. Please set up a printer on this device to continue.");
			alert.setTitle("Print");
			alert.setHeaderText("Printing Error!");
			alert.show();
		} else {
			// Set up Page Dialog
			PrinterJob pj = PrinterJob.createPrinterJob();
			PageLayout pl = p.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, MarginType.DEFAULT);
			// Get the image of the GUI contents
			WritableImage wi = anchorPane.snapshot(null, null);
			// Load the image
			ImageView v = new ImageView(wi);
			// Dimensions of the Printable area
			double w = pl.getPrintableWidth() / wi.getWidth();
			double h = pl.getPrintableHeight() / wi.getHeight();
			Scale s = new Scale(w, w);
			v.getTransforms().add(s);
			Window window = pane.getScene().getWindow();
			// Print
			if (pj != null && pj.showPrintDialog(window)) {
				Translate t = new Translate(0, 0);
				v.getTransforms().add(t);
				for (int i = 0; i < Math.ceil(w / h); i++) {
					t.setY((pl.getPrintableHeight() / w) * (-i));
					pj.printPage(pl, v);
				}
				pj.endJob();
			}
		}
	}

	@FXML
	private void handleGotoMeasure() throws IOException {
		int number = Integer.parseInt(goToMeasureField.getText());
		if (number >= 1 && number <= parser.getNumMeasures()) {
			update();
			goToMeasure(number);
		}
		else if (number < 1 || number > parser.getNumMeasures()) {
			Alert alert = new Alert(Alert.AlertType.ERROR,
					"The measure you entered is outside the valid range. Enter a measure betweeen 1 and " + parser.getNumMeasures() + ".");
			alert.setTitle("Go-To Measure");
			alert.setHeaderText("Invalid Measure!");
			alert.show();
		}
	}

	private void goToMeasure(int num) throws IOException {
		double startX = measureLocation.get(num-1).getStartX();
		double startY = measureLocation.get(num-1).getStartY();
		double endX = measureLocation.get(num-1).getEndX();	

		Rectangle rectangle = null;
		if (instrument.equalsIgnoreCase("guitar")) {
			rectangle = new Rectangle(startX, startY-15, endX-startX, 90);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setStyle("-fx-fill: #FFA500; -fx-opacity: 0.3;");	
			//			rectangle.setStyle("-fx-stroke: blue;");
			//			rectangle.setStrokeWidth(1.5);
		}
		else if (instrument.equalsIgnoreCase("drumset") || instrument.equalsIgnoreCase("bass")) {
			rectangle = new Rectangle(startX, startY-45, endX-startX, 110);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setStyle("-fx-fill: #FFA500; -fx-opacity: 0.3;");	
		}

		pane.getChildren().add(rectangle);
		Object obj = pane.getParent().getParent().getParent().getParent();
		if (obj instanceof ScrollPane) {
			ScrollPane scrollPane = (ScrollPane) obj;
			scrollPane.setVvalue(rectangle.getBoundsInLocal().getMaxY()/pane.getBoundsInLocal().getMaxY());
		}
	}

	/*
	 * highlight(int measureNumber, int noteNumber)
	 */
	private void highlight() throws IOException {
		for (int i = 0; i < noteValues.size(); i++) {
			for (int j = 0; j < noteValues.get(i).size(); j++) {
				double startX = this.noteLocation.get(i).get(j).getX();
				double startY = this.noteLocation.get(i).get(j).getStaffY();
				update();
				Rectangle highlight = null;

				if (instrument.equalsIgnoreCase("guitar")) {
					highlight = new Rectangle(startX-10, startY-30, 30, 90);
					highlight.setStyle("-fx-fill: #1E90FF; -fx-opacity: 0.5;");
				}
				else if (instrument.equalsIgnoreCase("drumset")) {
					highlight = new Rectangle(startX-10, startY-30, 30, 90);
					highlight.setStyle("-fx-fill: #1E90FF; -fx-opacity: 0.5;");
				}
				else if (instrument.equalsIgnoreCase("bass")) {
					highlight = new Rectangle(startX-10, startY-30, 30, 90);
					highlight.setStyle("-fx-fill: #1E90FF; -fx-opacity: 0.5;");
				}
				pane.getChildren().add(highlight);
				Object obj = pane.getParent().getParent().getParent().getParent();
				if (obj instanceof ScrollPane) {

					ScrollPane scrollPane = (ScrollPane) obj;
					scrollPane.setVvalue(highlight.getBoundsInLocal().getMaxY()/pane.getBoundsInLocal().getMaxY());
				}
			}
		}
	}

	/*
	 * The visibility of the buttons should toggle after each button press.
	 * 
	 * The play button either starts a new `player`, or resumes from where it was
	 * last paused. The pause button only pauses the `player`.
	 */
	@FXML
	private void handlePlayMusic() throws InvalidInputException, UnrecognizedInstrumentException, IOException {
		if (playButton.isVisible()) {
			if (mplayer.isStarted()) {
				mplayer.resume();
			} else {	
				player = new Player();
				mplayer = player.getManagedPlayer();
				setTempo();
				setMeasureList();
				setMusicSequence();
				setNoteValues();
				playMusic(musicSequence);
			}
			playButton.setVisible(false);
		} else {
			mplayer.pause();
			playButton.setVisible(true);
		}
	}

	public void playMusic(String recording) {
		System.out.println(recording);
		new Thread(() -> {
			player.play(recording);
			mplayer.reset();
			playButton.setVisible(true);
		}).start();
	}

	/*
	 * Each instrument MUST have a default `tempo` to fall back to whenever the `tempoField` is empty.
	 * The custom `tempo` MUST accept positive inputs ONLY. Any other inputs should throw an InvalidInputException. 
	 */
	private void setTempo() throws InvalidInputException {
		tempo = "";
		if (tempoField.getText().isEmpty()) 
		{
			switch (instrument) 
			{
			case "guitar":	tempo += "T100 V0 I[GUITAR] "; 			break;
			case "bass": 	tempo += "T100 V0 I[ACOUSTIC_BASS] ";	break;
			case "drumset": tempo += "T100 V9 ";					break;
			}
		} 
		else if (!tempoField.getText().isEmpty()) 
		{
			if (tempoField.getText().contains("-")) 
			{
				throw new InvalidInputException("Invalid input: Enter a positive number in the tempo field.");
			}
			int bpm = Integer.parseInt(tempoField.getText());
			if (bpm > 0) 
			{
				switch (instrument)
				{
				case "guitar": 	tempo += "T" + bpm + " V0 I[GUITAR] "; 			break;
				case "bass": 	tempo += "T" + bpm + " V0 I[ACOUSTIC_BASS] "; 	break;
				case "drumset": tempo += "T" + bpm + " V9 "; 					break;
				}
			}
		}
	}

	/*
	 * Set the measureList of the instrument you want to add here. 
	 * (e.g. result = piano()).
	 */
	private void setMeasureList() throws UnrecognizedInstrumentException {
		try {
			switch (instrument)
			{
			case "guitar":
			case "bass": 	measureList = GuitarBass(); break;
			case "drumset": measureList = Drum(); 		break;
			}	

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Combines the String `tempo` and the String `measureList` that has already been compiled
	 * to form the final music sequence.
	 */
	private void setMusicSequence() {
		musicSequence = tempo;
		for (String ml : measureList) {
			musicSequence += ml;
		}
	}

	/*
	 * Arranges a list of `measure numbers` that arranges a list of `note numbers`.
	 * Each `note number` contain different information about their type, duration, and more ...
	 * 
	 * (e.g. [ [ E2I, B2I, E3I, G#3I, B3I, E4I, B3I, G#3I ], E4W+B3W+G#3W+E3W+B2W+E2W ]	)
	 *			
	 *			-	The above `measureList` has a total of 2 `measure numbers`, and a total of 9 `note numbers`.
	 *			-	The first `measure number` has a total of 8 `note numbers`.
	 *			- 	The second `measure number` has 1 `note number`.
	 */
	private void setNoteValues() {
		String measures = "";
		for (String s: measureList) {
			measures += s;
		}

		int numberOfPipes = 1;		
		for (int i = 0; i < measures.length(); i++) {
			if (measures.charAt(i) == '|') {
				numberOfPipes++;
			}
		}

		String[] noteNum = measures.split(" | ");

		int j = 0;
		noteValues.add(new ArrayList<>());
		for (int i = 0; i < numberOfPipes; i++) {
			for ( ; j < noteNum.length; ) {
				if (noteNum[j].equals("|")) {
					noteValues.add(new ArrayList<String>());
					j++;
					break;
				} 
				else {
					noteValues.get(i).add(noteNum[j]);
				}
				j++;
			}
		}
	}

	/*
	 * Measure List for Guitar and Bass.
	 */
	private ArrayList<String> GuitarBass() throws IOException {

		int repeatedMeasures = 0;

		ArrayList<String> measureList = new ArrayList<>(); // split measures into array

		ArrayList<Measure> measures = parser.getMeasures();

		int numOfMeasures = parser.getNumMeasures();

		for (int i = 0; i < numOfMeasures; i++) {
			String measure = "";

			ArrayList<Barline> barLine = measures.get(i).getBarlines();			

			// First repeat in the list is always the left repeat
			if (barLine.size() > 0 && barLine.get(0).getLocation() == 'l') {

				repeatedMeasures++;

			}

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
				} else if (pitch.getAlter() == -1) {
					altervalue = "b";
				} else {
					altervalue = "";
				}

				// Check if note is a grace note.
				if (notes.get(j).getDuration() == 0) {
					measure += step + altervalue + octave + "o- ";
					measure += step + altervalue + octave + "-o";
				} else {

					measure += step + altervalue + octave + type;	

				}

				if (numOfNotes - j != 1) {
					if (notes.get(j + 1).isChord()) { // if next note is also part of the chord
						measure += "+";
					} else {	
						measure += " "; // add a space to split up notes											
					}
				}
			} // inner loop ends

			if (numOfMeasures - i != 1) {
				measure += " | "; // add space between notes to indicate measures
			}

			measureList.add(measure);

			// end of repeat section
			if ((barLine.size() == 1 && barLine.get(0).getLocation() == 'r') || (barLine.size() > 1 && barLine.get(1).getLocation() == 'r')) {

				ArrayList<String> repeatedSection = new ArrayList<>();
				int size = measureList.size();

				for (int k = repeatedMeasures; k > 0; k--) { // adds repeated section to a separate arraylist

					repeatedSection.add(measureList.get(size - k));

				}

				int repeatedsize = repeatedSection.size();

				int repeats = 0;

				if (barLine.get(0).getLocation() == 'r') {

					repeats = barLine.get(0).getRepeatTimes();

				}

				if (barLine.get(1).getLocation() == 'r') {

					repeats = barLine.get(1).getRepeatTimes();

				}

				for (int l = 0; l < repeats - 1; l++) {

					for (int m = 0; m < repeatedsize; m++) {

						measureList.add(repeatedSection.get(m));

					}

				}

				repeatedMeasures = 0;

			} else {

				repeatedMeasures++;

			}

		} // outer loop ends
		return measureList;
	}

	/*
	 * Measure List for Drum.
	 */
	private ArrayList<String> Drum() throws IOException {

		int repeatedMeasures = 0;

		ArrayList<String> measureList = new ArrayList<>(); // split measures into array

		ArrayList<Measure> measures = parser.getMeasures();

		int numOfMeasures = parser.getNumMeasures();

		for (int i = 0; i < numOfMeasures; i++) { // go through every measure
			String measure = "";

			ArrayList<Barline> barLine = measures.get(i).getBarlines();	
			// First repeat in the list is always the left repeat
			if (barLine.size() > 0 && barLine.get(0).getLocation() == 'l') {

				repeatedMeasures++;

			}

			int numOfNotes = measures.get(i).getNumNotes();
			ArrayList<Note> notes = measures.get(i).getNotes();

			for (int j = 0; j < measures.get(i).getNumNotes(); j++) { // go through all notes in specific measure
				String instrumentID = notes.get(j).getInstrumentID();
				char type = notes.get(j).getType();

				/*
				 * P1-I46 = Low Tom P1-I43 = Closed Hi-Hat P1-I42 = Low Floor Tom P1-I48 =
				 * Low-Mid Tom P1-I45 = Pedal Hi-Hat P1-I47 = Open Hi-Hat P1-I50 = Crash Cymbal
				 * 1 P1-I44 = High Floor Tom P1-I39 = Snare P1-I54 = Ride Bell P1-I53 = Chinese
				 * Cymbal 1 P1-I36 = Bass Drum 1 P1-I52 = Ride Cymbal 1
				 */

				switch(instrumentID) {
				case "P1-I46":	instrumentID = "[LO_TOM]"; break;
				case "P1-I43":	instrumentID = "[CLOSED_HI_HAT]"; break;
				case "P1-I42":	instrumentID = "[LO_FLOOR_TOM]"; break;
				case "P1-I48":	instrumentID = "[LO_MID_TOM]"; break;
				case "P1-I45":	instrumentID = "[PEDAL_HI_HAT]"; break;
				case "P1-I47":	instrumentID = "[OPEN_HI_HAT]"; break;
				case "P1-I50":	instrumentID = "[CRASH_CYMBAL_1]"; break;
				case "P1-I44":	instrumentID = "[HIGH_FLOOR_TOM]"; break;
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
				} else if (instrumentID == "none"){ // rest note

					measure += "R" + type;

				} else {

					measure += instrumentID + type;
				}

				if (measures.get(i).getNumNotes() - j != 1) {
					if (measures.get(i).getNotes().get(j + 1).isChord()) { // if next note is also part of the chord
						measure += "+";
					} else {
						measure += " ";
					}
				}
			} // end of inner loop

			if (numOfMeasures - i != 1) {
				measure += " | "; // add space between notes to indicate measures
			}
			measureList.add(measure);

			// end of repeat section
			if ((barLine.size() == 1 && barLine.get(0).getLocation() == 'r') || (barLine.size() > 1 && barLine.get(1).getLocation() == 'r')) {

				ArrayList<String> repeatedSection = new ArrayList<>();
				int size = measureList.size();

				for (int k = repeatedMeasures; k > 0; k--) { // adds repeated section to a separate arraylist

					repeatedSection.add(measureList.get(size - k));

				}

				int repeatedsize = repeatedSection.size();

				int repeats = 0;

				if (barLine.get(0).getLocation() == 'r') {

					repeats = barLine.get(0).getRepeatTimes();

				}else if (barLine.get(1).getLocation() == 'r') {

					repeats = barLine.get(1).getRepeatTimes();

				}

				for (int l = 0; l < repeats - 1; l++) {

					for (int m = 0; m < repeatedsize; m++) {

						measureList.add(repeatedSection.get(m));

					}

				}

				repeatedMeasures = 0;

			} else {

				repeatedMeasures++;

			}
		} // end of outer loop
		return measureList;
	}

	// Draw the bar to mark the end of a Measure
	// Must implement double bar and end bars soon?
	private void barLines(double x, double y, String instrument) {
		// Set base length of the bar
		int endY;
		// Change the vertical length depending on the instrument
		if (instrument.equalsIgnoreCase("Bass")) {
			endY = 36;
		} else if (instrument.equalsIgnoreCase("Drumset")) {
			endY = 48;
		}
		// Assumed to be guitar
		else {
			endY = 60;
		}

		// Draw the bar line
		Line bar = new Line();
		bar.setStartX(x);
		bar.setEndX(x);
		bar.setStartY(y);
		bar.setEndY(y + endY);

		// Add bar to pane
		pane.getChildren().add(bar);
	}

	// Draw the Clef at the left-end of the Staff
	private void clef(String symbol, double x, double y, String instrument) {
		if (symbol.equalsIgnoreCase("TAB")) {
			if (instrument.equalsIgnoreCase("guitar")) {
				// Draw onto the pane letter by letter
				for (int i = 0; i < symbol.length(); i++) {
					// Get the letter
					Text t = new Text(x, y, symbol.substring(i, i + 1));
					t.setFont(Font.font("times new roman", FontWeight.BLACK, 24));
					// Add letter to pane
					pane.getChildren().add(t);
					// Increment vertical distance for next letter
					y += 19;
				}
			}
			// Assumed to be bass
			else {
				y -= 7;
				// Draw onto the pane letter by letter
				for (int i = 0; i < symbol.length(); i++) {
					// Get the letter
					Text t = new Text(x, y, symbol.substring(i, i + 1));
					t.setFont(Font.font("times new roman", FontWeight.BLACK, 17));
					// Add letter to pane
					pane.getChildren().add(t);
					// Increment vertical distance for next letter
					y += 12.5;
				}
			}
		} else if (symbol.equalsIgnoreCase("percussion")) {
			symbol = "II";
			// Percussion symbol starts lower on the staff than TAB
			y += 18;
			for (int i = 0; i < symbol.length(); i++) {
				// Get the letter
				Text t = new Text(x, y, symbol.substring(i, i + 1));
				t.setFont(Font.font("veranda", FontWeight.BLACK, 34));
				// Add letter to pane
				pane.getChildren().add(t);
				// Increment vertical distance for next letter
				x += 8;
			}
		}
	}

	// Draws Sheet lines and places them on the GUI
	public void placeSheetLines(double y, String instrument) {
		if (instrument.equalsIgnoreCase("Bass")) {
			// Draws 4 lines
			for (int i = 1; i <= 4; i++, y += 12) {
				DrawSheetLines sheetLine = new DrawSheetLines(0.0, y, this.pane.getMaxWidth(), y);
				// Add lines to pane
				pane.getChildren().add(sheetLine.getLine());
			}
		} else if (instrument.equalsIgnoreCase("Drumset")) {
			// Draws 5 lines
			for (int i = 1; i <= 5; i++, y += 12) {
				DrawSheetLines sheetLine = new DrawSheetLines(0.0, y, this.pane.getMaxWidth(), y);
				// Add lines to pane
				pane.getChildren().add(sheetLine.getLine());
			}
		} else if (instrument.equalsIgnoreCase("Guitar")) {
			// Draws 6 lines
			for (int i = 1; i <= 6; i++, y += 12) {
				DrawSheetLines sheetLine = new DrawSheetLines(0.0, y, this.pane.getMaxWidth(), y);
				// Add lines to pane
				pane.getChildren().add(sheetLine.getLine());
			}
		}
	}

	public void timeSignature(int beats, int beatType, double x, double y, String instrument) {
		if (instrument.equalsIgnoreCase("guitar")) {
			Text beatsText = new Text(x, y, Integer.toString(beats));
			beatsText.setFont(Font.font("cambria", FontWeight.BLACK, 40));
			// Add number to pane
			pane.getChildren().add(beatsText);

			// Increment vertical distance for next letter
			y += 31;

			Text beatTypeText = new Text(x, y, Integer.toString(beatType));
			beatTypeText.setFont(Font.font("cambria", FontWeight.BLACK, 40));
			// Add number to pane
			pane.getChildren().add(beatTypeText);
		} else if (instrument.equalsIgnoreCase("drumset")) {
			y -= 6;

			Text beatsText = new Text(x, y, Integer.toString(beats));
			beatsText.setFont(Font.font("cambria", FontWeight.BLACK, 32));
			// Add number to pane
			pane.getChildren().add(beatsText);

			// Increment vertical distance for next letter
			y += 26;

			Text beatTypeText = new Text(x, y, Integer.toString(beatType));
			beatTypeText.setFont(Font.font("cambria", FontWeight.BLACK, 32));
			// Add number to pane
			pane.getChildren().add(beatTypeText);
		}
		// Assumed to be bass
		else {
			y -= 11;

			Text beatsText = new Text(x, y, Integer.toString(beats));
			beatsText.setFont(Font.font("cambria", FontWeight.BLACK, 23));
			// Add number to pane
			pane.getChildren().add(beatsText);

			// Increment vertical distance for next letter
			y += 19;

			Text beatTypeText = new Text(x, y, Integer.toString(beatType));
			beatTypeText.setFont(Font.font("cambria", FontWeight.BLACK, 23));
			// Add number to pane
			pane.getChildren().add(beatTypeText);
		}
	}

	public void drawRepeat(double x, double y, char direction, String words, String instrument) {
		// Set base length of the bar
		int endY;
		// Change the vertical length depending on the instrument
		if (instrument.equalsIgnoreCase("Bass")) {
			endY = 36;
		} else if (instrument.equalsIgnoreCase("Drumset")) {
			endY = 48;
		}
		// Assumed to be guitar
		else {
			endY = 60;
		}

		double thinX, dotX;
		if (direction == 'l') {
			thinX = x + 5;
			dotX = x + 12;
		}
		// Assumed to be right
		else {
			thinX = x - 5;
			dotX = x - 12;

			// Text
			Text t = new Text(x - 25, y - 10, words);
			t.setFont(Font.font("arial", 15));
			// Add letter to pane
			pane.getChildren().add(t);
		}
		// Thinn line
		barLines(thinX, y, instrument);
		// Thicc line
		Line barLine = new Line(x, y + 1, x, y + endY - 1);
		barLine.setStrokeWidth(4);
		pane.getChildren().add(barLine);
		// Circles
		Ellipse dot1 = new Ellipse(dotX, (2 * y + endY) / 2 - 7, 3, 3);
		Ellipse dot2 = new Ellipse(dotX, (2 * y + endY) / 2 + 7, 3, 3);

		pane.getChildren().add(dot1);
		pane.getChildren().add(dot2);
	}

	public void drawMeasureNumber(double y, int measureNumber) {
		Text t = new Text(7, y - 10, Integer.toString(measureNumber));
		t.setFont(Font.font("arial", FontPosture.ITALIC, 15));
		pane.getChildren().add(t);
	}

	private void leftAlign(List<Measure> measureList) {
		//Get the time and default to 4/4 if it doesn't exist
		int timeBeats = measureList.get(0).getAttributes().getTime().getBeats();
		int timeBeatType = measureList.get(0).getAttributes().getTime().getBeatType();
		if (timeBeats == -1)
			timeBeats = 4;
		if (timeBeatType == -1)
			timeBeatType = 4;
		printNotesInMeasures(measureList, 0, measureList.size()-1, noteSpacing, 20, false, timeBeats, timeBeatType);		
	}

	//Start and end measure are inclusive
	private void printNotesInMeasures(List<Measure> measureList, int startMeasure, int endMeasure, double noteSpacing, int currStaff, boolean drawNewStaff, int timeBeats, int timeBeatType) {
		// Initialize x and y coordinates of where to draw notes
		double x = 100.0, xVerify = 100, y = 20, yStaff = currStaff;
		// Iterate through each measure
		for (int i = startMeasure; i <= endMeasure; i++, x += noteSpacing) {
			this.noteLocation.add(new ArrayList<NoteLocation>());
			// Get the current measure
			Measure measure = measureList.get(i);
			// Get the list of notes for each measure
			ArrayList<Note> noteList = measure.getNotes();
			// Figure out if the measure should be drawn on a new line
			// It should by default be drawn on the first line if this is the first measure
			xVerify = x;
			for (int j = 0; j < noteList.size() && i != 0; j++, xVerify += noteSpacing) {
				// Get the current note
				Note note = noteList.get(j);
				// If it's a chord, draw the notes on the same line (x-coordinate)
				if (note.isChord()) {
					xVerify -= noteSpacing;
				}
			}
			boolean newStaff = false;
			// If the notes don't fit on the current staff or the time signature of this measure is different than the previous, add a new staff
			if (drawNewStaff || Math.floor(xVerify) > this.pane.getMaxWidth() || (i != 0 && ((measure.getAttributes().getTime().getBeats() != timeBeats && measure.getAttributes().getTime().getBeats() != -1) || (measure.getAttributes().getTime().getBeatType() != timeBeatType && measure.getAttributes().getTime().getBeatType() != -1)))) {
				newStaff = true;
				x = 100.0;
				yStaff += staffSpacing;
				drawMeasureNumber(yStaff, parser.getMeasures().get(i).getMeasureNumber());
				placeSheetLines(yStaff, instrument);
				clef(parser.getMeasures().get(0).getAttributes().getClef().getSign(), 6, 18 + yStaff, instrument);
				if ((measure.getAttributes().getTime().getBeats() != timeBeats && measure.getAttributes().getTime().getBeats() != -1) || (measure.getAttributes().getTime().getBeatType() != timeBeatType && measure.getAttributes().getTime().getBeatType() != -1)) {
					timeBeats = measure.getAttributes().getTime().getBeats();
					timeBeatType = measure.getAttributes().getTime().getBeatType();
				}
				timeSignature(timeBeats, timeBeatType, 35, 28 + yStaff, instrument);
				drawNewStaff = false;
			}

			// First repeat in the list is always the left repeat
			if (parser.getMeasures().get(i).getBarlines().size() > 0
					&& parser.getMeasures().get(i).getBarlines().get(0).getLocation() == 'l') {
				drawRepeat(x - 25, 0 + yStaff, 'l', parser.getMeasures().get(i).getDirection().getWords(),
						instrument);
				// If no measureLocation exists yet, add a measure first
				// This would only happen if this is the first measure, so a default value can
				// be assumed
				if (this.measureLocation.size() == 0) {
					this.measureLocation.add(new MeasureLocation(90, instrument));
				}
				this.measureLocation.get(i).setStartRepeat();
			}

			// Loop through all the notes in the current measure
			for (int j = 0; j < noteList.size(); j++, x += noteSpacing) {
				// Get the current note
				Note note = noteList.get(j);
				// If it's a chord, draw the notes on the same line (x-coordinate)
				if (note.isChord()) {
					x -= noteSpacing;
				}
				// Get the y value for each drum note
				if (instrument.equals("drumset")) {
					// The spacing of each note is based on the octave and step
					// Each value represents one "space" above, with a space either being the line
					// or the spaces between
					// 0 is the bottom line (going through the line), 1 is the space between the
					// bottom two lines, etc.
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
						case 'D':	y = 12;	break;
						}
					}
					// Value retrieved through guess and check :))))
					y = -7 + (y - 1) * 6;
				}
				// Get the y value for each guitar or bass note
				else {
					// Figure out which string the note is on
					int string = note.getString();
					// Set the y coordinate based on the line
					y = 5 + (string - 1) * 12; // Each staff line is 12 y-pixels apart
				}
				// Place staff lines for the first measure only
				if (i == 0 && j == 0) {
					placeSheetLines(yStaff, instrument);
				}
				// Draw each note
				if (x < this.pane.getMaxWidth()) {
					this.noteLocation.get(i).add(new NoteLocation(x, y + yStaff, yStaff, note, instrument));
					if (!note.isRest()) {
						boolean isLast = j == measure.getNumNotes() - 1 || (j == measure.getNumNotes() - 2 && measure.getNotes().get(measure.getNumNotes()-1).isChord());
						Note next = null;
						//if the note is a slide
						int notesPassed = 1;
						if (note.getSlide().getNumber() != -1) {
							//loop through next notes to find the stop (assuming it's in the same measure)
							for (int k = j+1; k < measure.getNumNotes(); k++) {
								if (measure.getNotes().get(k).getSlide().getType() != null && measure.getNotes().get(k).getSlide().getType().equalsIgnoreCase("stop")) {
									next = measure.getNotes().get(k);
									break;
								}
								notesPassed++;
							}
						}
						new DrawNotes(pane, x, y + yStaff, note, instrument, yStaff, noteSpacing, isLast, next, notesPassed*noteSpacing);
					} else {
						new DrawRests(pane, x, y + yStaff, note, yStaff);
					}
					clef(parser.getMeasures().get(0).getAttributes().getClef().getSign(), 6, 18 + yStaff, instrument);
					timeSignature(timeBeats, timeBeatType, 35, 28 + yStaff, instrument);
				}
				// If the note is the first of a new staff, draw the new staff lines and THEN
				// the notes
				else {
					x = 100.0;
					yStaff += staffSpacing;
					placeSheetLines(yStaff, instrument);
					this.noteLocation.get(i).add(new NoteLocation(x, y + yStaff, yStaff, note, instrument));
					if (!note.isRest()) {
						boolean isLast = j == measure.getNumNotes() - 1 || (j == measure.getNumNotes() - 2 && measure.getNotes().get(measure.getNumNotes()-1).isChord());
						Note next = null;
						//if the note is a slide
						int notesPassed = 1;
						if (note.getSlide().getNumber() != -1) {
							//loop through next notes to find the stop (assuming it's in the same measure)
							for (int k = j+1; k < measure.getNumNotes(); k++) {
								if (measure.getNotes().get(k).getSlide().getType() != null && measure.getNotes().get(k).getSlide().getType().equalsIgnoreCase("stop")) {
									next = measure.getNotes().get(k);
									break;
								}
								notesPassed++;
							}
						}
						new DrawNotes(pane, x, y + yStaff, note, instrument, yStaff, noteSpacing, isLast, next, notesPassed*noteSpacing);
					} else {
						new DrawRests(pane, x, y + yStaff, note, yStaff);
					}
					drawMeasureNumber(yStaff, parser.getMeasures().get(i).getMeasureNumber());
					clef(parser.getMeasures().get(0).getAttributes().getClef().getSign(), 6, 18 + yStaff, instrument);
					timeSignature(timeBeats, timeBeatType, 35, 28 + yStaff,	instrument);
				}
			}
			// Dynamically draw a bar line (after each measure) or a repeat line
			// Either first or second repeat in list is the right repeat line
			if ((parser.getMeasures().get(i).getBarlines().size() == 1
					&& parser.getMeasures().get(i).getBarlines().get(0).getLocation() == 'r')
					|| (parser.getMeasures().get(i).getBarlines().size() > 1
							&& parser.getMeasures().get(i).getBarlines().get(1).getLocation() == 'r')) {
				x += noteSpacing;
				drawRepeat(x, 0 + yStaff, 'r', parser.getMeasures().get(i).getDirection().getWords(), instrument);
				// If it's the first measure, add the first measure in the ArrayList
				if (this.measureLocation.size() == 0) {
					this.measureLocation.add(new MeasureLocation(90, instrument));
				}
				// Otherwise, if it's the first line on a staff, reset the startX value
				else if (newStaff) {
					this.measureLocation.get(i).setStartX(90);
				}
				// Can't be first measure
				this.measureLocation.get(i).setEndRepeat();
				this.measureLocation.get(i).setEndX(x);
				this.measureLocation.get(i).setStartY(yStaff);
				// Add a new instance of MeasureLocation for the next measure
				// Indicate that this is the start of the next measure
				this.measureLocation.add(new MeasureLocation(x, instrument));
			} else {
				// If it's the first measure, add the first measure in the ArrayList
				if (this.measureLocation.size() == 0) {
					this.measureLocation.add(new MeasureLocation(90, instrument));
				}
				// Otherwise, if it's the first line on a staff, reset the startX value
				else if (newStaff) {
					this.measureLocation.get(i).setStartX(90);
				}
				// Add the current barline location as the end of the measure
				this.measureLocation.get(i).setEndX(x);
				this.measureLocation.get(i).setStartY(yStaff);
				// Add a new instance of MeasureLocation for the next measure
				// Indicate that this is the start of the next measure
				this.measureLocation.add(new MeasureLocation(x, instrument));
				// Draw the barline
				barLines(x, 0 + yStaff, instrument);
				if (!instrument.equalsIgnoreCase("drumset"))
					new DrawGuitarBeams(pane, measure.getNotes(), this.noteLocation.get(i), noteSpacing);
				else 
					new DrawDrumBeams(pane, measure.getNotes(), this.noteLocation.get(i), noteSpacing);
			}
			
		}
	}

	private void justify(List<Measure> measureList) {
		//Get a list of the number of notes without chords in each measure
		ArrayList<Integer> notesWithoutChords = new ArrayList<Integer>();
		for (int i = 0; i < measureList.size(); i++) {
			if (i == 0)
				notesWithoutChords.add(0);
			else
				notesWithoutChords.add(1);
			for (int j = 0; j < measureList.get(i).getNumNotes(); j++) {
				if (!measureList.get(i).getNotes().get(j).isChord()) {
					notesWithoutChords.set(i, notesWithoutChords.get(i)+1);
				}
			}
		}	
		
		///Might need to add 1 (if the very end of the screen can be a note instead of a barline
		double maxNotesPerStaff = ((pane.getMaxWidth()-100)/(double)noteSpacing);
		int sum = 0, startMeasure = 0, endMeasure = -1, currStaff = 20-staffSpacing;
		if (measureList.get(0).getBarlines().size() != 0)
			sum = 1;
		
		//Get the time and default to 4/4 if it doesn't exist
		int timeBeats = measureList.get(0).getAttributes().getTime().getBeats();
		int timeBeatType = measureList.get(0).getAttributes().getTime().getBeatType();
		if (timeBeats == -1)
			timeBeats = 4;
		if (timeBeatType == -1)
			timeBeatType = 4;

		//Loop through every measure and print notes
		for (int i = 0; i < measureList.size(); i++) {
			//Get which measures are needed in each staff
			if (sum + notesWithoutChords.get(i) > Math.floor(maxNotesPerStaff) ||
					(measureList.get(i).getAttributes().getTime().getBeats() != timeBeats && measureList.get(i).getAttributes().getTime().getBeats() != -1) ||
					(measureList.get(i).getAttributes().getTime().getBeatType() != timeBeatType && measureList.get(i).getAttributes().getTime().getBeatType() != -1)) {
				endMeasure = i - 1;
				double justifyNoteSpacing = (pane.getMaxWidth()-100.0)/(sum);
				printNotesInMeasures(measureList, startMeasure, endMeasure, justifyNoteSpacing, currStaff, true, timeBeats, timeBeatType);
				//Rest time variables
				if ((measureList.get(i).getAttributes().getTime().getBeats() != timeBeats && measureList.get(i).getAttributes().getTime().getBeats() != -1) ||
						(measureList.get(i).getAttributes().getTime().getBeatType() != timeBeatType && measureList.get(i).getAttributes().getTime().getBeatType() != -1)) {
					timeBeats = measureList.get(i).getAttributes().getTime().getBeats();
					timeBeatType = measureList.get(i).getAttributes().getTime().getBeatType();
				}				
				//Add a new staff
				currStaff += staffSpacing;
				//Reset values
				startMeasure = i;
				endMeasure = -1;
				sum = -1; //Because first note in first measure of staff doesn't need 1 added to sum
				//Repeat will add one "note" to size
				if (measureList.get(i).getBarlines().size() != 0)
					sum = 0;
			}
			sum += notesWithoutChords.get(i);
			//Call left align but pass justifyNoteSpacing
		}
		double justifyNoteSpacing = (pane.getMaxWidth()-100.0)/(sum);
		printNotesInMeasures(measureList, startMeasure, measureList.size()-1, justifyNoteSpacing, currStaff, true, timeBeats, timeBeatType);		
	}
	
	// Update the SheetMusic GUI
	public void update() throws IOException {
		this.pane.getChildren().clear();
		try {
			setParser();
			setInstrument();
		} catch (UnrecognizedInstrumentException e) {
			e.printStackTrace();
		}
		// Get the list of measure from parser
		List<Measure> measureList = parser.getMeasures();
		//Reset Location lists
		this.measureLocation = new ArrayList<MeasureLocation>();
		this.noteLocation = new ArrayList<ArrayList<NoteLocation>>();
		if (justify)
			justify(measureList);
		else
			leftAlign(measureList);
	}

	@FXML
	private void handleEditInput() {
		mplayer.reset();
		playButton.setVisible(true);
		mvc.convertWindow.hide();
	}

	@FXML
	private void handleStopMusic() {
		mplayer.reset();
		playButton.setVisible(true);
	}

	@FXML void handleStepForward() {
		if (mplayer.getTickLength()-5000 > 5000) {
						System.out.println("before : " + mplayer.getTickPosition());
			mplayer.seek(mplayer.getTickPosition()+5000);
			try {
				mplayer.pause(); Thread.sleep(1000); 
				mplayer.resume();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (mplayer.getTickPosition() >= mplayer.getTickLength()) {
			mplayer.onEndOfTrack();
			mplayer.reset();
			playButton.setVisible(true);
		}
	}

	@FXML void handleStepBackward() {
		if (mplayer.getTickLength()-5000 > 0) {
			mplayer.seek(mplayer.getTickPosition()-5000);
			try {
				mplayer.pause(); Thread.sleep(1000); 
				mplayer.resume();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (mplayer.getTickPosition() >= mplayer.getTickLength()) {
			mplayer.onEndOfTrack();
			mplayer.reset();
			playButton.setVisible(true);
		}
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

	/*
	 * Getters and Setters for spacing.
	 */
	public int getNoteSpacing() {
		return noteSpacing;
	}

	public void setNoteSpacing(int noteSpacing) {
		this.noteSpacing = noteSpacing;
	}

	public int getStaffSpacing() {
		return staffSpacing;
	}

	public void setStaffSpacing(int staffSpacing) {
		this.staffSpacing = staffSpacing;
	}

	public boolean getJustify() {
		return justify;
	}

	public void setJustify(boolean justify) {
		this.justify = justify;
	}

	/*
	 * More Getters for easy access.
	 */
	private void setParser() {
		this.parser = new Parser(mvc.getMusicXML());
	}

	public ManagedPlayer getManagedPlayer() {
		return mplayer;
	}

	/*
	 * Add support for instruments here.
	 */
	private void setInstrument() throws UnrecognizedInstrumentException {
		instrument = parser.getInstrument();
		if (!instrument.equalsIgnoreCase("guitar") && !instrument.equalsIgnoreCase("bass") && !instrument.equalsIgnoreCase("drumset"))
			throw new UnrecognizedInstrumentException("Error: Instrument not supported");
	}

}