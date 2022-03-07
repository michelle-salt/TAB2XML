package GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import utility.Settings;

public class SaveMXLController extends Application {

	private MainViewController mvc;
	private static Window convertWindow = new Stage();

	@FXML private TextField titleField;
	@FXML private TextField artistField;
	@FXML private TextField fileNameField;

	public void setMainViewController(MainViewController mvcInput) {
		mvc = mvcInput;
	}

	public void initialize() {
		Settings s = Settings.getInstance();
		titleField.setText(s.title);
		artistField.setText(s.artist);
	}

	@FXML
	private void saveButtonClicked() {
		if (!titleField.getText().isBlank())
			Settings.getInstance().title = titleField.getText();
		if (!artistField.getText().isBlank())
			Settings.getInstance().artist = artistField.getText();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As");

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MusicXML files", "*.musicxml", "*.xml", "*.mxl");
		fileChooser.getExtensionFilters().add(extFilter);

		File initialDir = new File(Settings.getInstance().outputFolder);
		String initialName = null;
		if (!fileNameField.getText().isBlank() && fileNameField.getText().length()<50)
			initialName = fileNameField.getText().strip();

		if (mvc.saveFile != null) {
			if (initialName == null) {
				String name = mvc.saveFile.getName();
				if(name.contains("."))
					name = name.substring(0, name.lastIndexOf('.'));
				initialName = name;
			}
			File parentDir = new File(mvc.saveFile.getParent());
			if (parentDir.exists())
				initialDir = parentDir;
		}
		if (initialName != null)
			fileChooser.setInitialFileName(initialName);

		if (!(initialDir.exists() && initialDir.canRead()))
			initialDir = new File(System.getProperty("user.home"));

		fileChooser.setInitialDirectory(initialDir);

		File file = fileChooser.showSaveDialog(convertWindow);

		if (file != null) {
			mvc.converter.saveMusicXMLFile(file);
			mvc.saveFile = file;
			cancelButtonClicked();
		}
		//		FileChooser fileChooser = new FileChooser();
		//		File file = fileChooser.showSaveDialog(convertWindow);
		//		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
		//		fileChooser.getExtensionFilters().add(extFilter);
		//
		//		Document document = new Document();
		//		if (file != null){
		//			try {
		//				PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("sample.pdf"));
		//				document.open();
		//				document.add(new Paragraph("This is a sample pdf run."));
		//				document.close();
		//				pdfWriter.close();
		//				System.out.println("PDF file can now be found under src/main/resources/org.openjfx");
		//			}
		//			catch (DocumentException e) {
		//				e.printStackTrace();
		//			} 
		//			catch (FileNotFoundException e) {
		//				e.printStackTrace();
		//			}
		//		}
	}

	@FXML
	private void cancelButtonClicked()  {
		mvc.convertWindow.hide();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {}
}