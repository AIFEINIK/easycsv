package com.feinik.analysis;

import com.csvreader.CsvWriter;
import com.feinik.annotation.CsvProperty;
import com.feinik.context.CsvContext;
import com.feinik.metadata.CsvColumnProperty;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Feinik
 * @discription
 * @date 2019/7/10
 * @since 1.0.0
 */
public class CsvDataBuilder {

    private CsvContext context;

    public CsvDataBuilder() {
    }

    public CsvDataBuilder(CsvContext context) {
        this.context = context;
    }

    public <T> CsvWriter write(CsvWriter writer, List<T> writeData) throws Exception {
        if (context != null) {
            context.initWriter(writer);

        }

        List<List<CsvColumnProperty>> allCol = new ArrayList<>();
        for (Object data : writeData) {
            if (data != null) {
                List<Field> fields = getAllFields(data.getClass());

                List<CsvColumnProperty> columnProperties = getCsvColumnProperties(data, fields);
                Collections.sort(columnProperties);
                allCol.add(columnProperties);
            }

        }

        List<String> head = new ArrayList<>();
        List<List<String>> contents = new ArrayList<>();
        for (int i = 0; i < allCol.size(); i++) {
            final List<CsvColumnProperty> csvColumnProperties = allCol.get(i);
            if (i == 0) {
                head = csvColumnProperties.stream().map(CsvColumnProperty::getHead).collect(Collectors.toList());
            }

            final List<String> content = csvColumnProperties.stream().map(p -> {
                final String value = p.getContent();
                final String format = p.getFormat();
                final String formatValue = MessageFormat.format(format == null ? "{0}" : format, value);
                return formatValue;
            }).collect(Collectors.toList());

            contents.add(content);
        }

        final String[] headArr = head.toArray(new String[head.size()]);
        writer.writeRecord(headArr);
        for (List<String> content : contents) {
            writer.writeRecord(content.toArray(new String[content.size()]));
        }

        return writer;
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
}
