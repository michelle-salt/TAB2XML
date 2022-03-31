package GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
		
		if (instrument.equals("drumset")) {
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
		//Print the "x" if the notehead is an "x"
		if (this.note.getNotehead() != null && this.note.getNotehead().equalsIgnoreCase("x")) {
			Text text = new Text(x, y, "x");
			text.setFont(Font.font("veranda", FontWeight.BLACK, 18));
			pane.getChildren().add(text);
			drawStem(x+9, y-8, y-35);
		} 
		//Print the type of note otherwise
		else {
			//More to be implemented later
			switch (this.note.getType()) {
			case 'W':	printWholeNote();			break;
			case 'H':	printHalfNote();			break;
			case 'Q':	printQuarterNote();			break;
			case 'I':	printEighthPlusNote(1);		break;
			case 'S':	printEighthPlusNote(2);		break;
			case 'T':	printEighthPlusNote(3);		break;
			case 'X':	printEighthPlusNote(4);		break;
			case 'O':	printEighthPlusNote(5);		break;
			case 'U':	printEighthPlusNote(6);		break;
			case 'R':	printEighthPlusNote(7);		break;
			case 'C':	printEighthPlusNote(8);		break;
			}
			//Draw the stem for every note
			//Will be changed later
			drawStem(x+9, y-5, y-35);
		}
	}	
	
	public void drawStem(double x, double yStart, double yEnd) {
		Line stem = new Line(x, yStart, x, yEnd);
		pane.getChildren().add(stem);
	}
	
	public void printWholeNote() {
		printQuarterNote();
		Ellipse centre = new Ellipse(x+4, y-5, 2, 3);
		centre.setFill(Color.WHITE);
		pane.getChildren().add(centre);
	}
	
	public void printHalfNote() {
		printQuarterNote();
		Ellipse centre = new Ellipse(x+4, y-5, 4.75, 1.6);
		centre.setFill(Color.WHITE);
		centre.setRotate(330);
		pane.getChildren().add(centre);
	}
	
	public void printQuarterNote() {
		Ellipse ellipse = new Ellipse(x+4, y-5, 6, 4.25);
		ellipse.setRotate(320);
		pane.getChildren().add(ellipse);
	}
	
	/* 
	 * Draws the rest of the notes
	 * The only difference between these notes is the number of flags, 
	 * 		so they have been combined into one method
	 */
	public void printEighthPlusNote(int numFlags) {
		printQuarterNote();
//		drawFlag(numFlags);
	}
	
	//For test case purposes
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public Pane getPane() {
		return this.pane;
	}
	
	public Note getNote() {
		return this.note;
	}
}
	

