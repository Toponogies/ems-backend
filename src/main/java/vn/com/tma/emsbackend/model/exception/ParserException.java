package vn.com.tma.emsbackend.model.exception;

public class ParserException extends RuntimeException {
    public ParserException(String commandResult) {
        super("Command result has wrong format:\n" + commandResult);
    }
}
