package org.project.Service;

import org.project.domain.CommandMonster;
import org.project.standard.util.TypeChart;
import org.project.standard.util.Util;

import java.util.Random;

public class GameService {
    private Random random = new Random();
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
        if (p1.getSpd() > p2.getSpd()) return true;
        if (p1.getSpd() < p2.getSpd()) return false;

        // 스피드가 같을 경우 50% 확률로 결정
        return Math.random() > 0.5;
    }

    private void executeTurn(CommandMonster attacker, CommandMonster defender) {
        // 1. 공격 유형 결정 (물리 ATK vs 특수 SP)
        boolean isPhysical = determineAttackType(attacker);
        int attackPower = isPhysical ? attacker.getAtk() : attacker.getSp();
        int defensePower = isPhysical ? defender.getDef() : defender.getSp(); // 물리엔 방어, 특수엔 특방(SP)으로 대응

        String attackType = isPhysical ? "물리 공격" : "특수 공격";

        // 2. 대미지 공식 적용
        // Damage = ((2 * Level / 5) + 2) * (Atk / Def) * Random(0.85 ~ 1.15)
        double rawDamage = ((2.0 * attacker.getLevel() / 5.0) + 2.0)
                * ((double) attackPower / defensePower)
                + 2;

        // 2. 타입 상성 계산 (공격자의 주 타입 기준)
        String skillType = attacker.getType().split("/")[random.nextInt(1)];
        double typeEffect = TypeChart.getMultiplier(skillType, defender.getType());

        int finalDamage = (int) (rawDamage * typeEffect);

        // 3. 실제 체력 차감 및 출력
        defender.setCurrentHp(Math.max(0, defender.getCurrentHp() - finalDamage));
        System.out.printf("\n%s의 [%s속성 %s]! %s에게 %d의 대미지!\n",
                attacker.getName(), skillType, attackType,
                defender.getName(), finalDamage);

        Util.delay(500);
    }

    /**
     * 높은 능력치를 사용할 확률을 높이는 로직
     * (예: ATK 70, SP 30 이면 물리 공격 확률이 70%)
     */
    private boolean determineAttackType(CommandMonster attacker) {
        int atk = attacker.getBaseAtk();
        int sp = attacker.getBaseSp();
        int total = atk + sp;

        // 0부터 total 사이의 난수 발생
        int roll = (int) (Math.random() * total);

        // roll이 atk 범위 안에 있으면 물리, 아니면 특수
        return roll < atk;
    }
}
