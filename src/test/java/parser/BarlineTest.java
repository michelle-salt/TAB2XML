package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.*;

import musicxml.parsing.*;

public class BarlineTest {

	protected Parser repeat;
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
	}
	
	@Test
	public void testNoExtraBarlines() {
		assertEquals(1, this.repeat.getMeasures().get(0).getBarlines().size());
		assertEquals(1, this.repeat.getMeasures().get(1).getBarlines().size());
		assertEquals(0, this.repeat.getMeasures().get(2).getBarlines().size());
		assertEquals(0, this.repeat.getMeasures().get(3).getBarlines().size());
	}
	
	@Test
	public void testGetLocation() {
		assertEquals('l', this.repeat.getMeasures().get(0).getBarlines().get(0).getLocation());
		assertEquals('r', this.repeat.getMeasures().get(1).getBarlines().get(0).getLocation());
	}
	
	@Test
	public void testGetRepeatDirection() {
		assertEquals('f', this.repeat.getMeasures().get(0).getBarlines().get(0).getRepeatDirection());
		assertEquals('b', this.repeat.getMeasures().get(1).getBarlines().get(0).getRepeatDirection());
	}
	
	@Test
	public void testGetBarStyle() {
		assertEquals("heavy-light", this.repeat.getMeasures().get(0).getBarlines().get(0).getBarStyle());
		assertEquals("light-heavy", this.repeat.getMeasures().get(1).getBarlines().get(0).getBarStyle());
	}
	
	@Test
	public void testGetRepeatTimes() {
		assertEquals(0, this.repeat.getMeasures().get(0).getBarlines().get(0).getRepeatTimes());
		assertEquals(7, this.repeat.getMeasures().get(1).getBarlines().get(0).getRepeatTimes());
	}
}
