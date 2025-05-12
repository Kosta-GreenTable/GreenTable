package site.greentable.exception;

public class UnAuthorizedException extends Exception {
	public UnAuthorizedException() {

	}

	public UnAuthorizedException(String msg) {
		super(msg);
	}
}
