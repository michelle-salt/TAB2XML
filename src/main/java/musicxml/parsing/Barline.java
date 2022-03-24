package musicxml.parsing;

public class Barline {
	public final char NULL = '-';
	
	private char location, repeatDirection;
	private String barStyle;
	private int repeatTimes;
	
	public Barline(String location, String barStyle, String repeatDirection, int repeatTimes) {
		//Get location
		if (location.equalsIgnoreCase("right")) {
			this.location = 'r';
		} else if (location.equalsIgnoreCase("left")) {
			this.location = 'l';
		} 
		//Doesn't exist
		else {
			this.location = NULL;
		}
		
		//Get repeatDirection
		if (repeatDirection.equalsIgnoreCase("forward")) {
			this.repeatDirection = 'f';
		} else if (repeatDirection.equalsIgnoreCase("backward")) {
			this.repeatDirection = 'b';
		}
		//Doesn't exist
		else {
			this.repeatDirection = NULL;
		}
		this.barStyle = barStyle;
		this.repeatTimes = repeatTimes;
	}
	
	//Public getters
	public char getLocation() {
		return location;
	}

	public char getRepeatDirection() {
		return repeatDirection;
	}

	public String getBarStyle() {
		return barStyle;
	}

	public int getRepeatTimes() {
		return repeatTimes;
	}
}
