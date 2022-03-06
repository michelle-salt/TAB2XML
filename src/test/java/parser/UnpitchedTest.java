package parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import musicxml.parsing.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

//This is only testing with wikiDrumTab since only drum tabs have the 'unpitched' element
class UnpitchedTest {

	protected Parser wikiDrumTab;
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
	}

	@Test
	public void testGetStep() {
		//Measure 1
		Measure measure = this.wikiDrumTab.getMeasures().get(0);
		for (int noteCounter = 0; noteCounter < measure.getNumNotes(); noteCounter++) {
			//A
			if (noteCounter == 0) {
				assertEquals('A', measure.getNotes().get(noteCounter).getUnpitched().getStep());
			} 
			//C
			else if (noteCounter == 4 || noteCounter == 10) {
				assertEquals('C', measure.getNotes().get(noteCounter).getUnpitched().getStep());
			}
			//F
			else if (noteCounter == 1 || noteCounter == 7) {
				assertEquals('F', measure.getNotes().get(noteCounter).getUnpitched().getStep());
			}
			//G
			else {
				assertEquals('G', measure.getNotes().get(noteCounter).getUnpitched().getStep());
			}
		}

		//Measure 2
		measure = this.wikiDrumTab.getMeasures().get(1);
		for (int noteCounter = 0; noteCounter < measure.getNumNotes(); noteCounter++) {
			//A
			if (noteCounter == 9) {
				assertEquals('A', measure.getNotes().get(noteCounter).getUnpitched().getStep());
			} 
			//D
			else if (noteCounter == 7 || noteCounter == 8) {
				assertEquals('D', measure.getNotes().get(noteCounter).getUnpitched().getStep());
			}
			//E
			else if (noteCounter == 5 || noteCounter == 6) {
				assertEquals('E', measure.getNotes().get(noteCounter).getUnpitched().getStep());
			}
			//F
			else if (noteCounter == 1 || noteCounter == 10) {
				assertEquals('F', measure.getNotes().get(noteCounter).getUnpitched().getStep());
			}
			//C
			else {
				assertEquals('C', measure.getNotes().get(noteCounter).getUnpitched().getStep());
			}
		}
	}

	@Test
	public void testGetOctave() {
		//Measure 1
		Measure measure = this.wikiDrumTab.getMeasures().get(0);
		for (int noteCounter = 0; noteCounter < measure.getNumNotes(); noteCounter++) {
			//4
			if (noteCounter == 1 || noteCounter == 7) {
				assertEquals(4, measure.getNotes().get(noteCounter).getUnpitched().getOctave());
			} 
			//5
			else {
				assertEquals(5, measure.getNotes().get(noteCounter).getUnpitched().getOctave());
			}
		}

		//Measure 2
		measure = this.wikiDrumTab.getMeasures().get(1);
		for (int noteCounter = 0; noteCounter < measure.getNumNotes(); noteCounter++) {
			//4
			if (noteCounter == 1 || noteCounter == 10) {
				assertEquals(4, measure.getNotes().get(noteCounter).getUnpitched().getOctave());
			} 
			//5
			else {
				assertEquals(5, measure.getNotes().get(noteCounter).getUnpitched().getOctave());
			}
		}
	}
}
