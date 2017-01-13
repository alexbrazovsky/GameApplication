package com.example.abrazovsky.myapplication;

/**
 * Created by A.Brazovsky on 03.01.2017.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.abrazovsky.myapplication.database.DatabaseHandler;
import com.example.abrazovsky.myapplication.mailSender.GMailSender;
import com.example.abrazovsky.myapplication.menu.ConfirmActivity;
import com.example.abrazovsky.myapplication.menu.GameActivity;
import com.example.abrazovsky.myapplication.menu.HelpActivity;

public class ListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        final Button buttonGame = (Button) findViewById(R.id.buttonGame);
        buttonGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, ConfirmActivity.class);
                startActivity(intent);
            }
        });
        final Button buttonHelp = (Button) findViewById(R.id.buttonHelp);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GMailSender user = new GMailSender();
                            user.sendMail("Света просматривает обучалку");
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }
                }).start();*/
                Intent intent = new Intent(ListActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        final Button buttonExit = (Button) findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseHandler db = new DatabaseHandler(this);
        Button buttonContinue = (Button) findViewById(R.id.buttonContinue);
        if (db.getTasksCount()==0) {buttonContinue.setVisibility(View.GONE);}
        else {
            buttonContinue.setVisibility(View.VISIBLE);
            buttonContinue.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ListActivity.this, GameActivity.class);
                    startActivity(intent);
                }
            });
        }
        db.close();
    }
}
