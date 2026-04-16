package org.project.Service;

import org.project.Repository.GameRepository;
import org.project.domain.CommandMonster;
import org.project.standard.util.Util;

import java.util.List;
import java.util.Random;

public class GameService {
    private final GameRepository gameRepository = new GameRepository();
    private final Random random = new Random();

    /**
     * 야생 포켓몬 생성
     */
    public CommandMonster generateWildMonster(int avgLevel) {
        List<CommandMonster> db = gameRepository.getPokemonDatabase();
        int index = random.nextInt(db.size());
        CommandMonster base = db.get(index);

        // 초반에 진화체가 나오는 것을 방지
        while (base.getEvolutionId() == 0 && avgLevel < 30) {
            index = random.nextInt(db.size());
            base = db.get(index);
        }

        // 평균 레벨 기준 +-2 레벨로 조정
        int wildLevel = Math.max(1, avgLevel + random.nextInt(5) - 2);
        return new CommandMonster(
                base.getId(), base.getName(), base.getType(),
                base.getBaseHp(), base.getBaseAtk(), base.getBaseDef(),
                base.getBaseSp(), base.getBaseSpd(),
                base.getEvolutionLevel(), base.getEvolutionId(), wildLevel
        );
    }

    /**
     * 전투 승리 시 획득 경험치 계산
     */
    public int calculateEarnedExp(CommandMonster wild) {
        int baseSum = wild.getBaseHp() + wild.getBaseAtk() + wild.getBaseDef()
                + wild.getBaseSp() + wild.getBaseSpd();
        int earnedExp = (baseSum * wild.getLevel()) / 7;
        return Math.max(1, earnedExp); // 최소 1의 경험치는 보장
    }

    /**
     *  포획 확률 계산 및 판정
     */
    public boolean tryCapture(CommandMonster wild) {
        // 체력 비율에 따른 보너스 (체력이 낮을수록 확률 증가)
        double hpRatio = (double) wild.getCurrentHp() / wild.getMaxHp();
        double captureChance = 0.3 * (3 - 2 * hpRatio);
        return random.nextDouble() < captureChance;
    }

    /**
     *  진화 데이터 찾기
     */
    public CommandMonster getEvolvedBase(int evolutionId) {
        if (evolutionId == 0) return null;
        return gameRepository.getPokemonDatabase().stream()
                .filter(p -> p.getId() == evolutionId)
                .findFirst()
                .orElse(null);
    }

    /**
     * [전투 로직] 1턴 실행 (Controller에서 1번 누를 때마다 호출됨)
     * @return 1: 야생 포켓몬 기절, 2: 내 포켓몬 기절, 0: 전투 계속
     */
    public int startBattle(CommandMonster player, CommandMonster wild) {
        boolean playerGoesFirst = determinePriority(player, wild);

        if (playerGoesFirst) {
            executeTurn(player, wild);
            if (wild.getCurrentHp() <= 0) return 1;

            executeTurn(wild, player);
            if (player.getCurrentHp() <= 0) return 2;
        } else {
            executeTurn(wild, player);
            if (player.getCurrentHp() <= 0) return 2;

            executeTurn(player, wild);
            if (wild.getCurrentHp() <= 0) return 1;
        }
        return 0;
    }

    // --- 내부 전투 보조 로직 ---
    private boolean determinePriority(CommandMonster p1, CommandMonster p2) {
        if (p1.getSpd() > p2.getSpd()) return true;
        if (p1.getSpd() < p2.getSpd()) return false;
        return random.nextBoolean();
    }

    public void executeTurn(CommandMonster attacker, CommandMonster defender) {
        boolean isPhysical = determineAttackType(attacker);
        int attackPower = isPhysical ? attacker.getAtk() : attacker.getSp();
        int defensePower = isPhysical ? defender.getDef() : defender.getSp();

        String attackTypeStr = isPhysical ? "물리 공격" : "특수 공격";

        // 대미지 공식 적용
        double rawDamage = ((2.0 * attacker.getLevel() / 5.0) + 2.0) * ((double) attackPower / defensePower) + 2;
        int finalDamage = (int) rawDamage; // 타입 상성(TypeChart) 적용 시 여기에 곱셈 추가

        defender.setCurrentHp(Math.max(0, defender.getCurrentHp() - finalDamage));

        System.out.printf("\n%s의 %s! %s에게 %d의 대미지!\n",
                attacker.getName(), attackTypeStr, defender.getName(), finalDamage);
        Util.delay(500);
    }

    private boolean determineAttackType(CommandMonster attacker) {
        int atk = attacker.getBaseAtk();
        int sp = attacker.getBaseSp();
        int total = atk + sp;
        int roll = random.nextInt(total);
        return roll < atk;
    }
}