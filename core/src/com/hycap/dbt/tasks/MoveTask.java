package com.hycap.dbt.tasks;

import com.badlogic.gdx.math.Vector3;
import com.hycap.dbt.CameraManager;

import java.util.ArrayList;
import java.util.List;

public class MoveTask implements Task {
    public static boolean finished = false;

    @Override
    public String getTaskText() {
        return "Move with WASD or the arrow keys";
    }

    @Override
    public List<Task> getNextTasks() {
        List<Task> next = new ArrayList<>();
        next.add(new ScrollTask());
        return next;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
