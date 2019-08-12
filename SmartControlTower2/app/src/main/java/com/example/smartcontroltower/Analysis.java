package com.example.smartcontroltower;

import androidx.annotation.NonNull;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import java.util.concurrent.CountDownLatch;

public class Analysis extends AppCompatActivity {

    private DrawerLayout drawerl;
    private int radioid = 0;//给每个radiobutton设立唯一的id
    private ActionBarDrawerToggle toggle;
    public static MySmartTable<Object> table;
    private LinkedHashMap<String, List<Object>> maplistSum = new LinkedHashMap<>();
    private HashMap<String, HashMap<String, Boolean>> summary = new LinkedHashMap<>();//所有checkbox的集合
    private HashMap<String, ArrayList<RadioButton>> radioSummary = new LinkedHashMap<>();
    private HashMap<String, HashMap<String, Boolean>> summaryOld = new LinkedHashMap<>();//之前所有checkbox的集合
    private HashMap<String, ArrayList<RadioButton>> radioOld = new LinkedHashMap<>();
    private ArrayList<String> allCondition = new ArrayList<>();
    private ArrayList<String[]> allContent = new ArrayList<>();
    private ArrayList<LinkedHashMap<String, String>> answerAna;
    private LoadingDialog ld;
    private RadioGroup radioGroup, idcEoqGroup,anasv,anasv2,anacv,anacv2,anaSources;
    private RadioGroup IdcEoq;
    private TabLayout tl;
    private NoSrcoll vp;
    private RangeSeekBar rsb;
    private ViewPagerAdapter adapter;
    private int finishAna = 0;
    private static CountDownLatch cdl = null;
    private InitializeInfo info, info2;
    private int left = 9;
    private int right = 13;
    private static int gbChoseCount = 1;
    private static int tempgbChoseCount=1;
    private String pre,comp;
    private int[] AccessRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
////////////////////////////////继承之前的数据//////////////////////////////////////////
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            answerAna = (ArrayList<LinkedHashMap<String, String>>) savedInstanceState.getSerializable("initial");
            summaryOld = (HashMap<String, HashMap<String, Boolean>>) savedInstanceState.getSerializable("initial2");
            radioOld = (LinkedHashMap<String, ArrayList<RadioButton>>) savedInstanceState.getSerializable("initial3");
            left = savedInstanceState.getInt("left");
            right = savedInstanceState.getInt("right");
            pre=savedInstanceState.getString("pre");
            comp=savedInstanceState.getString("comp");
        } else {
            answerAna = new ArrayList<>();
            gbChoseCount=1;
        }
        info = (InitializeInfo) getIntent().getSerializableExtra("InitializeInfo");
        info2 = (InitializeInfo) getIntent().getSerializableExtra("InitializeInfo2");
        AccessRight= (int[]) getIntent().getSerializableExtra("AccessRight");

        pre=info.getVersion_addclosing().get(0);
        comp=info.getVersion_addclosing().get(1);

        setContentView(R.layout.activity_analysis);
        radioGroup = findViewById(R.id.ana_range);

        ld = new LoadingDialog(this);
        ld.setLoadingText("Loading...").setSuccessText("Success").setFailedText("Failed")
                .closeSuccessAnim();
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

        info = (InitializeInfo) getIntent().getSerializableExtra("InitializeInfo");
        /////////////////Table//////////////////////////////////////////////////
        //设置初始值
        if (answerAna.size() != 0 || savedInstanceState != null) {
            setNumber(selectRadioBtn(radioGroup));
        } else {
            ld.show();
            String sql = "EXEC SP_IDC_EOQ_SNI_CHANGE_ANALYSIS '" + "EoQ" + "','" + "FY20Q2WK12" + "','" + "FY20Q2WK11" + "','" + "Country" + "','" + "Invoice" + "','" + "Sales" + "'";
            test(sql);
        }

        //////////////////////////////////RadioGroup//////////////////////////////////////
        //K && Unit
        RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ((FragmentSystem) adapter.getItem(0)).collapse(adapter.getItem(0));
                ((FragmentClient) adapter.getItem(1)).collapse(adapter.getItem(1));
                ((FragmentClientLob) adapter.getItem(2)).collapse(adapter.getItem(2));
                ((FragmentISG) adapter.getItem(3)).collapse(adapter.getItem(3));
                ((FragmentISGLOB) adapter.getItem(4)).collapse(adapter.getItem(4));
                setNumber(selectRadioBtn(radioGroup));
            }
        };
        radioGroup.setOnCheckedChangeListener(listener);

        idcEoqGroup = findViewById(R.id.ana_rg);
        idcEoqGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(idcEoqGroup.getCheckedRadioButtonId());
                Button b1 = findViewById(R.id.ana_cv_btn);
                LinearLayout l1 = findViewById(R.id.ana_cv);
                Button b2 = findViewById(R.id.ana_cv_btn2);
                LinearLayout l2 = findViewById(R.id.ana_cv2);
                Button b3 = findViewById(R.id.ana_sv_btn);
                LinearLayout l3 = findViewById(R.id.ana_sv);
                Button b4 = findViewById(R.id.ana_sv_btn2);
                LinearLayout l4 = findViewById(R.id.ana_sv2);
                if (rb.getText().equals("EoQ")) {
                    b1.setVisibility(View.VISIBLE);
                    b2.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                    b3.setVisibility(View.VISIBLE);
                    b4.setVisibility(View.GONE);
                    l4.setVisibility(View.GONE);
                } else {
                    b2.setVisibility(View.VISIBLE);
                    b1.setVisibility(View.GONE);
                    l1.setVisibility(View.GONE);
                    b4.setVisibility(View.VISIBLE);
                    b3.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);
                }

            }
        });

///////////////////////////////////////***left side///////////////////////////////////////////
        Toolbar toolbar = findViewById(R.id.ana_toolbar);
        setSupportActionBar(toolbar);

        drawerl = findViewById(R.id.ana_drawer);
        ActionBar actionb = getSupportActionBar();
        NavigationView nv = findViewById(R.id.ana_nav_view);
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
                        Bundle bundle3 = new Bundle();
                        bundle3.putSerializable("InitializeInfo", info);
                        bundle3.putSerializable("InitializeInfo2", info2);
                        bundle3.putSerializable("AccessRight",AccessRight);
                        intent3.putExtras(bundle3);
                        startActivity(intent3);
                        finish();
                        break;
                    case R.id.nav_Dynamic:
                        Intent intent2 = new Intent(Analysis.this, Dynamic.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("InitializeInfo", info);
                        bundle2.putSerializable("InitializeInfo2", info2);
                        bundle2.putSerializable("AccessRight",AccessRight);
                        intent2.putExtras(bundle2);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_DirectBL:
                        Intent intent4 = new Intent(Analysis.this, DirectBL.class);
                        Bundle bundle4 = new Bundle();
                        bundle4.putSerializable("InitializeInfo", info);
                        bundle4.putSerializable("InitializeInfo2", info2);
                        bundle4.putSerializable("AccessRight",AccessRight);
                        intent4.putExtras(bundle4);
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
        RadioButton IdcEoq = findViewById(idcEoqGroup.getCheckedRadioButtonId());
        String IdcEoqChose = (String) IdcEoq.getText();

        anaSources = findViewById(R.id.ana_rg_source);
        String[] sources = getResources().getStringArray(R.array.ana_sources);
        ConstructRadio("sources", sources, anaSources, "Sales");

        anasv = findViewById(R.id.ana_rg_sv);
        String[] submitVersion;
        submitVersion = ((String[]) info.getVersion_addclosing().toArray(new String[info.getVersion().size()]));
        ConstructRadio("sv", submitVersion, anasv, submitVersion[0]);

        anasv2 = findViewById(R.id.ana_rg_sv2);
        String[] submitVersion2;
        submitVersion2 = ((String[]) info2.getVersion_addclosing().toArray(new String[info2.getVersion().size()]));
        ConstructRadio("sv2", submitVersion2, anasv2, submitVersion2[0]);

        anacv = findViewById(R.id.ana_rg_cv);
        String[] compareVersion;
        compareVersion = ((String[]) info.getVersion_addclosing().toArray(new String[info.getVersion().size()]));
        ConstructRadio("cv", compareVersion, anacv, compareVersion[1]);

        anacv2 = findViewById(R.id.ana_rg_cv2);
        String[] compareVersion2;
        compareVersion2 = ((String[]) info2.getVersion_addclosing().toArray(new String[info2.getVersion().size()]));
        ConstructRadio("cv2", compareVersion2, anacv2, compareVersion2[1]);

        LinearLayout anagroupby = findViewById(R.id.ana_gb);
        String[] groupby = getResources().getStringArray(R.array.ana_groupby);
        ConstructCheck("groupby", groupby, anagroupby, "Country");

        LinearLayout anaviewtype = findViewById(R.id.ana_vt);
        String[] viewtype = getResources().getStringArray(R.array.ana_viewtype);
        ConstructCheck("viewtype", viewtype, anaviewtype, "MFG-Phasing,MFG-Unshippable,SNI-Phasing,SNI-Unshippable,Invoice");


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

        //范围选择器初始化和监听器
        rsb = findViewById(R.id.rangeseek);
        rsb.setProgress(62f, 92f);
        rsb.setRange(1f, 14f, 0f);
        rsb.setProgressHeight(4);
        rsb.setIndicatorTextDecimalFormat("0");
        rsb.setSteps(13);
        rsb.setStepsWidth(10f);
        rsb.setStepsHeight(25f);
        rsb.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                //stop tracking touch
                left = Math.round(rsb.getLeftSeekBar().getProgress());
                right = Math.round(rsb.getRightSeekBar().getProgress());
                ((FragmentSystem) adapter.getItem(0)).collapse(adapter.getItem(0));
                ((FragmentClient) adapter.getItem(1)).collapse(adapter.getItem(1));
                ((FragmentClientLob) adapter.getItem(2)).collapse(adapter.getItem(2));
                ((FragmentISG) adapter.getItem(3)).collapse(adapter.getItem(3));
                ((FragmentISGLOB) adapter.getItem(4)).collapse(adapter.getItem(4));
                setNumber(selectRadioBtn(radioGroup));
            }
        });
///////////////////////////////Checkbox列表结束////////////////////////////////////
        //刷新表格（按钮监听器）
        final RadioButton Sourb = findViewById(anaSources.getCheckedRadioButtonId());
        Button refresh = findViewById(R.id.ana_refresh);

        refresh.setOnClickListener(new View.OnClickListener() {//Refresh的动态监控
            @Override
            public void onClick(View view) {
                RadioButton IErb = findViewById(idcEoqGroup.getCheckedRadioButtonId());
                RadioButton SVrb = findViewById(anasv.getCheckedRadioButtonId());
                RadioButton CVrb = findViewById(anacv.getCheckedRadioButtonId());
                RadioButton SVrb2 = findViewById(anasv2.getCheckedRadioButtonId());
                RadioButton CVrb2 = findViewById(anacv2.getCheckedRadioButtonId());
                ld = new LoadingDialog(view.getContext());
                ld.setLoadingText("Loading...").setSuccessText("Success").setFailedText("Failed")
                        .closeSuccessAnim().show();
                toggleRightSliding();

                ((FragmentSystem) adapter.getItem(0)).collapse(adapter.getItem(0));
                ((FragmentClient) adapter.getItem(1)).collapse(adapter.getItem(1));
                ((FragmentClientLob) adapter.getItem(2)).collapse(adapter.getItem(2));
                ((FragmentISG) adapter.getItem(3)).collapse(adapter.getItem(3));
                ((FragmentISGLOB) adapter.getItem(4)).collapse(adapter.getItem(4));

                ArrayList<String> searchSum = new ArrayList<>();
                boolean canrun = true;
                gbChoseCount = 0;
                summary.clear();
                searchSum=getSelectedCheckbox();
                Log.e("summary", summary.size() + " "+searchSum.get(0));
                Log.e("allcontent", allContent.size() + "");
                gbChoseCount=searchSum.get(0).split(",").length;
                if (canrun == false) {
                    Toast.makeText(view.getContext(), "View Type can't be blank", Toast.LENGTH_SHORT).show();
                } else if (gbChoseCount > 3) {
                    gbChoseCount=tempgbChoseCount;
                    ld.loadFailed();
                    Toast.makeText(view.getContext(), "Group By more than 3 level", Toast.LENGTH_SHORT).show();
                } else if (gbChoseCount == 0) {
                    gbChoseCount=tempgbChoseCount;
                    ld.closeFailedAnim().loadFailed();
                    Toast.makeText(view.getContext(), "Group by can't be blank", Toast.LENGTH_SHORT).show();
                } else {
                    tempgbChoseCount=gbChoseCount;
                    String sql = "";
                    Log.e("IErb",IErb.getText().toString());
                    if (IErb.getText().toString().equals("EoQ")) {
                        pre=SVrb.getText().toString();
                        comp=CVrb.getText().toString();
                        sql = "EXEC SP_IDC_EOQ_SNI_CHANGE_ANALYSIS '" + IErb.getText() + "','" + SVrb.getText() + "','" + CVrb.getText() + "','" + searchSum.get(0) + "','" + searchSum.get(1) + "','" + Sourb.getText() + "'";
                    } else {
                        pre=SVrb2.getText().toString();
                        comp=CVrb2.getText().toString();
                        sql = "EXEC SP_IDC_EOQ_SNI_CHANGE_ANALYSIS '" + IErb.getText() + "','" + SVrb2.getText() + "','" + CVrb2.getText() + "','" + searchSum.get(0) + "','" + searchSum.get(1) + "','" + Sourb.getText() + "'";
                    }

                    test(sql);
                }


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
            case R.id.ana_sv_btn2:
                ll = findViewById(R.id.ana_sv2);
                break;
            case R.id.ana_cv_btn2:
                ll = findViewById(R.id.ana_cv2);
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
                answerAna = DBUtil.QuerySQL(sql, 2);
                Message msg = new Message();
                if (answerAna.size() == 0) {
                    msg.what = 1002;
                } else {
                    //answerAna = DBUtil.sendRequestWithOkHttp();
                    setNumber(selectRadioBtn(radioGroup));
                    msg.what = 1001;
                }
                Bundle data = new Bundle();
                msg.setData(data);
                mHandler.sendMessage(msg);
                //updateAllTables(map);

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
                    //ld.loadFailed();
                    break;
            }
        }
    };

    /////////////////////////////////////////////////////////////////////////////////////////
    //将checklist放进界面中
    public void ConstructCheck(String title, String[] items, LinearLayout ll, String ini) {
        String[] initial = ini.split(",");
        HashMap<String, Boolean> map = new LinkedHashMap<>();
        allContent.add(items);
        for (int i = 0; i < items.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setTextColor(getResources().getColor(R.color.colorAccent));
            cb.setText(items[i]);
            cb.setTextSize(18);
            if (title.equals("viewtype")) {
                if (items[i].equals("MFG-Backlog") || items[i].equals("SNI-Backlog")) {
                    cb.setChecked(false);
                    cb.setEnabled(false);
                } else if (!items[i].equals("Invoice")) {
                    ViewGroup.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                    ((LinearLayout.LayoutParams) layoutParam).setMargins(40, 0, 0, 0);
                    cb.setLayoutParams(layoutParam);
                }
            }
            if (summaryOld.size() != 0) {
                if (summaryOld.get(title).get(items[i])) {
                    cb.setChecked(true);
                }
            } else {
                for (int j = 0; j < initial.length; j++) {
                    if (items[i].equals(initial[j])) {
                        cb.setChecked(true);
                    }
                }
            }
            map.put(items[i], cb.isChecked());
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
                    rb.setChecked(true);
                }
            }
        }
        radioSummary.put(title, radioButtons);
        allCondition.add(title);
    }

    //将数字放进table里
    public void setNumber(String unit) {
        if (answerAna.size() != 0) {
            List<Object> maplist = new ArrayList<>();
            maplistSum.clear();
            String title = "Empty";
            Log.e("object", unit);
            for (LinkedHashMap<String, String> a : answerAna) {
                LinkedHashMap<String, String> b = a;
                if (b.containsKey("Title")) {
                    maplistSum.put(title, maplist);
                    maplist = new ArrayList<>();
                    title = b.get("Title");

                }
                maplist.add(b);
            }
            maplistSum.put(title, maplist);
            maplistSum.remove("Empty");

//        for(String s:maplistSum.keySet()){
//            Log.e("数组",maplistSum.get(s).toString());
//        }

            LinkedHashMap<String, List<Object>> maplistSum2 = new LinkedHashMap<>();

            for (String typeTitle : maplistSum.keySet()) {
                List<Object> templist = new ArrayList<>();
                for (int h = 0; h < maplistSum.get(typeTitle).size(); h++) {
                    LinkedHashMap<String, String> tempmap = new LinkedHashMap<>();
                    for (String string : ((LinkedHashMap<String, String>) maplistSum.get(typeTitle).get(h)).keySet()) {
                        if (!string.equals("Title")) {
                            String value = ((LinkedHashMap<String, String>) maplistSum.get(typeTitle).get(h)).get(string);
                            value = value.replace("_", "");
                            if (value.contains("N") && !string.equals("COUNTRY") && !value.equals("-") && !string.equals("SITE") &&
                                    !string.equals("FACILITY") && !string.equals("ORDERTYPE")) {
                                value = value.replace("N", "");
                            }
                            value = value.replace("SI-Y", "");
                            value = value.replace("MFG-Y", "");
                            if (value.equals("0")) value = "-";
                            if (unit.equals("K")) {
                                if (!string.equals("COUNTRY") && !value.equals("-") && !string.equals("SITE") &&
                                        !string.equals("FACILITY") && !string.equals("ORDERTYPE")) {
                                    double dou = Double.parseDouble(value);
                                    dou = dou / 1000;
                                    value = String.format("%.1f", dou);
                                }
                            }
                            ((LinkedHashMap<String, String>) tempmap).put(string, value);
                        }
                    }
                    templist.add(tempmap);
                }
                maplistSum2.put(typeTitle, templist);
            }
            updateAllTables(maplistSum2);
        }

    }

    //在界面刷新之前保存旧数据
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        summary.clear();
        getSelectedCheckbox();
        outState.putSerializable("initial", answerAna);
        outState.putSerializable("initial2", summary);
        outState.putSerializable("initial3", radioSummary);
        outState.putInt("left", left);
        outState.putInt("right", right);
        outState.putString("pre",pre);
        outState.putString("comp",comp);
    }

    private String selectRadioBtn(RadioGroup rg) {
        RadioButton rb = Analysis.this.findViewById(rg.getCheckedRadioButtonId());
        return rb.getText() + "";

    }

    private void updateAllTables(LinkedHashMap<String, List<Object>> map2) {
        FragmentSystem fs = new FragmentSystem();
        FragmentClient fc = new FragmentClient();
        FragmentClientLob fcl = new FragmentClientLob();
        FragmentISG fisg = new FragmentISG();
        FragmentISGLOB fil = new FragmentISGLOB();

        ArrayList<List<Object>> ins = new ArrayList<>();
        ins.add((List<Object>) map2.get("systemoverall"));
        ins.add((List<Object>) map2.get("systemclient"));
        ins.add((List<Object>) map2.get("systemisg"));
        fs.setMaplistInFragSys(ins, left, right, gbChoseCount,pre,comp);


        ArrayList<List<Object>> ins2 = new ArrayList<>();
        ins2.add((List<Object>) map2.get("client"));
        ins2.add((List<Object>) map2.get("Consumer"));
        ins2.add((List<Object>) map2.get("Commercial"));
        ins2.add((List<Object>) map2.get("Alienware"));
        ins2.add((List<Object>) map2.get("Personal_Vostro"));
        ins2.add((List<Object>) map2.get("XPS_DT_NB"));
        ins2.add((List<Object>) map2.get("Lat_Opt"));
        ins2.add((List<Object>) map2.get("Workstation"));
        ins2.add((List<Object>) map2.get("CHROME"));
        ins2.add((List<Object>) map2.get("CLOUD_CLIENT_IOT"));
        Log.e("ins2", ins2.size() + "");
        fc.setMaplistInFragClient(ins2, left, right, gbChoseCount,pre,comp);

        ArrayList<List<Object>> ins3 = new ArrayList<>();
        ins3.add((List<Object>) map2.get("ALIENWARE_DESKTOPS"));
        ins3.add((List<Object>) map2.get("ALIENWARE_NOTEBOOKS"));
        ins3.add((List<Object>) map2.get("PERSONAL_DESKTOPS"));
        ins3.add((List<Object>) map2.get("PERSONAL_NOTEBOOKS"));
        ins3.add((List<Object>) map2.get("VOSTRO_DESKTOPS"));
        ins3.add((List<Object>) map2.get("VOSTRO_NOTEBOOKS"));
        ins3.add((List<Object>) map2.get("XPS_DESKTOPS"));
        ins3.add((List<Object>) map2.get("XPS_NOTEBOOKS"));
        ins3.add((List<Object>) map2.get("LATITUDE"));
        ins3.add((List<Object>) map2.get("OPTIPLEX_DESKTOPS"));
        ins3.add((List<Object>) map2.get("FIXED_WORKSTATIONS"));
        ins3.add((List<Object>) map2.get("MOBILE_WORKSTATIONS"));
        ins3.add((List<Object>) map2.get("CHROME"));
        ins3.add((List<Object>) map2.get("CLOUD_CLIENT"));
        ins3.add((List<Object>) map2.get("INTERNET_OF_THINGS"));
        Log.e("ins3", ins3.size() + "");
        fcl.setMaplistInFragcl(ins3, left, right, gbChoseCount,pre,comp);

        ArrayList<List<Object>> ins4 = new ArrayList<>();
        ins4.add((List<Object>) map2.get("isg_overall"));
        ins4.add((List<Object>) map2.get("isg_system"));
        ins4.add((List<Object>) map2.get("isg_Non_Sys"));
        Log.e("ins4", ins4.size() + "");
        fisg.setMaplistInFragIsg(ins4, left, right, gbChoseCount,pre,comp);

        ArrayList<List<Object>> ins5 = new ArrayList<>();
        ins5.add((List<Object>) map2.get("isg_PowerEdge"));
        ins5.add((List<Object>) map2.get("isg_Cloud"));
        ins5.add((List<Object>) map2.get("isg_storage"));
        ins5.add((List<Object>) map2.get("isg_Networking"));
        ins5.add((List<Object>) map2.get("isg_hit"));
        Log.e("ins5", ins5.size() + "");
        fil.setMaplistInFragIsg(ins5, left, right, gbChoseCount,pre,comp);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                ld.loadFailed();
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

    public ArrayList<String> getSelectedCheckbox() {
        ArrayList<String> searchResult = new ArrayList<>();

        HashMap<String, Boolean> gbmap = new HashMap<>();
        LinearLayout gbll = findViewById(R.id.ana_gb);
        String gb = "";
        for (int i = 0; i < gbll.getChildCount(); i++) {
            if (((CheckBox) gbll.getChildAt(i)).isChecked()) {
                if (gb.equals("")) {
                    gb += ((CheckBox) gbll.getChildAt(i)).getText();
                } else {
                    gb = gb + "," + ((CheckBox) gbll.getChildAt(i)).getText();
                }
            }
            gbmap.put(((CheckBox) gbll.getChildAt(i)).getText().toString(), ((CheckBox) gbll.getChildAt(i)).isChecked());

        }
        summary.put("groupby", gbmap);
        searchResult.add(gb);

        HashMap<String, Boolean> vtmap = new HashMap<>();
        LinearLayout vtll = findViewById(R.id.ana_vt);
        String vt = "";
        for (int i = 0; i < vtll.getChildCount(); i++) {
            if (((CheckBox) vtll.getChildAt(i)).isChecked()) {
                if (vt.equals("")) {
                    vt += ((CheckBox) vtll.getChildAt(i)).getText();
                } else {
                    vt = vt + "," + ((CheckBox) vtll.getChildAt(i)).getText();
                }
            }
            vtmap.put(((CheckBox) vtll.getChildAt(i)).getText().toString(), ((CheckBox) vtll.getChildAt(i)).isChecked());

        }
        summary.put("viewtype", vtmap);
        searchResult.add(vt);
        return searchResult;
    }
}
