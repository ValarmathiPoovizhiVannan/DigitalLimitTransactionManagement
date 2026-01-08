package exceptions;

public class AcessException extends RuntimeException {


	public AcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public AcessException(String message) {
		super(message);
	}
}
