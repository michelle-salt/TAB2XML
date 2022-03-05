package parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import musicxml.parsing.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class TiedTest {

	protected Parser wikiDrumTab, wikiGuitarTab, tieTest1;
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
		tieTest1 = new Parser(Files.readString(Paths.get(filePath.concat("tieTest1.txt"))));
	}

	@Test
	public void testGetters() {
		int measureNum;
		//Measure 1
		measureNum = 0;
		//Loop through each note in the measure
		for (int i = 0; i < this.tieTest1.getMeasures().get(measureNum).getNumNotes(); i++) {
			//Verify that all the tied values are correct, based on the input
			if (i == 0 || i == 1) {
				assertEquals(false, this.tieTest1.getMeasures().get(measureNum).getNotes().get(i).getTied().getStart());
			} else {
				assertEquals(true, this.tieTest1.getMeasures().get(measureNum).getNotes().get(i).getTied().getStart());
			}
			assertEquals(false, this.tieTest1.getMeasures().get(measureNum).getNotes().get(i).getTied().getStop());
			assertEquals(false, this.tieTest1.getMeasures().get(measureNum).getNotes().get(i).getTied().getCont());
			assertEquals(false, this.tieTest1.getMeasures().get(measureNum).getNotes().get(i).getTied().getLetRing());
		}
		
		//Measure 2
		measureNum = 1;
		//Loop through each note in the measure
		for (int i = 0; i < this.tieTest1.getMeasures().get(measureNum).getNumNotes(); i++) {
			//Verify that all the tied values are correct, based on the input
			if (i == 0 || i == 1) {
				assertEquals(true, this.tieTest1.getMeasures().get(measureNum).getNotes().get(i).getTied().getStart());
				assertEquals(true, this.tieTest1.getMeasures().get(measureNum).getNotes().get(i).getTied().getStop());
			} else if (i == 4 || i == 5) {
				assertEquals(false, this.tieTest1.getMeasures().get(measureNum).getNotes().get(i).getTied().getStart());
				assertEquals(false, this.tieTest1.getMeasures().get(measureNum).getNotes().get(i).getTied().getStop());
			} 
			//Notes 0, 1, 2, 3
			else {
				assertEquals(true, this.tieTest1.getMeasures().get(measureNum).getNotes().get(i).getTied().getStop());
			}
			assertEquals(false, this.tieTest1.getMeasures().get(measureNum).getNotes().get(i).getTied().getCont());
			assertEquals(false, this.tieTest1.getMeasures().get(measureNum).getNotes().get(i).getTied().getLetRing());
		}
	}

}
