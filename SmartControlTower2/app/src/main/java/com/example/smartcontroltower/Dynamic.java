package com.example.smartcontroltower;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bin.david.form.core.SmartTable;
import com.example.smartcontroltower.Fragment_dynamic.Fragment_Dynamic;
import com.example.smartcontroltower.Fragment_dynamic.Fragment_goal;
import com.gingold.basislibrary.utils.BasisTimesUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Dynamic extends AppCompatActivity {

    private int finish = 0;
    private DrawerLayout drawerl;
    private ActionBarDrawerToggle toggle;
    private TabLayout tl;
    private NoSrcoll vp;
    private Button datepicker;
    private ArrayList<Object> maplist = new ArrayList<>();
    private ArrayList<Object> maplist2 = new ArrayList<>();
    private HashMap<String, HashMap<String, CheckBox>> summary = new LinkedHashMap<>();//所有checkbox的集合
    private HashMap<String, ArrayList<RadioButton>> radioSummary = new LinkedHashMap<>();
    private HashMap<String, HashMap<String, CheckBox>> summaryOld = new LinkedHashMap<>();//之前所有checkbox的集合
    private HashMap<String, ArrayList<RadioButton>> radioOld = new LinkedHashMap<>();
    private ArrayList<String> allCondition = new ArrayList<>();
    private ArrayList<String[]> allContent = new ArrayList<>();
    private ArrayList<LinkedHashMap<String, String>> answer;
    private ArrayList<LinkedHashMap<String, String>> answer2;
    private LoadingDialog ld = null;
    public static SmartTable<Object> table;
    private ViewPagerAdapter adapter;
    private InitializeInfo info = null;
    private InitializeInfo info2 = null;
    private String hour = "";
    private int[] AccessRight = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            answer = (ArrayList<LinkedHashMap<String, String>>) savedInstanceState.getSerializable("initial");
            summaryOld = (HashMap<String, HashMap<String, CheckBox>>) savedInstanceState.getSerializable("initial2");
            radioOld = (LinkedHashMap<String, ArrayList<RadioButton>>) savedInstanceState.getSerializable("initial3");
            answer2 = (ArrayList<LinkedHashMap<String, String>>) savedInstanceState.getSerializable("initial4");
        } else {
            answer = new ArrayList<>();
            answer2 = new ArrayList<>();
        }

        ld = new LoadingDialog(this);
        ld.setLoadingText("Loading...").setSuccessText("Success").setFailedText("Failed")
                .closeSuccessAnim();
        Toolbar toolbar = (Toolbar) findViewById(R.id.dyn_toolbar);
        setSupportActionBar(toolbar);

        tl = findViewById(R.id.dyn_tablayout);
        vp = findViewById(R.id.dyn_viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Dynamic(), "Dynamic BL");
        adapter.addFragment(new Fragment_goal(), "vs Goal");
        vp.setAdapter(adapter);
        tl.setupWithViewPager(vp);
        adapter.notifyDataSetChanged();

        info = (InitializeInfo) getIntent().getSerializableExtra("InitializeInfo");
        info2 = (InitializeInfo) getIntent().getSerializableExtra("InitializeInfo2");
        AccessRight = (int[]) getIntent().getSerializableExtra("AccessRight");
////////////////////////////////////////////left side///////////////////////////////////////////
        drawerl = findViewById(R.id.dyn_drawer);
        ActionBar actionb = getSupportActionBar();
        NavigationView nv = findViewById(R.id.nav_view);
        Menu munu = nv.getMenu();
        if (AccessRight[0] == 0) {
            munu.findItem(R.id.nav_E2E).setVisible(false);
        }
        if (AccessRight[1] == 0) {
            munu.findItem(R.id.nav_analysis).setVisible(false);
        }
        if (AccessRight[2] == 0) {
            munu.findItem(R.id.nav_Dynamic).setVisible(false);
        }
        if (AccessRight[3] == 0) {
            munu.findItem(R.id.nav_DirectBL).setVisible(false);
        }

        if (actionb != null) {
            actionb.setDisplayHomeAsUpEnabled(true);
            actionb.setHomeAsUpIndicator(R.drawable.menu);
        }

        nv.setCheckedItem(R.id.nav_Dynamic);
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.nav_status_color);
        nv.setItemTextColor(csl);


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_E2E:
                        Intent intent2 = new Intent(Dynamic.this, E2E.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("InitializeInfo", info);
                        bundle2.putSerializable("InitializeInfo2", info2);
                        bundle2.putSerializable("AccessRight", AccessRight);
                        intent2.putExtras(bundle2);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_Dynamic:
                        break;
                    case R.id.nav_DirectBL:
                        Intent intent4 = new Intent(Dynamic.this, DirectBL.class);
                        Bundle bundle4 = new Bundle();
                        bundle4.putSerializable("InitializeInfo", info);
                        bundle4.putSerializable("InitializeInfo2", info2);
                        bundle4.putSerializable("AccessRight", AccessRight);
                        intent4.putExtras(bundle4);
                        startActivity(intent4);
                        finish();
                        break;
                    case R.id.nav_analysis:
                        Intent intent3 = new Intent(Dynamic.this, Analysis.class);
                        Bundle bundle3 = new Bundle();
                        bundle3.putSerializable("InitializeInfo", info);
                        bundle3.putSerializable("InitializeInfo2", info2);
                        bundle3.putSerializable("AccessRight", AccessRight);
                        intent3.putExtras(bundle3);
                        startActivity(intent3);
                        finish();
                        break;
                    default:
                }
                //Change color base on nav tatus

                drawerl.closeDrawers();
                return true;
            }
        });

////////////////////////////////////////right side////////////////////////////////////////////

        LinearLayout dynotll = findViewById(R.id.dyn_ot);
        String[] dynot = getResources().getStringArray(R.array.dyn_ordertype);
        ConstructCheck("ordertype", dynot, dynotll, "DIRECT");

        LinearLayout dynlobll = findViewById(R.id.dyn_lob);
        String[] dynlob = getResources().getStringArray(R.array.dyn_LOB);
        ConstructCheck("lob", dynlob, dynlobll, "SYSTEM");

        RadioGroup dynhourrg = findViewById(R.id.dyn_rg_hour);
        String[] dynhour = getResources().getStringArray(R.array.dyn_hour);
        ConstructRadio("hour", dynhour, dynhourrg, "0:00");
        ///////////////////////////////添加checkbox结束///////////////////////////////////////
        toggle = new ActionBarDrawerToggle(this, drawerl,
                toolbar, R.string.draw_open, R.string.draw_close) {
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);//抽屉关闭后
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);//抽屉打开后
            }
        };
//        drawerl.setDrawerListener(toggle);

        Button search = findViewById(R.id.dyn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRightSliding();
            }
        });

        datepicker = findViewById(R.id.dyn_datepicker);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        //获取当前时间
        final Date date = new Date(System.currentTimeMillis());
        datepicker.setText(simpleDateFormat.format(date));
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showYearMonthDayPicker();
            }
        });


        Button refresh = findViewById(R.id.dyn_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ld = new LoadingDialog(view.getContext());
                ld.setLoadingText("Loading...").setSuccessText("Success").setFailedText("Failed")
                        .closeSuccessAnim().show();
                String first = "";
                String second = "";
                String third = "";
                String fourth = "";
                for (String str : summary.get("ordertype").keySet()) {
                    CheckBox cb = summary.get("ordertype").get(str);
                    if (cb.isChecked()) {
                        if (first.equals("")) {
                            first += cb.getText();
                        } else {
                            first = first + "," + cb.getText();
                        }
                    }
                }


                for (String str : summary.get("lob").keySet()) {
                    CheckBox cb = summary.get("lob").get(str);
                    if (cb.isChecked()) {
                        if (second.equals("")) {
                            second += cb.getText();
                        } else {
                            second = second + "," + cb.getText();
                        }
                    }
                }

                for (int k = 0; k < radioSummary.get("hour").size(); k++) {
                    RadioButton rb = radioSummary.get("hour").get(k);
                    if (rb.isChecked()) {
                        third = rb.getText() + "";
                    }
                }
                third = (third.split(":"))[0];
                hour = third;
                fourth = datepicker.getText() + "";
                //////////////////////////////////////////////////////////////////
                String[] sql = new String[2];
                sql[0] = "EXEC [P_DYNAMIC_BACKLOG_XMN_RESULTE] '" + first + "','" + second + "','" + fourth + "','" + third + "'";
                sql[1] = "EXEC [P_DYNAMIC_BACKLOG_TRACK] '" + first + "','" + fourth + "','" + third + "'";
                test(sql);
                toggleRightSliding();
            }
        });

        ////////////////////////////////Initialize Table//////////////////////////////////
        if ((answer.size() == 0 || answer2.size() == 0)) {
            updateTable();
        } else if (savedInstanceState != null) {
            setNumber();
            Fragment_Dynamic fd = (Fragment_Dynamic) adapter.getItem(0);
            Fragment_goal fg = (Fragment_goal) adapter.getItem(1);
            fd.refreshDate(maplist, "APJ Dynamic CSR BL (" + datepicker.getText() + " " + hour + ":00)");
            fg.refreshDate(maplist2);
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////

    ///其他help method///

    //手动控制抽屉开关的
    private void toggleRightSliding() {//该方法控制右侧边栏的显示和隐藏
        if (drawerl.isDrawerOpen(GravityCompat.END)) {
            drawerl.closeDrawer(GravityCompat.END);//关闭抽屉
        } else {
            drawerl.openDrawer(GravityCompat.END);//打开抽屉
        }
    }

    private void test(final String[] sql) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //测试数据库的语句,在子线程操作
                answer.clear();
                answer2.clear();
                Message msg = new Message();
                answer = DBUtil.QuerySQL(sql[0], 3);
                answer2 = DBUtil.QuerySQL(sql[1], 3);
                //answer = DBUtil.sendRequestWithOkHttp();
                Log.e("大小", answer.size() + " " + answer2.size());
                if (answer2.size() != 0 || answer.size() != 0) {
                    msg.what = 1001;
                } else {
                    msg.what = 1002;
                }
                setNumber();
                Fragment_Dynamic fd = (Fragment_Dynamic) adapter.getItem(0);
                Fragment_goal fg = (Fragment_goal) adapter.getItem(1);
                fd.refreshDate(maplist, "APJ Dynamic CSR BL (" + datepicker.getText() + " " + hour + ":00)");
                fg.refreshDate(maplist2);
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
                    ld.loadSuccess();
                    break;
                case 1002:
                    Log.e("Timeout", "Timeout in handler");
                    ld.setFailedText("No Data").loadFailed();
                    break;
                default:
                    break;
            }
        }
    };

    public void ConstructCheck(String title, String[] items, LinearLayout ll, String ini) {
        String[] initial = ini.split(",");
        HashMap<String, CheckBox> map = new LinkedHashMap<>();
        allContent.add(items);
        for (int i = 0; i < items.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setTextColor(getResources().getColor(R.color.colorAccent));
            cb.setText(items[i]);
            cb.setTextSize(18);
            map.put(items[i], cb);
            if (summaryOld.size() != 0) {
                if (summaryOld.get(title).get(items[i]).isChecked()) {
                    cb.setChecked(true);
                }
            } else {
                for (int j = 0; j < initial.length; j++) {
                    if (items[i].equals(initial[j])) {
                        cb.setChecked(true);
                    }
                }
            }
            ll.addView(cb);
        }
        summary.put(title, map);
        allCondition.add(title);
    }

    public void ConstructRadio(String title, String[] items, RadioGroup rg, String ini) {
        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        allContent.add(items);
        for (int i = 0; i < items.length; i++) {
            RadioButton rb = new RadioButton(rg.getContext());
            rb.setText(items[i]);
            rb.setTextSize(18);
            rb.setId(i);
            rb.setTextColor(getResources().getColor(R.color.colorAccent));
            radioButtons.add(rb);
            rg.addView(rb);
            if (radioOld.size() != 0) {
                if (radioOld.get(title).get(i).isChecked()) {
                    rb.setChecked(true);
                }
            } else {
                if (items[i].equals(ini)) {
                    rb.setChecked(true);
                }
            }
        }
        radioSummary.put(title, radioButtons);
        allCondition.add(title);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerl.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    //展示filter中的内容
    public void showMutilAlertDialog(View view) {
        LinearLayout ll = null;
        switch (view.getId()) {
            case R.id.dyn_ot_btn:
                ll = findViewById(R.id.dyn_ot);
                break;
            case R.id.dyn_lob_btn:
                ll = findViewById(R.id.dyn_lob);
                break;
            case R.id.dyn_hour_btn:
                ll = findViewById(R.id.dyn_hour);
                break;
            default:

        }
        if (ll.getVisibility() == View.VISIBLE) {
            ll.setVisibility(View.GONE);
        } else {
            ll.setVisibility(View.VISIBLE);
        }
    }

    private void showYearMonthDayPicker() {
        BasisTimesUtils.showDatePickerDialog(this, BasisTimesUtils.THEME_HOLO_DARK, "Please Pick your date", 2019, 1, 1, new BasisTimesUtils.OnDatePickerListener() {

            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                if (month < 10) {
                    if (dayOfMonth >= 10) {
                        datepicker.setText("0" + month + "/" + dayOfMonth + "/" + year);
                    } else {
                        datepicker.setText("0" + month + "/" + "0" + dayOfMonth + "/" + year);
                    }
                } else {
                    if (dayOfMonth >= 10) {
                        datepicker.setText(month + "/" + dayOfMonth + "/" + year);
                    } else {
                        datepicker.setText(month + "/" + "0" + dayOfMonth + "/" + year);
                    }
                }
            }

            @Override
            public void onCancel() {
            }
        });
    }

    //将数字放进table里
    public void setNumber() {
        maplist.clear();
        for (LinkedHashMap<String, String> a : answer) {
            maplist.add(a);
        }

        maplist2.clear();
        for (LinkedHashMap<String, String> b : answer2) {
            maplist2.add(b);
        }
        Log.e("Setnumber", maplist.size() + " " + maplist2.size());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("initial", answer);
        outState.putSerializable("initial2", summary);
        outState.putSerializable("initial3", radioSummary);
        outState.putSerializable("initial4", answer2);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void updateTable() {
        hour = "";
        String first = "";
        String second = "";
        String third = "";
        String fourth = "";
        for (String str : summary.get("ordertype").keySet()) {
            CheckBox cb = summary.get("ordertype").get(str);
            if (cb.isChecked()) {
                if (first.equals("")) {
                    first += cb.getText();
                } else {
                    first = first + "," + cb.getText();
                }
            }
        }


        for (String str : summary.get("lob").keySet()) {
            CheckBox cb = summary.get("lob").get(str);
            if (cb.isChecked()) {
                if (second.equals("")) {
                    second += cb.getText();
                } else {
                    second = second + "," + cb.getText();
                }
            }
        }

        for (int k = 0; k < radioSummary.get("hour").size(); k++) {
            RadioButton rb = radioSummary.get("hour").get(k);
            if (rb.isChecked()) {
                third = rb.getText() + "";
            }
        }
        third = (third.split(":"))[0];
        hour = third;
        fourth = datepicker.getText() + "";
        String[] sql = new String[2];
        sql[0] = "EXEC [P_DYNAMIC_BACKLOG_XMN_RESULTE] '" + first + "','" + second + "','" + fourth + "','" + third + "'";
        sql[1] = "EXEC [P_DYNAMIC_BACKLOG_TRACK] '" + first + "','" + fourth + "','" + third + "'";
        ld.setLoadingText("Loading...").show();
        test(sql);
        Log.e("Size", answer.size() + " " + answer2.size());


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder build = new AlertDialog.Builder(this);
                build.setTitle("Notice").setMessage("Do you want to Log out?");
                build.setPositiveButton("Exit",
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
