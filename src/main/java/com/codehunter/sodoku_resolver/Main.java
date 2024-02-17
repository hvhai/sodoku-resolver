package com.codehunter.sodoku_resolver;

import com.codehunter.sodoku_resolver.model.Game;
import com.codehunter.sodoku_resolver.model.Node;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        InputStream is = Main.class.getClassLoader().getResourceAsStream("data.txt");
        String content = IOUtils.toString(is, "UTF-8");

        Game game = new Game(content);
        // ============================
//        Node[][] table = game.getTable();
//        String collect = game.getRectangle(table[5][3]).stream()
//                .map(String::valueOf)
//                .collect(Collectors.joining(","));
//        System.out.println("================");
//        System.out.println(collect);
//        System.out.println("================");
//        String collect1 = game.getMissingValue(table[5][3]).stream()
//                .map(String::valueOf)
//                .collect(Collectors.joining(","));
//        System.out.println(collect1);
//        System.out.println("================");
//        game.fillAllPredictList();
//        game.printTable();
        // ============================
        game.buildTable();
        game.printTable();
        System.out.println("===START====");
        game.resolve();
        System.out.println("===END====");
        game.printTable();
    }


}