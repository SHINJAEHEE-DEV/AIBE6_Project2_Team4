package org.project.domain;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommandMonster {
    private int id;
    private String name;
    private String type;
    private int level;
    private  int exp;
    private int totalExp;
    // JSON key랑 변수명 매핑
    @SerializedName("base_hp")
    private int baseHp;
    @SerializedName("base_atk")
    private int baseAtk;
    @SerializedName("base_def")
    private int baseDef;
    @SerializedName("base_sp")
    private int baseSp;
    @SerializedName("base_spd")
    private int baseSpd;
    @SerializedName("evolution_level")
    private int evolutionLevel;
    @SerializedName("evolution_id")
    private int evolutionId;//종족값 - 데이터 파일에서 가져올 것
    private  int maxHp, currentHp, atk, def, sp, spd;//실제 능력치

    public CommandMonster(int id, String name, String type,int baseHp, int baseAtk, int baseDef, int baseSp, int baseSpd,int evolutionLevel, int evolutionId, int level){
        //종족값 초기화
        this.id = id;
        this.name = name;
        this.type = type;
        this.baseHp = baseHp;
        this.baseAtk = baseAtk;
        this.baseDef = baseDef;
        this.baseSp = baseSp;
        this.baseSpd = baseSpd;
        this.evolutionLevel = evolutionLevel;
        this.evolutionId = evolutionId;
        this.level = level;
        ActualStats();
        this.currentHp = this.maxHp; // 처음엔 풀피로 시작 계산 적용후 현재체력 저장해야 함
    }

    public void ActualStats(){
        this.maxHp = (int)((baseHp * level) / 50.0) + level + 10;
        this.atk = (int)((baseAtk * level) / 50.0) + 5;
        this.def = (int)((baseDef * level) / 50.0) + 5;
        this.sp = (int)((baseSp * level) / 50.0) + 5;
        this.spd = (int)((baseSpd * level) / 50.0) + 5;
        this.totalExp = (int) Math.pow(level + 1, 3);
    }

    public boolean getExpByBattle(int exp) {
        this.exp = this.exp + exp;
        return this.exp >= totalExp;
    }


    public void levelUp() {
        this.level++; // 1. 레벨 증가
        int oldMaxHp = this.maxHp; // 2. 이전 최대 체력 기록
        ActualStats();  // 3. 증가한 레벨로 능력치 갱신
        this.exp -= this.totalExp;

        // 혹시라도 계산 실수로 마이너스가 되지 않게 방어
        if (this.exp < 0) this.exp = 0;
        // 4. 레벨업 보너스: 최대 체력이 늘어난 만큼 현재 체력도 회복
        this.currentHp += (this.maxHp - oldMaxHp);
    }

    public void evolve(CommandMonster evolvedBase) {
        // 1. 기본 정보 변경
        this.name = evolvedBase.getName();
        this.type = evolvedBase.getType();

        // 2. 종족값 변경
        this.baseHp = evolvedBase.getBaseHp();
        this.baseAtk = evolvedBase.getBaseAtk();
        this.baseDef = evolvedBase.getBaseDef();
        this.baseSp = evolvedBase.getBaseSp();
        this.baseSpd = evolvedBase.getBaseSpd();

        // 3. 다음 진화 정보 업데이트
        this.evolutionLevel = evolvedBase.getEvolutionLevel();
        this.evolutionId = evolvedBase.getEvolutionId();

        // 4. 새로운 종족값으로 스탯 재계산 및 체력 회복
        ActualStats();
        this.currentHp = this.maxHp;
    }
}
