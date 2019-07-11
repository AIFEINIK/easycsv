package com.feinik.csv;


import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.feinik.csv.analysis.CsvDataBuilder;
import com.feinik.csv.constant.CsvDelimiter;
import com.feinik.csv.context.CsvContext;
import com.feinik.csv.event.CsvListener;
import com.feinik.csv.exception.CsvUtilException;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;


/**
 * CSV is read and written through the tool
 *
 * @author Feinik
 */
public class CsvUtil {

    private static final int DEFAULT_SKIP_ROW = 1;

    /**
     *  Write the template object to CSV
     * @param filePath write file path
     * @param writeData write data
     * @param delimiter field separator
     * @param charset
     * @param <T>
     * @return
     */
    public static <T> boolean write(String filePath, List<T> writeData, char delimiter, Charset charset) {
        if (writeData == null) {
            throw new IllegalArgumentException("Parameter writeData can not be null");
        }

        return write(filePath, writeData, delimiter, charset, null);

    }

    /**
     * Write the template object to CSV, default delimiter is ','
     * @param filePath write file path
     * @param writeData write data
     * @param charset
     * @param <T>
     * @return
     */
    public static <T> boolean write(String filePath, List<T> writeData, Charset charset) {
        if (writeData == null) {
            throw new IllegalArgumentException("Parameter writeData can not be null");
        }

        return write(filePath, writeData, CsvDelimiter.symbol, charset, null);

    }

    /**
     *  Write the template object to CSV
     * @param filePath write file path
     * @param writeData write data
     * @param delimiter field separator
     * @param charset
     * @param context init CsvWriter
     * @param <T>
     */
    public static <T> boolean write(String filePath, List<T> writeData, char delimiter,
                                 Charset charset, CsvContext context) {
        if (writeData == null) {
            throw new IllegalArgumentException("Parameter writeData can not be null");
        }

        return writeWithContext(filePath, writeData, delimiter, charset, context);
    }

    /**
     * Write the template object to OutputStream
     * @param os Write stream
     * @param writeData write data
     * @param delimiter field separator
     * @param charset
     * @param <T>
     */
    public static <T> boolean write(OutputStream os, List<T> writeData, char delimiter,
                                 Charset charset) {
        if (writeData == null) {
            throw new IllegalArgumentException("Parameter writeData can not be null");
        }

        final CsvWriter writer = CsvFactory.getWriter(os, delimiter, charset);
        return write(writeData, null, writer);
    }

    /**
     * Write the template object to OutputStream
     * @param os Write stream
     * @param writeData write data
     * @param delimiter field separator
     * @param charset
     * @param context can init handle CsvWriter or CsvReader
     * @param <T>
     */
    public static <T> boolean write(OutputStream os, List<T> writeData, char delimiter,
                                 Charset charset, CsvContext context) {
        if (writeData == null) {
            throw new IllegalArgumentException("Parameter writeData can not be null");
        }

        final CsvWriter writer = CsvFactory.getWriter(os, delimiter, charset);
        return write(writeData, context, writer);
    }

    /**
     * Read small rows of data from CSV
     * @param filePath read file path
     * @param cls
     * @param skipRow Skip the x line read
     * @param charset
     * @param <T>
     * @return result data
     */
    public static <T> List<T> read(String filePath, Class<T> cls, int skipRow, Charset charset) throws Exception {
        final CsvReader reader = CsvFactory.getReader(filePath, charset);
        return readContext(reader, cls, skipRow, null);
    }

    /**
     * Read small rows of data from CSV, default skip head line
     * @param filePath read file path
     * @param cls
     * @param charset
     * @param <T>
     * @return result data
     */
    public static <T> List<T> read(String filePath, Class<T> cls, Charset charset) throws Exception {
        final CsvReader reader = CsvFactory.getReader(filePath, charset);
        return readContext(reader, cls, DEFAULT_SKIP_ROW, null);
    }

    /**
     * Read small rows of data from CSV, with context
     * @param filePath read file path
     * @param cls
     * @param skipRow Skip the x line read
     * @param charset
     * @param context can init handle CsvWriter or CsvReader
     * @param <T>
     * @return
     */
    public static <T> List<T> read(String filePath, Class<T> cls,
                            int skipRow, Charset charset, CsvContext context) throws Exception {

        final CsvReader reader = CsvFactory.getReader(filePath, charset);
        return readContext(reader, cls, skipRow, context);
    }

    /**
     * Read small rows of data from CSV, with context
     * @param filePath read file path
     * @param cls
     * @param delimiter field separator
     * @param skipRow Skip the x line read
     * @param charset
     * @param context can init handle CsvWriter or CsvReader
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> read(String filePath, Class<T> cls, char delimiter,
                                   int skipRow, Charset charset, CsvContext context) throws Exception {

        final CsvReader reader = CsvFactory.getReader(filePath, delimiter, charset);
        return readContext(reader, cls, skipRow, context);
    }

    /**
     * Read small rows of data from CSV, with context
     * @param is
     * @param cls
     * @param skipRow Skip the x line read
     * @param charset
     * @param context can init handle CsvWriter or CsvReader
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> read(InputStream is, Class<T> cls,
                                   int skipRow, Charset charset, CsvContext context) throws Exception {

        final CsvReader reader = CsvFactory.getReader(is, charset);
        return readContext(reader, cls, skipRow, context);
    }

    /**
     * Read small rows of data from CSV, with context
     * @param is
     * @param cls
     * @param delimiter field separator
     * @param skipRow Skip the x line read
     * @param charset
     * @param context
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> read(InputStream is, Class<T> cls, char delimiter,
                                   int skipRow, Charset charset, CsvContext context) throws Exception {

        final CsvReader reader = CsvFactory.getReader(is, delimiter, charset);
        return readContext(reader, cls, skipRow, context);
    }

    /**
     * Read small rows of data from CSV, with context.
     * default skip head line
     * @param filePath read file path
     * @param cls
     * @param charset
     * @param context Can init CsvWriter or CsvReader
     * @param <T>
     * @return
     */
    public static <T> List<T> read(String filePath, Class<T> cls,
                            Charset charset, CsvContext context) throws Exception {

        final CsvReader reader = CsvFactory.getReader(filePath, charset);
        return readContext(reader, cls, DEFAULT_SKIP_ROW, context);
    }

    /**
     * Read the large CSV file
     * @param filePath read file path
     * @param cls
     * @param skipRow Skip the x line read
     * @param charset
     * @param listener Synchronous call per read line
     * @param <T>
     */
    public static <T> void readOnListener(String filePath, Class<T> cls,
                                   int skipRow, Charset charset,
                                   CsvListener listener) throws Exception {

        final CsvReader reader = CsvFactory.getReader(filePath, charset);
        readOnListenerAndWithContext(reader, cls, skipRow, null, listener);
    }

    /**
     * Read the large CSV file
     * @param filePath
     * @param cls
     * @param delimiter
     * @param skipRow
     * @param charset
     * @param listener
     * @param <T>
     * @throws Exception
     */
    public static <T> void readOnListener(String filePath, Class<T> cls,
                                   char delimiter,
                                   int skipRow, Charset charset,
                                   CsvListener listener) throws Exception {

        final CsvReader reader = CsvFactory.getReader(filePath, delimiter, charset);
        readOnListenerAndWithContext(reader, cls, skipRow, null, listener);
    }

    /**
     * Read the large CSV file, default skip head line
     * @param filePath read file path
     * @param cls
     * @param charset
     * @param listener Synchronous call per read line
     * @param <T>
     */
    public static <T> void readOnListener(String filePath, Class<T> cls,
                                   Charset charset,
                                   CsvListener listener) throws Exception {

        final CsvReader reader = CsvFactory.getReader(filePath, charset);
        readOnListenerAndWithContext(reader, cls, DEFAULT_SKIP_ROW, null, listener);
    }

    /**
     * Read the large CSV file
     * @param filePath read file path
     * @param cls
     * @param skipRow Skip the x line read
     * @param charset
     * @param context Can init CsvWriter or CsvReader
     * @param listener Synchronous call per read line
     * @param <T>
     */
    public static <T> void readOnListener(String filePath, Class<T> cls,
                                   int skipRow, Charset charset,
                                   CsvContext context,
                                   CsvListener listener) throws Exception {

        final CsvReader reader = CsvFactory.getReader(filePath, charset);
        readOnListenerAndWithContext(reader, cls, skipRow, context, listener);
    }

    /**
     * Read the large CSV file
     * @param is
     * @param cls
     * @param skipRow
     * @param charset
     * @param context
     * @param listener
     * @param <T>
     */
    public static <T> void readOnListener(InputStream is, Class<T> cls,
                                   int skipRow, Charset charset,
                                   CsvContext context,
                                   CsvListener listener) throws Exception {

        final CsvReader reader = CsvFactory.getReader(is, charset);
        readOnListenerAndWithContext(reader, cls, skipRow, context, listener);
    }

    /**
     * Read the large CSV file
     * @param is
     * @param cls
     * @param delimiter
     * @param skipRow
     * @param charset
     * @param context
     * @param listener
     * @param <T>
     * @throws Exception
     */
    public static <T> void readOnListener(InputStream is, Class<T> cls, char delimiter,
                                          int skipRow, Charset charset,
                                          CsvContext context,
                                          CsvListener listener) throws Exception {

        final CsvReader reader = CsvFactory.getReader(is, delimiter, charset);
        readOnListenerAndWithContext(reader, cls, skipRow, context, listener);
    }

    /**
     * Read the large CSV file, default skip head line
     * @param filePath read file path
     * @param cls
     * @param charset
     * @param context Can init CsvWriter or CsvReader
     * @param listener Synchronous call per read line
     * @param <T>
     */
    public static <T> void readOnListener(String filePath, Class<T> cls,
                                   Charset charset,
                                   CsvContext context,
                                   CsvListener listener) throws Exception {

        final CsvReader reader = CsvFactory.getReader(filePath, charset);
        readOnListenerAndWithContext(reader, cls, DEFAULT_SKIP_ROW, context, listener);
    }

    /**
     *
     * @param filePath
     * @param writeData
     * @param delimiter
     * @param charset
     * @param context
     * @param <T>
     */
    private static <T> boolean writeWithContext(String filePath, List<T> writeData, char delimiter,
                                                Charset charset, CsvContext context) {
        if (writeData == null) {
            throw new IllegalArgumentException("Parameter writeData can not be null");
        }

        final CsvWriter writer = CsvFactory.getWriter(filePath, delimiter, charset);
        return write(writeData, context, writer);

    }

    private static <T> boolean write(List<T> writeData, CsvContext context, CsvWriter writer) {
        CsvDataBuilder builder = new CsvDataBuilder(context);
        boolean result = false;
        try {
            builder.write(writer, writeData);
            result = true;
        } catch (Exception e) {
            result = false;
            throw new CsvUtilException(e);

        } finally {
            if (writer != null) {
                writer.close();
            }
            return result;
        }
    }

    private static <T> void readOnListenerAndWithContext(CsvReader reader, Class<T> cls,
                                   int skipRow,
                                   CsvContext context,
                                   CsvListener listener) {
        try {
            for (int i = 0; i < skipRow; i++) {
                reader.readRecord();
            }

            CsvDataBuilder builder = new CsvDataBuilder(context);
            builder.readWithListener(reader, cls, listener);
        } catch (Exception e) {
            throw new CsvUtilException(e);

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private static <T> List<T> readContext(CsvReader reader,
                                           Class<T> cls, int skipRow, CsvContext context) {
        try {
            for (int i = 0; i < skipRow; i++) {
                reader.readRecord();
            }

            CsvDataBuilder builder = new CsvDataBuilder(context);
            return builder.read(reader, cls);
        } catch (Exception e) {
            throw new CsvUtilException(e);

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
