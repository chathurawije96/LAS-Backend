package edu.icbt.las.labappointmentsystem.exception;

public class ServiceException extends Exception{

    public static final int PROCESSING_FAILED = 1;
    public static final int VALIDATION_FAILED = 2;
    public static final int VALUE_DUPLICATION = 3;
    public static final int VALUE_NULL = 4;
    public static final int CUSTOM_ERROR = 5;
    public static final int INTEGRATION_FAILED = 6;
    public static final int AUTHENTICATION_FAILED = 7;
    public static final int ALREADY_ENROLLED = 8;

    private ServiceExceptionType type;
    private int code;
    private String errorCode;
    private String[] messageArgs;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(ServiceExceptionType type, String message) {
        super(message);
        this.code = type.getId();
    }

    public ServiceException(ServiceExceptionType type, String errorCode, String message) {
        super(message);
        this.code = type.getId();
        this.errorCode = errorCode;
    }

    public ServiceException(ServiceExceptionType type, String message, String... messageArgs) {
        super(message);
        this.code = type.getId();
        this.messageArgs = messageArgs;
    }

    public ServiceException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ServiceExceptionType getType() {
        return type;
    }

    public void setType(ServiceExceptionType type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String[] getMessageArgs() {
        return messageArgs;
    }

    public void setMessageArgs(String[] messageArgs) {
        this.messageArgs = messageArgs;
    }
}
