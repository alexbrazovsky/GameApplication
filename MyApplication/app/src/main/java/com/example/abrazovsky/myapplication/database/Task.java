package com.example.abrazovsky.myapplication.database;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

/**
 * Created by A.Brazovsky on 06.01.2017.
 */


public class Task {
    public int id;
    public String name;
    public String video;
    public String photo;
    public String text;
    public double lat;
    public double lon;
    public int checked;

    public Task(){};

    public Task(int id, String name, String video, String photo, String text,
                double lat, double lon, int checked){
        this.id = id;
        this.name = name;
        this.video = video;
        this.photo = photo;
        this.text = text;
        this.lat = lat;
        this.lon = lon;
        this.checked = checked;
    }
    public Task(int id, String name, String video, String photo, String text, double lat, double lon){
        this.id = id;
        this.name = name;
        this.video = video;
        this.photo = photo;
        this.text = text;
        this.lat = lat;
        this.lon = lon;
        this.checked = 0;
    }


    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getVideo(){
        return this.video;
    }

    public void setVideo(String video){
        this.video = video;
    }

    public String getPhoto(){
        return this.photo;
    }

    public void setPhoto(String photo){
        this.photo = photo;
    }

    public String getText(){
        return this.text;
    }

    public void setText(String text){
        this.text = text;
    }

    public double getLat(){
        return this.lat;
    }

    public void setLat(double lat){
        this.lat = lat;
    }

    public double getLon(){
        return this.lon;
    }

    public void setLon(double lon){
        this.lon = lon;
    }

    public int getChecked(){
        return this.checked;
    }

    public void setChecked(int checked){
        this.checked = checked;
    }
}