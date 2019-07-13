package com.example.smartcontroltower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    ProgressBar pb = null;
    Button btn;

    @Override


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar) this.findViewById(R.id.login_pb);
        btn = findViewById(R.id.login_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                ///////////////////Need edit////////////////////
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(MainActivity.this,WelcomePage.class);
                        startActivity(intent);
                        finish();
                    }
                };
                timer.schedule(task, 2000);//3秒后执行TimeTask的run方法
                //////////////////////////////////////////////
            }
        });

    }


}
