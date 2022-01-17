package custom_exceptions;

@SuppressWarnings("serial")
public class InvalidInputException extends Exception{

	/* This exception is called InvalidInputException 
	 * and inherits from the Exception class. The method takes
	 * a String parameter.
	 */
    public InvalidInputException(String msg){
        super(msg);
    }


}
