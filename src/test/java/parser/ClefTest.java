package parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import musicxml.parsing.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class ClefTest {
	protected Parser wikiDrumTab, wikiGuitarTab;
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
		wikiDrumTab = new Parser(Files.readString(Paths.get(filePath.concat("wikiDrumTab.txt"))));
		wikiGuitarTab = new Parser(Files.readString(Paths.get(filePath.concat("wikiGuitarTab.txt"))));
	}

	@Test
	public void testGetSign() {
		//Make sure there are the same number of measures (since each measure has its own clef)
		assertEquals(2, wikiGuitarTab.getNumMeasures());
		assertEquals(2, wikiDrumTab.getNumMeasures());

		//Verify that the signs for measure 1 are correct
		assertEquals("TAB", wikiGuitarTab.getMeasures().get(0).getAttributes().getClef().getSign());
		assertEquals("percussion", wikiDrumTab.getMeasures().get(0).getAttributes().getClef().getSign());

		//Verify that the signs for measure 2 are correct
		assertNull(wikiGuitarTab.getMeasures().get(1).getAttributes().getClef().getSign());
		assertNull(wikiDrumTab.getMeasures().get(1).getAttributes().getClef().getSign());
	}
	
	@Test
	public void testGetLineValues() {
		//Verify that the line values for measure 1 are correct
		assertEquals(5, wikiGuitarTab.getMeasures().get(0).getAttributes().getClef().getLineValues());
		assertEquals(2, wikiDrumTab.getMeasures().get(0).getAttributes().getClef().getLineValues());

		//Verify that the line values for measure 2 are correct
		assertEquals(0, wikiGuitarTab.getMeasures().get(1).getAttributes().getClef().getLineValues());
		assertEquals(0, wikiDrumTab.getMeasures().get(1).getAttributes().getClef().getLineValues());
	}
}
