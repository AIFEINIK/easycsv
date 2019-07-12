package com.feinik.csv;


import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.feinik.csv.analysis.CsvDataBuilder;
import com.feinik.csv.context.CsvContext;
import com.feinik.csv.event.CsvListener;
import com.feinik.csv.exception.CsvUtilException;

import java.io.InputStream;
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
     * @param writer get by CsvFactory
     * @param writeData write data
     * @param containHead whether to write to the head line
     * @param <T>
     * @return
     */
    public static <T> boolean write(CsvWriter writer, List<T> writeData, boolean containHead) {
        return write(writer, writeData, containHead, null);
    }

    /**
     *  Write the template object to CSV
     * @param writer get by CsvFactory
     * @param writeData write data
     * @param containHead whether to write to the head line
     * @param context can init handle CsvWriter or CsvReader
     * @param <T>
     * @return
     */
    public static <T> boolean write(CsvWriter writer, List<T> writeData,
                                    boolean containHead, CsvContext context) {
        CsvDataBuilder builder = new CsvDataBuilder(context);
        boolean result = false;
        try {
            builder.write(writer, containHead, writeData);
            result = true;
        } catch (Exception e) {
            throw new CsvUtilException(e);

        } finally {
            return result;
        }
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
