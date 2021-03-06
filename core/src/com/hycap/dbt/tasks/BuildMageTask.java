package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class BuildMageTask implements Task {
    public static boolean finished = false;
    @Override
    public String getTaskText() {
        return "Buy a mage card from the shop and build it.\nBuilding it will remove the card from your deck.";
    }

    @Override
    public List<Task> getNextTasks() {
        List<Task> next = new ArrayList<>();
        return next;    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
