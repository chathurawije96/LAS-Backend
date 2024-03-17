package edu.icbt.las.labappointmentsystem.exception;

public class DataAccessException extends Exception {

    public static final int PROCESSING_FAILED = 1;
    public static final int VALIDATION_FAILED = 2;

    private int code;

    public DataAccessException() {
        super();
    }

    public DataAccessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
