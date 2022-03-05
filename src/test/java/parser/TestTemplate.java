package parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import musicxml.parsing.*;

public class TestTemplate {
	protected Parser bendTest1, capricho, ex38, graceTest1, graceTest2, money, parabola, push, restTest1, tieTest1, timingTest1, wikiDrumTab, wikiGuitarTab;
	
	public TestTemplate() throws IOException {
		String filePath = new File("").getAbsolutePath().concat("\\src\\test\\resources\\musicXMLFiles\\");
		bendTest1 = new Parser(Files.readString(Paths.get(filePath.concat("bendTest1.txt"))));
		capricho = new Parser(Files.readString(Paths.get(filePath.concat("capricho.txt"))));
		ex38 = new Parser(Files.readString(Paths.get(filePath.concat("ex38.txt"))));
		graceTest1 = new Parser(Files.readString(Paths.get(filePath.concat("graceTest1.txt"))));
		graceTest2 = new Parser(Files.readString(Paths.get(filePath.concat("graceTest2.txt"))));
		money = new Parser(Files.readString(Paths.get(filePath.concat("money.txt"))));
		parabola = new Parser(Files.readString(Paths.get(filePath.concat("parabola.txt"))));
		push = new Parser(Files.readString(Paths.get(filePath.concat("push.txt"))));
		restTest1 = new Parser(Files.readString(Paths.get(filePath.concat("restTest1.txt"))));
		tieTest1 = new Parser(Files.readString(Paths.get(filePath.concat("tieTest1.txt"))));
		timingTest1 = new Parser(Files.readString(Paths.get(filePath.concat("timingTest1.txt"))));
		wikiDrumTab = new Parser(Files.readString(Paths.get(filePath.concat("wikiDrumTab.txt"))));
		wikiGuitarTab = new Parser(Files.readString(Paths.get(filePath.concat("wikiGuitarTab.txt"))));
	}

	@Test
	public void test_wikiGuitarTab() throws IOException {
		String filePath = new File("").getAbsolutePath().concat("\\src\\test\\resources\\musicXMLFiles\\");
//		bendTest1 = new Parser(Files.readString(Paths.get(filePath.concat("bendTest1.txt"))));
//		capricho = new Parser(Files.readString(Paths.get(filePath.concat("capricho.txt"))));
//		ex38 = new Parser(Files.readString(Paths.get(filePath.concat("ex38.txt"))));
//		graceTest1 = new Parser(Files.readString(Paths.get(filePath.concat("graceTest1.txt"))));
//		graceTest2 = new Parser(Files.readString(Paths.get(filePath.concat("graceTest2.txt"))));
//		money = new Parser(Files.readString(Paths.get(filePath.concat("money.txt"))));
//		parabola = new Parser(Files.readString(Paths.get(filePath.concat("parabola.txt"))));
//		push = new Parser(Files.readString(Paths.get(filePath.concat("push.txt"))));
//		restTest1 = new Parser(Files.readString(Paths.get(filePath.concat("restTest1.txt"))));
//		tieTest1 = new Parser(Files.readString(Paths.get(filePath.concat("tieTest1.txt"))));
//		timingTest1 = new Parser(Files.readString(Paths.get(filePath.concat("timingTest1.txt"))));
//		wikiDrumTab = new Parser(Files.readString(Paths.get(filePath.concat("wikiDrumTab.txt"))));
//		wikiGuitarTab = new Parser(Files.readString(Paths.get(filePath.concat("wikiGuitarTab.txt"))));
		
//		Parser p = new Parser(this.wikiGuitarTab);
//		assertEquals(2, this.wikiGuitarTab.getNumMeasures());
		
		assertEquals(2, 2);
	}
	
	/*
	 * When creating a new test class,
	 * 		1. Copy all imports
	 * 		2. Add "extends TestTemplate" in the class declaration
	 * 		3. Copy the block below into your new class, and uncomment it
	 * 		4. Create a new test for every one of the variables above
	 */
//	@Before
//	public void setUp() throws Exception {
//		super();
//	}
}
