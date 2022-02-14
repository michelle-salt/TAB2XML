package GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.io.IOException;

public class SheetMusicGUI {
	
    public void setMainViewController(MainViewController mvcInput) {
    }
    
    @FXML 
    private Pane pane;
    
	//Implements the "Save as PDF" button on the SheetMusic GUI
    public void handleSavePDF() {
    	//Implement - Mohammad
    }
    
    //Implements the "Go To Measure" button on the SheetMusic GUI
    public void handleGotoMeasure() {
    	//Implement - Duaa
    }
     
    //Draw the Bars to mark the end of a Measure
    //Must implement double bar and end bars soon
    private void barLines(double x, double y) {
    	//Draw two bar lines
    	Line middleBar = new Line();
      	middleBar.setStartX(x);
      	middleBar.setEndX(x);
      	middleBar.setStartY(y);
      	middleBar.setEndY(y + 60);
      	
      	Line endBar = new Line();
      	endBar.setStartX(x + 450);
      	endBar.setEndX(x + 450);
      	endBar.setStartY(y);
      	endBar.setEndY(y + 60);
      	
      	//Add bars to pane
      	pane.getChildren().add(middleBar);
      	pane.getChildren().add(endBar);
    }
     
    //Draw the Clef at the left-end of the Staff
    private void clef(String symbol, double x, double y) {
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
    
    //Draws 6 Sheet lines and places them on the GUI
    public void placeSheetLines(double y) {	
     	//Loop through each line
    	//Add 10 to the vertical distance after each loop
    	for (int i = 1; i <= 6; i++, y+= 12) {
         	DrawSheetLines sheetLine = new DrawSheetLines(0.0, y, this.pane.getMaxWidth(), y);
         	//Add lines to pane
         	pane.getChildren().add(sheetLine.getLine());
     	}
 	}	
    
    //Update the SheetMusic GUI
    public void update() throws IOException {
    	//HardCoded 4 sets of Sheet lines on the SheetMusic GUI, working on changing to dynamic
      	for (int i = 1, y = 0; i <= 4; i++, y += 100) {
      		placeSheetLines(y);
      	}
      	//Draw bar lines and clef
      	//Clef to be updated dynamically later
      	barLines(470, 0);
      	clef("TAB", 6, 18);
    }
 }