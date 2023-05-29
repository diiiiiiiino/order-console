package com.nos.order.excel;

import java.io.IOException;
import java.util.List;

public interface ItemFileReader {
    List<String[]> read() throws IOException;
}
