package org.project.view;

import org.project.domain.CommandMonster;
import org.project.domain.Pokedex;

public class OutputView {
    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printSelectMessage(String message){
        System.out.print(message);
    }

    public void printSystemMessage(String message) {
        System.out.println("[시스템] " + message);
    }

    public void printUserMessage(String message) {
        System.out.println("[유저] " + message);
    }

    public void printMainMenu() {
        System.out.println("\n▶1. 모험하기◀ | ▶2. 포켓몬 센터◀ | ▶3. 내 포켓몬 목록◀ | ▶4. 포켓몬 도감◀ | ▶5. 저장하기◀ | ▶0. 종료◀");
        System.out.print("[메뉴 선택]: ");
    }

    public void printPokedex(org.project.domain.Pokedex pokedex, java.util.List<CommandMonster> db) {
        System.out.println("\n=================[ 포켓몬 도감 ]=================");
        System.out.printf("   발견한 포켓몬: %d마리 | 잡은 포켓몬: %d마리\n", pokedex.getSeenCount(), pokedex.getCaughtCount());
        System.out.println("================================================");

        for (CommandMonster base : db) {
            int id = base.getId();
            if (pokedex.isCaught(id)) {
                System.out.printf(" No.%03d | %-10s | [포획 완료] 🔴\n", id, base.getName());
            } else if (pokedex.isSeen(id)) {
                System.out.printf(" No.%03d | %-10s | [발견함]   ⚪\n", id, base.getName());
            } else {
                System.out.printf(" No.%03d | %-10s | [---]      ⚫\n", id, "???");
            }
        }
        System.out.println("================================================");
    }

    public void printAdventureMenu() {
        System.out.println("\n▶1. 싸운다◀ | ▶2. 몬스터볼◀ | ▶3. 도망친다◀ | ▶4. 교체한다◀ ");
        System.out.print("[메뉴 선택]: ");
    }

    public void printMonsterStatus(CommandMonster monster, String tag, Pokedex pokedex) {
        String red = "\u001B[31m";
        String green = "\u001B[32m";
        String yellow = "\u001B[33m";
        String reset = "\u001B[0m";

        double hpRatio = (double) monster.getCurrentHp() / monster.getMaxHp();
        int barCount = (int) (hpRatio * 20);
        String hpBar = "■".repeat(barCount) + "-".repeat(20 - barCount);
        String hpColor = (hpRatio > 0.5) ? green : (hpRatio > 0.2) ? yellow : red;

        String marker = "";
        if (tag.equals("야생")) {
            marker = pokedex.isCaught(monster.getId()) ? "🔴 " : "⚪ ";
        }

        System.out.println((tag.equals("야생") ? red : green) + "========= [" + tag + " 포켓몬 정보] =========" + reset);
        System.out.printf(" %s 이름 : %-10s  Lv.%3d  타입 : %s\n", marker, monster.getName(), monster.getLevel(), monster.getType());
        System.out.print(" HP   : [");
        System.out.print(hpColor + hpBar + reset);
        System.out.printf("] %d / %d\n", monster.getCurrentHp(), monster.getMaxHp());
        System.out.println(" 종족값: ATK %d | SP %d |DEF %d | SPD %d".formatted(monster.getAtk(), monster.getSp(), monster.getBaseDef(), monster.getBaseSpd()));
        System.out.println(" EXP) " + monster.getExp() + " / " + monster.getTotalExp());
        System.out.println("==========================================");
    }
}