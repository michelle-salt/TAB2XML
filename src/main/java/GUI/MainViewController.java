package GUI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.xml.parsers.ParserConfigurationException;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;

import converter.Converter;
import converter.measure.TabMeasure;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import musicxml.parsing.Measure;
import musicxml.parsing.Note;
//import musicxml.parsing.GuitarNote;
import musicxml.parsing.Parser;
import musicxml.parsing.Pitch;
import nu.xom.ParsingException;
import utility.Range;
import utility.Settings;

import org.jfugue.player.Player;
import org.staccato.StaccatoParserListener;
import org.xml.sax.SAXException;
import org.jfugue.integration.MusicXmlParser;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.midi.MidiParser;
import org.jfugue.midi.MidiParserListener;
import org.jfugue.pattern.Pattern;

import converter.Converter;

public class MainViewController extends Application {

	private Preferences prefs;
	public static ExecutorService executor = Executors.newSingleThreadExecutor();
	public File saveFile;
	private static boolean isEditingSavedFile;

	public Window convertWindow;
	public Window settingsWindow;

	public Highlighter highlighter;
	public Converter converter;

	@FXML
	Label mainViewState;
	@FXML
	TextField instrumentMode;

	@FXML
	public CodeArea mainText;

	@FXML
	TextField gotoMeasureField;
	@FXML
	BorderPane borderPane;
	@FXML
	Button saveTabButton;
	@FXML
	Button saveMXLButton;
	@FXML
	Button showMXLButton;
	@FXML
	Button previewButton;
	@FXML
	Button goToline;
	@FXML
	ComboBox<String> cmbScoreType;

	@FXML
	Button playMusic;

	public MainViewController() {
		Settings s = Settings.getInstance();
		prefs = Preferences.userRoot();
		s.inputFolder = prefs.get("inputFolder", System.getProperty("user.home"));
		s.outputFolder = prefs.get("outputFolder", System.getProperty("user.home"));
		s.tsNum = Integer.parseInt(prefs.get("tsNum", "4"));
		s.tsDen = Integer.parseInt(prefs.get("tsDen", "4"));
		s.errorSensitivity = Integer.parseInt(prefs.get("errorSensitivity", "4"));
	}

	@FXML
	public void initialize() {
		mainText.setParagraphGraphicFactory(LineNumberFactory.get(mainText));
		converter = new Converter(this);
		highlighter = new Highlighter(this, converter);
		listenforTextAreaChanges();
	}

	@FXML
	private void handleCurrentSongSettings() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getClassLoader().getResource("GUI/currentSongSettingsWindow.fxml"));
			root = loader.load();
			CurrentSongSettingsWindowController controller = loader.getController();
			controller.setMainViewController(this);
			settingsWindow = this.openNewWindow(root, "Current Song Settings");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}

	@FXML
	private void handleSystemDefaultSettings() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getClassLoader().getResource("GUI/systemDefaultSettingsWindow.fxml"));
			root = loader.load();
			SystemDefaultSettingsWindowController controller = loader.getController();
			controller.setMainViewController(this);
			settingsWindow = this.openNewWindow(root, "System Default Settings");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}

	@FXML
	private void handleNew() {
		boolean userOkToGoAhead = promptSave();
		if (!userOkToGoAhead)
			return;
		this.mainText.clear();
		instrumentMode.setText("None");
		isEditingSavedFile = false;
	}

	@FXML
	private void handleOpen() {
		boolean userOkToGoAhead = promptSave();
		if (!userOkToGoAhead)
			return;

		String startFolder = prefs.get("inputFolder", System.getProperty("user.home"));
		File openDirectory;
		if (saveFile != null && saveFile.canRead()) {
			openDirectory = new File(saveFile.getParent());
		} else
			openDirectory = new File(startFolder);

		if (!openDirectory.canRead()) {
			openDirectory = new File("c:/");
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		fileChooser.setInitialDirectory(openDirectory);
		File openedFile = fileChooser.showOpenDialog(MainApp.STAGE);
		if (openedFile == null)
			return;
		if (openedFile.exists()) {
			try {
				String newText = Files.readString(Path.of(openedFile.getAbsolutePath())).replace("\r\n", "\n");
				mainText.replaceText(new IndexRange(0, mainText.getText().length()), newText);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		saveFile = openedFile;
		isEditingSavedFile = true;

	}

	@FXML
	private boolean handleSaveAs() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As");
		fileChooser.setInitialDirectory(new File(Settings.getInstance().outputFolder));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
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

	@FXML
	private boolean handleSave() {
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

	private boolean promptSave() {

		// we don't care about overwriting a blank file. If file is blank, we are ok to
		// go. it doesn't matter if it is saved or not
		if (mainText.getText().isBlank())
			return true;

		try {
			if (saveFile != null && Files.readString(Path.of(saveFile.getAbsolutePath())).replace("\r\n", "\n")
					.equals(mainText.getText()))
				return true; // if file didn't change, we are ok to go. no need to save anything, no chance
								// of overwriting.
		} catch (Exception e) {
			e.printStackTrace();
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Unsaved file");
		alert.setHeaderText("This document is unsaved and will be overwritten. Do you want to save it first?");
		alert.setContentText("Choose your option.");

		ButtonType buttonTypeSave = new ButtonType("Save");
		ButtonType buttonTypeOverwrite = new ButtonType("Overwrite");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeOverwrite, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();

		boolean userOkToGoAhead = false;
		if (result.get() == buttonTypeSave) {
			boolean saved;
			if (isEditingSavedFile) {
				saved = handleSave();
			} else {
				saved = handleSaveAs();
			}
			if (saved)
				userOkToGoAhead = true;
		} else if (result.get() == buttonTypeOverwrite) {
			// ... user chose "Override". we are good to go ahead
			userOkToGoAhead = true;
		}
		// if user chose "cancel", userOkToGoAhead is still false. we are ok.
		return userOkToGoAhead;
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
	private void saveTabButtonHandle() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/convertWindow.fxml"));
			root = loader.load();
			SaveMXLController controller = loader.getController();
			controller.setMainViewController(this);
			convertWindow = this.openNewWindow(root, "ConversionOptions");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}

	@FXML
	void saveMXLButtonHandle() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/saveMXLWindow.fxml"));
			root = loader.load();
			SaveMXLController controller = loader.getController();
			controller.setMainViewController(this);
			convertWindow = this.openNewWindow(root, "Save MusicXML");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}

	@FXML
	private void showMXLButtonHandle() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/showMXL.fxml"));
			root = loader.load();
			ShowMXLController controller = loader.getController();
			controller.setMainViewController(this);
			controller.update();
			convertWindow = this.openNewWindow(root, "MusicXML output");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}

	@FXML
	private void previewButtonHandle() throws IOException, SAXException {
		// converter.getMusicXML() returns the MusicXML output as a String
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/sheetMusicGUI.fxml"));
			root = loader.load();
			SheetMusicGUI controller = loader.getController();
			controller.setMainViewController(this);
			controller.update();
			convertWindow = this.openNewWindow(root, "Preview Sheet Music");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}

	@FXML
	private void playMusic() throws IOException {
		
		Parser parse = new Parser(converter.getMusicXML());
		String instrument = parse.getInstrument();
		String string = "T50 V0 I[" + instrument + "] ";
		ArrayList<Measure> measures = parse.getMeasures();

		for (int i = 0; i < parse.getNumMeasures(); i++) { // go through every measure

			ArrayList<Note> notesInMeasure = measures.get(i).getNotes();

			for (int j = 0; j < measures.get(i).getNumNotes(); j++) { // go through all note sin specific measure

				Pitch pitch = measures.get(i).getNotes().get(j).getPitch();
				String altervalue = "";
				char step = pitch.getStep();
				int alter = pitch.getAlter();
				int octave = pitch.getOctave();
				char type = measures.get(i).getNotes().get(j).getType();

				if(alter != 0) {
					
					if(alter == 1) {
						
						altervalue = "#"; // insert sharp accidental
						
					}
					
					if(alter == -1) {
						
						altervalue = "b"; // insert flat accidental
						
					}
					
				}else {
					
					altervalue = "";
					
				}
				
				if(measures.get(i).getNotes().get(j).getDuration() == 0) { // if note is a grace note
					
					string += step + altervalue + octave + "o-";
					
					string += " ";
					
					string += step + altervalue + octave + "-o";
					
				}else {
						
						string += step + altervalue + octave + type;				
					
				}

				if (measures.get(i).getNumNotes() - j != 1) {

					if (measures.get(i).getNotes().get(j + 1).isChord()) { //  if next note is also part of the chord
						
						string += "+";

					}else{
						
						string += " "; // add a space to split up notes
						
					}
					
				}else { // add the tie thing around here i think
					
					string += " ";
					
				}
				
				
				
				
				
			}
			
			if(parse.getNumMeasures() - i != 1) {
				
				string += " | "; // add space between notes to indicate measures
				
			}
		}
		
		System.out.println(string);
		Player player = new Player();
		player.play(string);

	}

	public void refresh() {
		mainText.replaceText(new IndexRange(0, mainText.getText().length()), mainText.getText() + " ");
	}

	@FXML
	private void handleGotoMeasure() {
		int measureNumber = Integer.parseInt(gotoMeasureField.getText());
		if (!goToMeasure(measureNumber)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Measure " + measureNumber + " could not be found.");
			alert.setHeaderText(null);
			alert.show();
		}
	}

	private boolean goToMeasure(int measureCount) {
		TabMeasure measure = converter.getScore().getMeasure(measureCount);
		if (measure == null)
			return false;
		List<Range> linePositions = measure.getRanges();
		int pos = linePositions.get(0).getStart();
		mainText.moveTo(pos);
		mainText.requestFollowCaret();
		mainText.requestFocus();
		return true;
	}

	public void listenforTextAreaChanges() {
		// Subscription cleanupWhenDone =
		mainText.multiPlainChanges().successionEnds(Duration.ofMillis(350)).supplyTask(this::update)
				.awaitLatest(mainText.multiPlainChanges()).filterMap(t -> {
					if (t.isSuccess()) {
						return Optional.of(t.get());
					} else {
						t.getFailure().printStackTrace();
						return Optional.empty();
					}
				}).subscribe(highlighter::applyHighlighting);
	}

	public Task<StyleSpans<Collection<String>>> update() {
		String text = mainText.getText();

		Task<StyleSpans<Collection<String>>> task = new Task<>() {
			@Override
			protected StyleSpans<Collection<String>> call() {
				converter.update();

				if (converter.getScore().getTabSectionList().isEmpty()) {
					saveMXLButton.setDisable(true);
					previewButton.setDisable(true);
					showMXLButton.setDisable(true);
					playMusic.setDisable(true);
				} else {
					saveMXLButton.setDisable(false);
					previewButton.setDisable(false);
					showMXLButton.setDisable(false);
					playMusic.setDisable(false);
				}
				return highlighter.computeHighlighting(text);
			}
		};
		executor.execute(task);
		task.isDone();
		return task;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

	}
}