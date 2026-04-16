package org.project.Service;

import org.project.domain.CommandMonster;
import org.project.standard.util.Util;

public class GameService {

    public void startBattle(CommandMonster player, CommandMonster wild) {
        System.out.println("\n[전투 시작] " + player.getName() + " vs " + wild.getName());
        Util.delay(500);
        // 1. 선공 결정
        boolean playerGoesFirst = determinePriority(player, wild);
        if (playerGoesFirst) {
            executeTurn(player, wild); // 플레이어 턴
            if (wild.getCurrentHp() > 0) executeTurn(wild, player); // 야생 포켓몬 반격
        } else {
            executeTurn(wild, player); // 야생 포켓몬 턴
            if (player.getCurrentHp() > 0) executeTurn(player, wild); // 플레이어 반격
        }
    }

    private boolean determinePriority(CommandMonster p1, CommandMonster p2) {
        if (p1.getBaseSpd() > p2.getBaseSpd()) return true;
        if (p1.getBaseSpd() < p2.getBaseSpd()) return false;

        // 스피드가 같을 경우 50% 확률로 결정
        return Math.random() > 0.5;
    }

    private void executeTurn(CommandMonster attacker, CommandMonster defender) {
        // TODO: 실제 대미지 계산 로직이 들어갈 곳입니다.
        System.out.println("\n" + attacker.getName() + "의 공격!");
        Util.delay(500);
    }
}
