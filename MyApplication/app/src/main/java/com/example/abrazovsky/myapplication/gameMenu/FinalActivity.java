package com.example.abrazovsky.myapplication.gameMenu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.abrazovsky.myapplication.R;
import com.github.jinatonic.confetti.CommonConfetti;

/**
 * Created by Alex on 14.01.2017.
 */

public class FinalActivity extends AppCompatActivity {
    public ViewGroup container;
    public Button button;
    public int blueDark, blueMed, purple, blueLight;
    public int[] colors;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        container = (ViewGroup) findViewById(R.id.container);
        button = (Button) findViewById(R.id.buttonGift);
        final Resources res = getResources();
        blueDark = Color.parseColor(res.getString(R.string.blue_dark));
        blueMed = Color.parseColor(res.getString(R.string.blue_med));
        purple = Color.parseColor(res.getString(R.string.purple));
        blueLight = Color.parseColor(res.getString(R.string.blue_light));
        colors = new int[] { blueDark, blueMed, purple, blueLight };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(FinalActivity.this);
                alertDialog.setCancelable(false)
                        .setTitle(getString(R.string.final_alert_title))
                        .setMessage(getString(R.string.final_alert_message))
                        .setPositiveButton(getString(R.string.final_positive_button)
                                , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(FinalActivity.this,MapActivity.class);
                                intent.putExtra("blue_marker","yes");
                                startActivity(intent);
                            }
                        }).show();
            }
        });
    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            CommonConfetti.rainingConfetti(container, colors)
                    .infinite();
        }
    }
}
