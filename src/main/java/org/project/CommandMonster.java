package org.project;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommandMonster {
    private  int id;
    private String name;
    private String type;
    private int level;
    private  int exp;
    private final int baseHp, baseAtk, baseDef, baseSp, baseSpd; //종족값 - 데이터 파일에서 가져올 것
    private  int maxHp, currentHp, atk, def, sp, spd;//실제 능력치

    public CommandMonster(String name, String type,int baseHp, int baseAtk, int baseDef, int baseSp, int baseSpd, int level){
        //종족값 초기화
        this.name = name;
        this.type = type;
        this.level = level;
        this.baseHp = baseHp;
        this.baseAtk = baseAtk;
        this.baseDef = baseDef;
        this.baseSp = baseSp;
        this.baseSpd = baseSpd;
        ActualStats();
        this.currentHp = this.maxHp; // 처음엔 풀피로 시작 계산 적용후 현재체력 저장해야 함
    }

    public void ActualStats(){
        this.maxHp = (int)((baseHp * level) / 50.0) + level + 10;
        this.atk = (int)((baseAtk * level) / 50.0) + 5;
        this.def = (int)((baseDef * level) / 50.0) + 5;
        this.sp = (int)((baseSp * level) / 50.0) + 5;
        this.spd = (int)((baseSpd * level) / 50.0) + 5;
    }


    public void levelUp() {
        this.level++; // 1. 레벨 증가

        int oldMaxHp = this.maxHp; // 2. 이전 최대 체력 기록
        ActualStats();             // 3. 증가한 레벨로 능력치 갱신

        // 4. 레벨업 보너스: 최대 체력이 늘어난 만큼 현재 체력도 회복
        this.currentHp += (this.maxHp - oldMaxHp);

        System.out.println("\n[ " + name + "의 레벨이 " + level + "로 올랐습니다! ]");
    }
}
