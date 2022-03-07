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

public class SaveController extends Application {

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
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(convertWindow);
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
		fileChooser.getExtensionFilters().add(extFilter);

		Document document = new Document();
		if (file != null){
			try {
				String filename = "sample.pdf";
				PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filename));
				document.open();
				document.add(new Paragraph("This is a sample pdf run."));
				document.close();
				pdfWriter.close();
				System.out.println(filename + " has been succesfully created!");
			}
			catch (DocumentException e) {
				e.printStackTrace();
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
    }

    @FXML
    private void cancelButtonClicked()  {
    	mvc.convertWindow.hide();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {}
}