package com.codehunter.sodoku_resolver;

import com.codehunter.sodoku_resolver.model.Game;
import com.codehunter.sodoku_resolver.model.Node;

import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.buildTable();
        game.printTable();
        Node[][] table = game.getTable();
        String collect = game.getRectangle(table[5][3]).stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        System.out.println("================");
        System.out.println(collect);
        System.out.println("================");
        String collect1 = game.getMissingValue(table[5][3]).stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        System.out.println(collect1);
        System.out.println("================");
        game.fillAllPredictList();
        game.printTable();

//        System.out.println("================");
//        game.fillAllPredictList();
//        game.printTable();
//        System.out.println("isNotFullFill" + game.isNotFullFill());
//
//        System.out.println("================");
//        game.fillAllPredictList();
//        game.printTable();
//        System.out.println("isNotFullFill" + game.isNotFullFill());
//
//        System.out.println("================");
//        game.fillAllPredictList();
//        game.printTable();
//        System.out.println("isNotFullFill" + game.isNotFullFill());
//
//        System.out.println("================");
//        game.fillAllPredictList();
//        game.printTable();
//        System.out.println("isNotFullFill" + game.isNotFullFill());

//        Node[][] tableWithPredict = game.getTable();
        game.resolve();
        System.out.println("isNotFullFill" + game.isNotFullFill());
        game.printTable();

    }
}