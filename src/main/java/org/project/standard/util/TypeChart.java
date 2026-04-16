package org.project.standard.util;

import java.util.HashMap;
import java.util.Map;

public class TypeChart {
    // [공격타입][방어타입] = 배율
    private static final Map<String, Map<String, Double>> chart = new HashMap<>();

    static {
        // 1. 노말 (Normal)
        addRelation("노말", "바위", 0.5);
        addRelation("노말", "고스트", 0.0);

        // 2. 불꽃 (Fire)
        addRelation("불꽃", "풀", 2.0); addRelation("불꽃", "벌레", 2.0); addRelation("불꽃", "얼음", 2.0);
        addRelation("불꽃", "물", 0.5); addRelation("불꽃", "바위", 0.5); addRelation("불꽃", "불꽃", 0.5); addRelation("불꽃", "드래곤", 0.5);

        // 3. 물 (Water)
        addRelation("물", "불꽃", 2.0); addRelation("물", "땅", 2.0); addRelation("물", "바위", 2.0);
        addRelation("물", "물", 0.5); addRelation("물", "풀", 0.5); addRelation("물", "드래곤", 0.5);

        // 4. 풀 (Grass)
        addRelation("풀", "물", 2.0); addRelation("풀", "땅", 2.0); addRelation("풀", "바위", 2.0);
        addRelation("풀", "불꽃", 0.5); addRelation("풀", "풀", 0.5); addRelation("풀", "독", 0.5);
        addRelation("풀", "비행", 0.5); addRelation("풀", "벌레", 0.5); addRelation("풀", "드래곤", 0.5);

        // 5. 전기 (Electric)
        addRelation("전기", "물", 2.0); addRelation("전기", "비행", 2.0);
        addRelation("전기", "전기", 0.5); addRelation("전기", "풀", 0.5); addRelation("전기", "드래곤", 0.5);
        addRelation("전기", "땅", 0.0);

        // 6. 얼음 (Ice)
        addRelation("얼음", "풀", 2.0); addRelation("얼음", "땅", 2.0); addRelation("얼음", "비행", 2.0); addRelation("얼음", "드래곤", 2.0);
        addRelation("얼음", "물", 0.5); addRelation("얼음", "얼음", 0.5);

        // 7. 격투 (Fighting)
        addRelation("격투", "노말", 2.0); addRelation("격투", "얼음", 2.0); addRelation("격투", "바위", 2.0);
        addRelation("격투", "독", 0.5); addRelation("격투", "비행", 0.5); addRelation("격투", "에스퍼", 0.5); addRelation("격투", "벌레", 0.5);
        addRelation("격투", "고스트", 0.0);

        // 8. 독 (Poison)
        addRelation("독", "풀", 2.0);
        addRelation("독", "독", 0.5); addRelation("독", "땅", 0.5); addRelation("독", "바위", 0.5); addRelation("독", "고스트", 0.5);

        // 9. 땅 (Ground)
        addRelation("땅", "불꽃", 2.0); addRelation("땅", "전기", 2.0); addRelation("땅", "독", 2.0); addRelation("땅", "바위", 2.0);
        addRelation("땅", "풀", 0.5); addRelation("땅", "벌레", 0.5);
        addRelation("땅", "비행", 0.0);

        // 10. 비행 (Flying)
        addRelation("비행", "풀", 2.0); addRelation("비행", "격투", 2.0); addRelation("비행", "벌레", 2.0);
        addRelation("비행", "전기", 0.5); addRelation("비행", "바위", 0.5);

        // 11. 에스퍼 (Psychic)
        addRelation("에스퍼", "격투", 2.0); addRelation("에스퍼", "독", 2.0);
        addRelation("에스퍼", "에스퍼", 0.5);

        // 12. 벌레 (Bug)
        addRelation("벌레", "풀", 2.0); addRelation("벌레", "에스퍼", 2.0);
        addRelation("벌레", "불꽃", 0.5); addRelation("벌레", "격투", 0.5); addRelation("벌레", "독", 0.5); addRelation("벌레", "비행", 0.5); addRelation("벌레", "고스트", 0.5);

        // 13. 바위 (Rock)
        addRelation("바위", "불꽃", 2.0); addRelation("바위", "얼음", 2.0); addRelation("바위", "비행", 2.0); addRelation("바위", "벌레", 2.0);
        addRelation("바위", "격투", 0.5); addRelation("바위", "땅", 0.5);

        // 14. 고스트 (Ghost)
        addRelation("고스트", "고스트", 2.0);
        addRelation("고스트", "노말", 0.0); addRelation("고스트", "에스퍼", 0.0); // 1세대 기준 에스퍼에 무효

        // 15. 드래곤 (Dragon)
        addRelation("드래곤", "드래곤", 2.0);
    }

    private static void addRelation(String atk, String def, double mult) {
        chart.computeIfAbsent(atk, k -> new HashMap<>()).put(def, mult);
    }

    public static double getMultiplier(String rawAtkType, String rawDefType) {
        // 공격 타입이 "불꽃/비행" 처럼 두 개일 경우, 첫 번째(주 타입)만 공격 속성으로 사용
        String atkType = rawAtkType.split("/")[0];

        // 방어 타입이 "풀/독" 처럼 두 개일 경우, 각각의 상성을 곱함
        String[] defTypes = rawDefType.split("/");
        double finalMult = 1.0;

        for (String type : defTypes) {
            if (chart.containsKey(atkType) && chart.get(atkType).containsKey(type)) {
                finalMult *= chart.get(atkType).get(type);
            }
        }

        return finalMult;
    }
}