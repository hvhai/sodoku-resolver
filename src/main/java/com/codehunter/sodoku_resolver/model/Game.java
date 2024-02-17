package com.codehunter.sodoku_resolver.model;

import java.util.*;
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

    public Game(String content) {
        this.content = content;
    }

    public Node[][] getTable() {
        return table;
    }

    public void buildTable() {
        String[] split = this.content.split(System.lineSeparator());
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

    public List<Node> getRectangleNode(Node node) {
        List<Node> list = new ArrayList<>();
        int colStart = node.getCol() / 3;
        int rowStart = node.getRow() / 3;
        for (int i = rowStart * 3; i < rowStart * 3 + 3; i++) {
            for (int j = colStart * 3; j < colStart * 3 + 3; j++) {
                if (table[i][j].getValue() != 0) {
                    list.add(table[i][j]);
                }
            }
        }
        return list;
    }

    public List<Node> getMissingRectangleNode(Node node) {
        List<Node> list = new ArrayList<>();
        int colStart = node.getCol() / 3;
        int rowStart = node.getRow() / 3;
        for (int i = rowStart * 3; i < rowStart * 3 + 3; i++) {
            for (int j = colStart * 3; j < colStart * 3 + 3; j++) {
                if (table[i][j].getValue() == 0) {
                    list.add(table[i][j]);
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

    public void isOnlyPossibleValueInRectangle(Node currentNode) {
        List<Integer> missingValues = currentNode.getPredictList();
        List<Node> rectangleList = getMissingRectangleNode(currentNode);
        rectangleList.remove(currentNode);
        HashMap<Integer, List<Node>> map = new HashMap<>();
        for (Integer value : missingValues) {
            List<Node> list = new ArrayList<>();
            map.put(value, list);
            for (Node node : rectangleList) {
                if (node.getPredictList().stream().anyMatch(integer -> integer.equals(value))) {
                    list.add(node);
                }
            }
        }

        for (Integer key : map.keySet()) {
            if (map.get(key).size() == 0) {
                System.out.println(String.format("Crosscheck: set value for node %d %d value %d", currentNode.getRow(), currentNode.getCol(), key));
                currentNode.setValue(key);
                currentNode.setPredictList(Collections.emptyList());
                printTable();
                fillAllPredictList();
                return;
            }
        }
    }

    public List<Node> getMissingVerticalNode(Node node) {
        List<Node> list = new ArrayList<>();
        int colStart = node.getCol();
        int rowStart = 0;
        for (int i = rowStart; i < 9; i++) {
            if (table[i][colStart].getValue() == 0) {
                list.add(table[i][colStart]);
            }
        }
        return list;
    }

    public void isOnlyPossibleValueInVertical(Node currentNode) {
        if (currentNode.getValue() != 0) return;

        List<Integer> missingValues = currentNode.getPredictList();
        List<Node> verticleList = getMissingVerticalNode(currentNode);
        verticleList.remove(currentNode);
        HashMap<Integer, List<Node>> map = new HashMap<>();
        for (Integer value : missingValues) {
            List<Node> list = new ArrayList<>();
            map.put(value, list);
            for (Node node : verticleList) {
                if (node.getPredictList().stream().anyMatch(integer -> integer.equals(value))) {
                    list.add(node);
                }
            }
        }

        for (Integer key : map.keySet()) {
            if (map.get(key).size() == 0) {
                System.out.println(String.format("Crosscheck Vertical: set value for node %d %d value %d", currentNode.getRow(), currentNode.getCol(), key));
                currentNode.setValue(key);
                currentNode.setPredictList(Collections.emptyList());
                printTable();
                fillAllPredictList();
                return;
            }
        }
    }

    public List<Node> getMissingHorizontalNode(Node node) {
        List<Node> list = new ArrayList<>();
        int colStart = 0;
        int rowStart = node.getRow();
        for (int i = colStart; i < 9; i++) {
            if (table[rowStart][i].getValue() == 0) {
                list.add(table[rowStart][i]);
            }
        }
        return list;
    }

    public void isOnlyPossibleValueInHorizontal(Node currentNode) {
        if (currentNode.getValue() != 0) return;

        List<Integer> missingValues = currentNode.getPredictList();
        List<Node> verticleList = getMissingHorizontalNode(currentNode);
        verticleList.remove(currentNode);
        HashMap<Integer, List<Node>> map = new HashMap<>();
        for (Integer value : missingValues) {
            List<Node> list = new ArrayList<>();
            map.put(value, list);
            for (Node node : verticleList) {
                if (node.getPredictList().stream().anyMatch(integer -> integer.equals(value))) {
                    list.add(node);
                }
            }
        }
        System.out.println("Crosscheck Horizontal: verticalList" + verticleList.stream().map(Node::display).collect(Collectors.joining(",")));

        for (Integer key : map.keySet()) {
            if (map.get(key).size() == 0) {
                System.out.println(String.format("Crosscheck Horizontal: set value for node %d %d value %d", currentNode.getRow(), currentNode.getCol(), key));
                currentNode.setValue(key);
                currentNode.setPredictList(Collections.emptyList());
                printTable();
                fillAllPredictList();
                return;
            }
        }
    }

    public void crossCheck() {
        Arrays.stream(table).forEach(nodes -> {
            Arrays.stream(nodes)
                    .forEach(node -> {
                        if (node.getValue() == 0 && !node.getPredictList().isEmpty()) {
                            System.out.println("cross check found" + node.display());
                            isOnlyPossibleValueInRectangle(node);
                            isOnlyPossibleValueInVertical(node);
                            isOnlyPossibleValueInHorizontal(node);
                        }
                    });
        });
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
                            fillAllPredictList();
                        }
                        removeDuplicateMissing(node);
                    });
        });
    }

    public void removeDuplicateMissing(Node currentNode) {
        List<Node> missingRectangleNode = getMissingRectangleNode(currentNode);
        if (missingRectangleNode.size() > 1) {
            List<Integer> tempPredictList = missingRectangleNode.get(0).getPredictList();
            boolean hasSameMissingListWithSize = true;
            for (Node node : missingRectangleNode) {
                if (!node.getPredictList().containsAll(tempPredictList)
                        || node.getPredictList().containsAll(tempPredictList) && node.getPredictList().size() != tempPredictList.size()) {
                    hasSameMissingListWithSize = false;
                    break;
                }
            }
            if (hasSameMissingListWithSize) {
                boolean hasSameRow = missingRectangleNode.stream()
                        .map(Node::getRow)
                        .collect(Collectors.toSet())
                        .size() == 1;
                if (hasSameRow) {
                    List<Node> missingHorizontalNode = getMissingHorizontalNode(currentNode);
                    missingHorizontalNode.removeAll(missingRectangleNode);
                    for(Node node : missingHorizontalNode) {
                        node.getPredictList().removeAll(tempPredictList);
                    }
                }
                boolean hasSameCol = missingRectangleNode.stream()
                        .map(Node::getCol)
                        .collect(Collectors.toSet())
                        .size() == 1;
                if (hasSameCol) {
                    List<Node> missingVerticalNode = getMissingVerticalNode(currentNode);
                    missingVerticalNode.removeAll(missingRectangleNode);
                    for(Node node : missingVerticalNode) {
                        node.getPredictList().removeAll(tempPredictList);
                    }
                }
            }
        }
    }

    public boolean isNotFullFill() {
        return Arrays.stream(table)
                .anyMatch(nodes -> Arrays.stream(nodes)
                        .anyMatch(node -> node.getPredictList().size() != 0));
    }

    public void resolve() {
        Scanner scanner = new Scanner(System.in);
//        while (isNotFullFill() && scanner.nextInt() == 1) {
        while (isNotFullFill()) {
            System.out.println("table not full fill");
            fillAllPredictList();
            crossCheck();
            printTable();
        }
    }
}
