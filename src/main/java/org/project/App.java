package org.project;

import org.project.Controller.GameController;

import java.util.Scanner;

public class App {

    // ANSI 색상 코드
    String red = "\u001B[31m";
    String white = "\u001B[37m";
    String yellow = "\u001B[33m";
    String reset = "\u001B[0m";
    void run(){
        System.out.println(yellow + """
             _____                                          _   ___  ___                 _             \s
            /  __ \\                                        | |  |  \\/  |                | |            \s
            | /  \\/ ___  _ __ ___  _ __ ___   __ _ _ __   __| |  | .  . | ___  _ __  ___| |_ ___ _ __ \s
            | |    / _ \\| '_ ` _ \\| '_ ` _ \\ / _` | '_ \\ / _` |  | |\\/| |/ _ \\| '_ \\/ __| __/ _ \\ '__|\s
            | \\__/\\ (_) | | | | | | | | | | | (_| | | | | (_| |  | |  | | (_) | | | \\__ \\ ||  __/ |   \s
             \\____/\\___/|_| |_| |_|_| |_| |_|\\__,_|_| |_|\\__,_|  \\_|  |_/\\___/|_| |_|___/\\__\\___|_|   \s
            """ + reset);

        // 빨간색 윗부분 몬스터볼
        System.out.println(red + "                          .----------." + reset);
        System.out.println(red + "                       /              \\" + reset);
        System.out.println(red + "                      /                \\" + reset);
        System.out.println(red + "                     |      .----.      |" + reset);

        // 중앙 및 아랫부분 (흰색)
        System.out.println("                     |==   |      |    ==|");
        System.out.println("                     |      '----'      |");
        System.out.println("                      \\                /");
        System.out.println("                       \\              /");
        System.out.println("                        '------------'");

        System.out.println(white + "\n               [ 1세대 관동버전 v1.0 ]" + reset);
        System.out.println("              ------------------------------------");
        System.out.println("                 ▶1.처음부터◀          ▶2.죵료◀   ");
        System.out.println("                숫자를 입력하여 모험을 떠나세요!"  );
        System.out.println("              ------------------------------------");

        while (true){
            Scanner scanner = new Scanner(System.in);
            System.out.print("Command : ");
            String cmd = scanner.nextLine();
            GameController gameController = new GameController();
            switch (cmd){
                case "1" -> {
                    System.out.println("모험을 시작합니다!");
                    gameController.start();
                }
                case "2" -> {
                    return;
                }
                default -> System.out.println("<알림!>제시된 올바른 커멘드를 입력하세요.");
            }

        }


    }



}
