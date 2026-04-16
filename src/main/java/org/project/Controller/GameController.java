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
        System.out.println(" [오박사] \"자, 여기 세 마리의 포켓몬이 있단다.");
        System.out.println(" 너의 여정을 함께할 파트너를 골라보려무나!\"");
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
                System.out.println("\n[오박사]  \"똑바로 알려줄래? 그런 포켓몬은 없는것 같단다.\"");
                choosePartner();
                return;
            }
        }
        inventory.addMonster(starter);
        System.out.println("\n[유저] " + starter.getName() + "을(를) 첫 번째 포켓몬으로 맞이했다!");
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
                    System.out.println("\n[유저] "+ name+"(이)가 떠나갔다. \"바이바이!\"\n\n");

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
        System.out.println("\n[유저] \"앗! 야생의 "+ wildMonster.getName()+"(이)가 나타났다.\"");
        System.out.println(" \"나와!  "+ myMonster.getName()+"!\"");
        while (true) {
                Util.delay(500);
                printMonsterStatus(wildMonster, "야생");
                printMonsterStatus(myMonster, "");
            Util.delay(500);
            System.out.println("\n▶1. 싸운다◀ | ▶2. 몬스터볼◀ | ▶3. 벗어난다.◀ | ▶4. 교체한다.◀ ");
            System.out.print("[메뉴 선택]: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    int result = gameService.startBattle(myMonster, wildMonster);

                    if (result == 1) {
                        Util.delay(500);// 야생 포켓몬 기절
                        System.out.println("\n[시스템] " + wildMonster.getName() + "이(가) 기절했다!");
                        int expAmount = gameService.calculateEarnedExp(wildMonster);
                        Util.delay(500);
                        System.out.println("[시스템] " + expAmount + " EXP를 획득했다!");

                        // 레벨업 체크 (연속 레벨업이 가능하도록 while로 변경하는 것을 추천합니다)
                        while (myMonster.getExpByBattle(expAmount)) {
                            myMonster.levelUp();
                            Util.delay(500);
                            System.out.println("[시스템] 레벨업! " + myMonster.getLevel() + "레벨이 되었다.");
                            // (getExpByBattle 내부에서 이미 exp가 더해졌기 때문)
                            expAmount = 0;
                        }
                        checkEvolution(myMonster);
                        return;
                    }

                    if (result == 2) { // 내 포켓몬 기절
                        myMonster = handlePlayerFaint(myMonster);
                        if (myMonster == null) return; // 전멸 시 전투 종료
                    }
                }
                case "2" -> {
                    // 1. 인벤토리 공간 확인
                    if (inventory.getMyMonsters().size() >= 6) {
                        System.out.println("\n[시스템] 인벤토리가 가득 찼습니다! 더 이상 잡을 수 없습니다.");
                        continue;
                    }

                    // 2. 몬스터볼 던지기 연출
                    System.out.println("\n[유저] \"가라! 몬스터볼!\"");
                    Util.delay(800);

                    for (int i = 0; i < 3; i++) {
                        System.out.print(" . ");
                        Util.delay(600);
                        System.out.print("흔들! ");
                        Util.delay(600);
                    }

                    // 3. 포획 판정
                    if (gameService.tryCapture(wildMonster)) {
                        System.out.println("\n\n [시스템] 찰카닥! " + wildMonster.getName() + "을(를) 잡았다!");

                        inventory.addMonster(wildMonster);

                        return; // 전투 종료 (메인 메뉴로)
                    } else {
                        System.out.println("\n\n [시스템] 뽀용! " + wildMonster.getName() + "볼에서 튀어 나왔다!");
                        Util.delay(500);
                        System.out.println("\n\n [유저] \"아깝다!\" 잡을 수 있었는데.");

                        // 포획 실패 시 야생 포켓몬의 반격 (턴 소모)
                        Util.delay(800);
                        gameService.executeTurn(wildMonster, myMonster);

                        // 내 포켓몬 기절 체크 (기존 기절 로직과 동일)
                        if (myMonster.getCurrentHp() <= 0) {
                            handlePlayerFaint(myMonster);
                            return;
                        }
                    }
                }
                case "3" -> {
                    System.out.println("\n이전화면으로 돌아갑니다.");
                    return;
                }
                case  "4" ->{
                    selectReplacement(myMonster);
                    gameService.executeTurn(wildMonster, myMonster);
                }
                default -> System.out.println("\n<알림>제시된 올바른 숫자를 입력하세요.");
            }
        }
    }
    private CommandMonster handlePlayerFaint(CommandMonster faintedMonster) {
        Util.delay(500);
        System.out.println("\n[시스템] " + faintedMonster.getName() + "이(가) 기절했다!");

        // 1. 모든 포켓몬이 기절했는지 확인 (전멸 체크)
        if (inventory.isAllFainted()) {
            Util.delay(800);
            System.out.println("\n[유저] \"더 이상 내보낼 포켓몬이 없어...!\"");
            Util.delay(500);
            System.out.println("[시스템] 눈앞이 깜깜해졌다! 급히 포켓몬 센터로 달려갔다.");
            Util.delay(1000);
            visitCenter();
            return null; // 전멸 상태 알림
        }

        // 2. 살아있는 포켓몬이 있다면 강제 교체 루프
        CommandMonster nextMonster = null;
        while (nextMonster == null) {
            System.out.println("\n[시스템] 다음 포켓몬을 선택해야 합니다.");
            nextMonster = selectReplacement(faintedMonster);

            if (nextMonster == null) {
                System.out.println("\n<알림> 기절한 상태에서는 도망칠 수 없습니다! 싸울 포켓몬을 고르세요.");
            }
        }

        Util.delay(500);
        System.out.println("\n[유저] \"나와! " + nextMonster.getName() + "!\"");
        return nextMonster;
    }
    private void checkEvolution(CommandMonster monster) {
        // 진화 조건: 진화 대상이 있고(id != 0), 현재 레벨이 진화 레벨 이상일 때
        if (monster.getEvolutionId() != 0 && monster.getLevel() >= monster.getEvolutionLevel()) {
            Util.delay(1000);
            System.out.println("\n[시스템] ......어라? " + monster.getName() + "의 상태가...!");

            // 진화 연출
            for (int i = 0; i < 3; i++) {
                Util.delay(600);
                System.out.print(" ✨ ");
            }
            System.out.println();

            // 데이터베이스에서 evolution_id에 해당하는 기본 포켓몬 정보 찾기
            CommandMonster evolvedBase = pokemonDatabase.stream()
                    .filter(p -> p.getId() == monster.getEvolutionId())
                    .findFirst()
                    .orElse(null);

            if (evolvedBase != null) {
                String oldName = monster.getName();
                monster.evolve(evolvedBase); // 진화 실행

                Util.delay(1000);
                System.out.println("\n[시스템] 축하합니다! " + oldName + "은(는) "
                        + monster.getName() + "(으)로 진화했다!");
                Util.delay(1000);
            }
        }
    }


    public CommandMonster selectReplacement(CommandMonster current) {
        while (true) {
            inventory.showMonsters(); // 현재 목록 출력
            System.out.println("\n[7. 취소(이전으로) | 번호 선택]");
            System.out.print("교체할 포켓몬의 번호를 입력하세요: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine()) - 1;
                if (choice == 7) return null; // 교체 취소

                CommandMonster selected = inventory.sendMonster(choice);

                if (selected == current) {
                    System.out.println("\n<알림> 이미 배틀 중인 포켓몬입니다!");
                } else if (selected.getCurrentHp() <= 0) {
                    System.out.println("\n<알림> 기절한 포켓몬은 내보낼 수 없습니다!");
                } else {
                    return selected; // 적절한 교체 대상 반환
                }
            } catch (Exception e) {
                System.out.println("\n<알림> 올바른 번호를 입력해주세요.");
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
        System.out.println("EXP) "+monster.getExp() + " / " +monster.getTotalExp());
        System.out.println("==========================================");
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