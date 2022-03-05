package GUI;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import javafx.scene.shape.Line;

class DrawSheetLinesTest {

	@Test
	public void testGetLine() {
		//Make a DrawSheetLine object
		DrawSheetLines sheetLine = new DrawSheetLines(0.0, 0.0, 20.0, 0.0);
		//Verify each individual value is correct
		assertEquals(0.0, sheetLine.getLine().getStartX());
		assertEquals(0.0, sheetLine.getLine().getStartY());
		assertEquals(20.0, sheetLine.getLine().getEndX());
		assertEquals(0.0, sheetLine.getLine().getEndY());
	}
	
	@Test
	public void testSetLine() {
		//Make a DrawSheetLine object
		//This line is known to be correct based on the test method above
		DrawSheetLines sheetLine = new DrawSheetLines(0.0, 0.0, 20.0, 0.0);
		//Make a new Line object
		Line line = new Line(20.0, 10.0, 40.0, 80.0);
		//Set the value of the SheetLine object with the setter method
		sheetLine.setLine(line);
		////Verify each individual value is correct
		assertEquals(20.0, sheetLine.getLine().getStartX());
		assertEquals(10.0, sheetLine.getLine().getStartY());
		assertEquals(40.0, sheetLine.getLine().getEndX());
		assertEquals(80.0, sheetLine.getLine().getEndY());
	}

}
