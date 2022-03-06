package parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import musicxml.parsing.*;

class PitchTest {

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
	public void testStep() {
		
		/*
		 * Checks the pitch value, step value, and alter value of each note in the wikiGuitarTab.txt file
		 */
		
		/*
		 * Measure 1
		 */
		assertEquals("E", String.valueOf(this.wikiGuitarTab.getMeasures().get(0).getNotes().get(0).getPitch().getStep()));
		assertEquals("B", String.valueOf(this.wikiGuitarTab.getMeasures().get(0).getNotes().get(1).getPitch().getStep()));
		assertEquals("E", String.valueOf(this.wikiGuitarTab.getMeasures().get(0).getNotes().get(2).getPitch().getStep()));
		assertEquals("G", String.valueOf(this.wikiGuitarTab.getMeasures().get(0).getNotes().get(3).getPitch().getStep()));
		assertEquals("B", String.valueOf(this.wikiGuitarTab.getMeasures().get(0).getNotes().get(4).getPitch().getStep()));
		assertEquals("E", String.valueOf(this.wikiGuitarTab.getMeasures().get(0).getNotes().get(5).getPitch().getStep()));
		assertEquals("B", String.valueOf(this.wikiGuitarTab.getMeasures().get(0).getNotes().get(6).getPitch().getStep()));
		assertEquals("G", String.valueOf(this.wikiGuitarTab.getMeasures().get(0).getNotes().get(7).getPitch().getStep()));
		
		/*
		 * Measure 2
		 */
		assertEquals("E", String.valueOf(this.wikiGuitarTab.getMeasures().get(1).getNotes().get(0).getPitch().getStep()));
		assertEquals("B", String.valueOf(this.wikiGuitarTab.getMeasures().get(1).getNotes().get(1).getPitch().getStep()));
		assertEquals("G", String.valueOf(this.wikiGuitarTab.getMeasures().get(1).getNotes().get(2).getPitch().getStep()));
		assertEquals("E", String.valueOf(this.wikiGuitarTab.getMeasures().get(1).getNotes().get(3).getPitch().getStep()));
		assertEquals("B", String.valueOf(this.wikiGuitarTab.getMeasures().get(1).getNotes().get(4).getPitch().getStep()));
		assertEquals("E", String.valueOf(this.wikiGuitarTab.getMeasures().get(1).getNotes().get(5).getPitch().getStep()));
		
	}
	
	@Test
	public void testAlter() {
		
		/*
		 * Checks the pitch value, step value, and alter value of each note in the wikiGuitarTab.txt file
		 */
		
		/*
		 * Measure 1
		 */
		assertEquals(0, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(0).getPitch().getAlter());
		assertEquals(0, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(1).getPitch().getAlter());
		assertEquals(0, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(2).getPitch().getAlter());
		assertEquals(1, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(3).getPitch().getAlter());
		assertEquals(0, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(4).getPitch().getAlter());
		assertEquals(0, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(5).getPitch().getAlter());
		assertEquals(0, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(6).getPitch().getAlter());
		assertEquals(1, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(7).getPitch().getAlter());
		
		/*
		 * Measure 2
		 */
		assertEquals(0, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(0).getPitch().getAlter());
		assertEquals(0, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(1).getPitch().getAlter());
		assertEquals(1, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(2).getPitch().getAlter());
		assertEquals(0, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(3).getPitch().getAlter());
		assertEquals(0, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(4).getPitch().getAlter());
		assertEquals(0, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(5).getPitch().getAlter());
	}
	
	@Test
	public void testOctave() {
		
		/*
		 * Checks the pitch value, step value, and alter value of each note in the wikiGuitarTab.txt file
		 */
		
		/*
		 * Measure 1
		 */
		assertEquals(2, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(0).getPitch().getOctave());
		assertEquals(2, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(1).getPitch().getOctave());
		assertEquals(3, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(3).getPitch().getOctave());
		assertEquals(3, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(3).getPitch().getOctave());
		assertEquals(3, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(4).getPitch().getOctave());
		assertEquals(4, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(5).getPitch().getOctave());
		assertEquals(3, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(6).getPitch().getOctave());
		assertEquals(3, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(7).getPitch().getOctave());
		
		/*
		 * Measure 2
		 */
		assertEquals(4, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(0).getPitch().getOctave());
		assertEquals(3, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(1).getPitch().getOctave());
		assertEquals(3, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(2).getPitch().getOctave());
		assertEquals(3, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(3).getPitch().getOctave());
		assertEquals(2, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(4).getPitch().getOctave());
		assertEquals(2, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(5).getPitch().getOctave());
	}
	
}
