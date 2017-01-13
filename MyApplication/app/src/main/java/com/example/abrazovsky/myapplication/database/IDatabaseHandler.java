package com.example.abrazovsky.myapplication.database;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A.Brazovsky on 06.01.2017.
 */

public interface IDatabaseHandler {
    public void addTask(JSONObject task) throws JSONException;
    public void addHelper(JSONObject helper) throws JSONException;
    public Task getTask(String name);
    public Helper getHelper (int id);
    public List<Task> getAllTasks();
    public int updateTask(Task task);
    public void updateHelper(int helper);
    public void refreshTable();
    public void refreshTableHelpers();
    public int getTasksCount();
    public int getHelpersCount();
}
