package org.project;

import java.util.Scanner;

public class App {

    void run(){
        System.out.println("""
                
                =========== CommandMonster ===========
                
                """);

        while (true){
            Scanner scanner = new Scanner(System.in);
            System.out.print("Command : ");
            String cmd = scanner.nextLine();
            switch (cmd){
                case "시작" -> System.out.println("게임을 시작합니다1");
                case "종료" -> {
                    return;
                }
                default -> System.out.println("<알림!>제시된 올바른 커멘드를 입력하세요.");
            }

        }


    }



}
