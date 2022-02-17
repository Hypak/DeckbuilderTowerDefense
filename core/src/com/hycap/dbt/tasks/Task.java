package com.hycap.dbt.tasks;

import java.util.List;

public interface Task {
    String getTaskText();
    List<Task> getNextTasks();
    boolean isFinished();
}
