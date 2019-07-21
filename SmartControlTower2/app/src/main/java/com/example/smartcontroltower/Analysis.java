package com.example.smartcontroltower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.google.android.material.navigation.NavigationView;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


public class Analysis extends AppCompatActivity {

    private DrawerLayout drawerl;
    private ActionBarDrawerToggle toggle;
    public static SmartTable<Object> table;
    private HashMap<String, HashMap<String, CheckBox>> summary = new HashMap<>();//所有checkbox的集合
    private ArrayList<String> allCondition = new ArrayList<>();
    private ArrayList<String[]> allContent = new ArrayList<>();
    private ArrayList<LinkedHashMap<String, String>> answer;
    private LoadingDialog ld;
    private NiceSpinner niceSpinner;
    private Button verbtn;
    private Button yqbtn;
    private LinearLayout yqll;
    private LinearLayout verll;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            answer = (ArrayList<LinkedHashMap<String, String>>) savedInstanceState.getSerializable("initial");

        } else {
            answer = new ArrayList<>();
        }

        setContentView(R.layout.activity_e2_e);
        radioGroup = findViewById(R.id.e2e_range);
        verbtn = findViewById(R.id.e2e_ver_btn);
        yqbtn = findViewById(R.id.e2e_yq_btn);
        yqll = findViewById(R.id.e2e_yq);
        verll = findViewById(R.id.e2e_ver);
        table = findViewById(R.id.table);
        /////////////////Table//////////////////////////////////////////////////
        RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setNumber(selectRadioBtn());
                Log.d("Unit", checkedId + "");
            }
        };
        radioGroup.setOnCheckedChangeListener(listener);


        //设置初始值
        if (answer.size() != 0) {
            setNumber(selectRadioBtn());
        } else {
            ld = new LoadingDialog(this);
            ld.setLoadingText("Loading...").setSuccessText("Success").setFailedText("Failed")
                    .closeSuccessAnim().show();
            test("EXEC [SP_IDC_EOQ_SUMMARY1] '" + "EoQ" + "','" + "QuarView" + "','" + "FY20Q2" + "','" + "" + "','" + "overall" + "','" + "system" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "'");
        }

        //////////////////////////////////drop-down list//////////////////////////////////////
//        niceSpinner = findViewById(R.id.nice_spinner);
//        List<String> dataset = new LinkedList<>(Arrays.asList("QuarView", "VersionView"));
//        niceSpinner.attachDataSource(dataset);
//
//        niceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
//            @Override
//            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
//                String item = (String) parent.getItemAtPosition(position);
//
//                if (item.equals("VersionView")) {
//                    verbtn.setVisibility(View.VISIBLE);
//                    yqbtn.setVisibility(View.GONE);
//                    yqll.setVisibility(View.GONE);
//                    for(String checkb:summary.get("yq").keySet()){
//                        CheckBox cbb =summary.get("yq").get(checkb);
//                        cbb.setChecked(false);
//                    }
//
//                } else {
//                    verbtn.setVisibility(View.GONE);
//                    verll.setVisibility(View.GONE);
//                    yqbtn.setVisibility(View.VISIBLE);
//                    for(String checkb:summary.get("version").keySet()){
//                        CheckBox cbb =summary.get("version").get(checkb);
//                        cbb.setChecked(false);
//                    }
//                }
//                Toast.makeText(Analysis.this, item, Toast.LENGTH_SHORT).show();
//            }
//        });
////////////////////////////////////////////left side///////////////////////////////////////////
        Toolbar toolbar = findViewById(R.id.e2e_toolbar);
        setSupportActionBar(toolbar);

        drawerl = findViewById(R.id.e2e_drawer);
        ActionBar actionb = getSupportActionBar();
        NavigationView nv = findViewById(R.id.nav_view);
        if (actionb != null) {
            actionb.setDisplayHomeAsUpEnabled(true);
            actionb.setHomeAsUpIndicator(R.drawable.menu);
        }
        nv.setCheckedItem(R.id.nav_E2E);
        Resources resource = getBaseContext().getResources();
        ColorStateList csl = resource.getColorStateList(R.color.nav_status_color);
        nv.setItemTextColor(csl);

        //界面切换
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_E2E:
                        break;
                    case R.id.nav_MFG:
                        Intent intent2 = new Intent(Analysis.this, MFG.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_SNI:
                        Intent intent4 = new Intent(Analysis.this, SNI.class);
                        startActivity(intent4);
                        finish();
                        break;
                    case R.id.nav_analysis:
                        Intent intent3 = new Intent(Analysis.this, Analysis.class);
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

        //右侧抽屉
        toggle = new ActionBarDrawerToggle(this, drawerl,
                toolbar, R.string.draw_open, R.string.draw_close) {
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);//抽屉关闭后
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);//抽屉打开后
            }
        };
        //右侧上方按钮
        final Button search = findViewById(R.id.e2e_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRightSliding();
            }
        });

        ///////////////////////CheckBox列表/////////////////////////////////////////

        LinearLayout e2ever = findViewById(R.id.e2e_ver);
        String[] ver = getResources().getStringArray(R.array.Version);
        ConstructCheck("version", ver, e2ever, "");

        LinearLayout e2eyq = findViewById(R.id.e2e_yq);
        String[] yq = getResources().getStringArray(R.array.Year_quar);
        ConstructCheck("yq", yq, e2eyq, "FY20Q2");

        LinearLayout e2elob = findViewById(R.id.e2e_lob);
        String[] lob = getResources().getStringArray(R.array.Lob);
        ConstructCheck("lob", lob, e2elob, "OVERALL");

        LinearLayout e2esf = findViewById(R.id.e2e_sf);
        String[] sf = getResources().getStringArray(R.array.system_flag);
        ConstructCheck("is_system", sf, e2esf, "System");

        LinearLayout e2ept = findViewById(R.id.e2e_pt);
        String[] pt = getResources().getStringArray(R.array.Product_Type);
        ConstructCheck("product_type", pt, e2ept, "OVERALL");

        LinearLayout e2elg = findViewById(R.id.e2e_lg);
        String[] lg = getResources().getStringArray(R.array.LOB_Group);
        ConstructCheck("lob_group", lg, e2elg, "OVERALL");

        LinearLayout e2eregion = findViewById(R.id.e2e_region);
        String[] region = getResources().getStringArray(R.array.Region);
        ConstructCheck("region", region, e2eregion, "OVERALL");

        LinearLayout e2esegment = findViewById(R.id.e2e_segment);
        String[] segment = getResources().getStringArray(R.array.Segment);
        ConstructCheck("segment", segment, e2esegment, "OVERALL");

        LinearLayout e2esite = findViewById(R.id.e2e_site);
        String[] site = getResources().getStringArray(R.array.Site);
        ConstructCheck("site", site, e2esite, "OVERALL");


        ////////////////////////Checkbox列表结束////////////////////////////////////

        Button refresh = findViewById(R.id.e2e_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {//Refresh的动态监控
            @Override
            public void onClick(View view) {

                ld = new LoadingDialog(view.getContext());
                ld.setLoadingText("Loading...").setSuccessText("Success").setFailedText("Failed")
                        .closeSuccessAnim().show();
                answer.clear();
                ArrayList<String> searchSum = new ArrayList<>();//被check的checkbox集合
                RadioGroup radioGroup = findViewById(R.id.e2e_rg);
                RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                String ratioText = radioButton.getText().toString();
                searchSum.add(ratioText);
                String spinText = niceSpinner.getText().toString();
                searchSum.add(spinText);
                for (int i = 0; i < summary.size(); i++) {
                    String oneCondition = "";
                    HashMap<String, CheckBox> innerMap = summary.get(allCondition.get(i));
                    for (int j = 0; j < innerMap.size(); j++) {
                        CheckBox cbc = innerMap.get((allContent.get(i))[j]);
                        if (cbc.isChecked()) {
                            if (j == 0) {
                                oneCondition += cbc.getText();
                            } else {
                                oneCondition += "," + cbc.getText();
                            }
                        }
                    }
                    searchSum.add(oneCondition);
                }
                String sql = "EXEC [SP_IDC_EOQ_SUMMARY1] '" + searchSum.get(0) + "','" + searchSum.get(1) + "','" + searchSum.get(3) + "','" + searchSum.get(2) + "','" + searchSum.get(4) + "','" + searchSum.get(5) + "','" + searchSum.get(6) + "','" + searchSum.get(7) + "','" + searchSum.get(8) + "','" + searchSum.get(9) + "','" + searchSum.get(10) + "'";
                toggleRightSliding();
                test(sql);

            }
        });
    }
    //////////////////////////////////////////////////////////////////////////////////////////

    //手动控制抽屉开关
    private void toggleRightSliding() {//该方法控制右侧边栏的显示和隐藏
        if (drawerl.isDrawerOpen(GravityCompat.END)) {
            drawerl.closeDrawer(GravityCompat.END);//关闭抽屉
        } else {
            drawerl.openDrawer(GravityCompat.END);//打开抽屉
        }
    }

    public void showMutilAlertDialog(View view) {
        LinearLayout ll = null;
        switch (view.getId()) {

            case R.id.e2e_yq_btn:
                ll = findViewById(R.id.e2e_yq);
                break;
            case R.id.e2e_ver_btn:
                ll = findViewById(R.id.e2e_ver);
                break;
            case R.id.e2e_lob_btn:
                ll = findViewById(R.id.e2e_lob);
                break;
            case R.id.e2e_sf_btn:
                ll = findViewById(R.id.e2e_sf);
                break;
            case R.id.e2e_pt_btn:
                ll = findViewById(R.id.e2e_pt);
                break;
            case R.id.e2e_lg_btn:
                ll = findViewById(R.id.e2e_lg);
                break;
            case R.id.e2e_region_btn:
                ll = findViewById(R.id.e2e_region);
                break;
            case R.id.e2e_segment_btn:
                ll = findViewById(R.id.e2e_segment);
                break;
            case R.id.e2e_site_btn:
                ll = findViewById(R.id.e2e_site);
                break;
            default:

        }
        if (ll.getVisibility() == View.VISIBLE) {

            ll.setVisibility(View.GONE);
        } else {
            ll.setVisibility(View.VISIBLE);
        }
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

    private void test(final String sql) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //测试数据库的语句,在子线程操作
                answer = DBUtil.QuerySQL(sql);
                Log.d("shenmegui","ERROR");
                // answer = DBUtil.sendRequestWithOkHttp();
                setNumber(selectRadioBtn());
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
                    ld.loadSuccess();
                    break;

                default:
                    ld.loadFailed();
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
            for (int j = 0; j < initial.length; j++) {
                if (items[i].equals(ini)) {
                    cb.setChecked(true);
                }
            }
            ll.addView(cb);
        }
        summary.put(title, map);
        allCondition.add(title);
    }

    public void setNumber(String unit) {
        List<Object> maplist = new ArrayList<>();
        for (LinkedHashMap<String, String> a : answer) {
            LinkedHashMap<String,String> lhm=new LinkedHashMap<>();
            if (unit.equals("K")) {
                for (String b : a.keySet()) {
                    if (!b.equals("COL_TYPE")) {
                        double dou = Double.parseDouble(a.get(b));
                        dou = dou / 1000;
                        lhm.put(b,String.format("%.1f", dou));
                    }
                    else{
                        lhm.put(b,a.get(b));
                    }
                }
                maplist.add(lhm);
            }
            else{
                maplist.add(a);}
        }

        MapTableData tableData = MapTableData.create("EOQ", maplist);
        //Column groupColumn = new Column("组合", tableData.getColumns().get(0), tableData.getColumns().get(1));
        table.getConfig().setFixedTitle(true);
        tableData.getColumns().get(0).setFixed(true);
        table.setZoom(true, 2, 1);
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
        table.setTableData(tableData);

        table.getConfig().setTableTitleStyle(new FontStyle(50, getResources().getColor(R.color.table_gray)));
        table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(getResources().getColor(R.color.table_gray)));
        table.getConfig().setContentStyle(new FontStyle(40, getResources().getColor(R.color.table_gray)));
        table.getConfig().setColumnTitleStyle(new FontStyle(40, getResources().getColor(R.color.white)));
    }

    //在界面刷新之前保存旧数据
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("initial", answer);
    }

    private String selectRadioBtn() {

        RadioButton rb = Analysis.this.findViewById(radioGroup.getCheckedRadioButtonId());

        return rb.getText() + "";

    }


}
