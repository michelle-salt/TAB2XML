package GUI;

import java.io.FileInputStream;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import musicxml.parsing.Note;

public class DrawNotes {

	@FXML 
	private Pane pane;
	private double x, y, yStaff;
	private Note note;
	private double noteSpacing;
	private boolean lastNote;

	public DrawNotes(Pane pane, double x, double y, Note note, String instrument, double yStaff, double noteSpacing, boolean lastNote, Note nextNote, double slideDistance) {
		this.pane = pane;
		this.x = x;
		this.y = y;
		this.yStaff = yStaff;
		this.note = note;
		this.noteSpacing = noteSpacing;
		this.lastNote = lastNote;

		//Draw bend if needed
		if (note.getBendAlter() != 0) {
			//Draw curve
			CubicCurve curve1 = new CubicCurve(x, y, x + (noteSpacing/2), y -3, x + noteSpacing - 7, yStaff, x + noteSpacing - 7, yStaff - 5);
		    curve1.setStroke(Color.BLACK);
		    curve1.setStrokeWidth(1);
		    curve1.setFill(null);
		    pane.getChildren().add(curve1);
		    //Write number
		    Text text = new Text(x + noteSpacing - 10, yStaff - 10, Double.toString(note.getBendAlter()/2));
			text.setFont(Font.font("arial", 10));
			pane.getChildren().add(text);
		}
		
		
		//Draw slide if needed
		if (nextNote != null && note.getSlide().getType() != null && note.getSlide().getType().equalsIgnoreCase("start")) {
			if (note.getFret() < nextNote.getFret()) {
				Line l = new Line(x + 9, y + 1, x + slideDistance - 3, y - 7);
				pane.getChildren().add(l);
			} else {
				Line l = new Line(x + 18, y - 8, x + slideDistance - 1, y + 1);
				pane.getChildren().add(l);
			}
		}
		
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
		String note = "";

		if(!this.note.isGraceNote()) {
			note = Integer.toString(this.note.getFret());
			Text text = new Text(x, y, note);
			text.setFont(Font.font("arial", FontWeight.BLACK, 14));
			int width = 12;

			if (this.note.getFret() > 9) {
				width += 8;
			}
			//Draws white rectangle behind notes to prevent line from going through
			Rectangle r = new Rectangle(x-2, y-10, width, 15);
			r.setFill(Color.WHITE);
			pane.getChildren().add(r);
			pane.getChildren().add(text);
		} 
		else {
			note = Integer.toString(this.note.getFret());; 

			Text text = new Text(x + 6, y, note);
			text.setFont(Font.font("arial", FontWeight.BLACK, 9));
			int width = 9;
			//note  ------ note
			QuadCurve quadcurve = new QuadCurve(x+10, y-9.5, ((noteSpacing)/2) + x+6, y-20, x + noteSpacing, y-9.5);
			QuadCurve quadcurve2 = new QuadCurve(x+12, y-9.5, ((noteSpacing)/2) + x+6, y-16, x + noteSpacing - 2, y-9.5);
			quadcurve2.setFill(Color.WHITE);
			if (this.note.getFret() > 9) {
				width += 4;
			}
			//Draws white rectangle behind notes to prevent line from going through
			Rectangle r = new Rectangle(x+5, y-10, width, 10);
			r.setFill(Color.WHITE);
			pane.getChildren().add(r);
			pane.getChildren().add(text);
			pane.getChildren().add(quadcurve);
			pane.getChildren().add(quadcurve2);
		}
		if(this.note.getTied().getStart()) {
			note = Integer.toString(this.note.getFret());; 

			double midX = ((noteSpacing)/2) + x+6, endX = x + noteSpacing + 2;
			if (lastNote) {
				midX = noteSpacing + x+6;
				endX += noteSpacing;
			}

			QuadCurve quadcurve = new QuadCurve(x+6, y-9.5, midX, y-22, endX, y-9.5);
			QuadCurve quadcurve2 = new QuadCurve(x+8, y-9.5, midX, y-18, endX - 2, y-9.5);
			quadcurve2.setFill(Color.WHITE);

			pane.getChildren().add(quadcurve);
			pane.getChildren().add(quadcurve2);
		}
		if(this.note.getSlur().getPlacement() != null) {
			note = Integer.toString(this.note.getFret());; 

			double midX = ((noteSpacing)/2) + x+6, endX = x + noteSpacing + 2;
			if (lastNote) {
				midX = noteSpacing + x+6;
				endX += noteSpacing;
			}

			QuadCurve quadcurve = new QuadCurve(x+6, y-9.5, midX, y-22, endX, y-9.5);
			QuadCurve quadcurve2 = new QuadCurve(x+8, y-9.5, midX, y-18, endX - 2, y-9.5);
			quadcurve2.setFill(Color.WHITE);

			pane.getChildren().add(quadcurve);
			pane.getChildren().add(quadcurve2);
		}
	}

	//Draws the notehead of each note, if the instrument is a drum
	public void drawDrumNote() {
		//Print the "x" if the notehead is an "x"
		if (this.note.getNotehead() != null && this.note.getNotehead().equalsIgnoreCase("x")) {
			Text text = new Text(x, y, "x");
			text.setFont(Font.font("veranda", FontWeight.BLACK, 18));
			pane.getChildren().add(text);
			drawStem(x+9, y-8);
			double dotX = x+14;
			for (int i = 0; i < this.note.getNumDots(); i++) {
				Ellipse dot = new Ellipse(dotX, y-5, 2, 2);
				pane.getChildren().add(dot);
				dotX += 6;
			}

			if(this.note.getTied().getStart()) {

				double midX = ((noteSpacing)/2) + x+6, endX = x + noteSpacing + 2;
				if (lastNote) {
					midX = noteSpacing + x+6;
					endX += noteSpacing;
				}

				QuadCurve quadcurve = new QuadCurve(x+10, y-5.5, midX, y-22, endX, y-5.5);
				QuadCurve quadcurve2 = new QuadCurve(x+12, y-4.5, midX, y-18, endX - 2, y-4.5);
				quadcurve2.setFill(Color.WHITE);

				pane.getChildren().add(quadcurve);
				pane.getChildren().add(quadcurve2);
			}
			if(this.note.getTied().getStart()) {

				double midX = ((noteSpacing)/2) + x+6, endX = x + noteSpacing + 2;
				if (lastNote) {
					midX = noteSpacing + x+6;
					endX += noteSpacing;
				}

				QuadCurve quadcurve = new QuadCurve(x+10, y-5.5, midX, y-22, endX, y-5.5);
				QuadCurve quadcurve2 = new QuadCurve(x+12, y-4.5, midX, y-18, endX - 2, y-4.5);
				quadcurve2.setFill(Color.WHITE);

				pane.getChildren().add(quadcurve);
				pane.getChildren().add(quadcurve2);
			}
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
			//			if (!this.note.isGraceNote())
			drawStem(x+9, y-5);
		}
		if (this.note.isGraceNote() == true) {
			QuadCurve quadcurve = new QuadCurve(x+6, y-9, ((noteSpacing)/2) + x+6, y+10, x + noteSpacing, y-9);
			QuadCurve quadcurve2 = new QuadCurve(x+8, y-9, ((noteSpacing)/2) + x+6, y+4, x + noteSpacing - 2, y-9);
			quadcurve2.setFill(Color.WHITE);
			pane.getChildren().add(quadcurve);
			pane.getChildren().add(quadcurve2);
		}
		y -= 30;
		//Draw tremolo as needed
		if (note.getTremolo().getValue() != 0) {
			Polygon parallelogram = new Polygon(new double[]{(double)x, (double)y, (double)x+15, (double)y-15, (double)x+15, (double)y-7.5, (double)x, (double)y+7.5});
			pane.getChildren().add(parallelogram);
		}
	}	
	public void drawStem(double x, double yStart) {
		double top =  yStaff - 40;
		if (this.note.isGraceNote()) {
			x -= 2;
			yStart -= 1;
			top = yStart - 20;
			//Draw flags
			//Add vertical lines underneath notes
			//			NoteLocation note = noteLocation.get(i);
			double xNew = x, yNew = yStart;
			//Get number of flags needed
			int numFlags = 0;
			switch (this.note.getType()) {
			case 'I':	numFlags = 1;		break;
			case 'S':	numFlags = 2;		break;
			case 'T':	numFlags = 3;		break;
			}

			//Draw flag
			for (int j = 0; j < numFlags; j++) {
				Line flag = new Line(x+7, y-14, x, y-27);
				pane.getChildren().add(flag);
				y += 10;
			}
		}
		Line stem = new Line(x, yStart, x, top);
		pane.getChildren().add(stem);
	}

	public void printWholeNote() {
		printQuarterNote();
		if(this.note.isGraceNote()) {
			Ellipse centre = new Ellipse(x+4, y-5, 1.3, 2);
			centre.setFill(Color.WHITE);
			pane.getChildren().add(centre);
		}
		else {
			Ellipse centre = new Ellipse(x+4, y-5, 2, 3);
			centre.setFill(Color.WHITE);
			pane.getChildren().add(centre);
		}

	}

	public void printHalfNote() {
		printQuarterNote();
		if(this.note.isGraceNote()) {
			Ellipse centre = new Ellipse(x+4, y-5, 3.167, 1.067);
			centre.setFill(Color.WHITE);
			centre.setRotate(330);
			pane.getChildren().add(centre);

		}
		else {
			Ellipse centre = new Ellipse(x+4, y-5, 4.75, 1.6);
			centre.setFill(Color.WHITE);
			centre.setRotate(330);
			pane.getChildren().add(centre);
		}
	}

	public void printQuarterNote() {
		if(this.note.isGraceNote()) {
			Ellipse ellipse = new Ellipse(x+4, y-5, 4, 2.83);
			ellipse.setRotate(320);
			pane.getChildren().add(ellipse);
		}
		else {
			Ellipse ellipse = new Ellipse(x+4, y-5, 6, 4.25);
			ellipse.setRotate(320);
			pane.getChildren().add(ellipse);
		}
		double dotX = x+13;
		for (int i = 0; i < this.note.getNumDots(); i++) {
			Ellipse dot = new Ellipse(dotX, y-5, 2, 2);
			pane.getChildren().add(dot);
			dotX += 6;
		}
		if(this.note.getTied().getStart()) {

			double midX = ((noteSpacing)/2) + x+6, endX = x + noteSpacing + 2;
			if (lastNote) {
				midX = noteSpacing + x+6;
				endX += noteSpacing;
			}

			QuadCurve quadcurve = new QuadCurve(x+6, y-0.5, midX, y+22, endX, y-0.5);
			QuadCurve quadcurve2 = new QuadCurve(x+8, y-0.5, midX, y+18, endX - 2, y-0.5);
			quadcurve2.setFill(Color.WHITE);

			pane.getChildren().add(quadcurve);
			pane.getChildren().add(quadcurve2);
		}
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


