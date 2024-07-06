package jp.co.jim.exceptions;

public class DuplicatedUserException extends Exception {
    public DuplicatedUserException() {
        super();
    }

    public DuplicatedUserException(String message) {
        super(message);
    }

    public DuplicatedUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedUserException(Throwable cause) {
        super(cause);
    }

}
