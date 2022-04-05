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
import javafx.scene.control.ButtonType;
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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.*;

import org.fxmisc.richtext.CodeArea;
import org.jfugue.player.ManagedPlayer;
import org.jfugue.player.Player;

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
	@FXML TextField tempoField;
	@FXML TextField goToMeasureField;
	@FXML Button goToMeasureButton;

	BooleanProperty printButtonPressed = new SimpleBooleanProperty(false);

	public PreviewSheetController() { 
		/* Set default noteSpacing to 25 and staffSpacing to 100 */
		noteSpacing = 25;
		staffSpacing = 100;
	}

	public void setMainViewController(MainViewController mvcInput) {
		mvc = mvcInput;
	}

	@FXML
	public <printButtonPressed> void printHandle() {
		//Set up a printer
		Printer p = Printer.getDefaultPrinter();
		if (p == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "You do not have a printer setup. Please set up a printer on this device to continue.");
			alert.setTitle("Print");
			alert.setHeaderText("Printing Error!");
			alert.show();
			/* 
			 * Setup default printer if p is null. 
			 */
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
	public void handleGotoMeasure() throws IOException {
		String os = System.getProperty("os.name").toLowerCase();
		String path = new File("").getAbsolutePath();

		if (os.contains("win")) {
			path = path.concat("\\");
		}
		else {
			path = path.concat("/");
		}
		Parser p = new Parser(Files.readString(Paths.get(path.concat("musicXML.txt"))));

		/*
		 * 	1 - non-empty field (either within range or out of range)
		 * 	2 - empty field
		 */
		String field = goToMeasureField.getText();
		int max = p.getNumMeasures();
		if (!field.isEmpty()) { /* non-empty field */
			if (Integer.parseInt(field) < 1 || Integer.parseInt(field) > max) {
				Alert alert = new Alert(Alert.AlertType.ERROR, 
						"The measure you entered is outside the valid range. Enter a measure betweeen 1 and " + max + ".");
				alert.setTitle("Go-To Measure");
				alert.setHeaderText("Invalid Measure!");
				alert.show();
			}
			else { /* valid measure */

			}
		}
		else {
			/* nothing */
		}
	}
	
	@FXML
	private void handleEditInput() {
		mvc.convertWindow.hide();
	}

	String tempo;
	@FXML
	public void handlePlayMusic() {
		try {
			if (tempoField.getText().isEmpty()) {
				tempo = "100";
			}
			else {
				tempo = tempoField.getText();
			}
			playMusic();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Player player = new Player();
	ManagedPlayer mplayer = player.getManagedPlayer();
	boolean pause = false;
	
	public void handlePauseMusic() {
		
		if(!pause) {
			
			mplayer.pause();
			pause = true;
			
		}else {
			
			mplayer.resume();
			pause = false;
			
		}
	}

	public void handleStopMusic() {
		
		mplayer.finish();
		
	}
	
	String seq;
	
	@FXML
	void playMusic() throws IOException {
		
		Parser parse = new Parser(mvc.getMusicXML());
		String instrument = parse.getInstrument();
		ArrayList<Measure> measures = parse.getMeasures();
		
		System.out.println("Tempo: " + tempo);
		System.out.println("instrument: " + instrument);
		
		if(instrument.equals("guitar") || instrument.equals("bass")) {
			
			GuitarBass(parse, instrument);
			
		}
		
		if(instrument.equals("drumset")) {
			
			Drum(parse,instrument);
			
		}

	}
	
	private String finalString;
	
	void GuitarBass(Parser parse, String instrument) throws IOException {

		ArrayList<Measure> measures = parse.getMeasures();
		ArrayList<String> measuresarray = new ArrayList<>(); // split measures into array

		for (int i = 0; i < parse.getNumMeasures(); i++) { // go through every measure

			String measure = "";

			ArrayList<Note> notesInMeasure = measures.get(i).getNotes();

			for (int j = 0; j < measures.get(i).getNumNotes(); j++) { // go through all notes in specific measure

				Pitch pitch = measures.get(i).getNotes().get(j).getPitch();
				String altervalue = "";
				char step = pitch.getStep();
				int alter = pitch.getAlter();
				int octave = pitch.getOctave();
				char type = measures.get(i).getNotes().get(j).getType();

				if (alter != 0) {
					if (alter == 1) {
						altervalue = "#"; // sharp accidental
					}
					if (alter == -1) {
						altervalue = "b"; // flat accidental
					}
				} else {
					altervalue = "";
				}

				if (measures.get(i).getNotes().get(j).getDuration() == 0) { // if note is a grace note
					measure += step + altervalue + octave + "o-";
					measure += " ";
					measure += step + altervalue + octave + "-o";
				} else {
					measure += step + altervalue + octave + type;
				}

				if (measures.get(i).getNumNotes() - j != 1) {
					if (measures.get(i).getNotes().get(j + 1).isChord()) { // if next note is also part of the chord
						measure += "+";
					} else {
						measure += " "; // add a space to split up notes
					}
				} else { // add the tie thing around here i think
					
					
					
				}

			}

			if (parse.getNumMeasures() - i != 1) {
				measure += " | "; // add space between notes to indicate measures
			}
			
			measuresarray.add(measure);

		}

		String finalString = "T" + tempo + " V0 I[" + instrument + "] ";
		
		for (int i = 0; i < measuresarray.size(); i++) {

			finalString += measuresarray.get(i);

		}
	
		setSeq(finalString);
		
		new Thread(() -> {
            player.play(getSeq());
        }).start();
		
	}
	
	void Drum(Parser parse, String instrument) throws IOException {
		
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

		ArrayList<Measure> measures = parse.getMeasures();
		ArrayList<String> measuresarray = new ArrayList<>(); // split measures into array

		for (int i = 0; i < parse.getNumMeasures(); i++) { // go through every measure

			String measure = "";
			ArrayList<Note> notesInMeasure = measures.get(i).getNotes();

			for (int j = 0; j < measures.get(i).getNumNotes(); j++) { // go through all notes in specific measure

				String instrumentID = measures.get(i).getNotes().get(j).getInstrumentID();
				char type = measures.get(i).getNotes().get(j).getType();
				
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

				if (measures.get(i).getNotes().get(j).getDuration() == 0) { // if note is a grace note
					measure += instrumentID + "o-";
					measure += " ";
					measure += instrumentID + "-o";
				} else {
					measure += instrumentID + type;
				}

				if (measures.get(i).getNumNotes() - j != 1) {
					if (measures.get(i).getNotes().get(j + 1).isChord()) { // if next note is also part of the chord
						measure += "+";
					} else {
						measure += " "; // add a space to split up notes
					}
				} else { // add the tie thing around here i think
					
					
					
				}

			}

			if (parse.getNumMeasures() - i != 1) {
				measure += "| "; // add space between notes to indicate measures
			}
			
			measuresarray.add(measure);

		}

		String finalString = "T" + tempo + " V9 ";

		for (int i = 0; i < measuresarray.size(); i++) {

			finalString += measuresarray.get(i);

		}

		setSeq(finalString);
		
		new Thread(() -> {
            player.play(getSeq());
        }).start();
		
	}
	
	public void setSeq(String sequence) {
		
		seq = sequence;
		
	}
	
	public String getSeq() {
		
		System.out.println("Sequence: " + seq);
		return seq;
		
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
				//Draw the note
				if (x < this.pane.getMaxWidth()) {
					new DrawNotes(pane, x, y + yStaff, note, p.getInstrument());
					placeSheetLines(0, p.getInstrument());
					clef(p.getMeasures().get(0).getAttributes().getClef().getSign(), 6, 18+yStaff, p.getInstrument());
					timeSignature(p.getMeasures().get(0).getAttributes().getTime().getBeats(), p.getMeasures().get(0).getAttributes().getTime().getBeatType(), 35, 28+yStaff, p.getInstrument());
				}
				else {
					x = 100.0;
					yStaff += staffSpacing;
					new DrawNotes(pane, x, y + yStaff, note, p.getInstrument());
					drawMeasureNumber(yStaff, p.getMeasures().get(i).getMeasureNumber());
					placeSheetLines(yStaff, p.getInstrument());
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