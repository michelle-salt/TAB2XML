package GUI;

import javafx.fxml.FXML;
import javafx.scene.shape.Line;

public class DrawSheetLines extends Line {

	@FXML 
	private Line line = new Line();
	
 	public DrawSheetLines(double startX, double startY, double endX, double endY) {
     	this.line.setStartX(startX); 
     	this.line.setStartY(startY);         
     	this.line.setEndX(endX); 
     	this.line.setEndY(endY);
 	}

 	//Setters/Getters for all attributes
 	public Line getLine() {
 		return line;
 	}
 	
 	public void setLine(Line line) {
 		this.line = line;
 	}
}
