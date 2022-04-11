package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.*;

import musicxml.parsing.*;

class NoteTest {

	protected Parser wikiDrumTab, wikiGuitarTab, capricho, parabola, bendTest1, graceTest1;
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
		capricho = new Parser(Files.readString(Paths.get(filePath.concat("capricho.txt"))));
		parabola = new Parser(Files.readString(Paths.get(filePath.concat("parabola.txt"))));
		bendTest1 = new Parser(Files.readString(Paths.get(filePath.concat("bendTest1.txt"))));
		graceTest1 = new Parser(Files.readString(Paths.get(filePath.concat("graceTest1.txt"))));
	}
	
	/*
	 * Test values applicable to wikiGuitarTab
	 */
	@Test
	public void testGetDuration() {
		//Verify that the values in wikiGuitarTab are correct
		for (int i = 0; i < this.wikiGuitarTab.getMeasures().get(0).getNumNotes(); i++) {
			//Every note in measure 1 has a duration of 8
			assertEquals(8, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).getDuration());
		}
		for (int i = 0; i < this.wikiGuitarTab.getMeasures().get(1).getNumNotes(); i++) {
			//Every note in measure 2 has a duration of 64
			assertEquals(64, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(i).getDuration());
		}
		
		//Verify that the values in wikiDrumTab are correct
		for (int i = 0; i < this.wikiDrumTab.getMeasures().get(0).getNumNotes(); i++) {
			//Every note in measure 1 has a duration of 8
			assertEquals(8, this.wikiDrumTab.getMeasures().get(0).getNotes().get(i).getDuration());
		}
		for (int i = 0; i < this.wikiDrumTab.getMeasures().get(1).getNumNotes(); i++) {
			if (i == 9 || i == 10) 
				assertEquals(32, this.wikiDrumTab.getMeasures().get(1).getNotes().get(i).getDuration());
			else
				assertEquals(4, this.wikiDrumTab.getMeasures().get(1).getNotes().get(i).getDuration());
		}
	}
	
	@Test
	public void testGetVoice() {
		//Verify that the values in wikiGuitarTab are correct
		for (int i = 0; i < this.wikiGuitarTab.getMeasures().get(0).getNumNotes(); i++) {
			//Every note in measure 1 has a voice of 1
			assertEquals(1, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).getVoice());
		}
		for (int i = 0; i < this.wikiGuitarTab.getMeasures().get(1).getNumNotes(); i++) {
			//Every note in measure 2 has a voice of 1
			assertEquals(1, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(i).getVoice());
		}
		
		//Verify that the values in wikiDrumTab are correct
		for (int i = 0; i < this.wikiDrumTab.getMeasures().get(0).getNumNotes(); i++) {
			//Every note in measure 1 has a voice of 1
			assertEquals(1, this.wikiDrumTab.getMeasures().get(0).getNotes().get(i).getVoice());
		}
		for (int i = 0; i < this.wikiDrumTab.getMeasures().get(1).getNumNotes(); i++) {
			//Every note in measure 1 has a voice of 1
			assertEquals(1, this.wikiDrumTab.getMeasures().get(1).getNotes().get(i).getVoice());
		}
	}
	
	@Test
	public void testGetType() {
		//Verify that the values in wikiGuitarTab are correct
		for (int i = 0; i < this.wikiGuitarTab.getMeasures().get(0).getNumNotes(); i++) {
			//Every note in measure 1 is an eighth note ('I')
			assertEquals('I', this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).getType());
		}
		for (int i = 0; i < this.wikiGuitarTab.getMeasures().get(1).getNumNotes(); i++) {
			//Every note in measure 2 is a whole note ('W')
			assertEquals('W', this.wikiGuitarTab.getMeasures().get(1).getNotes().get(i).getType());
		}
		
		//Verify that the values in wikiDrumTab are correct
		for (int i = 0; i < this.wikiDrumTab.getMeasures().get(0).getNumNotes(); i++) {
			//Every note in measure 1 is an eighth note ('I')
			assertEquals('I', this.wikiDrumTab.getMeasures().get(0).getNotes().get(i).getType());
		}
		for (int i = 0; i < this.wikiDrumTab.getMeasures().get(1).getNumNotes(); i++) {
			if (i == 9 || i == 10) 
				assertEquals('H', this.wikiDrumTab.getMeasures().get(1).getNotes().get(i).getType());
			else
				assertEquals('S', this.wikiDrumTab.getMeasures().get(1).getNotes().get(i).getType());
		}
	}

	@Test
	public void testGetString() {
		//Verify that the values in wikiGuitarTab are correct
		for (int i = 0; i < this.wikiGuitarTab.getMeasures().get(0).getNumNotes(); i++) {
			if (i == 0) 
				assertEquals(6, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).getString());
			else if (i == 1)
				assertEquals(5, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).getString());
			else if (i == 2)
				assertEquals(4, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).getString());
			else if (i == 3 || i == 7)
				assertEquals(3, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).getString());
			else if (i == 5)
				assertEquals(1, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).getString());
			else 
				assertEquals(2, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).getString());
		}
		for (int i = 0; i < this.wikiGuitarTab.getMeasures().get(1).getNumNotes(); i++) {
			assertEquals(i+1, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(i).getString());
		}
	}
	
	@Test
	public void testGetFret() {
		//Verify that the values in wikiGuitarTab are correct
		for (int i = 0; i < this.wikiGuitarTab.getMeasures().get(0).getNumNotes(); i++) {
			if (i == 1 || i == 2)
				assertEquals(2, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).getFret());
			else if (i == 3 || i == 7)
				assertEquals(1, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).getFret());
			else 
				assertEquals(0, this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).getFret());
		}
		for (int i = 0; i < this.wikiGuitarTab.getMeasures().get(1).getNumNotes(); i++) {
			if (i == 2)
				assertEquals(1, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(i).getFret());
			else if (i == 3 || i == 4)
				assertEquals(2, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(i).getFret());
			else 
				assertEquals(0, this.wikiGuitarTab.getMeasures().get(1).getNotes().get(i).getFret());

		}
	}

	@Test
	public void testIsChord_SetChord() {
		//Verify that the values in wikiGuitarTab are correct
		for (int i = 0; i < this.wikiGuitarTab.getMeasures().get(0).getNumNotes(); i++) {
			assertFalse(this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).isChord());
			this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).setChord();
			assertTrue(this.wikiGuitarTab.getMeasures().get(0).getNotes().get(i).isChord());
		}
		for (int i = 0; i < this.wikiGuitarTab.getMeasures().get(1).getNumNotes(); i++) {
			//First one is false since it's not a chord with the previous note
			if (i == 0) {
				assertFalse(this.wikiGuitarTab.getMeasures().get(1).getNotes().get(i).isChord());
				this.wikiGuitarTab.getMeasures().get(1).getNotes().get(i).setChord();
				assertTrue(this.wikiGuitarTab.getMeasures().get(1).getNotes().get(i).isChord());
			} else 
				assertTrue(this.wikiGuitarTab.getMeasures().get(1).getNotes().get(i).isChord());
		}
	}

	/*
	 * Test values applicable to wikiDrumTab
	 */
	@Test
	public void testGetInstrumentID() {
		//Verify that the values in wikiDrumTab are correct
		for (int i = 0; i < this.wikiDrumTab.getMeasures().get(0).getNumNotes(); i++) {
			if (i == 0)
				assertEquals("P1-I50", this.wikiDrumTab.getMeasures().get(0).getNotes().get(i).getInstrumentID());
			else if (i == 1 || i == 7)
				assertEquals("P1-I36", this.wikiDrumTab.getMeasures().get(0).getNotes().get(i).getInstrumentID());
			else if (i == 4 || i == 10)
				assertEquals("P1-I39", this.wikiDrumTab.getMeasures().get(0).getNotes().get(i).getInstrumentID());
			else 
				assertEquals("P1-I43", this.wikiDrumTab.getMeasures().get(0).getNotes().get(i).getInstrumentID());
		}
		for (int i = 0; i < this.wikiDrumTab.getMeasures().get(1).getNumNotes(); i++) {
			if (i == 1 || i == 10)
				assertEquals("P1-I36", this.wikiDrumTab.getMeasures().get(1).getNotes().get(i).getInstrumentID());
			else if (i == 5 || i == 6)
				assertEquals("P1-I48", this.wikiDrumTab.getMeasures().get(1).getNotes().get(i).getInstrumentID());
			else if (i == 7 || i == 8)
				assertEquals("P1-I46", this.wikiDrumTab.getMeasures().get(1).getNotes().get(i).getInstrumentID());
			else if (i == 9)
				assertEquals("P1-I50", this.wikiDrumTab.getMeasures().get(1).getNotes().get(i).getInstrumentID());
			else 
				assertEquals("P1-I39", this.wikiDrumTab.getMeasures().get(1).getNotes().get(i).getInstrumentID());
		}
	}

	@Test
	public void testGetStem() {
		//Verify that the values in wikiDrumTab are correct
		//3 represents "up" for stem
		for (int i = 0; i < this.wikiDrumTab.getMeasures().get(0).getNumNotes(); i++) {
			assertEquals(3, this.wikiDrumTab.getMeasures().get(0).getNotes().get(i).getStem());
		}
		for (int i = 0; i < this.wikiDrumTab.getMeasures().get(1).getNumNotes(); i++) {
			assertEquals(3, this.wikiDrumTab.getMeasures().get(1).getNotes().get(i).getStem());
		}
	}
	
	@Test
	public void testGetNotehead() {		
		//Verify that the values in wikiDrumTab are correct
		for (int i = 0; i < this.wikiDrumTab.getMeasures().get(0).getNumNotes(); i++) {
			if (i == 1 || i == 4 || i == 7 || i == 10)
				assertNull(this.wikiDrumTab.getMeasures().get(0).getNotes().get(i).getNotehead());
			else
				assertEquals("x", this.wikiDrumTab.getMeasures().get(0).getNotes().get(i).getNotehead());
		}
		for (int i = 0; i < this.wikiDrumTab.getMeasures().get(1).getNumNotes(); i++) {
			if (i == 9)
				assertEquals("x", this.wikiDrumTab.getMeasures().get(1).getNotes().get(i).getNotehead());
			else
				assertNull(this.wikiDrumTab.getMeasures().get(1).getNotes().get(i).getNotehead());
		}
	}
	
	/*
	 * Test all other common values
	 */
	@Test
	public void testGetBendAlter() {
		assertEquals(2, this.bendTest1.getMeasures().get(0).getNotes().get(0).getBendAlter());
		assertEquals(0, this.bendTest1.getMeasures().get(0).getNotes().get(1).getBendAlter());

		for (int i = 0; i < this.bendTest1.getMeasures().get(1).getNumNotes(); i++) {
			if (i == 2)
				assertEquals(1, this.bendTest1.getMeasures().get(1).getNotes().get(i).getBendAlter());
			else if (i == 1 || i == 5)
				assertEquals(2, this.bendTest1.getMeasures().get(1).getNotes().get(i).getBendAlter());
			else 
				assertEquals(0, this.bendTest1.getMeasures().get(1).getNotes().get(i).getBendAlter());
		}
	}
	
	@Test
	public void testGetNumDots() {
		assertEquals(0, this.parabola.getMeasures().get(0).getNotes().get(1).getNumDots());
		assertEquals(0, this.parabola.getMeasures().get(3).getNotes().get(2).getNumDots());
		assertEquals(1, this.parabola.getMeasures().get(16).getNotes().get(0).getNumDots());
		assertEquals(1, this.capricho.getMeasures().get(0).getNotes().get(0).getNumDots());
		assertEquals(2, this.parabola.getMeasures().get(66).getNotes().get(4).getNumDots());
		assertEquals(2, this.parabola.getMeasures().get(66).getNotes().get(5).getNumDots());
	}
	
	@Test
	public void testIsGraceNote_SetGraceNote() {
		assertTrue(this.capricho.getMeasures().get(14).getNotes().get(0).isGraceNote());
		assertFalse(this.capricho.getMeasures().get(14).getNotes().get(1).isGraceNote());
		this.capricho.getMeasures().get(14).getNotes().get(1).setGraceNote();
		assertTrue(this.capricho.getMeasures().get(14).getNotes().get(1).isGraceNote());
		
		assertFalse(this.graceTest1.getMeasures().get(0).getNotes().get(0).isGraceNote());
		this.graceTest1.getMeasures().get(0).getNotes().get(0).setGraceNote();
		assertTrue(this.graceTest1.getMeasures().get(0).getNotes().get(0).isGraceNote());
		
		assertTrue(this.graceTest1.getMeasures().get(0).getNotes().get(1).isGraceNote());
		assertTrue(this.graceTest1.getMeasures().get(0).getNotes().get(2).isGraceNote());
		
		assertFalse(this.graceTest1.getMeasures().get(0).getNotes().get(3).isGraceNote());
		this.graceTest1.getMeasures().get(0).getNotes().get(3).setGraceNote();
		assertTrue(this.graceTest1.getMeasures().get(0).getNotes().get(3).isGraceNote());
	}
	
	@Test
	public void testIsRest_SetRest() {
		assertTrue(this.parabola.getMeasures().get(64).getNotes().get(0).isRest());
		assertTrue(this.parabola.getMeasures().get(64).getNotes().get(1).isRest());
		assertTrue(this.parabola.getMeasures().get(64).getNotes().get(2).isRest());
		assertFalse(this.parabola.getMeasures().get(64).getNotes().get(3).isRest());
		this.parabola.getMeasures().get(64).getNotes().get(3).setRest();
		assertTrue(this.parabola.getMeasures().get(64).getNotes().get(3).isRest());
	}
	
	@Test
	public void testIsNatural_SetNatural() {
		assertTrue(this.capricho.getMeasures().get(0).getNotes().get(0).isNatural());
		assertTrue(this.capricho.getMeasures().get(0).getNotes().get(1).isNatural());
		
		assertFalse(this.capricho.getMeasures().get(1).getNotes().get(1).isNatural());
		this.capricho.getMeasures().get(1).getNotes().get(1).setNatural();
		assertTrue(this.capricho.getMeasures().get(1).getNotes().get(1).isNatural());
	}
	
	@Test
	public void testIsArtificial_SetArtificial() {
		assertFalse(this.capricho.getMeasures().get(1).getNotes().get(0).isArtificial());
		this.capricho.getMeasures().get(1).getNotes().get(0).setArtificial();
		assertTrue(this.capricho.getMeasures().get(1).getNotes().get(0).isArtificial());
	}
	
	@Test
	public void testGetNoteheadParentheses_SetNoteheadParentheses() {
		assertTrue(this.parabola.getMeasures().get(7).getNotes().get(11).getNoteheadParentheses());
		
		assertFalse(this.parabola.getMeasures().get(7).getNotes().get(12).getNoteheadParentheses());
		this.parabola.getMeasures().get(7).getNotes().get(12).setNoteheadParentheses();
		assertTrue(this.parabola.getMeasures().get(7).getNotes().get(12).getNoteheadParentheses());
	}
	
	/*
	 * All other attributes are unique classes, which have their own test classes
	 */
}
