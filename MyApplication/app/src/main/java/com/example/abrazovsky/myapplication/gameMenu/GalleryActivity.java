package com.example.abrazovsky.myapplication.gameMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.abrazovsky.myapplication.R;
import com.example.abrazovsky.myapplication.database.DatabaseHandler;
import com.example.abrazovsky.myapplication.database.Task;

/**
 * Created by A.Brazovsky on 05.01.2017.
 */

public class GalleryActivity extends AppCompatActivity {
    LinearLayout.LayoutParams Params = new LinearLayout
            .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT, 0.5f);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        LinearLayout main = (LinearLayout) findViewById(R.id.linearLayoutGallery);
        drawScreenLayout(main);
    }
    public void drawScreenLayout( LinearLayout main) {
        int a=3;
        int index = 0;
        for (int i = 1; i <= a; i++) {
            LinearLayout newline = drawLinearLayout(i);
            for (int b = 1; b <= a - 1; b++) {
                index++;
                newline.addView(drawButton(index));
            }
            main.addView(newline);
        }
    }
    public LinearLayout drawLinearLayout(final int id) {
        final LinearLayout newline = new LinearLayout(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                newline.post(new Runnable() {
                    @Override
                    public void run() {
                        newline.setId(id);
                        newline.setOrientation(LinearLayout.HORIZONTAL);
                        newline.setLayoutParams(Params);
                    }
                });
            }
        }).start();
        return newline;
    }
    public Button drawButton(int id) {
        final DatabaseHandler db = new DatabaseHandler(this);
        final Task task = db.getTask(id);
        db.close();
        final Button btn = new Button(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                btn.post(new Runnable() {
                    @Override
                    public void run() {
                        btn.setId(task.getID());
                        btn.setLayoutParams(Params);
                    }
                });
            }
        }).start();

        if ( task.getChecked() != 0 ){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    btn.post(new Runnable() {
                        @Override
                        public void run() {
                            btn.setText("/*Пока не пройдено задание*/");
                            btn.setEnabled(false);
                        }
                    });
                }
            }).start();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    btn.post(new Runnable() {
                        @Override
                        public void run() {
                            btn.setText(task.getName());
                            btn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final Intent intent = new Intent(GalleryActivity.this, TakePhotoActivity.class);
                                            intent.putExtra("task_name",task.getName());
                                            intent.putExtra("task_text",task.getText());
                                            intent.putExtra("task_video",task.getVideo());
                                            intent.putExtra("task_photo",task.getPhoto());
                                            intent.putExtra("helper_id",task.getID());
                                            startActivity(intent);
                                        }
                                    }).start();
                                }
                            });
                        }
                    });
                }
            }).start();
        }
        return btn;
    }
}
