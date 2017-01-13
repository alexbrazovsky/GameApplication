package com.example.abrazovsky.myapplication.gameMenu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import com.example.abrazovsky.myapplication.R;
import com.example.abrazovsky.myapplication.database.DatabaseHandler;
import com.example.abrazovsky.myapplication.database.Helper;
import com.example.abrazovsky.myapplication.menu.GameActivity;

/**
 * Created by A.Brazovsky on 05.01.2017.
 */

public class TakePhotoActivity extends AppCompatActivity {
    TextView name;
    VideoView video;
    ImageView image;
    TextView text;
    Button button;
    Bundle extras;
    MenuItem item;
    MenuItem show;
    Helper helper;
    TextView num_of_helpers;
    ImageView iv;
    RelativeLayout badgeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takephoto);
        name = (TextView) findViewById(R.id.textViewName);
        video = (VideoView) findViewById(R.id.videoView);
        image = (ImageView) findViewById(R.id.imageViewPhoto);
        text = (TextView) findViewById(R.id.textViewText);
        button = (Button) findViewById(R.id.buttonStartTask);
        extras = getIntent().getExtras();
        name.setText(extras.getString("task_name"));
        text.setText(extras.getString("task_text"));

        if (extras.getString("task_video").isEmpty()){
            configureImageView();
            image.setVisibility(View.VISIBLE);
        }else{
            configureVideoView();
            video.setVisibility(View.VISIBLE);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                button.post(new Runnable() {
                    @Override
                    public void run() {
                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                video.pause();
                                Intent intent = new Intent(TakePhotoActivity.this, GameActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        }).start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_take_photo,menu);
        item = menu.findItem(R.id.miCompose);
        show = menu.findItem(R.id.viewHelper);
        DatabaseHandler db = new DatabaseHandler(this);
        helper = db.getHelper(extras.getInt("helper_id"));
        db.close();
        badgeLayout = (RelativeLayout) item.getActionView();
        num_of_helpers = (TextView) badgeLayout.findViewById(R.id.badge_textView);
        iv = (ImageView) badgeLayout.findViewById(R.id.hotlist_bell);
        if (helper.getChecked()==0){
            item.setVisible(true);
            configureNumOfHelpers();
        }else show.setVisible(true);
        configureShowItem();
        return true;
    }
    public void configureNumOfHelpers () {
        final DatabaseHandler db = new DatabaseHandler(this);
        int num_of_t = db.getTasksCount();
        int num_of_h = db.getHelpersCount();
        db.close();
        Double double_num = (num_of_t*0.5+1.5)-(num_of_h);
        int num = double_num.intValue();
        if (num < 1){
            num_of_helpers.setText("0");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    iv.post(new Runnable() {
                        @Override
                        public void run() {
                            iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder alert = set_alert("Ой", "Ты использовала все подсказки." +
                                            "Звони людям, которых ты больше всех любишь.","Плохо");
                                    AlertDialog alert_dialog = alert.create();
                                    alert_dialog.show();
                                }
                            });
                        }
                    });
                }
            }).start();
        }else {
            num_of_helpers.setText(String.valueOf(num));
            configureHelperImageView();
        }
    }
    public AlertDialog.Builder set_alert (final String title, final String message, final String no ){
        AlertDialog.Builder alert = new AlertDialog.Builder(TakePhotoActivity.this);
        alert.setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return alert;
    }
    public void configureShowItem (){
        show.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showThatHelper(helper.getText());
                return false;
            }
        });
    }
    public void configureHelperImageView (){
        final DatabaseHandler db = new DatabaseHandler(this);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = set_alert("Подумай.", "Ты действительно хочешь"
                        +" потратить подсказку?","Нет");
                alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                item.setVisible(false);
                                show.setVisible(true);
                                dialog.cancel();
                                showThatHelper(helper.getText());
                                db.updateHelper(helper.getID());
                            }
                        });
                AlertDialog alert_dialog = alert.create();
                alert_dialog.show();
            }
        });
        db.close();
    }
    public void showThatHelper (String text){
        AlertDialog.Builder alert = set_alert("Подсказка", text, "Понятно");
        AlertDialog alert_dialog = alert.create();
        alert_dialog.show();
    }
    public void configureImageView (){
        int id = getResources().getIdentifier(extras.getString("task_photo"),
                "drawable", getPackageName());
        image.setImageResource(id);
    }
    public void configureVideoView (){
        final MediaController mediaController = new MediaController(this);
        final Uri uri = getUri(extras.getString("task_video"));
        video.setVideoURI(uri);
        new Thread(new Runnable() {
            @Override
            public void run() {
                video.post(new Runnable() {
                    @Override
                    public void run() {
                        //video.seekTo(100);
                        video.setOnTouchListener( new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                video.setMediaController(mediaController);
                                mediaController.setAnchorView(video);
                                mediaController.show();
                                return true;
                            }
                        });
                    }
                });
            }
        }).start();
    }
    public Uri getUri (String name){
        int id = this.getResources().getIdentifier(name, "raw", this.getPackageName());
        String videopath = "android.resource://com.example.abrazovsky.myapplication/"
                +id;
        Uri uri = Uri.parse(videopath);
        return uri;
    }
}
