package com.codehunter.sodoku_resolver.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {
    private Integer value = 0;
    private Integer col;
    private Integer row;
    Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    // create a list from the array
    private List<Integer> predictList = new ArrayList<Integer>(Arrays.asList(array));

    public Node(Integer value, Integer col, Integer row) {
        this.value = value;
        this.col = col;
        this.row = row;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getCol() {
        return col;
    }

    public Integer getRow() {
        return row;
    }

    public List<Integer> getPredictList() {
        return predictList;
    }

    public void setPredictList(List<Integer> predictList) {
        this.predictList = predictList;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String template = """
                %s|""";
        String text = value == 0 ? " " : String.valueOf(value);
        return String.format(template, text);
    }

}
