package com.example.smartcontroltower;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private ProgressBar pb = null;
    private Button btn;
    private ArrayList<LinkedHashMap<String, String>> answerMain;
    private LoadingDialog ld = null;
    private ArrayList<String> version = null;
    private ArrayList<String> version_year = null;
    private ArrayList<String> version_addclosing = null;
    private ArrayList<String> version_year_quar = null;
    private ArrayList<String> version_year_quar_week = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar) this.findViewById(R.id.login_pb);
        btn = findViewById(R.id.login_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pb.setVisibility(View.VISIBLE);
                ///////////////////Need edit////////////////////
//                Timer timer = new Timer();
                test("sql");
//                TimerTask task = new TimerTask() {
//                    @Override
//                    public void run() {
//                    }
//                };
//                timer.schedule(task, 2000);//1.5秒后执行TimeTask的run方法
                //////////////////////////////////////////////
            }
        });

    }

    private void test(final String sql) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //测试数据库的语句,在子线程操作
                answerMain = DBUtil4Initial.QuerySQL("EXEC SP_IDC_EOQ_GETFILTER 'EOQ'");
                //answerMain = DBUtil4Initial.sendRequestWithOkHttp();
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 0; i < answerMain.size(); i++) {
                    if (answerMain.get(i).containsValue("version_addclosing")) {
                        version = (ArrayList<String>) temp.clone();
                        temp.clear();
                    } else if (answerMain.get(i).containsValue("version_year")) {
                        version_addclosing = (ArrayList<String>) temp.clone();
                        temp.clear();
                    } else if (answerMain.get(i).containsValue("version_year_quar")) {
                        version_year = (ArrayList<String>) temp.clone();
                        temp.clear();
                    } else if (answerMain.get(i).containsValue("version_year_quar_week")) {
                        version_year_quar = (ArrayList<String>) temp.clone();
                        temp.clear();
                    } else if (answerMain.get(i).containsKey("option")) {
                        temp.add(answerMain.get(i).get("option"));
                    }
                }
                version_year_quar_week = (ArrayList<String>) temp.clone();


                Message msg = new Message();
                msg.what = 1001;
                Bundle data = new Bundle();
                msg.setData(data);
                mHandler.sendMessage(msg);

                InitializeInfo info = new InitializeInfo(version, version_addclosing, version_year, version_year_quar, version_year_quar_week);
                Intent intent = new Intent(MainActivity.this, WelcomePage.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("InitializeInfo",info);
                intent.putExtras(bundle);
                startActivity(intent);
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
                    break;

                default:
                    break;
            }
        }
    };


}
