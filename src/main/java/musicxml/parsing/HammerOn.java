package musicxml.parsing;

public class HammerOn {
	private int number;
	private String type, textValue;
	
	public HammerOn(int num, String type, String val) {
		this.number = num;
		this.type = type;
		this.textValue = val;
	}

	//Public getters
	public int getNumber() {
		return number;
	}

	public String getType() {
		return type;
	}

	public String getTextValue() {
		return textValue;
	}
}
