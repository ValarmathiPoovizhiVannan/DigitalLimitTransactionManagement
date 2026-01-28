package exceptions;

public class AccessException extends RuntimeException {


	public AccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessException(String message) {
		super(message);
	}

	public AccessException(Exception e) {
		super(e);
 	}

	
}
