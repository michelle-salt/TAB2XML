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
		//In theory, this should work since mvc is initialized when the window pops up
		//In practice, it doesn't
		try {
			leftRadio.setSelected(!mvc.getJustify());
			justRadio.setSelected(mvc.getJustify());
		} catch (NullPointerException e) {
			leftRadio.setSelected(true);
		}
	}

	@FXML
	public <applyButtonPressed> void applyButton() throws IOException {
		if (!staffSpacingField.getText().isEmpty()) {
			mvc.setStaffSpacing(Integer.parseInt(staffSpacingField.getText()));
		}
		if (!noteSpacingField.getText().isEmpty()) {
			mvc.setNoteSpacing(Integer.parseInt(noteSpacingField.getText()));
		}
		if (justRadio.isSelected()) {
			mvc.setJustify(true);
		} else {
			mvc.setJustify(false);
		}
		mvc.update();
		mvc.convertWindow.hide();
	}
} 