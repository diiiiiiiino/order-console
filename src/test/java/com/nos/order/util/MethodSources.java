package com.nos.order.util;

import java.util.Collections;
import java.util.stream.Stream;

public class MethodSources {
    static Stream listValidSource(){
        return Stream.of(
                null,
                Collections.EMPTY_LIST
        );
    }
}
