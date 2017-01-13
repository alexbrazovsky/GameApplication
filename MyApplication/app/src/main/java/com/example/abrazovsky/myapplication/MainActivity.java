package com.example.abrazovsky.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.abrazovsky.myapplication.database.DatabaseHandler;
import com.example.abrazovsky.myapplication.database.Task;
import com.example.abrazovsky.myapplication.mailSender.GMailSender;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button2);
        final DatabaseHandler db = new DatabaseHandler(this);
        new Thread(new Runnable() {
                @Override
                public void run() {
                    if (db.getAllTasks().isEmpty()) {
                        addData(db);
                    }
                }
            }).start();
        db.close();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText password = (EditText)findViewById(R.id.editText);
                String text = password.getText().toString();
                if (text.equals(getString(R.string.pass_word))) {
                /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GMailSender user = new GMailSender();
                            user.sendMail("Света вошла в приложение!!!");
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }
                }).start();*/
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setCancelable(false)
                            .setTitle("Ошибочка!")
                            .setMessage("Попробуй еще раз!")
                            .setNegativeButton("Еще раз!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert_dialog = alert.create();
                    alert_dialog.show();
                }
            }
        });
    }
    @Override
    protected void onStop(){
        super.onStop();
        EditText password = (EditText)findViewById(R.id.editText);
        password.setText("");
    }
    public void addData (DatabaseHandler db){
        String jsonString = loadJSONFromAsset("tasks.json");
        String jsonStringHelpers = loadJSONFromAsset("helpers.json");
        JSONArray jsonTasks = null;
        JSONArray jsonHelpers = null;
        try {
            jsonTasks = new JSONArray(jsonString);
            jsonHelpers = new JSONArray(jsonStringHelpers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i=0; i<jsonTasks.length();i++) {
            try {
                db.addTask(jsonTasks.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i=0; i<jsonHelpers.length();i++) {
            try {
                db.addHelper(jsonHelpers.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public String loadJSONFromAsset(String asset) {
        String json = null;
        try {
            InputStream is = null;
            try {
                is = getAssets().open(asset);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
