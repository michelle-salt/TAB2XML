package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.*;

import musicxml.parsing.*;

class TimeModificationTest {

	protected Parser capricho;
	@BeforeEach
	void setUp() throws Exception {
		//Get the OS
        String OS = System.getProperty("os.name").toLowerCase();
        //Get the filepath of all MusicXML files, based on the device's OS
        String filePath = new File("").getAbsolutePath();
        //Insert "\\" in path
        if (OS.contains("win")) {
            filePath = filePath.concat("\\src\\test\\resources\\musicXMLFiles\\");
        }
        //Assumed to be unix (mac or linux)
        //Insert "/" in path
        else {
            filePath = filePath.concat("/src/test/resources/musicXMLFiles/");
        }
        capricho = new Parser(Files.readString(Paths.get(filePath.concat("capricho.txt"))));
	}
	
	@Test
	public void testGetActual() {
		//Default
		assertEquals(-1, this.capricho.getMeasures().get(9).getNotes().get(0).getTimeModification().getActualNotes());
		//Actual
		assertEquals(3, this.capricho.getMeasures().get(10).getNotes().get(0).getTimeModification().getActualNotes());
	}
	
	@Test
	public void testGetNormal() {
		//Default
		assertEquals(-1, this.capricho.getMeasures().get(9).getNotes().get(0).getTimeModification().getNormalNotes());
		//Actual
		assertEquals(2, this.capricho.getMeasures().get(10).getNotes().get(0).getTimeModification().getNormalNotes());
	}

}
