package com.nos.order.excel;

import com.nos.order.base.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ExcelReaderTest extends BaseTest {

    @Test
    @DisplayName("1-1. 엑셀 파일 read (File 존재)")
    void readExcelFile1_1() throws IOException {
        ItemFileReader fileReader = new CsvItemFileReader("excel/items.csv");

        List<String[]> results = fileReader.read();

        Assertions.assertTrue(results.size() > 1);
    }

    @Test
    @DisplayName("1-2. 엑셀 파일 read (헤더만 존재)")
    void readExcelFile1_2() throws IOException {
        ItemFileReader fileReader = new CsvItemFileReader("excel/items_only_header.csv");

        List<String[]> results = fileReader.read();

        Assertions.assertTrue(results.size() == 1);
    }

    @Test
    @DisplayName("1-3. 엑셀 파일 read (빈 파일)")
    void readExcelFile1_3() throws IOException {
        ItemFileReader fileReader = new CsvItemFileReader("excel/items_empty.csv");

        List<String[]> results = fileReader.read();

        Assertions.assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("2-1. 엑셀 파일 read (File 미 존재)")
    void readUnknownExcelFile2_1() {
        ItemFileReader fileReader = new CsvItemFileReader("excel/unknown.csv");

        Assertions.assertThrows(FileNotFoundException.class, () -> fileReader.read());
    }
}
