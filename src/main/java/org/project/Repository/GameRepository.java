package org.project.Repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.domain.CommandMonster;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {
    private List<CommandMonster> pokemonDatabase = new ArrayList<>();

    // Repository가 생성될 때 자동으로 데이터를 불러오도록 설정
    public GameRepository() {
        loadMonstersData();
    }

    private void loadMonstersData() {
        try {
            Reader reader = new FileReader("src/main/resources/pokemonDatabase.json");
            Gson gson = new Gson();
            Type monsterListType = new TypeToken<ArrayList<CommandMonster>>(){}.getType();
            pokemonDatabase = gson.fromJson(reader, monsterListType);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("포켓몬 데이터를 불러오는 중 오류 발생 (파일을 찾을 수 없음)", e);
        }
    }

    // 전체 포켓몬 데이터베이스 반환
    public List<CommandMonster> getPokemonDatabase() {
        return pokemonDatabase;
    }

    // 데이터베이스 크기 반환
    public int getDatabaseSize() {
        return pokemonDatabase.size();
    }
}