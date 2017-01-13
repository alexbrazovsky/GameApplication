package com.example.abrazovsky.myapplication.menu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.abrazovsky.myapplication.R;
import com.example.abrazovsky.myapplication.mailSender.GMailSender;

/**
 * Created by A.Brazovsky on 04.01.2017.
 */

public class HelpActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender user = new GMailSender();
                    user.sendMail("Света просматривает обучалку");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        }).start();
        */
    }
}
