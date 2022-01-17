package custom_exceptions;

@SuppressWarnings("serial")
public class InvalidInputException extends Exception{

	/* This exception is called InvalidInputException 
	 * and inherits from the Exception class.
	 */
    public InvalidInputException(String msg){
        super(msg);
    }


}
