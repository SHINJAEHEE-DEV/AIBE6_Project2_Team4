package org.project.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommandMonster {
    private int id;
    private String name;
    private String type;
    private int level;
    private int exp;

    @SerializedName("base_hp") private int baseHp;
    @SerializedName("base_atk") private int baseAtk;
    @SerializedName("base_def") private int baseDef;
    @SerializedName("base_sp") private int baseSp;
    @SerializedName("base_spd") private int baseSpd;
    @SerializedName("evolution_level") private int evolutionLevel;
    @SerializedName("evolution_id") private int evolutionId;

    private int maxHp, currentHp, atk, def, sp, spd;

    public CommandMonster(int id, String name, String type, int baseHp, int baseAtk, int baseDef, int baseSp, int baseSpd, int evolutionLevel, int evolutionId, int level) {
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
        updateActualStats();
        this.currentHp = this.maxHp;
    }

    // 능력치 재계산 로직 통합
    public void updateActualStats() {
        this.maxHp = (int)((baseHp * level) / 50.0) + level + 10;
        this.atk = (int)((baseAtk * level) / 50.0) + 5;
        this.def = (int)((baseDef * level) / 50.0) + 5;
        this.sp = (int)((baseSp * level) / 50.0) + 5;
        this.spd = (int)((baseSpd * level) / 50.0) + 5;
    }

    // 다음 레벨업을 위한 목표 경험치 계산 (L^3)
    public int getTotalExp() {
        return (int) Math.pow(level + 1, 3);
    }

    public boolean getExpByBattle(int earned) {
        this.exp += earned;
        return this.exp >= getTotalExp();
    }

    public void levelUp() {
        this.level++;
        this.exp -= getTotalExp();
        if (this.exp < 0) this.exp = 0;

        int oldMaxHp = this.maxHp;
        updateActualStats();
        this.currentHp += (this.maxHp - oldMaxHp);
    }

    public void evolve(CommandMonster evolvedBase) {
        this.id = evolvedBase.id;
        this.name = evolvedBase.name;
        this.type = evolvedBase.type;
        this.baseHp = evolvedBase.baseHp;
        this.baseAtk = evolvedBase.baseAtk;
        this.baseDef = evolvedBase.baseDef;
        this.baseSp = evolvedBase.baseSp;
        this.baseSpd = evolvedBase.baseSpd;
        this.evolutionLevel = evolvedBase.evolutionLevel;
        this.evolutionId = evolvedBase.evolutionId;
        updateActualStats();
        this.currentHp = this.maxHp;
    }
}