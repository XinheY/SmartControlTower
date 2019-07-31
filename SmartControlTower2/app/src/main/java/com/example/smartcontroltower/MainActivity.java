package com.example.smartcontroltower;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private ProgressBar pb = null;
    private Button btn;
    private ArrayList<LinkedHashMap<String, String>> answerMain;

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
                timer.schedule(task, 1500);//1.5秒后执行TimeTask的run方法
                //////////////////////////////////////////////
            }
        });

    }

    private void test(final String sql) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //测试数据库的语句,在子线程操作
                //answerE2E = DBUtil.QuerySQL(sql);
                answerMain = DBUtil.sendRequestWithOkHttp();
                Message msg = new Message();
                msg.what = 1001;
                Bundle data = new Bundle();
                msg.setData(data);
                mHandler.sendMessage(msg);
            }
        };
        new Thread(run).start();
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1001:
                    String str = msg.getData().getString("result");
                    //ld.loadFailed();
                    break;

                default:
                    break;
            }
        }
    };



}
