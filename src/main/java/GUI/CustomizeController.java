package GUI;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class CustomizeController {
    private PreviewSheetController mvc;
    
    @FXML private RadioButton leftRadio;
    @FXML private RadioButton justRadio;
    
    @FXML TextField noteSpacingField;
    @FXML TextField staffSpacingField;

	public CustomizeController() {}
	
    public void setPreviewSheetController(PreviewSheetController mvcInput) {
    	mvc = mvcInput;
    }
    
    @FXML
    private void initialize() {
    	ToggleGroup group = new ToggleGroup();
    	leftRadio.setToggleGroup(group);
    	justRadio.setToggleGroup(group);
    }
    
    @FXML
    public void applyButton() throws IOException {
    	mvc.setNoteSpacing(Integer.parseInt(noteSpacingField.getText()));
    	mvc.setStaffSpacing(Integer.parseInt(staffSpacingField.getText()));
    	mvc.update();
    	mvc.convertWindow.hide();
    }
 } 