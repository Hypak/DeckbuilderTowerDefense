package com.hycap.dbt.tasks;

import java.util.List;

interface Task {
    String getTaskText();
    List<Task> getNextTasks();
    boolean isCompleted();
}
