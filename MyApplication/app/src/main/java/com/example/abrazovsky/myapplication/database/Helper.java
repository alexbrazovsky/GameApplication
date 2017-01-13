package com.example.abrazovsky.myapplication.database;

/**
 * Created by A.Brazovsky on 12.01.2017.
 */

public class Helper {
    public int id;
    public String text;
    public int checked;
    public Helper(){};

    public Helper(int id, String text, int checked){
        this.id = id;
        this.text = text;
        this.checked = checked;
    }
    public Helper(int id, String text){
        this.id = id;
        this.text = text;
        this.checked = 0;
    }
    public int getID(){
        return this.id;
    }
    public void setID(int id){
        this.id = id;
    }
    public String getText(){
        return this.text;
    }
    public void setText(String text){
        this.text = text;
    }
    public int getChecked(){
        return this.checked;
    }
    public void setChecked(int checked){
        this.checked = checked;
    }
}
