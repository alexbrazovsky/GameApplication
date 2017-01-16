package com.example.abrazovsky.myapplication.gameMenu;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    public int goldDark, goldMed, gold, goldLight;
    public int[] colors;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        container = (ViewGroup) findViewById(R.id.container);
        final Resources res = getResources();
        goldDark = Color.parseColor(res.getString(R.string.blue_dark));
        goldMed = Color.parseColor(res.getString(R.string.blue_med));
        gold = Color.parseColor(res.getString(R.string.purple));
        goldLight = Color.parseColor(res.getString(R.string.blue_light));
        colors = new int[] { goldDark, goldMed, gold, goldLight };
    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            CommonConfetti.rainingConfetti(container, colors)
                    .infinite();
        }
    }
}
