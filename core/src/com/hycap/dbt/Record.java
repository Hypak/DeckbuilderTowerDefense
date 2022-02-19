package com.hycap.dbt;

public class Record {
    public int mostRounds;
    public int mostBasesDestroyed;
    public Record(int mostRounds, int mostBasesDestroyed) {
        this.mostRounds = mostRounds;
        this.mostBasesDestroyed = mostBasesDestroyed;
    }
    public void updateRecord(int mostRounds, int mostBasesDestroyed) {
        if (mostRounds > this.mostRounds) {
            this.mostRounds = mostRounds;
        }
        if (mostBasesDestroyed > this.mostBasesDestroyed) {
            this.mostBasesDestroyed = mostBasesDestroyed;
        }
    }

    public void updateRecord(Record record) {
        updateRecord(record.mostRounds, record.mostBasesDestroyed);
    }
}
