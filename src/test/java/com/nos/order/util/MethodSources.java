package com.nos.order.util;

import com.nos.order.base.BaseTest;

import java.util.Collections;
import java.util.stream.Stream;

public class MethodSources extends BaseTest {
    static Stream listValidSource(){
        return Stream.of(
                null,
                Collections.EMPTY_LIST
        );
    }
}
