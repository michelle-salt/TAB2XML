package custom_exceptions;

public class UnrecognizedInstrumentException extends Exception {
	public UnrecognizedInstrumentException(String s) {
		super(s);
	}
}
