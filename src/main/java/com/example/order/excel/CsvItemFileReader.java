package com.example.order.excel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CsvItemFileReader implements ItemFileReader {
    String path;

    public CsvItemFileReader(@Value("${excel.path}") String path) {
        this.path = path;
    }

    @Override
    public List<String[]> read() throws IOException {
        Resource resource = new ClassPathResource(path);
        List<String[]> results = new ArrayList<>();

        try (FileReader fileReader = new FileReader(resource.getFile());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                results.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return results;
    }
}
