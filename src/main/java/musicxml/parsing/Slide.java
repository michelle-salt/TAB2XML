package musicxml.parsing;

public class Slide {
	private String type;
	private int number;
	
	public Slide (String type, int number) {
		this.type = type;
		this.number = number;
	}

	//Public getters
	public String getType() {
		return type;
	}

	public int getNumber() {
		return number;
	}
}
