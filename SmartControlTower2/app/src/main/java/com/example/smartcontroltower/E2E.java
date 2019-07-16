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
import android.graphics.Color;
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
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


public class E2E extends AppCompatActivity {

    private DrawerLayout drawerl;
    private ActionBarDrawerToggle toggle;
    public static SmartTable<Object> table;
    private HashMap<String, HashMap<String, CheckBox>> summary = new HashMap<>();//所有checkbox的集合
    private ArrayList<String> allCondition = new ArrayList<>();
    private ArrayList<String[]> allContent = new ArrayList<>();
    private ArrayList<LinkedHashMap<String, String>> answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            Gson g = new Gson();
            answer = (ArrayList<LinkedHashMap<String, String>>) savedInstanceState.getSerializable("initial");
            Log.d("initial", answer.size() + "");
        } else {
            answer = new ArrayList<>();
        }

        setContentView(R.layout.activity_e2_e);

        /////////////////Table//////////////////////////////////////////////////

        table = findViewById(R.id.table);
        //表格数据 datas 是需要填充的数据
//        List<Object> maplist = new ArrayList<>();
        if (answer.size() != 0) {
            setNumber();
        }


        //////////////////////////////////drop-down list//////////////////////////////////////
        NiceSpinner niceSpinner = findViewById(R.id.nice_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("QuarView", "VersionView"));
        niceSpinner.attachDataSource(dataset);

        niceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                Button verbtn = findViewById(R.id.e2e_ver_btn);
                Button yqbtn = findViewById(R.id.e2e_yq_btn);
                if (item.equals("VersionView")) {
                    verbtn.setVisibility(View.VISIBLE);
                    yqbtn.setVisibility(View.GONE);
                } else {
                    verbtn.setVisibility(View.GONE);
                    yqbtn.setVisibility(View.VISIBLE);
                }
                Toast.makeText(E2E.this, item, Toast.LENGTH_SHORT).show();
            }
        });
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
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.nav_status_color);
        nv.setItemTextColor(csl);

        //界面切换
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_E2E:
                        break;
                    case R.id.nav_MFG:
                        Intent intent2 = new Intent(E2E.this, MFG.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_SNI:
                        Intent intent4 = new Intent(E2E.this, SNI.class);
                        startActivity(intent4);
                        finish();
                        break;
                    case R.id.nav_analysis:
                        Intent intent3 = new Intent(E2E.this, Analysis.class);
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
        Button search = findViewById(R.id.e2e_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRightSliding();
            }
        });

        ///////////////////////CheckBox列表/////////////////////////////////////////
        //Lob
        HashMap<String, CheckBox> lobmap = new LinkedHashMap<>();
        LinearLayout e2elob = findViewById(R.id.e2e_lob);
        String[] lob = getResources().getStringArray(R.array.Lob);
        allContent.add(lob);
        for (int i = 0; i < lob.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(lob[i]);
            cb.setTextSize(15);
            e2elob.addView(cb);
            lobmap.put(lob[i], cb);

        }
        summary.put("lob", lobmap);
        allCondition.add("lob");

        //System Flag
        HashMap<String, CheckBox> sfmap = new LinkedHashMap<>();
        LinearLayout e2esf = findViewById(R.id.e2e_sf);
        String[] sf = getResources().getStringArray(R.array.system_flag);
        allContent.add(sf);
        for (int i = 0; i < sf.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(sf[i]);
            cb.setTextSize(15);
            e2esf.addView(cb);
            sfmap.put(sf[i], cb);
        }
        summary.put("is_system", sfmap);
        allCondition.add("is_system");

        //Product Type
        HashMap<String, CheckBox> ptmap = new LinkedHashMap<>();
        LinearLayout e2ept = findViewById(R.id.e2e_pt);
        String[] pt = getResources().getStringArray(R.array.Product_Type);
        allContent.add(pt);
        for (int i = 0; i < pt.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(pt[i]);
            cb.setTextSize(15);
            e2ept.addView(cb);
            ptmap.put(pt[i], cb);
        }
        summary.put("product_type", ptmap);
        allCondition.add("product_type");

        //Lob Group
        HashMap<String, CheckBox> lgmap = new LinkedHashMap<>();
        LinearLayout e2elg = findViewById(R.id.e2e_lg);
        String[] lg = getResources().getStringArray(R.array.LOB_Group);
        allContent.add(lg);
        for (int i = 0; i < lg.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(lg[i]);
            cb.setTextSize(15);
            e2elg.addView(cb);
            lgmap.put(lg[i], cb);
        }
        summary.put("lob_group", lgmap);
        allCondition.add("lob_group");

        //Region
        HashMap<String, CheckBox> regionmap = new LinkedHashMap<>();
        LinearLayout e2eregion = findViewById(R.id.e2e_region);
        String[] region = getResources().getStringArray(R.array.Region);
        allContent.add(region);
        for (int i = 0; i < region.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(region[i]);
            cb.setTextSize(15);
            e2eregion.addView(cb);
            regionmap.put(region[i], cb);
        }
        summary.put("region", regionmap);
        allCondition.add("region");

        //Segment
        HashMap<String, CheckBox> segmentmap = new LinkedHashMap<>();
        LinearLayout e2esegment = findViewById(R.id.e2e_segment);
        String[] segment = getResources().getStringArray(R.array.Segment);
        allContent.add(segment);
        for (int i = 0; i < segment.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(segment[i]);
            cb.setTextSize(15);
            e2esegment.addView(cb);
            segmentmap.put(segment[i], cb);
        }
        summary.put("segment", segmentmap);
        allCondition.add("segment");

        //Site
        HashMap<String, CheckBox> sitemap = new LinkedHashMap<>();
        LinearLayout e2esite = findViewById(R.id.e2e_site);
        String[] site = getResources().getStringArray(R.array.Site);
        allContent.add(site);
        for (int i = 0; i < site.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(site[i]);
            cb.setTextSize(15);
            e2esite.addView(cb);
            sitemap.put(site[i], cb);
        }
        summary.put("site", sitemap);
        allCondition.add("site");
        ////////////////////////Checkbox列表结束////////////////////////////////////

        Button refresh = findViewById(R.id.e2e_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {//Refresh的动态监控
            @Override
            public void onClick(View view) {

                ArrayList<String> searchSum = new ArrayList<>();//被check的checkbox集合
                RadioGroup radioGroup = findViewById(R.id.e2e_rg);
                RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                String ratioText = "model=" + radioButton.getText().toString();
                for (int i = 0; i < summary.size(); i++) {
                    String oneCondition = allCondition.get(i) + "=";
                    HashMap<String, CheckBox> innerMap = summary.get(allCondition.get(i));
                    for (int j = 0; j < innerMap.size(); j++) {
                        CheckBox cbc = innerMap.get((allContent.get(i))[j]);
                        if (cbc.isChecked()) {
                            oneCondition += cbc.getText();
                        }
                    }
                    searchSum.add(oneCondition);
                }

                toggleRightSliding();
                test();

//                MapTableData tableData = MapTableData.create("E2E OLK Version", maplist2);
//                //Column groupColumn = new Column("组合", tableData.getColumns().get(2), tableData.getColumns().get(3));
//                tableData.getColumns().get(0).setFixed(true);//first column
//                table.setTableData(tableData);
////        table.getConfig().setContentStyle(new FontStyle(50, Color.BLUE));
//                table.getConfig().setColumnTitleStyle(new FontStyle(35, Color.DKGRAY));
//                table.getConfig().setTableTitleStyle(new FontStyle(43, Color.rgb(0, 0, 0)));
//                toggleRightSliding();
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

    private void test() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //测试数据库的语句,在子线程操作
                answer= DBUtil.QuerySQL();
               // answer = DBUtil.sendRequestWithOkHttp();
                setNumber();
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
                    break;

                default:
                    break;
            }
        }

        ;
    };

    public void setNumber() {
        List<Object> maplist = new ArrayList<>();
        for (LinkedHashMap<String, String> a : answer) {
            maplist.add(a);
        }

        MapTableData tableData = MapTableData.create("表格名", maplist);
        //Column groupColumn = new Column("组合", tableData.getColumns().get(0), tableData.getColumns().get(1));
        table.getConfig().setFixedTitle(true);
        tableData.getColumns().get(0).setFixed(true);
        table.setZoom(true, 2, 1);
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
        table.setTableData(tableData);
        table.getConfig().setContentStyle(new FontStyle(40, Color.BLUE));
        table.getConfig().setColumnTitleStyle(new FontStyle(40, Color.BLUE));
    }

    //在界面刷新之前保存旧数据
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("initial", answer);
    }

}
