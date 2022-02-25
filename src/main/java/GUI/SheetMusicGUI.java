package GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import musicxml.parsing.*;

import java.io.IOException;

public class SheetMusicGUI {
	
	@FXML 
    private Pane pane;
    private MainViewController mvc;
	
    public void setMainViewController(MainViewController mvcInput) {
    	mvc = mvcInput;
    }
    
	//Implements the "Save as PDF" button on the SheetMusic GUI
    public void handleSavePDF() {
    	//Implement - Mohammad.
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
    	//Add 12 to the vertical distance after each loop
    	for (int i = 1; i <= 6; i++, y+= 12) {
         	DrawSheetLines sheetLine = new DrawSheetLines(0.0, y, this.pane.getMaxWidth(), y);
         	//Add lines to pane
         	pane.getChildren().add(sheetLine.getLine());
     	}
 	}	
    
    //Update the SheetMusic GUI
    public void update() throws IOException { 	
    	Parser p = new Parser(mvc.converter.getMusicXML());
    	//Dynamically draw the Sheet lines on the SheetMusic GUI
      	for (int i = 1, y = 0; i <= Math.ceil(p.getNumMeasures()/2); i++, y += 100) {
      		placeSheetLines(y);
      		//Dynamically draw bar lines and clef
          	barLines(470, 0+y);
          	clef("TAB", 6, 18+y);
      	}
    }
 }