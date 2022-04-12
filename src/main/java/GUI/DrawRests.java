package GUI;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import musicxml.parsing.Note;

public class DrawRests {
	public DrawRests(Pane pane, double x, double y, Note note, double yStaff) {
		if (note.getType() == 'H') {
			Rectangle r = new Rectangle(x-27, yStaff + 28, 10, 5);
			pane.getChildren().add(r);
		} else if (note.getType() == 'Q') {
			Line top = new Line(x-5, yStaff+5, x+3, yStaff+17);
			Line middle = new Line(x+3, yStaff+17, x-3.5, yStaff+25);
			Line bottom = new Line(x-3.5, yStaff+25, x+3, yStaff+33);
			
			top.setStrokeWidth(3);
			middle.setStrokeWidth(3);
			bottom.setStrokeWidth(3);
			
			Text c = new Text(x-7, yStaff+43, "c");
			c.setFont(Font.font("arial", FontWeight.BLACK, FontPosture.ITALIC, 20));
			
			pane.getChildren().add(top);
			pane.getChildren().add(middle);
			pane.getChildren().add(bottom);
			pane.getChildren().add(c);
		}
		//No other rests are given
	}
}
