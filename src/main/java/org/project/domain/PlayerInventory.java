package org.project.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlayerInventory {
    private List<CommandMonster> myMonsters;
    private final int MAX_CAPACITY = 6;

    public PlayerInventory() {
        this.myMonsters = new ArrayList<>();
    }

    // 포켓몬 추가 (6마리 제한 체크)
    public boolean addMonster(CommandMonster monster) {
        if (myMonsters.size() >= MAX_CAPACITY) {
            System.out.println("\n[경고] 인벤토리가 가득 찼습니다! (최대 6마리)");
            return false;
        }
        myMonsters.add(monster);
        return true;
    }

    // 현재 보유한 포켓몬 목록 출력
    public void showMonsters() {
        System.out.println("\n========= [ 내 포켓몬 목록 ] =========");
        if (myMonsters.isEmpty()) {
            System.out.println("보유한 포켓몬이 없습니다.");
        } else {
            for (int i = 0; i < myMonsters.size(); i++) {
                CommandMonster m = myMonsters.get(i);
                System.out.printf("%d. %s (Lv.%d) [HP: %d/%d]\n",
                        (i + 1), m.getName(), m.getLevel(), m.getCurrentHp(), m.getMaxHp());
            }
        }
        System.out.println("======================================");
    }

}