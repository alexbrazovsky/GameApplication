package com.example.abrazovsky.myapplication.database;

/**
 * Created by A.Brazovsky on 06.01.2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tasksManager";
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_HELPERS = "helpers";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_VIDEO = "video";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_TEXT = "text_of_task";
    private static final String KEY_LAT = "lattitude";
    private static final String KEY_LON = "longitude";
    private static final String KEY_CHECK = "checked_if_done";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT, "
                + KEY_VIDEO + " TEXT, " + KEY_PHOTO + " TEXT, " + KEY_TEXT + " TEXT, "
                + KEY_LAT + " DOUBLE, " + KEY_LON + " DOUBLE, " + KEY_CHECK + " INTEGER" + ")";
        String CREATE_HELPERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HELPERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TEXT + " TEXT, "
                + KEY_CHECK + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_HELPERS_TABLE);
    }
    public void resetTables() {
        String drop_tasks = "DROP TABLE IF EXISTS " + TABLE_TASKS ;
        String drop_helpers = "DROP TABLE IF EXISTS " + TABLE_HELPERS ;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(drop_tasks);
        db.execSQL(drop_helpers);
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetTables();
        onCreate(db);
        db.close();
    }
    @Override
    public void addTask(JSONObject task) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,  task.getString("name"));
        values.put(KEY_VIDEO, task.getString("video"));
        values.put(KEY_PHOTO, task.getString("photo"));
        values.put(KEY_TEXT, task.getString("text"));
        values.put(KEY_LAT, task.getDouble("lat"));
        values.put(KEY_LON, task.getDouble("lon"));
        values.put(KEY_CHECK, task.getInt("checked"));
        db.insert(TABLE_TASKS, null, values);
        db.close();
    }
    @Override
    public void addHelper(JSONObject helper) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, helper.getString("text"));
        values.put(KEY_CHECK, helper.getInt("checked"));
        db.insert(TABLE_HELPERS, null, values);
        db.close();
    }
    @Override
    public Task getTask(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_ID,
                        KEY_NAME, KEY_VIDEO, KEY_PHOTO,
                        KEY_TEXT, KEY_LAT, KEY_LON, KEY_CHECK }, KEY_NAME + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        Task task = new Task(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2) ,cursor.getString(3), cursor.getString(4),
                Double.parseDouble(cursor.getString(5)), Double.parseDouble(cursor.getString(6)),
                Integer.parseInt(cursor.getString(7)));
        return task;
    }
    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_ID,
                        KEY_NAME, KEY_VIDEO, KEY_PHOTO,
                        KEY_TEXT, KEY_LAT, KEY_LON, KEY_CHECK }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        Task task = new Task(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2) ,cursor.getString(3), cursor.getString(4),
                Double.parseDouble(cursor.getString(5)), Double.parseDouble(cursor.getString(6)),
                Integer.parseInt(cursor.getString(7)));
        return task;
    }
    @Override
    public Helper getHelper (int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HELPERS, new String[] { KEY_ID,
                        KEY_TEXT, KEY_CHECK }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        Helper helper = new Helper(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                Integer.parseInt(cursor.getString(2)));
        return helper;
    }
    @Override
    public List<Task> getAllTasks() {
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Task> tasks = new ArrayList<Task>();
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setID(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setVideo(cursor.getString(2));
                task.setPhoto(cursor.getString(3));
                task.setText(cursor.getString(4));
                task.setLat(Double.parseDouble(cursor.getString(5)));
                task.setLon(Double.parseDouble(cursor.getString(6)));
                task.setChecked(Integer.parseInt(cursor.getString(7)));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        return tasks;
    }
    @Override
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CHECK, 1);
        return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getID()) });
    }
    @Override
    public void updateHelper(int helper){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CHECK, 1);
        db.update(TABLE_HELPERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(helper)});
    }
    @Override
    public void refreshTable() {
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues values = new ContentValues();
                values.put(KEY_CHECK, 0);
                db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                        new String[]{String.valueOf(cursor.getString(0))});
            } while (cursor.moveToNext());
        }
    }
    @Override
    public void refreshTableHelpers() {
        String selectQuery = "SELECT  * FROM " + TABLE_HELPERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues values = new ContentValues();
                values.put(KEY_CHECK, 0);
                db.update(TABLE_HELPERS, values, KEY_ID + " = ?",
                        new String[]{String.valueOf(cursor.getString(0))});
            } while (cursor.moveToNext());
        }
    }
    @Override
    public int getTasksCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE " + KEY_CHECK + "=1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    @Override
    public int getHelpersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_HELPERS + " WHERE " + KEY_CHECK + "=1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
