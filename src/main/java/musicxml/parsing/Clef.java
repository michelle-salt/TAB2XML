package musicxml.parsing;

public class Clef {

	private String sign;
	private int lineValues;
		
	public Clef(String sign, int lineValues) {
		this.sign = sign;
		this.lineValues = lineValues;
	}
	
	//Getters
	public String getSign() {
		return sign;
	}

	public int getLineValues() {
		return lineValues;
	}
}
