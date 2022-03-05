package parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import musicxml.parsing.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class TestTemplate {	
	/*
	 * When creating a new test class,
	 * 		1. Copy all imports
	 * 		2. Copy the block below into your new class, and uncomment it
	 * 		3. Create a new test for both the wikiDrumTab and wikiGuitarTab
	 * 		4. Create test cases using the template block shown below the setUp method
	 * 			Remember to change the method name!	
	 */
//	@BeforeEach
//	void setUp() throws Exception {
//		//Get the OS
//        String OS = System.getProperty("os.name").toLowerCase();
//        //Get the filepath of all MusicXML files, based on the device's OS
//        String filePath = new File("").getAbsolutePath();
//        //Insert "\\" in path
//        if (OS.contains("win")) {
//            filePath = filePath.concat("\\src\\test\\resources\\musicXMLFiles\\");
//        }
//        //Assumed to be unix (mac or linux)
//        //Insert "/" in path
//        else {
//            filePath = filePath.concat("/src/test/resources/musicXMLFiles/");
//        }
//		wikiDrumTab = new Parser(Files.readString(Paths.get(filePath.concat("wikiDrumTab.txt"))));
//		wikiGuitarTab = new Parser(Files.readString(Paths.get(filePath.concat("wikiGuitarTab.txt"))));
//	}
//	
//	@Test
//	public void testInsertNameHere() {
//		assertEquals(expectedValue, this.wikiGuitarTab.methodNameAsNeeded());
//		assertEquals(expectedValue, this.wikiDrumTab.methodNameAsNeeded())
//	}
}
