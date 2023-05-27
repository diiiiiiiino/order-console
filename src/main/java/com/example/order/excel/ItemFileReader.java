package com.example.order.excel;

import java.io.IOException;
import java.util.List;

public interface ItemFileReader {
    List<String[]> read() throws IOException;
}
