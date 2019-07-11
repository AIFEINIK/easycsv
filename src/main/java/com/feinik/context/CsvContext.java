package com.feinik.context;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

/**
 * @author Feinik
 * @discription
 * @date 2019/7/10
 * @since 1.0.0
 */
public interface CsvContext {

    void initWriter(CsvWriter writer);

    void initReader(CsvReader reader);
}
