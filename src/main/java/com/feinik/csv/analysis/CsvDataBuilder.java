package com.feinik.csv.analysis;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.feinik.csv.annotation.CsvProperty;
import com.feinik.csv.context.CsvContext;
import com.feinik.csv.event.CsvListener;
import com.feinik.csv.metadata.CsvColumnProperty;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * read and writ data build
 *
 * @author Feinik
 */
public class CsvDataBuilder {

    private static Logger log = LoggerFactory.getLogger(CsvDataBuilder.class);

    private static NumberFormat numberFormat = new DecimalFormat();

    private CsvContext context;

    public CsvDataBuilder() {
    }

    public CsvDataBuilder(CsvContext context) {
        this.context = context;
    }

    /**
     *
     * @param reader CsvReader
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> read(CsvReader reader, Class<T> cls) throws Exception {
        readBefore(reader, cls);

        List<T> result = new ArrayList<>();
        while (reader.readRecord()) {
            try {
                T target = cls.newInstance();
                readRowDataToObject(reader, target);

                result.add(target);
            } catch (Exception e) {
                log.error("Read csv data failed, cause is:{}", e.toString());
            }
        }
        return result;
    }

    private <T> void readBefore(CsvReader reader, Class<T> cls) throws Exception {
        if (context != null) {
            context.initReader(reader);

        }

        final List<CsvColumnProperty> columnProperties = getCsvColumnProperties(cls.newInstance(), getAllFields(cls));
        Collections.sort(columnProperties);

        final List<String> heads = columnProperties.stream().map(CsvColumnProperty::getHead).collect(Collectors.toList());
        final String[] newHeaders = heads.toArray(new String[heads.size()]);
        final String[] headers = reader.getHeaders();
        if (headers == null || headers.length == 0) {
            reader.setHeaders(newHeaders);
        }
    }

    /**
     *
     * @param reader
     * @param cls
     * @param listener Synchronous call per read line
     * @param <T>
     * @throws Exception
     */
    public <T> void readWithListener(CsvReader reader, Class<T> cls, CsvListener listener) throws Exception {
        readBefore(reader, cls);

        if (listener != null) {
            while (reader.readRecord()) {
                try {
                    T target = cls.newInstance();
                    readRowDataToObject(reader, target);

                    listener.invoke(target);
                } catch (Exception e) {
                    log.error("Read csv data failed, cause is:{}", e.toString());
                }
            }
            listener.complete();
        }

    }

    /**
     *
     * @param reader
     * @param target
     * @param <T>
     * @throws IOException
     * @throws IllegalAccessException
     */
    private <T> void readRowDataToObject(CsvReader reader, T target) throws IOException, IllegalAccessException {
        for (Field field : getAllFields(target.getClass())) {
            field.setAccessible(true);
            String headName = getHeadName(field);
            final String value = reader.get(headName);

            final Class<?> fieldType = field.getType();
            if (value != null) {
                if (fieldType == Long.class) {
                    field.set(target, formatParse(value).longValue());

                } else if (fieldType == Integer.class) {
                    field.set(target, formatParse(value).intValue());

                } else if (fieldType == Double.class) {
                    field.set(target, formatParse(value).doubleValue());

                } else if (fieldType == Short.class) {
                    field.set(target, formatParse(value).shortValue());

                } else if (fieldType == Float.class) {
                    field.set(target, formatParse(value).floatValue());

                } else {
                    field.set(target, value);
                }
            }
        }
    }

    /**
     *
     * @param writer CsvWriter
     * @param writeData
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> CsvWriter write(CsvWriter writer, boolean containHead, List<T> writeData) throws Exception {
        if (context != null) {
            context.initWriter(writer);

        }

        List<List<CsvColumnProperty>> allCol = new ArrayList<>();
        for (Object data : writeData) {
            if (data != null) {
                List<Field> fields = getAllFields(data.getClass());

                allCol.add(getCsvColumnProperties(data, fields));
            }

        }

        List<List<String>> contents = getContents(allCol);

        if (containHead) {
            //write head line
            writer.writeRecord(getHeadArray(allCol.get(0)));
        }

        for (List<String> content : contents) {
            writer.writeRecord(content.toArray(new String[content.size()]));
        }

        return writer;
    }

    private List<List<String>> getContents(List<List<CsvColumnProperty>> allCol) {
        List<List<String>> contents = new ArrayList<>();
        for (List<CsvColumnProperty> columnProperties : allCol) {
            final List<String> content = columnProperties.stream().map(p -> {
                final String value = p.getContent();
                final String format = p.getFormat();
                final String formatValue = MessageFormat.format(format == null ? "{0}" : format, value);
                return formatValue;
            }).collect(Collectors.toList());

            contents.add(content);
        }
        return contents;
    }

    private String[] getHeadArray(List<CsvColumnProperty> columnProperties) {
        final List<String> headers = columnProperties.stream().map(CsvColumnProperty::getHead).collect(Collectors.toList());
        return headers.toArray(new String[headers.size()]);
    }

    /**
     *
     * @param obj
     * @param fields
     * @return
     * @throws IllegalAccessException
     */
    private List<CsvColumnProperty> getCsvColumnProperties(Object obj, List<Field> fields) {
        List<CsvColumnProperty> columnProperties = new ArrayList<>();
        CsvColumnProperty ccp;
        CsvProperty csvProperty;
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                ccp = new CsvColumnProperty();
                final Object value = field.get(obj);
                ccp.setContent(value == null ? StringUtils.EMPTY : String.valueOf(value));
                ccp.setHead(field.getName());

                csvProperty = field.getAnnotation(CsvProperty.class);
                if (csvProperty != null) {
                    if (StringUtils.isNotEmpty(csvProperty.value())) {
                        ccp.setHead(csvProperty.value());
                    }
                    ccp.setFormat(csvProperty.format());
                    ccp.setIndex(csvProperty.index());
                }
                columnProperties.add(ccp);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Collections.sort(columnProperties);
        return columnProperties;
    }

    /**
     * Gets all the properties of the object including the parent class properties
     * @param cls
     * @return all Field
     */
    private List<Field> getAllFields(Class<?> cls) {
        List<Field> fields = new ArrayList<>();
        while (cls != null) {
            final Field[] fd = cls.getDeclaredFields();
            fields.addAll(Arrays.asList(fd));
            cls = cls.getSuperclass();
        }
        return fields;
    }

    private Number formatParse(String text) {
        Number parse = null;
        try {
            parse = numberFormat.parse(text.trim());
        } catch (ParseException e) {
            log.error("parse data err.", e);
        }
        return parse;
    }

    private String getHeadName(Field field) {
        String headName = field.getName();
        final CsvProperty cp = field.getAnnotation(CsvProperty.class);
        if (cp != null && StringUtils.isNotEmpty(cp.value())) {
            headName = cp.value();
        }
        return headName;
    }
}
