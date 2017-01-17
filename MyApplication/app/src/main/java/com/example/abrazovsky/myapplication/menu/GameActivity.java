package com.example.abrazovsky.myapplication.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrazovsky.myapplication.ListActivity;
import com.example.abrazovsky.myapplication.R;
import com.example.abrazovsky.myapplication.database.DatabaseHandler;
import com.example.abrazovsky.myapplication.database.Task;
import com.example.abrazovsky.myapplication.gameMenu.FinalActivity;
import com.example.abrazovsky.myapplication.gameMenu.GalleryActivity;
import com.example.abrazovsky.myapplication.gameMenu.MapActivity;
import com.example.abrazovsky.myapplication.gameMenu.TakePhotoActivity;
import com.example.abrazovsky.myapplication.mailSender.GMailSender;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Created by A.Brazovsky on 04.01.2017.
 */

public class GameActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        final Button buttonPhoto = (Button) findViewById(R.id.buttonPhoto);
        final Activity activity = this;
        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IntentIntegrator integrator  = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt(getString(R.string.scaning_word));
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        final Button buttonGallery = (Button) findViewById(R.id.buttonGallery);
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        });

        final Button buttonMap = (Button) findViewById(R.id.buttonMap);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String content = result.getContents();
        System.out.println(content);
        if (result != null) {
            if (content==null){
                Toast.makeText(this, getString(R.string.scaning_cancel), Toast.LENGTH_LONG).show();
            }
            else if (content.equals(getString(R.string.final_word))){
                Intent intent = new Intent(GameActivity.this, FinalActivity.class);
                startActivity(intent);
                /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                GMailSender user = new GMailSender();
                                user.sendMail("Света закончила игру" );
                            } catch (Exception e) {
                                Log.e("SendMail", e.getMessage(), e);
                            }
                        }
                    }).start();*/
            }
            else{
                DatabaseHandler db = new DatabaseHandler(this);
                Task task = new Task();
                try{
                    task = db.getTask(content);
                    db.updateTask(task);
                    db.close();
                    Log.d("task",String.valueOf(task));
                }catch (Exception e){}
                if (task.getID()==0) {
                    Toast.makeText(this, getString(R.string.scaning_wrong), Toast.LENGTH_LONG)
                            .show();
                }
                else{
                    Intent intent = new Intent(GameActivity.this, TakePhotoActivity.class);
                    intent.putExtra("task_name",task.getName());
                    intent.putExtra("task_text",task.getText());
                    intent.putExtra("task_video",task.getVideo());
                    intent.putExtra("task_photo",task.getPhoto());
                    intent.putExtra("helper_id",task.getID());

                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                GMailSender user = new GMailSender();
                                if (task.getChecked()==0){
                                    db.updateTask(task);
                                    user.sendMail("Света начала выполнять задание \""
                                        + task.getName() + "\"" );}
                            } catch (Exception e) {
                                Log.e("SendMail", e.getMessage(), e);
                            }
                        }
                    }).start();*/
                    startActivity(intent);
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_game_activity,menu);
        MenuItem item = menu.findItem(R.id.GameExit);
        final DatabaseHandler db = new DatabaseHandler(this);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GameActivity.this);
                alert.setCancelable(true)
                        .setTitle(getString(R.string.game_exit_alert_title))
                        .setMessage(getString(R.string.game_exit_alert_message))
                        .setNegativeButton(getString(R.string.game_exit_negative_button),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                        .setPositiveButton(getString(R.string.game_exit_positive_button),
                                new DialogInterface.OnClickListener() {
                            @Override
                                public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(GameActivity.this, ListActivity.class);
                                int FLAG_ACTIVITY_NO_HISTORY = 1073741824;
                                intent.setFlags(FLAG_ACTIVITY_NO_HISTORY);
                                finish();
                                db.refreshTable();
                                db.refreshTableHelpers();
                                db.close();
                                }
                            });
                AlertDialog alert_dialog = alert.create();
                alert_dialog.show();
                return false;
            }
        });
        db.close();
        return true;
    }
}
