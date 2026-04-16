package org.project.Service;

import org.project.domain.CommandMonster;
import org.project.standard.util.TypeChart;
import org.project.standard.util.Util;

import java.util.Random;

public class GameService {
    private Random random = new Random();
    public int startBattle(CommandMonster player, CommandMonster wild) {
        boolean playerGoesFirst = determinePriority(player, wild);

        // 공격 순서에 따른 실행
        if (playerGoesFirst) {
            executeTurn(player, wild);
            if (wild.getCurrentHp() <= 0) return 1; // 야생 포켓몬 기절 (승리)

            executeTurn(wild, player);
            if (player.getCurrentHp() <= 0) return 2; // 내 포켓몬 기절 (패배)
        } else {
            executeTurn(wild, player);
            if (player.getCurrentHp() <= 0) return 2; // 내 포켓몬 기절 (패배)

            executeTurn(player, wild);
            if (wild.getCurrentHp() <= 0) return 1; // 야생 포켓몬 기절 (승리)
        }

        return 0; // 아무도 기절하지 않음 (전투 계속)
    }

    private boolean determinePriority(CommandMonster p1, CommandMonster p2) {
        if (p1.getSpd() > p2.getSpd()) return true;
        if (p1.getSpd() < p2.getSpd()) return false;

        // 스피드가 같을 경우 50% 확률로 결정
        return Math.random() > 0.5;
    }

    public void executeTurn(CommandMonster attacker, CommandMonster defender) {
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

    public int calculateEarnedExp(CommandMonster wild) {
        // 상대의 종족값 합산 (BaseSum)
        int baseSum = wild.getBaseHp() +
                wild.getBaseAtk() +
                wild.getBaseDef() +
                wild.getBaseSp() +
                wild.getBaseSpd();
        // 공식 적용: (BaseSum * L_enemy) / 7
        int earnedExp = (baseSum * wild.getLevel()) / 7;
        // 최소 경험치 보장 (레벨이 아주 낮더라도 최소 1은 획득)
        return Math.max(1, earnedExp);
    }

    public boolean tryCapture(CommandMonster wild) {
        // 체력이 100%일 때 1배, 1%일 때 약 3배의 보너스
        double hpBonus = (double) (wild.getMaxHp() * 3 - wild.getCurrentHp() * 2) / wild.getMaxHp();

        // 기본 포획률 설정 (0.0 ~ 1.0)
        // 기본 30% 확률에 HP 보너스를 곱함
        double captureChance = 0.31 * hpBonus;

        // 난수 발생 및 결과 확인
        return Math.random() < captureChance;
    }
}
