package com.hycap.dbt;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.HashMap;
import java.util.Map;
public class RecordsHolder {
    private final Map<GameScreen.Difficulty, Record> records;
    private final Preferences preferences = Gdx.app.getPreferences("DBTD/Records");
    public RecordsHolder() {
        records = new HashMap<>();
        if (preferences.contains("notEmpty")) {
            final Record easyRecord = new Record(preferences.getInteger("easyRounds"),
                    preferences.getInteger("easyBases"));
            records.put(GameScreen.Difficulty.EASY, easyRecord);

            final Record normalRecord = new Record(preferences.getInteger("normalRounds"),
                    preferences.getInteger("normalBases"));
            records.put(GameScreen.Difficulty.NORMAL, normalRecord);

            final Record hardRecord = new Record(preferences.getInteger("hardRounds"),
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

    public Record getRecord(final GameScreen.Difficulty difficulty) {
        if (records.containsKey(difficulty)) {
            return records.get(difficulty);
        } else {
            return new Record(0, 0);
        }
    }

    public void updateRecord(final GameScreen.Difficulty difficulty, final Record newRecord) {
        getRecord(difficulty).updateRecord(newRecord);
        updatePrefs();
    }

    public void updateBestRound(final GameScreen.Difficulty difficulty, final int bestRound) {
        getRecord(difficulty).updateRecord(bestRound, 0);
        updatePrefs();
    }
    public void updateMostBases(final GameScreen.Difficulty difficulty, final int mostBases) {
        getRecord(difficulty).updateRecord(0, mostBases);
        updatePrefs();
    }
}
