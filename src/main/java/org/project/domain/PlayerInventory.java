package org.project.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
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
    public void swapMonsters(int i, int j) {
        if (i < 0 || i >= myMonsters.size() || j < 0 || j >= myMonsters.size()) {
            System.out.println("\n<알림> 올바르지 않은 번호입니다.");
            return;
        }
        Collections.swap(myMonsters, i, j);
        System.out.println("\n[시스템] 포켓몬의 순서가 변경되었습니다!");
    }

    public void releaseMonster(int releaseIndex) {
        myMonsters.remove(releaseIndex);
    }
    public  int getAvgLevel(){
       return (int) myMonsters.stream()
                       .mapToInt( e -> e.getLevel())
                            .average()
                                    .orElse(1);
    }
}