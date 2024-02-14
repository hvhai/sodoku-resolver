package com.codehunter.sodoku_resolver.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    public static final String EXAMPLE = """
            530070000
            600195000
            098000060
            800060003
            400803001
            700020006
            060000280
            000419005
            000080079
            """;
    public static final String EXAMPLE1 = """
            060039000
            302000905
            190000600
            080704132
            000000590
            010000700
            920403070
            403000050
            000060300
            """;
    private String content;
    private Node[][] table = new Node[9][9];

    public Node[][] getTable() {
        return table;
    }

    public void buildTable() {
        String[] split = EXAMPLE1.split("\n");
        for (int row = 0; row < split.length; row++) {
            char[] charArray = split[row].toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                this.table[row][col] = new Node(Integer.parseInt(String.valueOf(charArray[col])), col, row);
            }
        }
    }

    public void printTable() {
        Arrays.stream(table).forEach(nodes -> {
            Arrays.stream(nodes)
                    .forEach(node -> System.out.print(node.toString()));
            System.out.print("\n");
        });
    }

    public List<Integer> getRectangle(Node node) {
        List<Integer> list = new ArrayList<>();
        int colStart = node.getCol() / 3;
        int rowStart = node.getRow() / 3;
        for (int i = rowStart * 3; i < rowStart * 3 + 3; i++) {
            for (int j = colStart * 3; j < colStart * 3 + 3; j++) {
                if (table[i][j].getValue() != 0) {
                    list.add(table[i][j].getValue());
                }
            }
        }
        return list;
    }

    public List<Integer> getMissingValue(Node currentNode) {
        System.out.println(String.format("getMissingValue Node (%d,%d): value - %d", currentNode.getRow(), currentNode.getCol(), currentNode.getValue()));
        if (currentNode.getValue() != 0) {
            return Collections.emptyList();
        }
//        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
//        // create a list from the array
//        ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(array));
        List<Integer> list = currentNode.getPredictList();

        // remove existent row

        List<Node> rowNode = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Node node = table[currentNode.getRow()][i];
            rowNode.add(node);
            list.remove(node.getValue());
        }
        String rowList = rowNode.stream()
                .map(Node::getValue)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        System.out.println("rowList:" + rowList);

        // remove existent column
        List<Node> colNode = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Node node = table[i][currentNode.getCol()];
            colNode.add(node);
            list.remove(node.getValue());
        }
        String colList = colNode.stream()
                .map(Node::getValue)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        System.out.println("colList:" + colList);

        List<Integer> rectangleList = getRectangle(currentNode);
        rectangleList.forEach(list::remove);
        String recList = rectangleList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        System.out.println("recList:" + recList);

        String missingList = list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        System.out.println("missingList:" + missingList);
        return list;
    }

    public void fillAllPredictList() {
        Arrays.stream(table).forEach(nodes -> {
            Arrays.stream(nodes)
                    .forEach(node -> {
                        List<Integer> missingValue = this.getMissingValue(node);
                        node.setPredictList(missingValue);
                        if (missingValue.size() == 1) {
                            node.setValue(missingValue.get(0));
                            System.out.println(String.format("set value for node %d %d value %d", node.getRow(), node.getCol(), node.getValue()));
                        }
                    });
        });
    }

    public boolean isNotFullFill() {
        return Arrays.stream(table)
                .anyMatch(nodes -> Arrays.stream(nodes)
                        .anyMatch(node -> node.getPredictList().size() != 0));
    }

    public void resolve() {
        while (isNotFullFill()) {
            System.out.println("table not full fill");
            fillAllPredictList();
            printTable();
        }
    }
}
