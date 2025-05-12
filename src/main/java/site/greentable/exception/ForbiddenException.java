package site.greentable.exception;

public class ForbiddenException extends Exception {
	public ForbiddenException() {

	}

	public ForbiddenException(String msg) {
		super(msg);
	}
}
