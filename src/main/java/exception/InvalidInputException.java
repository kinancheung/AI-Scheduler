package exception;

/**
 * Author: Alex
 * Date: 8/08/2021
 * 
 * Throws an exception with a custom message to indicate invalid cli input.
 */ 
public class InvalidInputException extends Exception {
    //constructor with message
    public InvalidInputException(String message) {
        super(message);
    }

}
