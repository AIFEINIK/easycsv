package com.feinik;


import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.feinik.analysis.CsvDataBuilder;
import com.feinik.constant.CsvDelimiter;
import com.feinik.context.CsvContext;
import com.feinik.exception.CsvUtilException;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Feinik
 * @discription CsvUtil
 * @date 2019/7/8
 * @since 1.0.0
 */
public class CsvUtil {

    /**
     *  Write the template object to CSV
     * @param filePath write file path
     * @param writeData write data
     * @param delimiter
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
     * @param filePath
     * @param writeData
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
     * @param delimiter
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
     * @param os stream
     * @param writeData
     * @param delimiter
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
     * @param os
     * @param writeData
     * @param delimiter
     * @param charset
     * @param context
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

    public <T> List<T> read(String filePath, Class<T> cls, int skipRow, Charset charset) {
        CsvReader reader = null;
        try {
            reader = CsvFactory.getReader(filePath, charset);
            for (int i = 0; i < skipRow; i++) {
                reader.readRecord();
            }

            CsvDataBuilder builder = new CsvDataBuilder(null);
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
