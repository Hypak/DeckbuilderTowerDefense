package com.hycap.dbt;
import java.util.HashMap;
import java.util.Map;
public class RecordsHolder {
    public Map<GameScreen.Difficulty, Record> records;
    public RecordsHolder() {
        records = new HashMap<>();
        records.put(GameScreen.Difficulty.EASY, new Record(0, 0));
        records.put(GameScreen.Difficulty.NORMAL, new Record(0, 0));
        records.put(GameScreen.Difficulty.HARD, new Record(0, 0));
    }

    public void updateRecord(GameScreen.Difficulty difficulty, Record newRecord) {
        records.get(difficulty).updateRecord(newRecord);
    }

    public void updateBestRound(GameScreen.Difficulty difficulty, int bestRound) {
        records.get(difficulty).updateRecord(bestRound, 0);
    }
    public void updateMostBases(GameScreen.Difficulty difficulty, int mostBases) {
        records.get(difficulty).updateRecord(0, mostBases);
    }
}
