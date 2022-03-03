package GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import musicxml.parsing.Note;

public class DrawNotes {

	@FXML 
	private Pane pane;
	private double x, y;
	private Note note;
	
	public DrawNotes(Pane pane, double x, double y, Note note, String instrument) {
		this.pane = pane;
		this.x = x;
		this.y = y;
		this.note = note;
		
		if (instrument.equals("drum")) {
			this.drawDrumNote();
		}
		//Works for "guitar" or "bass"
		else {
			this.drawGuitarNote();
		}
	}

	//Draws the number of each note, if the instrument is a guitar or bass
	public void drawGuitarNote() {
		String note = Integer.toString(this.note.getFret());
		Text text = new Text(x, y, note);
		pane.getChildren().add(text);
	}
	
	//Draws the notehead of each note, if the instrument is a drum
	public void drawDrumNote() {
//		String note = this.note.getNotehead(); //Will be implemented soon
		Text text = new Text(x, y, "x");
		pane.getChildren().add(text);
	}	
}
