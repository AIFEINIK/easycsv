package com.feinik.csv.exception;

/**
 *
 *
 * @author Feinik
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
