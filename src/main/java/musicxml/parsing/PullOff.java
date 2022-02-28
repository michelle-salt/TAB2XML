package musicxml.parsing;

public class PullOff {
	private int number; //Valid numbers: 1-16
	private String type;
	private String value;
	
	public PullOff(int number, String type, String value) {
		this.number = number;
		this.type = type;
		this.value = value;
	}

	//Getters
	public int getNumber() {
		return number;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}
}
