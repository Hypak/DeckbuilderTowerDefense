package com.hycap.dbt;

class Record {
    public int mostRounds;
    public int mostBasesDestroyed;
    Record(final int mostRounds, final int mostBasesDestroyed) {
        this.mostRounds = mostRounds;
        this.mostBasesDestroyed = mostBasesDestroyed;
    }
    void updateRecord(final int mostRounds, final int mostBasesDestroyed) {
        if (mostRounds > this.mostRounds) {
            this.mostRounds = mostRounds;
        }
        if (mostBasesDestroyed > this.mostBasesDestroyed) {
            this.mostBasesDestroyed = mostBasesDestroyed;
        }
    }

    void updateRecord(final Record record) {
        updateRecord(record.mostRounds, record.mostBasesDestroyed);
    }
}
