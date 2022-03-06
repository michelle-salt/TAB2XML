package parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import musicxml.parsing.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class TiedTest {

	protected Parser tieTest1;
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
	
	@Test
	public void testSetters() {
		//Everything here should be false initially, verified by testGetters method
		Tied tied = this.tieTest1.getMeasures().get(0).getNotes().get(0).getTied();
		
		//Set all the values to true
		tied.setCont(true);
		tied.setLetRing(true);
		tied.setStart(true);
		tied.setStop(true);
		
		//Verify that the values are true, as expected
		assertEquals(true, tied.getStart());
		assertEquals(true, tied.getStop());
		assertEquals(true, tied.getCont());
		assertEquals(true, tied.getLetRing());
	}

}
