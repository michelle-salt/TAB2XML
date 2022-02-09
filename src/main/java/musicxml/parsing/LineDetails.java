package musicxml.parsing;

public class LineDetails {
	//The following variables are attributes, not sub-tags
	private int line;
	private String color;
	private int lineType; //Dashed (1), dotted (2), solid (3), wavy (4)
	private boolean printObject = true; //Assumed to be yes if not specified
		//Should change this ^ to be initialized in the constructor instead
	private double width;
	
}
