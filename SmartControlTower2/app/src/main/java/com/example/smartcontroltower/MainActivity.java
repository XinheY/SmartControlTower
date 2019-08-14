package com.example.smartcontroltower;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private Button btn;//登陆键
    private ArrayList<LinkedHashMap<String, String>> answerEOQ;
    private ArrayList<LinkedHashMap<String, String>> answerIDC;
    private LoadingDialog ld = null;
    private ArrayList<String> version = null;
    private ArrayList<String> version_year = null;
    private ArrayList<String> version_addclosing = null;
    private ArrayList<String> version_year_quar = null;
    private ArrayList<String> version_year_quar_week = null;
    private ArrayList<String> version2 = null;
    private ArrayList<String> version_year2 = null;
    private ArrayList<String> version_addclosing2 = null;
    private ArrayList<String> version_year_quar2 = null;
    private ArrayList<String> version_year_quar_week2 = null;
    private int[] AccessRight = new int[4];
    private EditText userName;

    /**
     * 创建界面，获取layout中组件
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.login_username);
        //加载中动画
        ld = new LoadingDialog(this);
        ld.setLoadingText("Loading...").setSuccessText("Success").setFailedText("Failed")
                .closeSuccessAnim();

        //登陆键
        btn = findViewById(R.id.login_login);
        //按键监听
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ld = new LoadingDialog(view.getContext());
                ld.setLoadingText("Loading...").setSuccessText("Success").setFailedText("Failed")
                        .closeSuccessAnim();
                ld.show();
                test("sql");
            }
        });
    }

    /**
     * 链接数据库获取数据
     * @param sql
     */
    private void test(final String sql) {
        Runnable run = new Runnable() {
            @Override
            public void run() {

                //判断用户名是否拥有权限查看该软件包含的四个表格的内容
                AccessRight[0] = DBUtil4Initial.QuerySQLAccess("EXEC SP_MENU_PKG_CHECKACCESSRIGHT ?,?,? output", userName.getText().toString(), "/View/SmartControlTower/PerfDashboard/IDC_EOQ_SummaryNew.aspx");
                AccessRight[1] = DBUtil4Initial.QuerySQLAccess("EXEC SP_MENU_PKG_CHECKACCESSRIGHT ?,?,? output", userName.getText().toString(), "/View/SmartControlTower/PerfDashboard/IDC_EOQ_SNI_ChangeAnalysis.aspx");
                AccessRight[2] = DBUtil4Initial.QuerySQLAccess("EXEC SP_MENU_PKG_CHECKACCESSRIGHT ?,?,? output", userName.getText().toString(), "/View/SmartControlTower/PerfDashboard/Dynamic_CSR_BL.aspx");
                AccessRight[3] = DBUtil4Initial.QuerySQLAccess("EXEC SP_MENU_PKG_CHECKACCESSRIGHT ?,?,? output", userName.getText().toString(), "/View/SmartControlTower/PerfDashboard/BacklogTracking.aspx");

                boolean canrun = false;
                Message msg = new Message();
                for (int i : AccessRight) {
                    if (i == 1) {
                        canrun = true;
                        break;
                    }
                }

                if (canrun) {
                    /**用户有至少一个表格的权限，获取初始化的信息：version/version_addclosing/version_year/
                    version_year_quar/version_year_quar_week
                     初始化包括IDC和EOQ模式**/
                    answerEOQ = DBUtil4Initial.QuerySQL("EXEC SP_IDC_EOQ_GETFILTER 'EOQ'");
                    answerIDC = DBUtil4Initial.QuerySQL("EXEC SP_IDC_EOQ_GETFILTER 'IDC'");
                    if (answerEOQ.size() != 0 && answerIDC.size() != 0) {
                        ArrayList<String> temp = new ArrayList<>();
                        ArrayList<String> temp2 = new ArrayList<>();
                        for (int i = 0; i < answerEOQ.size(); i++) {
                            if (answerEOQ.get(i).containsValue("version_addclosing")) {
                                version = (ArrayList<String>) temp.clone();
                                temp.clear();
                            } else if (answerEOQ.get(i).containsValue("version_year")) {
                                version_addclosing = (ArrayList<String>) temp.clone();
                                temp.clear();
                            } else if (answerEOQ.get(i).containsValue("version_year_quar")) {
                                version_year = (ArrayList<String>) temp.clone();
                                temp.clear();
                            } else if (answerEOQ.get(i).containsValue("version_year_quar_week")) {
                                version_year_quar = (ArrayList<String>) temp.clone();
                                temp.clear();
                            } else if (answerEOQ.get(i).containsKey("option")) {
                                temp.add(answerEOQ.get(i).get("option"));
                            }
                        }
                        version_year_quar_week = (ArrayList<String>) temp.clone();
                        InitializeInfo info = new InitializeInfo(version, version_addclosing, version_year, version_year_quar, version_year_quar_week);
                        for (int j = 0; j < answerIDC.size(); j++) {
                            if (answerIDC.get(j).containsValue("version_addclosing")) {
                                version2 = (ArrayList<String>) temp2.clone();
                                temp2.clear();
                            } else if (answerIDC.get(j).containsValue("version_year")) {
                                version_addclosing2 = (ArrayList<String>) temp2.clone();
                                temp2.clear();
                            } else if (answerIDC.get(j).containsValue("version_year_quar")) {
                                version_year2 = (ArrayList<String>) temp2.clone();
                                temp2.clear();
                            } else if (answerIDC.get(j).containsValue("version_year_quar_week")) {
                                version_year_quar2 = (ArrayList<String>) temp2.clone();
                                temp2.clear();
                            } else if (answerIDC.get(j).containsKey("option")) {
                                temp2.add(answerIDC.get(j).get("option"));
                            }
                        }
                        version_year_quar_week2 = (ArrayList<String>) temp2.clone();

                        InitializeInfo info2 = new InitializeInfo(version2, version_addclosing2, version_year2, version_year_quar2, version_year_quar_week2);
                        msg.what = 1001;
                        Intent intent = new Intent(MainActivity.this, WelcomePage.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("InitializeInfo", info);
                        bundle.putSerializable("InitializeInfo2", info2);
                        bundle.putSerializable("AccessRight", AccessRight);
                        bundle.putString("username", userName.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        msg.what = 1002;//加载超时，输出错误代码
                    }
                } else {
                    //用户无权限
                    msg.what = 1003;
                }
                Bundle data = new Bundle();
                msg.setData(data);
                mHandler.sendMessage(msg);


            }
        };
        new Thread(run).start();
    }

    /**
     * 通过获取结果代码来判断输出结果为成功加载，加载失败或者没有权限
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1001:
                    String str = msg.getData().getString("result");
                    ld.loadSuccess();
                    break;
                case 1002:
                    ld.loadFailed();
                    break;
                case 1003:
                    ld.setFailedText("Access Denied").loadFailed();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 返回键退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                ld.close();
                AlertDialog.Builder build = new AlertDialog.Builder(this);
                build.setTitle("Notice").setMessage("Do you want to Exit?");
                build.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                build.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


}
