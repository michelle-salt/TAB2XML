package musicxml.parsing;

public class Clef {

	private String sign;
	private int numLines;
		
	public Clef(String sign, int numLines) {
		this.sign = sign;
		this.numLines = numLines;
	}
	
	//Getters
	public String getSign() {
		return sign;
	}

	public int getLineValues() {
		return numLines;
	}
}
