package org.project.Controller;

import org.project.Service.GameService;
import org.project.domain.CommandMonster;
import org.project.domain.PlayerInventory;
import org.project.standard.util.Util;
import org.project.view.InputView;
import org.project.view.OutputView;

public class GameController {
    // 입출력을 담당할 View 객체들을 생성
    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    // 데이터와 비즈니스 로직을 담당할 객체들
    private final PlayerInventory inventory = new PlayerInventory();
    private final GameService gameService = new GameService();

    public void start() {
        Util.delay(500);

        outputView.printSystemMessage("게임 준비가 완료되었습니다.");
        choosePartner();
        mainMenu();
    }

    // --- [페이지 1: 파트너 포켓몬 선택] ---
    private void choosePartner() {
        outputView.printMessage("\n====================================");
        outputView.printMessage(" [오박사] \"자, 여기 세 마리의 포켓몬이 있단다.");
        outputView.printMessage(" 너의 여정을 함께할 파트너를 골라보려무나!\"");
        outputView.printMessage("====================================");
        outputView.printMessage(" 1. 이상해씨 (풀) | 2. 파이리 (불꽃) | 3. 꼬부기 (물)");
        outputView.printMessage(" 번호 선택: ");


        String choice = inputView.inputString();
        CommandMonster starter = null;

        switch (choice) {
            case "1" -> starter = new CommandMonster(1,"이상해씨", "풀", 45, 49, 49, 65, 45, 16,2,5);
            case "2" -> starter = new CommandMonster(4,"파이리", "불꽃", 39, 52, 43, 50, 65, 16,5,5);
            case "3" -> starter = new CommandMonster(7, "꼬부기", "물", 44, 48, 65, 50, 43, 16,8,5);
            default -> {
                outputView.printMessage("\n[오박사]  \"똑바로 알려줄래? 그런 포켓몬은 없는것 같단다.\"");
                choosePartner();
                return;
            }
        }
        inventory.addMonster(starter);
        outputView.printUserMessage(starter.getName() + "을(를) 첫 번째 포켓몬으로 맞이했다!");
    }

    private void mainMenu() {
        while (true) {
            outputView.printMainMenu();
            String choice = inputView.inputString();

            switch (choice) {
                case "1" -> startAdventure();
                case "2" -> visitCenter();
                case "3" -> showMyMonstersMenu();
                case "0" -> {
                    outputView.printMessage("\n시작화면으로 돌아갑니다.");
                    return;
                }
                default -> outputView.printMessage("\n<알림> 제시된 올바른 숫자를 입력하세요.");
            }
        }
    }

    private void showMyMonstersMenu() {
        while (true) {
            Util.delay(500);
            inventory.showMonsters();
            outputView.printMessage("\n▶1. 순서바꾸기◀ | ▶2. 놓아주기◀ | ▶0. 이전으로◀");
            outputView.printMessage("[메뉴 선택]: ");

            String choice = inputView.inputString();

            switch (choice) {
                case "1" -> {
                    if (inventory.getMyMonsters().size() < 2) {
                        outputView.printMessage("\n<알림> 순서를 바꿀 포켓몬이 부족합니다.");
                        continue;
                    }
                    try {
                        outputView.printMessage("첫 번째 포켓몬 번호: ");
                        int first = inputView.inputInt() - 1;
                        outputView.printMessage("두 번째 포켓몬 번호: ");
                        int second = inputView.inputInt() - 1;

                        inventory.swapMonsters(first, second);
                    } catch (NumberFormatException e) {
                        outputView.printMessage("\n<알림> 숫자만 입력해주세요.");
                    }
                }
                case "2" -> {
                    if (inventory.getMyMonsters().size() < 2) {
                        outputView.printMessage("\n<알림> 1마리 이상은 가지고 있어야 합니다.");
                        continue;
                    }
                    try {
                        outputView.printMessage("\n놓아줄 포켓몬 번호: ");
                        int releaseIndex = inputView.inputInt() - 1;
                        String name = inventory.getMyMonsters().get(releaseIndex).getName();
                        inventory.releaseMonster(releaseIndex);
                        Util.delay(500);
                        outputView.printMessage("\n...");
                        Util.delay(500);
                        outputView.printUserMessage(name + "(이)가 떠나갔다. \"바이바이!\"\n\n");
                    } catch (Exception e) {
                        outputView.printMessage("\n<알림> 올바른 번호를 입력해주세요.");
                    }
                }
                case "0" -> {
                    outputView.printMessage("\n이전화면으로 돌아갑니다.");
                    return;
                }
                default -> outputView.printMessage("\n<알림> 제시된 올바른 숫자를 입력하세요.");
            }
        }
    }

    private void visitCenter() {
        outputView.printMessage("\n[포켓몬 센터] 간호순: 안녕하세요! 포켓몬 센터입니다.");
        outputView.printMessage("잠시만 기다려 주세요. 포켓몬을 치료해 드릴게요.");

        Util.delay(500);
        System.out.print("치료 중...");
        for(int i = 0; i < 3; i++) {
            Util.delay(400);
            System.out.print("띵~♪ ♬ ");
        }
        outputView.printMessage("\n");
        Util.delay(500);

        for (CommandMonster m : inventory.getMyMonsters()) {
            m.setCurrentHp(m.getMaxHp());
        }

        outputView.printMessage("띠리리링! ♬ 모든 포켓몬이 건강해졌습니다!");
        Util.delay(800);
    }

    private void startAdventure() {
        Util.delay(500);
        CommandMonster wildMonster = gameService.generateWildMonster(inventory.getAvgLevel());
        CommandMonster myMonster = inventory.sendMonster();

        if (myMonster == null) {
            outputView.printSystemMessage("싸울 수 있는 포켓몬이 없습니다. 센터로 이동합니다.");
            visitCenter();
            return;
        }

        outputView.printUserMessage("\"앗! 야생의 "+ wildMonster.getName()+"(이)가 나타났다.\"");
        outputView.printUserMessage("\"나와라!  "+ myMonster.getName()+"!\"");

        while (true) {
            Util.delay(500);
            outputView.printMonsterStatus(wildMonster, "야생");
            outputView.printMonsterStatus(myMonster, "");
            Util.delay(500);

            outputView.printAdventureMenu();
            String choice = inputView.inputString();

            switch (choice) {
                case "1" -> {
                    int result = gameService.startBattle(myMonster, wildMonster);

                    if (result == 1) {
                        handleVictory(myMonster, wildMonster);
                        return;
                    }

                    if (result == 2) {
                        myMonster = handlePlayerFaint(myMonster);
                        if (myMonster == null) return;
                    }
                }
                case "2" -> {
                    if (handleCapture(myMonster, wildMonster)) return;
                }
                case "3" -> {
                    outputView.printUserMessage("무사히 도망쳤다!");
                    Util.delay(500);
                    return;
                }
                case "4" -> {
                    CommandMonster next = selectReplacement(myMonster);
                    if (next != null) {
                        outputView.printUserMessage("\"수고했어 " + myMonster.getName() + ", 돌아와!\"");
                        Util.delay(500);
                        myMonster = next;
                        outputView.printUserMessage("\"가라! " + myMonster.getName() + "!\"");

                        Util.delay(800);
                        gameService.executeTurn(wildMonster, myMonster);
                        if (myMonster.getCurrentHp() <= 0) {
                            myMonster = handlePlayerFaint(myMonster);
                            if (myMonster == null) return;
                        }
                    }
                }
                default -> outputView.printMessage("\n<알림> 제시된 올바른 숫자를 입력하세요.");
            }
        }
    }

    private void handleVictory(CommandMonster myMonster, CommandMonster wildMonster) {
        Util.delay(500);
        outputView.printSystemMessage(wildMonster.getName() + "이(가) 기절했다!");

        int expAmount = gameService.calculateEarnedExp(wildMonster);
        Util.delay(500);
        outputView.printSystemMessage(expAmount + " EXP를 획득했다!");

        while (myMonster.getExpByBattle(expAmount)) {
            myMonster.levelUp();
            Util.delay(500);
            outputView.printSystemMessage("레벨업! " + myMonster.getLevel() + "레벨이 되었다.");
            expAmount = 0;
        }
        checkEvolution(myMonster);
    }

    private boolean handleCapture(CommandMonster myMonster, CommandMonster wildMonster) {
        if (inventory.getMyMonsters().size() >= 6) {
            outputView.printSystemMessage("인벤토리가 가득 찼습니다! 더 이상 잡을 수 없습니다.");
            return false;
        }

        outputView.printUserMessage("\"가라! 몬스터볼!\"");
        Util.delay(800);

        for (int i = 0; i < 3; i++) {
            System.out.print(" . ");
            Util.delay(600);
            System.out.print("흔들! ");
            Util.delay(600);
        }

        if (gameService.tryCapture(wildMonster)) {
            outputView.printMessage("\n\n [시스템] 찰카닥! " + wildMonster.getName() + "을(를) 잡았다!");
            wildMonster.setCurrentHp(wildMonster.getMaxHp());
            inventory.addMonster(wildMonster);
            return true;
        } else {
            outputView.printMessage("\n\n [시스템] 뽀용! " + wildMonster.getName() + "이(가) 볼에서 튀어 나왔다!");
            Util.delay(500);
            outputView.printUserMessage("\"아깝다! 잡을 수 있었는데...\"");

            Util.delay(800);
            gameService.executeTurn(wildMonster, myMonster);

            if (myMonster.getCurrentHp() <= 0) {
                handlePlayerFaint(myMonster);
            }
            return false;
        }
    }

    private CommandMonster handlePlayerFaint(CommandMonster faintedMonster) {
        Util.delay(500);
        outputView.printSystemMessage(faintedMonster.getName() + "이(가) 기절했다!");

        if (inventory.isAllFainted()) {
            Util.delay(800);
            outputView.printUserMessage("\"더 이상 내보낼 포켓몬이 없어...!\"");
            Util.delay(500);
            outputView.printSystemMessage("눈앞이 깜깜해졌다! 급히 포켓몬 센터로 실려갔다.");
            Util.delay(1000);
            visitCenter();
            return null;
        }

        CommandMonster nextMonster = null;
        while (nextMonster == null) {
            outputView.printSystemMessage("다음 포켓몬을 선택해야 합니다.");
            nextMonster = selectReplacement(faintedMonster);

            if (nextMonster == null) {
                outputView.printMessage("\n<알림> 기절한 상태에서는 도망칠 수 없습니다! 싸울 포켓몬을 고르세요.");
            }
        }

        Util.delay(500);
        outputView.printUserMessage("\"나와라! " + nextMonster.getName() + "!\"");
        return nextMonster;
    }

    private void checkEvolution(CommandMonster monster) {
        if (monster.getEvolutionId() != 0 && monster.getLevel() >= monster.getEvolutionLevel()) {
            Util.delay(1000);
            outputView.printSystemMessage("......어라? " + monster.getName() + "의 상태가...!");

            for (int i = 0; i < 3; i++) {
                Util.delay(600);
                System.out.print(" ✨ ");
            }
            outputView.printMessage("\n");

            CommandMonster evolvedBase = gameService.getEvolvedBase(monster.getEvolutionId());

            if (evolvedBase != null) {
                String oldName = monster.getName();
                monster.evolve(evolvedBase);

                Util.delay(1000);
                outputView.printSystemMessage("축하합니다! " + oldName + "은(는) "
                        + monster.getName() + "(으)로 진화했다!");
                Util.delay(1000);
            }
        }
    }

    public CommandMonster selectReplacement(CommandMonster current) {
        while (true) {
            inventory.showMonsters();
            outputView.printMessage("\n[0. 취소(이전으로) | 번호 선택]");
            outputView.printMessage("교체할 포켓몬의 번호를 입력하세요: ");
            try {
                int choice = inputView.inputInt();
                if (choice == 0) return null;

                CommandMonster selected = inventory.sendMonster(choice - 1);

                if (selected == current) {
                    outputView.printMessage("\n<알림> 이미 배틀 중인 포켓몬입니다!");
                } else if (selected.getCurrentHp() <= 0) {
                    outputView.printMessage("\n<알림> 기절한 포켓몬은 내보낼 수 없습니다!");
                } else {
                    return selected;
                }
            } catch (Exception e) {
                outputView.printMessage("\n<알림> 올바른 번호를 입력해주세요.");
            }
        }
    }
}