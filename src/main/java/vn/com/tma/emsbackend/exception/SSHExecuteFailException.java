package vn.com.tma.emsbackend.exception;


import vn.com.tma.emsbackend.model.exception.ApplicationException;

public class SSHExecuteFailException  extends ApplicationException {
    public SSHExecuteFailException() {
    }

    public SSHExecuteFailException(String message) {
        super(message);
    }

    public SSHExecuteFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSHExecuteFailException(Throwable cause) {
        super(cause);
    }

    public SSHExecuteFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
