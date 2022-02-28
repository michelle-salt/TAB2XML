package parser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import musicxml.parsing.Note;
import musicxml.parsing.Parser;
import musicxml.parsing.Pitch;

public class ParserTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Test
   void testPitch(){
		
		String string = "|-----------0-----|-0---------------|\r\n"
				+ "|---------0---0---|-0---------------|\r\n"
				+ "|-------1-------1-|-1---------------|\r\n"
				+ "|-----2-----------|-2---------------|\r\n"
				+ "|---2-------------|-2---------------|\r\n"
				+ "|-0---------------|-0---------------|";
		
		Parser parse = new Parser(string);
		ArrayList<Note> notes = parse.getMeasures().get(0).getNotes();
		
		//assertEquals("",notes.get(0).getPitch().getStep());
		
       
   }

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
