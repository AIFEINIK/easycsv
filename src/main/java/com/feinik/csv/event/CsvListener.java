package com.feinik.csv.event;

/**
 * Use read large csv data callback
 *
 * @author Feinik
 */
public abstract class CsvListener<T> {

    /**
     * read csv result data callback
     * @param object
     */
    public abstract void invoke(T object);

    /**
     * read complete callback
     */
    public abstract void complete();
}
