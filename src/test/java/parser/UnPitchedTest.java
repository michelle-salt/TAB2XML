package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import musicxml.parsing.Parser;

class UnPitchedTest {
	
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
	public void testGetStep() {
		
		/*
		 * Checks the step value, and alter value of each note in the wikiDrumTab.musicxml file
		 */
		
		/*
		 * Measure 1
		 */
		assertEquals("A", String.valueOf(this.wikiDrumTab.getMeasures().get(0).getNotes().get(0).getUnpitched().getStep()));
		assertEquals("F", String.valueOf(this.wikiDrumTab.getMeasures().get(0).getNotes().get(1).getUnpitched().getStep()));
		assertEquals("G", String.valueOf(this.wikiDrumTab.getMeasures().get(0).getNotes().get(2).getUnpitched().getStep()));
		assertEquals("G", String.valueOf(this.wikiDrumTab.getMeasures().get(0).getNotes().get(3).getUnpitched().getStep()));
		assertEquals("C", String.valueOf(this.wikiDrumTab.getMeasures().get(0).getNotes().get(4).getUnpitched().getStep()));
		assertEquals("G", String.valueOf(this.wikiDrumTab.getMeasures().get(0).getNotes().get(5).getUnpitched().getStep()));
		assertEquals("G", String.valueOf(this.wikiDrumTab.getMeasures().get(0).getNotes().get(6).getUnpitched().getStep()));
		assertEquals("F", String.valueOf(this.wikiDrumTab.getMeasures().get(0).getNotes().get(7).getUnpitched().getStep()));
		assertEquals("G", String.valueOf(this.wikiDrumTab.getMeasures().get(0).getNotes().get(8).getUnpitched().getStep()));
		assertEquals("G", String.valueOf(this.wikiDrumTab.getMeasures().get(0).getNotes().get(9).getUnpitched().getStep()));
		assertEquals("C", String.valueOf(this.wikiDrumTab.getMeasures().get(0).getNotes().get(10).getUnpitched().getStep()));
		assertEquals("G", String.valueOf(this.wikiDrumTab.getMeasures().get(0).getNotes().get(11).getUnpitched().getStep()));
		
		/*
		 * Measure 2
		 */
		assertEquals("C", String.valueOf(this.wikiDrumTab.getMeasures().get(1).getNotes().get(0).getUnpitched().getStep()));
		assertEquals("F", String.valueOf(this.wikiDrumTab.getMeasures().get(1).getNotes().get(1).getUnpitched().getStep()));
		assertEquals("C", String.valueOf(this.wikiDrumTab.getMeasures().get(1).getNotes().get(2).getUnpitched().getStep()));
		assertEquals("C", String.valueOf(this.wikiDrumTab.getMeasures().get(1).getNotes().get(3).getUnpitched().getStep()));
		assertEquals("C", String.valueOf(this.wikiDrumTab.getMeasures().get(1).getNotes().get(4).getUnpitched().getStep()));
		assertEquals("E", String.valueOf(this.wikiDrumTab.getMeasures().get(1).getNotes().get(5).getUnpitched().getStep()));
		assertEquals("E", String.valueOf(this.wikiDrumTab.getMeasures().get(1).getNotes().get(6).getUnpitched().getStep()));
		assertEquals("D", String.valueOf(this.wikiDrumTab.getMeasures().get(1).getNotes().get(7).getUnpitched().getStep()));
		assertEquals("D", String.valueOf(this.wikiDrumTab.getMeasures().get(1).getNotes().get(8).getUnpitched().getStep()));
		assertEquals("A", String.valueOf(this.wikiDrumTab.getMeasures().get(1).getNotes().get(9).getUnpitched().getStep()));
		assertEquals("F", String.valueOf(this.wikiDrumTab.getMeasures().get(1).getNotes().get(10).getUnpitched().getStep()));

	}
	
public void testGetOctave() {
		
		/*
		 * Checks the step value, and alter value of each note in the wikiDrumTab.musicxml file
		 */
		
		/*
		 * Measure 1
		 */
		assertEquals(5, this.wikiDrumTab.getMeasures().get(0).getNotes().get(0).getUnpitched().getOctave());
		assertEquals(4, this.wikiDrumTab.getMeasures().get(0).getNotes().get(1).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(0).getNotes().get(2).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(0).getNotes().get(3).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(0).getNotes().get(4).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(0).getNotes().get(5).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(0).getNotes().get(6).getUnpitched().getOctave());
		assertEquals(4, this.wikiDrumTab.getMeasures().get(0).getNotes().get(7).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(0).getNotes().get(8).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(0).getNotes().get(9).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(0).getNotes().get(10).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(0).getNotes().get(11).getUnpitched().getOctave());
		
		/*
		 * Measure 2
		 */
		assertEquals(5, this.wikiDrumTab.getMeasures().get(1).getNotes().get(0).getUnpitched().getOctave());
		assertEquals(4, this.wikiDrumTab.getMeasures().get(1).getNotes().get(1).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(1).getNotes().get(2).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(1).getNotes().get(3).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(1).getNotes().get(4).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(1).getNotes().get(5).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(1).getNotes().get(6).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(1).getNotes().get(7).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(1).getNotes().get(8).getUnpitched().getOctave());
		assertEquals(5, this.wikiDrumTab.getMeasures().get(1).getNotes().get(9).getUnpitched().getOctave());
		assertEquals(4, this.wikiDrumTab.getMeasures().get(1).getNotes().get(10).getUnpitched().getOctave());

	}

}
