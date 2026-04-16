package org.project.Repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.project.domain.PlayerInventory;

import java.io.*;

public class PlayerRepository {
    // 저장될 파일 이름 설정
    private static final String SAVE_FILE = "player_save.json";

    // setPrettyPrinting()을 통해 JSON 파일이 줄바꿈되어 예쁘게 저장되도록 설정
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * 플레이어 데이터 저장
     */
    public void save(PlayerInventory inventory) {
        try (Writer writer = new FileWriter(SAVE_FILE)) {
            gson.toJson(inventory, writer);
        } catch (IOException e) {
            System.out.println("[시스템] 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 플레이어 데이터 불러오기
     */
    public PlayerInventory load() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return null; // 저장된 파일이 없으면 null 반환
        }

        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, PlayerInventory.class);
        } catch (IOException e) {
            System.out.println("[시스템] 불러오기 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }
}