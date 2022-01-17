package custom_exceptions;

@SuppressWarnings("serial")
public class InvalidInputException extends Exception{

	/* This exception is called InvalidInputException */
    public InvalidInputException(String msg){
        super(msg);
    }


}
