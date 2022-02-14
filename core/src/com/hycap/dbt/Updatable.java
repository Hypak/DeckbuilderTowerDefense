package com.hycap.dbt;

public interface Updatable {
    void update(float deltaT);
    boolean keepActive();
}
