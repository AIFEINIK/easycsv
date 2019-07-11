package com.feinik;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.feinik.constant.CsvDelimiter;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author Feinik
 * @discription Reader and writer csv factory class
 * @date 2019/7/8
 * @since 1.0.0
 */
public class CsvFactory {

    /**
     *
     * @param filePath
     * @param delimiter
     * @param charset
     * @return CsvWriter
     */
    public static CsvWriter getWriter(String filePath, char delimiter, Charset charset) {
        return new CsvWriter(filePath, delimiter, charset);
    }

    /**
     *
     * @param filePath
     * @param charset
     * @return
     */
    public static CsvWriter getWriter(String filePath, Charset charset) {
        return new CsvWriter(filePath, CsvDelimiter.symbol, charset);
    }

    /**
     *
     * @param writer
     * @param delimiter
     * @return CsvWriter
     */
    public static CsvWriter getWriter(Writer writer, char delimiter) {
        return new CsvWriter(writer, delimiter);
    }

    /**
     *
     * @param os
     * @param delimiter
     * @param charset
     * @return
     */
    public static CsvWriter getWriter(OutputStream os, char delimiter, Charset charset) {
        return new CsvWriter(os, delimiter, charset);
    }

    /**
     *
     * @param filePath
     * @param delimiter
     * @param charset
     * @return
     * @throws Exception
     */
    public static CsvReader getReader(String filePath, char delimiter, Charset charset) throws Exception {
        return new CsvReader(filePath, delimiter, charset);
    }

    /**
     *
     * @param filePath
     * @param charset
     * @return
     * @throws Exception
     */
    public static CsvReader getReader(String filePath, Charset charset) throws Exception {
        return new CsvReader(filePath, CsvDelimiter.symbol, charset);
    }

    /**
     *
     * @param is
     * @param delimiter
     * @param charset
     * @return
     * @throws Exception
     */
    public static CsvReader getReader(InputStream is, char delimiter, Charset charset) throws Exception {
        return new CsvReader(is, delimiter, charset);
    }

    /**
     *
     * @param is
     * @param charset
     * @return
     * @throws Exception
     */
    public static CsvReader getReader(InputStream is, Charset charset) throws Exception {
        return new CsvReader(is, charset);
    }
}
