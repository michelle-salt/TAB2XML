package GUI;

import javafx.fxml.FXML;

public class SheetMusicGUI {
    private MainViewController mvc;

	public SheetMusicGUI() {
	}

	@FXML 
	public void initialize() {
	}

    public void setMainViewController(MainViewController mvcInput) {
    	mvc = mvcInput;
    }
    
    public void SavePDFHandle() {
     	System.out.println("SavePDF button is clicked!");
     }
     
     public void handleGoToMeasure() {
     }
 }