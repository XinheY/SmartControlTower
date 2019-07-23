package com.example.smartcontroltower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.example.smartcontroltower.Fragment_ana.FragmentClient;
import com.example.smartcontroltower.Fragment_ana.FragmentClientLob;
import com.example.smartcontroltower.Fragment_ana.FragmentISG;
import com.example.smartcontroltower.Fragment_ana.FragmentISGLOB;
import com.example.smartcontroltower.Fragment_ana.FragmentSystem;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class Analysis extends AppCompatActivity {

    private DrawerLayout drawerl;
    private int radioid = 0;//给每个radiobutton设立唯一的id
    private ActionBarDrawerToggle toggle;
    public static SmartTable<Object> table;
    private HashMap<String, HashMap<String, CheckBox>> summary = new LinkedHashMap<>();//所有checkbox的集合
    private HashMap<String, ArrayList<RadioButton>> radioSummary = new LinkedHashMap<>();
    private HashMap<String, HashMap<String, CheckBox>> summaryOld = new LinkedHashMap<>();//之前所有checkbox的集合
    private HashMap<String, ArrayList<RadioButton>> radioOld = new LinkedHashMap<>();
    private ArrayList<String> allCondition = new ArrayList<>();
    private ArrayList<String[]> allContent = new ArrayList<>();
    private ArrayList<LinkedHashMap<String, String>> answer;
    private LoadingDialog ld;
    private RadioGroup radioGroup;
    private TabLayout tl;
    private NoSrcoll vp;
    private RangeSeekBar rsb;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
////////////////////////////////继承之前的数据//////////////////////////////////////////
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            answer = (ArrayList<LinkedHashMap<String, String>>) savedInstanceState.getSerializable("initial");
            summaryOld = (HashMap<String, HashMap<String, CheckBox>>) savedInstanceState.getSerializable("initial2");
            radioOld = (LinkedHashMap<String, ArrayList<RadioButton>>) savedInstanceState.getSerializable("initial3");
        } else {
            answer = new ArrayList<>();
        }
        setContentView(R.layout.activity_analysis);
        radioGroup = findViewById(R.id.ana_range);
        ///////////////////////////////////添加fragment/////////////////////////////////////

        tl = findViewById(R.id.ana_tablayout);
        vp = findViewById(R.id.ana_viewpager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentSystem(), "System");
        adapter.addFragment(new FragmentClient(), "Client");
        adapter.addFragment(new FragmentClientLob(), "Client(LOB)");
        adapter.addFragment(new FragmentISG(), "ISG");
        adapter.addFragment(new FragmentISGLOB(), "ISG(LOB)");

        vp.setAdapter(adapter);
        tl.setupWithViewPager(vp);



        /////////////////Table//////////////////////////////////////////////////
        //设置初始值
        if (answer.size() != 0) {
            // setNumber(selectRadioBtn(radioGroup));
        } else {
//            ld = new LoadingDialog(this);
//            ld.setLoadingText("Loading...").setSuccessText("Success").setFailedText("Failed")
//                    .closeSuccessAnim().show();
//            test("EXEC [SP_IDC_EOQ_SUMMARY1] '" + "EoQ" + "','" + "QuarView" + "','" + "FY20Q2" + "','" + "" + "','" + "overall" + "','" + "system" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "'");
        }

        //////////////////////////////////RadioGroup//////////////////////////////////////
        //K && Unit
//        RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                setNumber(selectRadioBtn(radioGroup));
//            }
//        };
//        radioGroup.setOnCheckedChangeListener(listener);

///////////////////////////////////////***left side///////////////////////////////////////////
        Toolbar toolbar = findViewById(R.id.ana_toolbar);
        setSupportActionBar(toolbar);

        drawerl = findViewById(R.id.ana_drawer);
        ActionBar actionb = getSupportActionBar();
        NavigationView nv = findViewById(R.id.ana_nav_view);
        if (actionb != null) {
            actionb.setDisplayHomeAsUpEnabled(true);
            actionb.setHomeAsUpIndicator(R.drawable.menu);
        }
        nv.setCheckedItem(R.id.nav_analysis);
        Resources resource = getBaseContext().getResources();
        ColorStateList csl = resource.getColorStateList(R.color.nav_status_color);
        nv.setItemTextColor(csl);

        //界面切换
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_E2E:
                        Intent intent3 = new Intent(Analysis.this, E2E.class);
                        startActivity(intent3);
                        finish();
                        break;
                    case R.id.nav_Dynamic:
                        Intent intent2 = new Intent(Analysis.this, Dynamic.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_DirectBL:
                        Intent intent4 = new Intent(Analysis.this, DirectBL.class);
                        startActivity(intent4);
                        finish();
                        break;
                    case R.id.nav_analysis:
                        break;
                    default:
                }
                //Change color base on nav status

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
        final Button search = findViewById(R.id.ana_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRightSliding();
            }
        });

/////////////////////////////CheckBox列表/////////////////////////////////////////
        RadioGroup anaSources = findViewById(R.id.ana_rg_source);
        String[] sources = getResources().getStringArray(R.array.ana_sources);
        ConstructRadio("sources", sources, anaSources, "Sales");

        RadioGroup anasv = findViewById(R.id.ana_rg_sv);
        String[] submitVersion = getResources().getStringArray(R.array.ana_sv);
        ConstructRadio("sv", submitVersion, anasv, "FY20Q2WK10");

        RadioGroup anacv = findViewById(R.id.ana_rg_cv);
        String[] compareVersion = getResources().getStringArray(R.array.ana_cv);
        ConstructRadio("cv", compareVersion, anacv, "FY20Q2WK9");

        LinearLayout anagroupby = findViewById(R.id.ana_gb);
        String[] groupby = getResources().getStringArray(R.array.ana_groupby);
        ConstructCheck("groupby", groupby, anagroupby, "Country");

        LinearLayout anaviewtype = findViewById(R.id.ana_vt);
        String[] viewtype = getResources().getStringArray(R.array.ana_viewtype);
        ConstructCheck("viewtype", viewtype, anaviewtype, "MFG-Phasing,MFG-Unshippable,SNI-Backlog,SNI-Phasing,SNI-Unshippable,Invoice");


        anaSources.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkView = (RadioButton) radioGroup.getChildAt(i);
                Button vt = findViewById(R.id.ana_vt_btn);
                LinearLayout vtll = findViewById(R.id.ana_vt);
                if (!(checkView.getText() + "").equals("Sales")) {
                    vt.setVisibility(View.GONE);
                    vtll.setVisibility(View.GONE);
                } else {
                    vt.setVisibility(View.VISIBLE);
                }
            }
        });

        rsb = findViewById(R.id.rangeseek);
        rsb.setProgress(1f, 10f);
        rsb.setRange(1f,13f,0f);
        rsb.setProgressHeight(4);
        rsb.setIndicatorTextDecimalFormat("0");
        rsb.setSteps(12);
        rsb.setStepsWidth(10f);
        rsb.setStepsHeight(25f);
        rsb.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
            }
            @Override
            public void onStartTrackingTouch(RangeSeekBar view,  boolean isLeft) {
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view,  boolean isLeft) {
                //stop tracking touch
                Log.e("stop",rsb.getRightSeekBar().getProgress()+"");
            }
        });
///////////////////////////////Checkbox列表结束////////////////////////////////////

        Button refresh = findViewById(R.id.ana_refresh);
       refresh.setOnClickListener(new View.OnClickListener() {//Refresh的动态监控
            @Override
           public void onClick(View view) {

//                Bundle bundle = new Bundle();
//                bundle.putString("data","传递到的数据");
//                Fragment fragment=new FragmentClient();
//                fragment.setArguments(bundle);//数据传递到fragment中
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.client_layout,fragment);
//                fragmentTransaction.commit();
//                adapter.notifyDataSetChanged();
//
//                ld = new LoadingDialog(view.getContext());
//                ld.setLoadingText("Loading...").setSuccessText("Success").setFailedText("Failed")
//                        .closeSuccessAnim().show();
//                answer.clear();
//                ArrayList<String> searchSum = new ArrayList<>();//被check的checkbox集合
//                RadioGroup radio = findViewById(R.id.ana_rg);
//                RadioButton radioButton = findViewById(radio.getCheckedRadioButtonId());
//                String ratioText = radioButton.getText().toString();
//                searchSum.add(ratioText);
//                RadioButton radioButton2 = findViewById(viewType.getCheckedRadioButtonId());
//                String ratioText2 = radioButton2.getText().toString();
//                searchSum.add(ratioText2);
//                boolean canrun = true;
//                for (int i = 0; i < summary.size(); i++) {
//                    String oneCondition = "";
//                    HashMap<String, CheckBox> innerMap = summary.get(allCondition.get(i));
//                    int count = 0;
//                    for (int j = 0; j < innerMap.size(); j++) {
//                        CheckBox cbc = innerMap.get((allContent.get(i))[j]);
//                        if (cbc.isChecked()) {
//                            count++;
//                            if (oneCondition.equals("")) {
//                                oneCondition += cbc.getText();
//                            } else {
//                                oneCondition += "," + cbc.getText();
//                            }
//                        }
//                    }
//                    if (count == 0 && i > 1) {
//                        canrun = false;
//                        Toast.makeText(Analysis.this, "Blank " + allCondition.get(i), Toast.LENGTH_LONG).show();
//                        ld.closeFailedAnim().loadFailed();
//                        break;
//                    }
//                    searchSum.add(oneCondition);
//                }
//                if (canrun) {
//                    if (searchSum.get(2) != "" || searchSum.get(3) != "") {
//                        String sql = "EXEC [SP_IDC_EOQ_SUMMARY1] '" + searchSum.get(0) + "','" + searchSum.get(1) + "','" + searchSum.get(3) + "','" + searchSum.get(2) + "','" + searchSum.get(4) + "','" + searchSum.get(5) + "','" + searchSum.get(6) + "','" + searchSum.get(7) + "','" + searchSum.get(8) + "','" + searchSum.get(9) + "','" + searchSum.get(10) + "'";
//                        toggleRightSliding();
//                        test(sql);
//                    }
//                    else{
//                        Toast.makeText(Analysis.this, "Blank Error", Toast.LENGTH_LONG).show();
//                        ld.closeFailedAnim().loadFailed();
//                    }
//                }
//
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
            case R.id.ana_sv_btn:
                ll = findViewById(R.id.ana_sv);
                break;
            case R.id.ana_cv_btn:
                ll = findViewById(R.id.ana_cv);
                break;
            case R.id.ana_gb_btn:
                ll = findViewById(R.id.ana_gb);
                break;
            case R.id.ana_vt_btn:
                ll = findViewById(R.id.ana_vt);
                break;
            case R.id.ana_source_btn:
                ll = findViewById(R.id.ana_source);
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
                setNumber(selectRadioBtn(radioGroup));
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

    /////////////////////////////////////////////////////////////////////////////////////////
    //将checklist放进界面中
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
            if (title.equals("viewtype")) {
                if (items[i].equals("MFG-Backlog") || items[i].equals("SNI-Backlog")) {
                    cb.setEnabled(false);
                } else if (!items[i].equals("Invoice")) {
                    ViewGroup.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                    ((LinearLayout.LayoutParams) layoutParam).setMargins(40, 0, 0, 0);
                    cb.setLayoutParams(layoutParam);
                }
            }
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
            rb.setId(radioid);
            radioid += 1;
            rb.setTextColor(getResources().getColor(R.color.colorAccent));
            radioButtons.add(rb);
            rg.addView(rb);
            if (title.equals("sources")) {
                if (items[i].equals("Sales Impact") || items[i].equals("Parameters Impact") || items[i].equals("E2E Summary")) {
                    rb.setEnabled(false);
                } else {
                    ViewGroup.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                    ((LinearLayout.LayoutParams) layoutParam).setMargins(40, 0, 0, 0);
                    rb.setLayoutParams(layoutParam);
                }
            }
            if (radioOld.size() != 0) {
                if (radioOld.get(title).get(i).isChecked()) {
                    rb.setChecked(true);
                    Button vt = findViewById(R.id.ana_vt_btn);
                    LinearLayout vtll = findViewById(R.id.ana_vt);
                   if (title.equals("sources")) {
                        if (!(radioOld.get(title).get(i).getText() + "").equals("Sales")) {
                            vt.setVisibility(View.GONE);
                            vtll.setVisibility(View.GONE);
                        } else {
                            vt.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else {
                if (items[i].equals(ini)) {
                    Log.d("initial", items[i]);
                    rb.setChecked(true);
                }
            }
        }
        radioSummary.put(title, radioButtons);
        allCondition.add(title);
    }

    //将数字放进table里
    public void setNumber(String unit) {
        List<Object> maplist = new ArrayList<>();
        for (LinkedHashMap<String, String> a : answer) {
            LinkedHashMap<String, String> lhm = new LinkedHashMap<>();
            if (unit.equals("K")) {
                for (String b : a.keySet()) {
                    if (!b.equals("COL_TYPE")) {
                        double dou = Double.parseDouble(a.get(b));
                        dou = dou / 1000;
                        lhm.put(b, String.format("%.1f", dou));
                    } else {
                        lhm.put(b, a.get(b));
                    }
                }
                maplist.add(lhm);
            } else {
                maplist.add(a);
            }
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
        outState.putSerializable("initial3", radioSummary);
    }

    private String selectRadioBtn(RadioGroup rg) {
        RadioButton rb = Analysis.this.findViewById(rg.getCheckedRadioButtonId());
        return rb.getText() + "";

    }


}
