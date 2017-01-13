package com.example.abrazovsky.myapplication.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.abrazovsky.myapplication.R;
import com.example.abrazovsky.myapplication.database.DatabaseHandler;
import com.example.abrazovsky.myapplication.mailSender.GMailSender;

/**
 * Created by A.Brazovsky on 04.01.2017.
 */

public class ConfirmActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        final DatabaseHandler db = new DatabaseHandler(this);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender user = new GMailSender();
                    user.sendMail("Света начала новую игру и подтвердила лицензию");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        }).start();*/
        final Button buttonGame = (Button) findViewById(R.id.buttonLicense);
        buttonGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmActivity.this, GameActivity.class);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            db.refreshTable();
                            db.refreshTableHelpers();
                            db.close();
                        } catch (Exception e) {
                            Log.e("Refreshing Databasse", e.getMessage(), e);
                        }
                    }
                }).start();
                startActivity(intent);
            }
        });
    }
}
