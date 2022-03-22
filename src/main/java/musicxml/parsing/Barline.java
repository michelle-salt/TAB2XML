package musicxml.parsing;

public class Barline {
	private char location, repeatDirection;
	private String barStyle;
	private int repeatTimes;
	
	public Barline(String location, String barStyle, String repeatDirection, int repeatTimes) {
		//Get location
		if (location.equalsIgnoreCase("right")) {
			this.location = 'r';
		} 
		//Assumed to be left
		else {
			this.location = 'l';
		}
		
		//Get repeatDirection
		if (repeatDirection.equalsIgnoreCase("forwards")) {
			this.repeatDirection = 'f';
		} 
		//Assumed to be backward
		else {
			this.repeatDirection = 'b';
		}
		this.barStyle = barStyle;
	}
}
