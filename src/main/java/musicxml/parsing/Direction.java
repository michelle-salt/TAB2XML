package musicxml.parsing;

public class Direction {
	private char placement;
	private double x, y;
	private String words;
	
	public Direction(String placement, double x, double y, String words) {
		if (placement.equalsIgnoreCase("above")) {
			this.placement = 'a';
		} 
		//Assumed to be below
		else {
			this.placement = 'b';
		}
		
		this.x = x;
		this.y = y;
		this.words = words;
	}

	//Public getters
	public char getPlacement() {
		return placement;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public String getWords() {
		return words;
	}
}
