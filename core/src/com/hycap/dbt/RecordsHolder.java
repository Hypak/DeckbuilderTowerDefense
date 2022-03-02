package com.hycap.dbt;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.HashMap;
import java.util.Map;
public class RecordsHolder {
    public Map<GameScreen.Difficulty, Record> records;
    Preferences preferences = Gdx.app.getPreferences("DBTD/Records");
    public RecordsHolder() {
        records = new HashMap<>();
        if (preferences.contains("notEmpty")) {
            Record easyRecord = new Record(preferences.getInteger("easyRounds"),
                    preferences.getInteger("easyBases"));
            records.put(GameScreen.Difficulty.EASY, easyRecord);

            Record normalRecord = new Record(preferences.getInteger("normalRounds"),
                    preferences.getInteger("normalBases"));
            records.put(GameScreen.Difficulty.NORMAL, normalRecord);

            Record hardRecord = new Record(preferences.getInteger("hardRounds"),
                    preferences.getInteger("hardBases"));
            records.put(GameScreen.Difficulty.HARD, hardRecord);
        } else {
            preferences.putBoolean("notEmpty", true);
            records.put(GameScreen.Difficulty.EASY, new Record(0, 0));
            records.put(GameScreen.Difficulty.NORMAL, new Record(0, 0));
            records.put(GameScreen.Difficulty.HARD, new Record(0, 0));
            updatePrefs();
        }
    }

    private void updatePrefs() {
        preferences.putInteger("easyRounds", records.get(GameScreen.Difficulty.EASY).mostRounds);
        preferences.putInteger("normalRounds", records.get(GameScreen.Difficulty.NORMAL).mostRounds);
        preferences.putInteger("hardRounds", records.get(GameScreen.Difficulty.HARD).mostRounds);

        preferences.putInteger("easyBases", records.get(GameScreen.Difficulty.EASY).mostBasesDestroyed);
        preferences.putInteger("normalBases", records.get(GameScreen.Difficulty.NORMAL).mostBasesDestroyed);
        preferences.putInteger("hardBases", records.get(GameScreen.Difficulty.HARD).mostBasesDestroyed);
        preferences.flush();
    }

    public void updateRecord(GameScreen.Difficulty difficulty, Record newRecord) {
        records.get(difficulty).updateRecord(newRecord);
        updatePrefs();
    }

    public void updateBestRound(GameScreen.Difficulty difficulty, int bestRound) {
        records.get(difficulty).updateRecord(bestRound, 0);
        updatePrefs();
    }
    public void updateMostBases(GameScreen.Difficulty difficulty, int mostBases) {
        records.get(difficulty).updateRecord(0, mostBases);
        updatePrefs();
    }
}
