package org.project.standard.util;

import java.util.HashMap;
import java.util.Map;

public class TypeChart {
    // [공격타입][방어타입] = 배율
    private static final Map<String, Map<String, Double>> chart = new HashMap<>();

    static {
        // 예시 데이터 (실제 상성표의 일부)
        addRelation("불꽃", "풀", 2.0);
        addRelation("불꽃", "물", 0.5);
        addRelation("물", "불꽃", 2.0);
        addRelation("물", "풀", 0.5);
        addRelation("풀", "물", 2.0);
        addRelation("풀", "불꽃", 0.5);
        addRelation("전기", "물", 2.0);
        addRelation("전기", "풀", 0.5);
        addRelation("전기", "땅", 0.0); // 효과 없음
    }

    private static void addRelation(String atk, String def, double mult) {
        chart.computeIfAbsent(atk, k -> new HashMap<>()).put(def, mult);
    }

    public static double getMultiplier(String atkType, String defType) {
        // 복합 타입 처리 (예: "풀/독")
        String[] defTypes = defType.split("/");
        double finalMult = 1.0;

        for (String type : defTypes) {
            if (chart.containsKey(atkType) && chart.get(atkType).containsKey(type)) {
                finalMult *= chart.get(atkType).get(type);
            }
        }
        return finalMult;
    }
}