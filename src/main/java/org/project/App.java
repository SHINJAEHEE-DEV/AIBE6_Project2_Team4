package org.project;

import org.project.Controller.GameController;
import org.project.Repository.PlayerRepository;
import org.project.domain.PlayerInventory;
import org.project.standard.util.Util;

import java.util.Scanner;

public class App {

    // ANSI 색상 코드
    String red = "\u001B[31m";
    String white = "\u001B[37m";
    String yellow = "\u001B[33m";
    String reset = "\u001B[0m";

    void run(){
        PlayerRepository playerRepository = new PlayerRepository();

        while (true){
            System.out.println("\n... COMMAND MONSTER 로딩 중 ...");
            Util.delay(500); // 0.5초 대기
            Scanner scanner = new Scanner(System.in);
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

            System.out.println(white + "\n                           [ v1.0 ]" + reset);
            System.out.println("              ------------------------------------");
            System.out.println("           ▶1.처음부터◀    ▶2.이어서 하기◀    ▶0.종료◀   ");
            System.out.println("                숫자를 입력하여 모험을 떠나세요!"  );
            System.out.println("              ------------------------------------");
            System.out.print("[메뉴선택] : ");
            String cmd = scanner.nextLine();

            switch (cmd){
                case "1" -> {
                    System.out.println("\n\n새로운 모험을 시작합니다!");
                    // 새 게임: 빈 인벤토리 객체와 isNewGame=true 플래그 전달
                    GameController gameController = new GameController(new PlayerInventory(), true);
                    gameController.start();
                }
                case "2" -> {
                    PlayerInventory loadedData = playerRepository.load();
                    if (loadedData == null) {
                        System.out.println("\n<알림> 리포트(저장된 데이터)가 없습니다. 처음부터 시작해주세요.");
                    } else {
                        System.out.println("\n\n이전 모험을 이어서 시작합니다!");
                        GameController gameController = new GameController(loadedData, false);
                        gameController.start();
                    }
                }
                case "0" -> {
                    System.out.println("\n게임을 종료합니다. 안녕히 가세요!");
                    return;
                }
                default -> System.out.println("\n<알림> 제시된 올바른 숫자를 입력하세요.");
            }
        }
    }
}