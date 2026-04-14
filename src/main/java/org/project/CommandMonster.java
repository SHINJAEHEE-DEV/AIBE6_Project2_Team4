package org.project;

public class CommandMonster {
    private  int id;
    private String name;
    private String type;
    private int level;
    private  int exp;
    private int baseHp, baseAtk, baseDef, baseSp, baseSpd; //종족값 - 데이터 파일에서 가져올 것
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


    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
    public  String getType(){
        return type;
    }

}
