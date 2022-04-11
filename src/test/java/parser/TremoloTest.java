package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.*;

import musicxml.parsing.*;

class TremoloTest {

	protected Parser parabola;
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
		parabola = new Parser(Files.readString(Paths.get(filePath.concat("parabola.txt"))));
	}
	
	@Test
	public void testGetType() {
		//Default
		assertNull(this.parabola.getMeasures().get(43).getNotes().get(12).getTremolo().getType());
		//Actual
		assertEquals("single", this.parabola.getMeasures().get(43).getNotes().get(14).getTremolo().getType());
	}
	
	@Test
	public void testGetValue() {
		//Default
		assertEquals(0, this.parabola.getMeasures().get(43).getNotes().get(12).getTremolo().getValue());
		//Actual
		assertEquals(1, this.parabola.getMeasures().get(43).getNotes().get(14).getTremolo().getValue());
	}

}
