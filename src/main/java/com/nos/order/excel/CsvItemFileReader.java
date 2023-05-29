package com.nos.order.excel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

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
