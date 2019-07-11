package com.feinik.csv.context;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

/**
 * Use init handle CsvWriter or CsvReader
 *
 * @author Feinik
 */
public interface CsvContext {

    void initWriter(CsvWriter writer);

    void initReader(CsvReader reader);
}
