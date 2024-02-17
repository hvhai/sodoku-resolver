package com.codehunter.sodoku_resolver;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {
    @Test
    public void check() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);
        integerList.add(3);

        assertTrue(integerList.contains(Integer.parseInt("1")));

    }
}
