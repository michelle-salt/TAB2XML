package parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import musicxml.parsing.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class ParserTest {

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
	public void testGetNumMeasures() {
		assertEquals(2, this.wikiGuitarTab.getNumMeasures());
		assertEquals(2, this.wikiDrumTab.getNumMeasures());
	}
	
	@Test
	public void testGetInstrument() {
		assertEquals("guitar", this.wikiGuitarTab.getInstrument());
		assertEquals("drumset", this.wikiDrumTab.getInstrument());
	}
	
	@Test
	public void testGetArtist() {
		assertEquals(null, this.wikiGuitarTab.getArtist());
		assertEquals(null, this.wikiDrumTab.getArtist());
	}
	
	@Test
	public void testGetTitle() {
		assertEquals(null, this.wikiGuitarTab.getTitle());
		assertEquals(null, this.wikiDrumTab.getTitle());
	}

}
