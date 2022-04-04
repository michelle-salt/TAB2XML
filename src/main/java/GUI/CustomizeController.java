package GUI;

import java.io.IOException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

	BooleanProperty applyButtonPressed = new SimpleBooleanProperty(false);

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
	public <applyButtonPressed> void applyButton() throws IOException {
		if (noteSpacingField.getText().isEmpty() && !staffSpacingField.getText().isEmpty()) {
			mvc.setStaffSpacing(Integer.parseInt(staffSpacingField.getText()));
			mvc.update();
		}
		else if (staffSpacingField.getText().isEmpty() && !noteSpacingField.getText().isEmpty()) {
			mvc.setNoteSpacing(Integer.parseInt(noteSpacingField.getText()));
			mvc.update();
		}
		else if (!staffSpacingField.getText().isEmpty() && !noteSpacingField.getText().isEmpty()) {
			mvc.setNoteSpacing(Integer.parseInt(noteSpacingField.getText()));
			mvc.setStaffSpacing(Integer.parseInt(staffSpacingField.getText()));
			mvc.update();
		}
		else { /* default */
			mvc.setNoteSpacing(25);
			mvc.setStaffSpacing(100);
			mvc.update();
		}
		mvc.convertWindow.hide();
	}
} 