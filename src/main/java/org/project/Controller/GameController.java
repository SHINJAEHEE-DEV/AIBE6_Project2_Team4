package org.project.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.Service.GameService;
import org.project.domain.CommandMonster;
import org.project.domain.PlayerInventory;
import org.project.standard.util.Util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameController {
    private Scanner scanner = new Scanner(System.in);
    private PlayerInventory inventory = new PlayerInventory();
    private GameService gameService = new GameService();
    private List<CommandMonster> pokemonDatabase = new ArrayList<>();
    private Random random = new Random();

    public void start() {
        loadMostersData();
        Util.delay(500);
        System.out.println("[시스템] 포켓몬 데이터 " + pokemonDatabase.size() + "건 로드 완료.");
        choosePartner();
        mainMenu();
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
            case "1" -> starter = new CommandMonster(1,"이상해씨", "풀", 45, 49, 49, 65, 45, 16,2,5);
            case "2" -> starter = new CommandMonster(4,"파이리", "불꽃", 39, 52, 43, 50, 65, 16,5,5);
            case "3" -> starter = new CommandMonster(7, "꼬부기", "물", 44, 48, 65, 50, 43, 16,8,5);
            default -> {
                System.out.println("\n[오박사] :  똑바로 알려줄래? 그런 포켓몬은 없는것 같단다.");
                choosePartner();
                return;
            }
        }
        inventory.addMonster(starter);
        System.out.println("\n[시스템] " + starter.getName() + "을(를) 첫 번째 포켓몬으로 맞이했다!");
    }
    private void mainMenu() {
        while (true) {
            System.out.println("\n▶1. 모험하기◀ | ▶2. 포켓몬 센터◀ | ▶3. 내 포켓몬 목록◀ | ▶0. 종료◀");
            System.out.print("[메뉴 선택]: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> startAdventure();
                case "2" -> visitCenter();
                case "3" -> showMyMonstersMenu();
                case "0" -> {
                    System.out.println("\n시작화면으로 돌아갑니다.");
                    return;
                }
            }
        }
    }
    private void showMyMonstersMenu() {
        while (true) {
            Util.delay(500);
            // 1. 현재 목록 출력
            inventory.showMonsters(); //  출력 메서드 호출
            System.out.println("\n▶1. 순서바꾸기◀ | ▶2. 놓아주기◀ | ▶0. 이전으로◀");
            System.out.print("[메뉴 선택]: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    // 순서 바꾸기 로직
                    if (inventory.getMyMonsters().size() < 2) {
                        System.out.println("\n<알림> 순서를 바꿀 포켓몬이 부족합니다.");
                        continue;
                    }
                    try {
                            System.out.print("첫 번째 포켓몬 번호: ");
                            int first = Integer.parseInt(scanner.nextLine()) - 1; // 인덱스라 -1
                            System.out.print("두 번째 포켓몬 번호: ");
                            int second = Integer.parseInt(scanner.nextLine()) - 1;

                            inventory.swapMonsters(first, second);

                    } catch (NumberFormatException e) {
                            System.out.println("\n<알림> 숫자만 입력해주세요.");
                    }
                }
                case "2" -> {
                    if (inventory.getMyMonsters().size() < 2) {
                        System.out.println("\n<알림> 1마리 이상은 가지고 있어야 합니다.");
                        continue;
                    }
                    System.out.print("\n놓아줄 포켓몬 번호: ");
                    int releaseIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    String name = inventory.getMyMonsters().get(releaseIndex).getName();
                    inventory.releaseMonster(releaseIndex);
                    Util.delay(500);
                    System.out.println("\n...");
                    Util.delay(500);
                    System.out.println("\n[시스템] 내 친구 "+ name+"(이)가 떠나갔다. 바이바이!\n\n");

                }
                case "0" -> {
                    System.out.println("\n이전화면으로 돌아갑니다.");
                    return;
                }
                default -> System.out.println("\n<알림>제시된 올바른 숫자를 입력하세요.");
            }
        }
    }

    private void visitCenter() {
        System.out.println("\n[포켓몬 센터] 간호순: 안녕하세요! 포켓몬 센터입니다.");
        System.out.println("잠시만 기다려 주세요. 포켓몬을 치료해 드릴게요.");

        Util.delay(500);
        System.out.print("치료 중...");
        for(int i = 0; i < 3; i++) {
            Util.delay(400);
            System.out.print("띵~♪ ♬");
        }
        System.out.println("\n");
        Util.delay(500);

        // 인벤토리 내 모든 포켓몬 회복 로직
        for (CommandMonster m : inventory.getMyMonsters()) {
            m.setCurrentHp(m.getMaxHp());
        }

        System.out.println("띠리리링! ♬ 모든 포켓몬이 건강해졌습니다!");
        Util.delay(800);
    }

    private void startAdventure() {
        Util.delay(500);
        CommandMonster wildMonster =  getRandomMonster(inventory.getAvgLevel());
        CommandMonster myMonster = inventory.sendMonster();
        System.out.println("\n[시스템] 앗! 야생의 "+ wildMonster.getName()+"(이)가 나타났다.");
        System.out.println(" 나와!  "+ myMonster.getName()+"!");
        while (true) {
            if(monsterOrTrainer()){
                Util.delay(500);
                printMonsterStatus(wildMonster, "야생");
                printMonsterStatus(myMonster, "");
            }else{
                //플레이어 조우시
                continue;
            }

            Util.delay(500);
            System.out.println("\n▶1. 싸운다◀ | ▶2. 몬스터볼◀ | ▶3. 벗어난다.◀ ");
            System.out.print("[메뉴 선택]: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {

                }
                case "2" -> {

                }
                case "3" -> {
                    System.out.println("\n이전화면으로 돌아갑니다.");
                    return;
                }
                default -> System.out.println("\n<알림>제시된 올바른 숫자를 입력하세요.");
            }
        }
    }


    private void printMonsterStatus(CommandMonster monster, String tag) {
        String red = "\u001B[31m";
        String green = "\u001B[32m";
        String yellow = "\u001B[33m";
        String reset = "\u001B[0m";

        // 체력 비율 계산 (체력 바 표시용)
        double hpRatio = (double) monster.getCurrentHp() / monster.getMaxHp();
        int barCount = (int) (hpRatio * 20); // 20칸 기준 체력 바
        String hpBar = "■".repeat(barCount) + "-".repeat(20 - barCount);

        // 체력 색상 결정
        String hpColor = (hpRatio > 0.5) ? green : (hpRatio > 0.2) ? yellow : red;

        System.out.println((tag.equals("야생") ? red : green) + "========= [" + tag + " 포켓몬 정보] =========" + reset);
        System.out.printf(" 이름 : %-10s  Lv.%3d  타입 : %s\n", monster.getName(), monster.getLevel(), monster.getType());
        System.out.print(" HP   : [");
        System.out.print(hpColor + hpBar + reset);
        System.out.printf("] %d / %d\n", monster.getCurrentHp(), monster.getMaxHp());
        System.out.println(" 종족값: ATK %d | SP %d |DEF %d | SPD %d".formatted(monster.getAtk(), monster.getSp(), monster.getBaseDef(),monster.getBaseSpd()));
        System.out.println("==========================================");
    }


    private boolean monsterOrTrainer(){
        int randomNum = random.nextInt(20);
        boolean isMonster = randomNum != 0;
        return isMonster;
    }


    // 야생 포켓몬 한 마리 무작위 추출
    public CommandMonster getRandomMonster(int avgLevel) {

        int index = random.nextInt(pokemonDatabase.size());
        CommandMonster base = pokemonDatabase.get(index);

        while(base.getEvolutionId() == 0 && avgLevel < 30) { //초반에 진화체 나오는것 방지
            index = random.nextInt(pokemonDatabase.size());
            base = pokemonDatabase.get(index);
        }

        // 레벨 결정 (평균 레벨 기준 +-2)
        int wildLevel = Math.max(1, avgLevel + random.nextInt(5) - 2);
        // 원본 종족값을 이용해 새로운 인스턴스 생성
        return new CommandMonster(
                base.getId(),
                base.getName(),
                base.getType(),
                base.getBaseHp(),
                base.getBaseAtk(),
                base.getBaseDef(),
                base.getBaseSp(),
                base.getBaseSpd(),
                base.getEvolutionLevel(),
                base.getEvolutionId(),
                wildLevel
        );
    }



    private void loadMostersData() {
        try{
            Reader reader = new FileReader("src/main/resources/pokemonDatabase.json");
            Gson gson = new Gson();
            // TypeToken을 사용하여 List<CommandMonster> 임을 명시적으로 선언
            Type monsterListType = new TypeToken<ArrayList<CommandMonster>>(){}.getType();
            pokemonDatabase = gson.fromJson(reader, monsterListType);
        }catch (FileNotFoundException e){
            throw new RuntimeException("파일 읽기 중 오류 발생", e);
        }

    }

}