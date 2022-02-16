package musicxml.parsing;

public class Slur {
	private int number;
	private String placement;
	private String type;
	
	public Slur(int number, String placement, String type) {
		this.number = number;
		this.placement = placement;
		this.type = type;
	}

	//Getters
	public int getNumber() {
		return number;
	}

	public String getPlacement() {
		return placement;
	}

	public String getType() {
		return type;
	}
}
