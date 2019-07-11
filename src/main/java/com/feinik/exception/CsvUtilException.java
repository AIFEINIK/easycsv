package com.feinik.exception;

/**
 * @author Feinik
 * @discription
 * @date 2019/7/10
 * @since 1.0.0
 */
public class CsvUtilException extends RuntimeException {

    public CsvUtilException() {
    }

    public CsvUtilException(String message) {
        super(message);
    }

    public CsvUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public CsvUtilException(Throwable cause) {
        super(cause);
    }
}
