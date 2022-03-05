package parser;

import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import musicxml.parsing.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class MeasureTest {

	protected Parser wikiDrumTab, wikiGuitarTab;
	
	//String filePath = new File("").getAbsolutePath().concat("\\src\\test\\resources\\musicXMLFiles\\");
	
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
	public void testMeasureNumber() {
		//Assertions to verify Measure numbers are being parsed correctly for wikiGuitarTab
		assertEquals(1, this.wikiGuitarTab.getMeasures().get(0).getMeasureNumber());
		assertEquals(2, this.wikiGuitarTab.getMeasures().get(1).getMeasureNumber());
		//Confirm that there are only 2 measures for wikiGuitarTab
		assertEquals(2, this.wikiGuitarTab.getNumMeasures());
		
		//Assertions to verify Measure numbers are being parsed correctly for wikiDrumTab
		assertEquals(1, this.wikiDrumTab.getMeasures().get(0).getMeasureNumber());
		assertEquals(2, this.wikiDrumTab.getMeasures().get(1).getMeasureNumber());
		//Confirm that there are only 2 measures for wikiDrumTab
		assertEquals(2, this.wikiDrumTab.getNumMeasures());
	}
	
	@Test
	public void testGetNumNotes() {
		//Assertions to verify the number of Notes in each Measure are being parsed correctly for wikiGuitarTab
		assertEquals(8, this.wikiGuitarTab.getMeasures().get(0).getNumNotes());
		assertEquals(6, this.wikiGuitarTab.getMeasures().get(1).getNumNotes());
		
		//Assertions to verify the number of Notes in each Measure are being parsed correctly for wikiDrumTab
		assertEquals(12, this.wikiDrumTab.getMeasures().get(0).getNumNotes());
		assertEquals(11, this.wikiDrumTab.getMeasures().get(1).getNumNotes());
	}

}