package musicxml.parsing;

public class Tremolo {
	private String type;
	private int value;
	
	public Tremolo(String type, int val) {
		this.type = type;
		this.value = val;
	}

	//Public getters
	public String getType() {
		return type;
	}

	public int getValue() {
		return value;
	}
}
