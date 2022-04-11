package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.*;

import musicxml.parsing.*;

class TimeTest {

	protected Parser capricho, ex38, money, parabola;
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
//        capricho = new Parser(Files.readString(Paths.get(filePath.concat("capricho.txt"))));
//        ex38 = new Parser(Files.readString(Paths.get(filePath.concat("ex38.txt"))));
        money = new Parser(Files.readString(Paths.get(filePath.concat("money.txt"))));
        //Parabola breaks for some reason
//        parabola = new Parser(Files.readString(Paths.get(filePath.concat("parabola.txt"))));
	}
	
	@Test
	void testBeats() {
		//Verify that the value for the first measure is correct
		assertEquals(7, money.getMeasures().get(0).getAttributes().getTime().getBeats());
		//Verify that the value for all other measures don't exist and default to 0
		for (int i = 1; i < money.getNumMeasures(); i++) {
			assertEquals(-1, money.getMeasures().get(i).getAttributes().getTime().getBeats());
		}
//		assertFalse(false);
	}

	@Test
	void testBeatType() {
		//Verify that the value for the first measure is correct
		assertEquals(4, money.getMeasures().get(0).getAttributes().getTime().getBeatType());
		//Verify that the value for all other measures don't exist and default to 0
		for (int i = 1; i < money.getNumMeasures(); i++) {
			assertEquals(-1, money.getMeasures().get(i).getAttributes().getTime().getBeatType());
		}
	}
}
