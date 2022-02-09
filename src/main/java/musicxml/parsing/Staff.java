package musicxml.parsing;

import java.util.ArrayList;

public class Staff {
	private ArrayList lines;
	
	private String staffType; //Optional
	private int staffLines;
	//Create a lineDetails class to store these Objects
	private ArrayList<LineDetails> lineDetails; //Idk if this is used in the starter code
	private ArrayList<StaffTuning> staffTuning;
	private int capo; //Optional
	private double staffSize; //Optional, should create a class with attributes??
}
