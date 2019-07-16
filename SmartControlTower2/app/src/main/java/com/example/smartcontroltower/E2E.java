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
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

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
    private HashMap<String, HashMap<String, CheckBox>> summary;//所有checkbox的集合
    private ArrayList<String> allCondition = new ArrayList<>();
    private ArrayList<String[]> allContent = new ArrayList<>();
    private ArrayList<LinkedHashMap<String, String>> answer;
    private LoadingDialog ld;
    private NiceSpinner niceSpinner;
    private Button verbtn;
    private Button yqbtn;
    private LinearLayout yqll;
    private LinearLayout verll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            answer = (ArrayList<LinkedHashMap<String, String>>) savedInstanceState.getSerializable("initial");
            summary = (HashMap<String, HashMap<String, CheckBox>>) savedInstanceState.getSerializable("initial2");
        } else {
            answer = new ArrayList<>();
            summary = new HashMap<>();
        }

        setContentView(R.layout.activity_e2_e);

        verbtn = findViewById(R.id.e2e_ver_btn);
        yqbtn = findViewById(R.id.e2e_yq_btn);
        yqll = findViewById(R.id.e2e_yq);
        verll = findViewById(R.id.e2e_ver);
        /////////////////Table//////////////////////////////////////////////////

        table = findViewById(R.id.table);
        //表格数据 datas 是需要填充的数据
//        List<Object> maplist = new ArrayList<>();
        if (answer.size() != 0) {
            setNumber();
        }


        //////////////////////////////////drop-down list//////////////////////////////////////
        niceSpinner = findViewById(R.id.nice_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("QuarView", "VersionView"));
        niceSpinner.attachDataSource(dataset);


        niceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);

                if (item.equals("VersionView")) {
                    verbtn.setVisibility(View.VISIBLE);
                    yqbtn.setVisibility(View.GONE);
                    yqll.setVisibility(View.GONE);

                } else {
                    verbtn.setVisibility(View.GONE);
                    verll.setVisibility(View.GONE);
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
        final Button search = findViewById(R.id.e2e_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRightSliding();
            }
        });

        ///////////////////////CheckBox列表/////////////////////////////////////////

        //Version
        HashMap<String, CheckBox> vermap = new LinkedHashMap<>();
        LinearLayout e2ever = findViewById(R.id.e2e_ver);
        String[] ver = getResources().getStringArray(R.array.Version);
        allContent.add(ver);
        for (int i = 0; i < ver.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setTextColor(getResources().getColor(R.color.colorAccent));
            cb.setText(ver[i]);
            cb.setTextSize(18);
            vermap.put(ver[i], cb);
            e2ever.addView(cb);

        }
        summary.put("version", vermap);

        allCondition.add("version");

        //Year_Quar
        HashMap<String, CheckBox> yqmap = new LinkedHashMap<>();
        LinearLayout e2eyq = findViewById(R.id.e2e_yq);
        String[] yq = getResources().getStringArray(R.array.Year_quar);
        allContent.add(yq);
        for (int i = 0; i < yq.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setTextColor(getResources().getColor(R.color.colorAccent));
            cb.setText(yq[i]);
            cb.setTextSize(18);
            e2eyq.addView(cb);
            yqmap.put(yq[i], cb);

        }

        summary.put("yq", yqmap);
        allCondition.add("yq");

        //Lob
        HashMap<String, CheckBox> lobmap = new LinkedHashMap<>();
        LinearLayout e2elob = findViewById(R.id.e2e_lob);
        String[] lob = getResources().getStringArray(R.array.Lob);
        allContent.add(lob);
        for (int i = 0; i < lob.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setTextColor(getResources().getColor(R.color.colorAccent));
            cb.setText(lob[i]);
            cb.setTextSize(18);
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
            cb.setTextColor(getResources().getColor(R.color.colorAccent));
            cb.setText(sf[i]);
            cb.setTextSize(18);
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
            cb.setTextColor(getResources().getColor(R.color.colorAccent));
            cb.setText(pt[i]);
            cb.setTextSize(18);
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
            cb.setTextColor(getResources().getColor(R.color.colorAccent));
            cb.setText(lg[i]);
            cb.setTextSize(18);
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
            cb.setTextColor(getResources().getColor(R.color.colorAccent));
            cb.setText(region[i]);
            cb.setTextSize(18);
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
            cb.setTextColor(getResources().getColor(R.color.colorAccent));
            cb.setText(segment[i]);
            cb.setTextSize(18);
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
            cb.setTextColor(getResources().getColor(R.color.colorAccent));
            cb.setText(site[i]);
            cb.setTextSize(18);
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

                ld = new LoadingDialog(view.getContext());
                ld.setLoadingText("Loading...").setSuccessText("Success").show();

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
                    ld.loadSuccess();
                    //ld.close();
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
        outState.putSerializable("initial2", summary);
    }

}
