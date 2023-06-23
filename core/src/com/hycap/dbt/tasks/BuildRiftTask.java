package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class BuildRiftTask implements Task {
    public static boolean finished = false;
    @Override
    public String getTaskText() {
        return "Build over a rift to generate more energy\nand power-up the placed building.";
    }

    @Override
    public List<Task> getNextTasks() {
        return new ArrayList<>();
    }

    @Override
    public boolean isCompleted() {
        return finished;
    }
}
