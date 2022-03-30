package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.*;

import musicxml.parsing.*;

public class DirectionTest {

	protected Parser repeat, money;
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
		repeat = new Parser(Files.readString(Paths.get(filePath.concat("repeat.txt"))));
		money = new Parser(Files.readString(Paths.get(filePath.concat("money.txt"))));
	}
	
	@Test
	public void testGetPlacement() {
		assertEquals('-', this.repeat.getMeasures().get(0).getDirection().getPlacement());
		assertEquals('a', this.repeat.getMeasures().get(1).getDirection().getPlacement());
		assertEquals('-', this.repeat.getMeasures().get(2).getDirection().getPlacement());
		assertEquals('-', this.repeat.getMeasures().get(3).getDirection().getPlacement());
		
		assertEquals('a', this.money.getMeasures().get(0).getDirection().getPlacement());
		assertEquals('-', this.money.getMeasures().get(1).getDirection().getPlacement());
		assertEquals('-', this.money.getMeasures().get(2).getDirection().getPlacement());
		assertEquals('-', this.money.getMeasures().get(3).getDirection().getPlacement());
	}
	
	@Test
	public void testGetX() {
		assertEquals(0.0, this.repeat.getMeasures().get(0).getDirection().getX(), 0.1);
		assertEquals(0.0, this.repeat.getMeasures().get(1).getDirection().getX(), 0.1);
		assertEquals(0.0, this.repeat.getMeasures().get(2).getDirection().getX(), 0.1);
		assertEquals(0.0, this.repeat.getMeasures().get(3).getDirection().getX(), 0.1);
		
		assertEquals(0.0, this.money.getMeasures().get(0).getDirection().getX(), 0.1);
		assertEquals(0.0, this.money.getMeasures().get(1).getDirection().getX(), 0.1);
		assertEquals(0.0, this.money.getMeasures().get(2).getDirection().getX(), 0.1);
		assertEquals(0.0, this.money.getMeasures().get(3).getDirection().getX(), 0.1);
	}
	
	@Test
	public void testGetY() {
		assertEquals(0.0, this.repeat.getMeasures().get(0).getDirection().getY(), 0.1);
		assertEquals(0.0, this.repeat.getMeasures().get(1).getDirection().getY(), 0.1);
		assertEquals(0.0, this.repeat.getMeasures().get(2).getDirection().getY(), 0.1);
		assertEquals(0.0, this.repeat.getMeasures().get(3).getDirection().getY(), 0.1);
		
		assertEquals(0.0, this.money.getMeasures().get(0).getDirection().getY(), 0.1);
		assertEquals(0.0, this.money.getMeasures().get(1).getDirection().getY(), 0.1);
		assertEquals(0.0, this.money.getMeasures().get(2).getDirection().getY(), 0.1);
		assertEquals(0.0, this.money.getMeasures().get(3).getDirection().getY(), 0.1);
	}
	
	@Test
	public void testGetWords() {
		assertNull(this.repeat.getMeasures().get(0).getDirection().getWords());
		assertEquals("x7", this.repeat.getMeasures().get(1).getDirection().getWords());
		assertNull(this.repeat.getMeasures().get(2).getDirection().getWords());
		assertNull(this.repeat.getMeasures().get(3).getDirection().getWords());
		
		assertEquals("x8", this.money.getMeasures().get(0).getDirection().getWords());
		assertNull(this.money.getMeasures().get(1).getDirection().getWords());
		assertNull(this.money.getMeasures().get(2).getDirection().getWords());
		assertNull(this.money.getMeasures().get(3).getDirection().getWords());
	}
}
