package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class CustomizeController {
    private PreviewSheetController mvc;
    
    @FXML
    private RadioButton leftRadio;
    @FXML
    private RadioButton justRadio;

	public CustomizeController() {
	}
	

    public void setPreviewSheetController(PreviewSheetController mvcInput) {
    	mvc = mvcInput;
    }
    
    @FXML
    private void initialize() {
    	ToggleGroup group = new ToggleGroup();
    	leftRadio.setToggleGroup(group);
    	justRadio.setToggleGroup(group);
    }
    
    public void applyButton() {
    	
    }
 } 