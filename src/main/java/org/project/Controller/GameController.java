package org.project.Controller;

import org.project.domain.CommandMonster;
import org.project.domain.PlayerInventory;
import org.project.Service.MonsterService;
import java.util.Scanner;

public class GameController {
    private Scanner scanner = new Scanner(System.in);
    private PlayerInventory inventory = new PlayerInventory();
    private MonsterService monsterService = new MonsterService();

    public void start() {
        choosePartner();
    }

    // --- [페이지 1: 파트너 포켓몬 선택] ---
    private void choosePartner() {
        System.out.println("\n====================================");
        System.out.println(" 오박사: 자, 여기 세 마리의 포켓몬이 있단다.");
        System.out.println(" 너의 여정을 함께할 파트너를 골라보려무나!");
        System.out.println("====================================");
        System.out.println(" 1. 이상해씨 (풀) | 2. 파이리 (불꽃) | 3. 꼬부기 (물)");
        System.out.print(" 번호 선택: ");

        String choice = scanner.nextLine();
        CommandMonster starter = null;

        switch (choice) {
            case "1" -> starter = new CommandMonster("이상해씨", "풀", 45, 49, 49, 65, 45, 5);
            case "2" -> starter = new CommandMonster("파이리", "불꽃", 39, 52, 43, 50, 65, 5);
            case "3" -> starter = new CommandMonster("꼬부기", "물", 44, 48, 65, 50, 43, 5);
            default -> {
                System.out.println("\n오박사 :  똑바로 알려줄래? 그런 포켓몬은 없는것 같단다.");
                choosePartner();
                return;
            }
        }
        System.out.println("\n[시스템] " + starter.getName() + "을(를) 첫 번째 포켓몬으로 맞이했습니다!");
    }


}