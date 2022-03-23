package parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import musicxml.parsing.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class AttributesTest {

	protected Parser wikiDrumTab, wikiGuitarTab;

	@BeforeEach
	void setUp() throws Exception {
		// Get the OS
		String OS = System.getProperty("os.name").toLowerCase();

		// Get the filepath of all MusicXML files, based on the device's OS
		String filePath = new File("").getAbsolutePath();

		if (OS.contains("win")) {
			filePath = filePath.concat("\\src\\test\\resources\\musicXMLFiles\\");
		}

		else {
			filePath = filePath.concat("/src/test/resources/musicXMLFiles/");
		}

		wikiDrumTab = new Parser(Files.readString(Paths.get(filePath.concat("wikiDrumTab.txt"))));
		wikiGuitarTab = new Parser(Files.readString(Paths.get(filePath.concat("wikiGuitarTab.txt"))));
	}

	@Test
	public void testDrumTab() {

		// Confirm the number of measures.
		assertEquals(2, this.wikiDrumTab.getNumMeasures());

		/* Verify the value of each accessor. 
		 * 	NOTE: 
		 * 		-100000 for invalid fifths values.
		 * 		Clef should only exist for the first measures.
		 */
		
		// DrumTab
		assertEquals(16, this.wikiDrumTab.getMeasures().get(0).getAttributes().getDivisions());
		assertEquals(0, this.wikiDrumTab.getMeasures().get(0).getAttributes().getFifths());
		
		assertEquals(16, this.wikiDrumTab.getMeasures().get(1).getAttributes().getDivisions());
		assertEquals(-100000, this.wikiDrumTab.getMeasures().get(1).getAttributes().getFifths());
		
		Clef clef;
		try {
			clef = this.wikiDrumTab.getMeasures().get(0).getAttributes().getClef();
			clef = this.wikiDrumTab.getMeasures().get(1).getAttributes().getClef();
			try {
				clef = this.wikiDrumTab.getMeasures().get(2).getAttributes().getClef();
				fail("Unsuccesfull");
			}
			catch (IndexOutOfBoundsException e) {
				// Clef doesn't exist
			}
		}
		catch (IndexOutOfBoundsException e) {
			fail("TEST FAILED");
		}
	}
	
	@Test
	public void testGuitarTab() {

		// Confirm the number of measures.
		assertEquals(2, this.wikiGuitarTab.getNumMeasures());

		/* Verify the value of each accessor. 
		 * 	NOTE: 
		 * 		-100000 for invalid fifths values.
		 * 		Clef should only exist for the first measures.
		 */
		
		// GuitarTab
		assertEquals(16, this.wikiGuitarTab.getMeasures().get(0).getAttributes().getDivisions());
		assertEquals(0, this.wikiGuitarTab.getMeasures().get(0).getAttributes().getFifths());
		
		assertEquals(16, this.wikiGuitarTab.getMeasures().get(1).getAttributes().getDivisions());
		assertEquals(0, this.wikiGuitarTab.getMeasures().get(1).getAttributes().getFifths());
		
		Clef clef;
		try {
			clef = this.wikiGuitarTab.getMeasures().get(0).getAttributes().getClef();
			clef = this.wikiGuitarTab.getMeasures().get(1).getAttributes().getClef();
			try {
				clef = this.wikiGuitarTab.getMeasures().get(2).getAttributes().getClef();
				fail("Unsuccesfull");
			}
			catch (IndexOutOfBoundsException e) {
				// Clef doesn't exist
			}
		}
		catch (IndexOutOfBoundsException e) {
			fail("TEST FAILED");
		}
	}

}
