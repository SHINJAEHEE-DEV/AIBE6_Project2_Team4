package org.project.domain;

import java.util.HashSet;
import java.util.Set;

public class Pokedex {
    private final Set<Integer> seenIds = new HashSet<>();
    private final Set<Integer> caughtIds = new HashSet<>();

    // 발견 처리
    public void markSeen(int id) {
        seenIds.add(id);
    }

    // 포획 처리 (잡은 것은 무조건 본 것으로도 처리)
    public void markCaught(int id) {
        seenIds.add(id);
        caughtIds.add(id);
    }

    public boolean isSeen(int id) { return seenIds.contains(id); }
    public boolean isCaught(int id) { return caughtIds.contains(id); }

    public int getSeenCount() { return seenIds.size(); }
    public int getCaughtCount() { return caughtIds.size(); }
}