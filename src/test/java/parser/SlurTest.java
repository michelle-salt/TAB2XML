package parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import musicxml.parsing.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class SlurTest {

	protected Parser capricho;
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
        capricho = new Parser(Files.readString(Paths.get(filePath.concat("capricho.txt"))));
	}
	
	@Test
	public void testGetNumber() {
		//Measure 1
		ArrayList<Note> notes = this.capricho.getMeasures().get(0).getNotes();
		for (int i = 0; i < notes.size(); i++) {
			assertEquals(0, notes.get(i).getSlur().getNumber());
		}
		
		//Measure 2
		notes = this.capricho.getMeasures().get(1).getNotes();
		for (int i = 0; i < notes.size(); i++) {
			int num = notes.get(i).getSlur().getNumber();
			
			if (i == 4 || i == 5) {
				assertEquals(6, num);
			} else if (i == 2 || i == 3 || i ==  8 || i == 9) {
				assertEquals(4, num);
			} 
			//Pull-off doesn't exist for these notes so default is 0
			else if (i == 10 || i == 11) {
				assertEquals(0, num);
			} else {
				assertEquals(2, num);
			}
		}
	}
	
	@Test
	public void testGetType() {
		//Measure 1
		ArrayList<Note> notes = this.capricho.getMeasures().get(0).getNotes();
		for (int i = 0; i < notes.size(); i++) {
			assertNull(notes.get(i).getPullOff().getType());
		}
		
		//Measure 2
		notes = this.capricho.getMeasures().get(1).getNotes();
		for (int i = 0; i < notes.size(); i++) {
			if (i == 0 || i == 2 || i == 4 || i == 6 || i == 8) {
				assertEquals("start", notes.get(i).getSlur().getType());
			} 
			//Pull-off doesn't exist for these notes so default is null
			else if (i == 10 || i == 11) {
				assertNull(notes.get(i).getSlur().getType());
			} else {
				assertEquals("stop", notes.get(i).getSlur().getType());
			}
		}
	}
	
	@Test
	public void testGetPlacement() {
		//Measure 1
		ArrayList<Note> notes = this.capricho.getMeasures().get(0).getNotes();
		for (int i = 0; i < notes.size(); i++) {
			assertNull(notes.get(i).getSlur().getPlacement());
		}
		
		//Measure 2
		notes = this.capricho.getMeasures().get(1).getNotes();
		for (int i = 0; i < notes.size(); i++) {
			String placement = notes.get(i).getSlur().getPlacement();
			if (i == 0 || i == 2 || i == 4 || i == 6 || i == 8) {
				assertEquals("above", placement);
			} 
			//Pull-off doesn't exist for these notes so default is 0
			else {
				assertNull(placement);
			}
		}
	}
}
