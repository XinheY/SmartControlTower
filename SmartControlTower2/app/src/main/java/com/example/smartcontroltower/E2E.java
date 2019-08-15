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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
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

import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.google.android.material.navigation.NavigationView;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class E2E extends AppCompatActivity {

    private DrawerLayout drawerl;
    private ActionBarDrawerToggle toggle;
    public static MySmartTable<Object> table;
    private HashMap<String, HashMap<String, Boolean>> summary = new HashMap<>();//所有checkbox的集合 HashMap<类型, HashMap <内容，是否被打勾>>
    private HashMap<String, HashMap<String, Boolean>> summaryOld = new HashMap<>();//之前所有checkbox的集合
    private static ArrayList<LinkedHashMap<String, String>> answerE2E=new ArrayList<>();//储存表格中数据
    private LoadingDialog ld = null;//加载动画
    private Button version_button, version_button2,year_quar_button,expand;
    private LinearLayout year_quar_LinearLayout,version_linearlayout,version_linearlayout2;
    private RadioGroup viewType,radioGroup,IdcEoqChose;
    private String expandTitle = "Expand";
    private InitializeInfo EoQInitialInfo = null;//EoQ模式下的version,version_year等
    private InitializeInfo IDCInitialInfo = null;//IDC模式下的version，version_year等
    private int[] AccessRight = null;

    @Override
    protected void onPause() {
        super.onPause();
        if (ld != null) {
            ld.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         //判断是否存在之前该页面的缓存内容，存在就继承，不存在就创建新的
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            summaryOld = (HashMap<String, HashMap<String, Boolean>>) savedInstanceState.getSerializable("initial2");
            expandTitle = savedInstanceState.getString("initial3");
        }

        setContentView(R.layout.activity_e2_e);
        radioGroup = findViewById(R.id.e2e_range);
        viewType = findViewById(R.id.e2e_spinner);
        version_button = findViewById(R.id.e2e_ver_btn);
        version_button2 = findViewById(R.id.e2e_ver_btn2);
        year_quar_button = findViewById(R.id.e2e_yq_btn);
        year_quar_LinearLayout = findViewById(R.id.e2e_yq);
        version_linearlayout = findViewById(R.id.e2e_ver);
        version_linearlayout2 = findViewById(R.id.e2e_ver2);
        table = findViewById(R.id.table);

        //从之前的页面获取变量
        EoQInitialInfo = (InitializeInfo) getIntent().getSerializableExtra("InitializeInfo");
        IDCInitialInfo = (InitializeInfo) getIntent().getSerializableExtra("InitializeInfo2");
        AccessRight = (int[]) getIntent().getSerializableExtra("AccessRight");
        /////////////////Table//////////////////////////////////////////////////
        //设置初始值
        if (answerE2E.size() != 0 || savedInstanceState != null) {
            //如果存在历史数据直接放进表格中，主要用于屏幕旋转
            setNumber(selectRadioBtn(radioGroup), expandTitle);
        } else {
            //不存在历史数据重新从数据库获取数据
            ld = new LoadingDialog(this);
            ld.setLoadingText("Loading...").setSuccessText("Success").setFailedText("Failed")
                    .closeSuccessAnim().show();
            test("EXEC [SP_IDC_EOQ_SUMMARY] '" + "EoQ" + "','" + "QuarView" + "','" + EoQInitialInfo.getVersion_year_quar().get(0) + "','" + "" + "','" + "overall" + "','" + "system" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "'");
        }
        ///////////////////////////////Expand Button 展开or收起数据/////////////////////////////////////
        expand = findViewById(R.id.e2e_isexpand);
        expand.setText(expandTitle);
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expandTitle.equals("Expand")) {
                    expandTitle = "Collapse";
                } else if (expandTitle.equals("Collapse")) {
                    expandTitle = "Expand";
                }
                expand.setText(expandTitle);
                setNumber(selectRadioBtn(radioGroup), expandTitle);
            }
        });

        //////////////////////////////////RadioGroup//////////////////////////////////////
        RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setNumber(selectRadioBtn(radioGroup), expandTitle);
            }
        };

        radioGroup.setOnCheckedChangeListener(listener);

        IdcEoqChose = findViewById(R.id.e2e_rg);
        IdcEoqChose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (selectRadioBtn(viewType).equals("VersionView")) {
                    if (selectRadioBtn(radioGroup).equals("EoQ")) {
                        version_button.setVisibility(View.VISIBLE);
                        version_button2.setVisibility(View.GONE);
                        version_linearlayout2.setVisibility(View.GONE);
                    } else {
                        version_button2.setVisibility(View.VISIBLE);
                        version_button.setVisibility(View.GONE);
                        version_linearlayout.setVisibility(View.GONE);
                    }
                }
            }
        });

        RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (selectRadioBtn(viewType).equals("VersionView")) {
                    if (selectRadioBtn(IdcEoqChose).equals("EoQ")) {
                        version_button.setVisibility(View.VISIBLE);
                    } else {
                        version_button2.setVisibility(View.VISIBLE);
                    }
                    year_quar_button.setVisibility(View.GONE);
                    year_quar_LinearLayout.setVisibility(View.GONE);
                } else {
                    version_button.setVisibility(View.GONE);
                    version_button2.setVisibility(View.GONE);
                    version_linearlayout2.setVisibility(View.GONE);
                    version_linearlayout.setVisibility(View.GONE);
                    year_quar_button.setVisibility(View.VISIBLE);
                }
            }
        };
        viewType.setOnCheckedChangeListener(listener2);

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
                    case R.id.nav_Dynamic:
                        Intent intent2 = new Intent(E2E.this, Dynamic.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("InitializeInfo", EoQInitialInfo);
                        bundle2.putSerializable("InitializeInfo2", IDCInitialInfo);
                        bundle2.putSerializable("AccessRight", AccessRight);
                        intent2.putExtras(bundle2);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_DirectBL:
                        Intent intent4 = new Intent(E2E.this, DirectBL.class);
                        Bundle bundle4 = new Bundle();
                        bundle4.putSerializable("InitializeInfo", EoQInitialInfo);
                        bundle4.putSerializable("InitializeInfo2", IDCInitialInfo);
                        bundle4.putSerializable("AccessRight", AccessRight);
                        intent4.putExtras(bundle4);
                        startActivity(intent4);
                        finish();
                        break;
                    case R.id.nav_analysis:
                        Intent intent3 = new Intent(E2E.this, Analysis.class);
                        Bundle bundle3 = new Bundle();
                        bundle3.putSerializable("InitializeInfo", EoQInitialInfo);
                        bundle3.putSerializable("InitializeInfo2", IDCInitialInfo);
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
        String[] ver = ((String[]) EoQInitialInfo.getVersion().toArray(new String[EoQInitialInfo.getVersion().size()]));
        ConstructCheck("version", ver, e2ever, "");

        LinearLayout e2ever2 = findViewById(R.id.e2e_ver2);
        String[] ver2 = ((String[]) IDCInitialInfo.getVersion().toArray(new String[IDCInitialInfo.getVersion().size()]));
        ConstructCheck("version2", ver2, e2ever2, "");

        LinearLayout e2eyq = findViewById(R.id.e2e_yq);
        String[] yq = ((String[]) EoQInitialInfo.getVersion_year_quar().toArray(new String[EoQInitialInfo.getVersion_year_quar().size()]));
        ConstructCheck("yq", yq, e2eyq, yq[0]);

        LinearLayout e2elob = findViewById(R.id.e2e_lob);
        String[] lob = getResources().getStringArray(R.array.Lob);
        ConstructCheck("lob", lob, e2elob, "OVERALL");

        LinearLayout e2esf = findViewById(R.id.e2e_sf);
        String[] sf = getResources().getStringArray(R.array.system_flag);
        ConstructCheck("sf", sf, e2esf, "System");

        LinearLayout e2ept = findViewById(R.id.e2e_pt);
        String[] pt = getResources().getStringArray(R.array.Product_Type);
        ConstructCheck("pt", pt, e2ept, "OVERALL");

        LinearLayout e2elg = findViewById(R.id.e2e_lg);
        String[] lg = getResources().getStringArray(R.array.LOB_Group);
        ConstructCheck("lg", lg, e2elg, "OVERALL");

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
                answerE2E.clear();
                ArrayList<String> searchSum = new ArrayList<>();//被check的checkbox集合
                RadioGroup radio = findViewById(R.id.e2e_rg);
                RadioButton radioButton = findViewById(radio.getCheckedRadioButtonId());
                String ratioText = radioButton.getText().toString();
                searchSum.add(ratioText);
                RadioButton radioButton2 = findViewById(viewType.getCheckedRadioButtonId());
                String ratioText2 = radioButton2.getText().toString();
                searchSum.add(ratioText2);
                boolean canrun = true;
                summary.clear();
                ArrayList<String> searchSum2 = getSelectedCheckbox();
                String sql = "";
                if (searchSum.get(0).equals("IDC")) {
                    sql = "EXEC [SP_IDC_EOQ_SUMMARY] '" + searchSum.get(0) + "','" + searchSum.get(1) + "','" + searchSum2.get(0) + "','" + searchSum2.get(2) + "','" + searchSum2.get(3) + "','" + searchSum2.get(4) + "','" + searchSum2.get(5) + "','" + searchSum2.get(6) + "','" + searchSum2.get(7) + "','" + searchSum2.get(8) + "','" + searchSum2.get(9) + "'";
                } else {
                    sql = "EXEC [SP_IDC_EOQ_SUMMARY] '" + searchSum.get(0) + "','" + searchSum.get(1) + "','" + searchSum2.get(0) + "','" + searchSum2.get(1) + "','" + searchSum2.get(3) + "','" + searchSum2.get(4) + "','" + searchSum2.get(5) + "','" + searchSum2.get(6) + "','" + searchSum2.get(7) + "','" + searchSum2.get(8) + "','" + searchSum2.get(9) + "'";
                }
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
            case R.id.e2e_ver_btn2:
                ll = findViewById(R.id.e2e_ver2);
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

    /**
     * 连接数据库获取数据之后放进表格中
     * @param sql
     */
    private void test(final String sql) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //测试数据库的语句,在子线程操作
                Message msg = new Message();
                answerE2E = DBUtil.QuerySQL(sql, 1);
                if (answerE2E.size() == 0) {
                    setNumber(selectRadioBtn(radioGroup), expandTitle);
                    msg.what = 1002;
                } else {
                    setNumber(selectRadioBtn(radioGroup), expandTitle);
                    msg.what = 1001;
                }
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
                    table.setVisibility(View.VISIBLE);
                    ld.loadSuccess();
                    break;
                case 1002:
                    Log.e("Timeout", "Timeout in handler");
                    table.setVisibility(View.GONE);
                    ld.setFailedText("No Data").loadFailed();
                    break;
                default:
                    ld.loadFailed();
                    break;
            }
        }
    };

    /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 将checklist放进界面中
     * @param title
     * @param items
     * @param ll
     * @param ini
     */
    public void ConstructCheck(String title, String[] items, LinearLayout ll, String ini) {
        String[] initial = ini.split(",");
        HashMap<String, Boolean> map = new LinkedHashMap<>();
        for (int i = 0; i < items.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setTextColor(getResources().getColor(R.color.colorAccent));
            cb.setText(items[i]);
            cb.setTextSize(18);
            if (summaryOld.size() != 0) {
                Log.e("item", items[i]);
                if (summaryOld.get(title).get(items[i])) {
                    cb.setChecked(true);
                }
            } else {
                for (int j = 0; j < initial.length; j++) {
                    if (items[i].equals(ini)) {
                        cb.setChecked(true);
                    }
                }
            }
            map.put(items[i], cb.isChecked());
            ll.addView(cb);
        }
        summary.put(title, map);
    }

    /**
     * 将数字放进table里
     * @param unit
     * @param title
     */
    public void setNumber(String unit, String title) {
        List<Object> maplist = new ArrayList<>();
        List<Object> collapsemap = new ArrayList<>();
        if (answerE2E.size() != 0) {
            for (LinkedHashMap<String, String> a : answerE2E) {
                LinkedHashMap<String, String> lhm = new LinkedHashMap<>();
                LinkedHashMap<String, String> lhm2 = new LinkedHashMap<>();
                if (unit.equals("K")) {
                    for (String b : a.keySet()) {
                        if (!b.equals("Item")) {
                            double dou = Double.parseDouble(a.get(b));
                            dou = dou / 1000;
                            lhm.put(b, String.format("%.1f", dou));
                        } else {
                            lhm.put(b, a.get(b));
                        }
                    }
                    if (lhm.get("Item").contains("Direct_")) {
                        String temp = lhm.get("Item");
                        temp = temp.replace("Direct_", "    ");
                        lhm.put("Item", temp.replace("_", " "));
                    } else if (lhm.get("Item").contains("Scheduled_")) {
                        String temp = lhm.get("Item");
                        temp = temp.replace("Scheduled_", "    ");
                        lhm.put("Item", temp.replace("_", " "));
                    } else if (lhm.get("Item").equals("ANZ") || lhm.get("Item").equals("GC") || lhm.get("Item").equals("India") || lhm.get("Item").equals("Japan") || lhm.get("Item").equals("Korea") || lhm.get("Item").equals("SA")) {
                        String temp = lhm.get("Item");
                        temp = "    " + temp;
                        lhm.put("Item", temp);
                    } else {
                        collapsemap.add(lhm);
                    }
                    maplist.add(lhm);
                } else {
                    for (String b : a.keySet()) {
                        lhm2.put(b, a.get(b));
                    }
                    if (a.get("Item").contains("Direct_")) {
                        String temp = a.get("Item");
                        temp = temp.replace("Direct_", "    ");
                        lhm2.put("Item", temp.replace("_", " "));
                    } else if (a.get("Item").contains("Scheduled_")) {
                        String temp = a.get("Item");
                        temp = temp.replace("Scheduled_", "    ");
                        lhm2.put("Item", temp.replace("_", " "));
                    } else if (a.get("Item").equals("ANZ") || a.get("Item").equals("GC") || a.get("Item").equals("India") || a.get("Item").equals("Japan") || a.get("Item").equals("Korea") || a.get("Item").equals("SA")) {
                        String temp = a.get("Item");
                        temp = "    " + temp;
                        lhm2.put("Item", temp);
                    } else {
                        collapsemap.add(lhm2);
                    }
                    maplist.add(lhm2);
                }
            }
            RadioGroup radio = findViewById(R.id.e2e_rg);
            RadioButton radioButton = findViewById(radio.getCheckedRadioButtonId());
            String ratioText = radioButton.getText().toString();

            if (maplist.size() != 0 && collapsemap.size() != 0) {
                MapTableData tableData;
                if (title.equals("Expand")) {
                    tableData = MapTableData.create(ratioText, collapsemap);

                    //给表格特定行添加颜色
                    table.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {
                        @Override
                        public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                            if (cellInfo.row == 4 || cellInfo.row == 5 || cellInfo.row == 6) {
                                paint.setColor(getResources().getColor(R.color.rowgray));
                                canvas.drawRect(rect, paint);
                            }
                        }

                        @Override
                        public int getTextColor(CellInfo cellInfo) {
                            return 0;
                        }
                    });


                } else {
                    tableData = MapTableData.create(ratioText, maplist);
                    table.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {
                        @Override
                        public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                            if (cellInfo.row == 4 || cellInfo.row == 11 || cellInfo.row == 21) {
                                paint.setColor(getResources().getColor(R.color.rowgray));
                                canvas.drawRect(rect, paint);
                            } else if ((cellInfo.row > 4 && cellInfo.row < 11) || (cellInfo.row > 11 && cellInfo.row < 21) || (cellInfo.row > 21 && cellInfo.row < 28)) {
                                paint.setColor(getResources().getColor(R.color.itemgray));
                                canvas.drawRect(rect, paint);
                            }
                        }
                        @Override
                        public int getTextColor(CellInfo cellInfo) {
                            return 0;
                        }
                    });
                }

                table.getConfig().setFixedTitle(true);//将第一行固定
                tableData.getColumns().get(0).setFixed(true);//固定第一列
                tableData.getColumns().get(0).setTextAlign(Paint.Align.LEFT);//第一列字向左对齐
                table.setZoom(true, 2, 1);//缩小放大的倍数
                table.getConfig().setShowXSequence(false);//不显示顺序行
                table.getConfig().setShowYSequence(false);//不显示顺序列
                table.getConfig().setTableTitleStyle(new FontStyle(50, getResources().getColor(R.color.table_gray)));
                table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(getResources().getColor(R.color.table_gray)));
                table.getConfig().setContentStyle(new FontStyle(45, getResources().getColor(R.color.table_gray)));
                table.getConfig().setColumnTitleStyle(new FontStyle(45, getResources().getColor(R.color.white)));
                table.getConfig().setVerticalPadding(10);//纵向距离
                table.setTableData(tableData);//将数据放进表格中
                table.invalidate();
            }
        }


    }

    /**
     * 在界面刷新之前保存旧数据
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        summary.clear();
        getSelectedCheckbox();
        outState.putSerializable("initial", answerE2E);
        outState.putSerializable("initial2", summary);
        outState.putString("initial3", expandTitle);
    }

    /**
     * 获取在当前radiogroup中被选中的radioButton
     * @param rg radioGroup
     * @return 该radiogroup被选中的项的字
     */
    private String selectRadioBtn(RadioGroup rg) {
        RadioButton rb = E2E.this.findViewById(rg.getCheckedRadioButtonId());
        return rb.getText() + "";

    }

    /**
     * 返回键退出
     * @param keyCode
     * @param event
     * @return
     */
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

    /**
     * 获取filter中所有被选中的checkbox
     * @return 每个filter种类被选中的项的string
     */
    public ArrayList<String> getSelectedCheckbox() {
        ArrayList<String> searchResult = new ArrayList<>();
        String yq = "";
        HashMap<String, Boolean> yqmap = new HashMap<>();
        for (int i = 0; i < year_quar_LinearLayout.getChildCount(); i++) {
            if (((CheckBox) year_quar_LinearLayout.getChildAt(i)).isChecked()) {
                if (yq.equals("")) {
                    yq += ((CheckBox) year_quar_LinearLayout.getChildAt(i)).getText();
                } else {
                    yq = yq + "," + ((CheckBox) year_quar_LinearLayout.getChildAt(i)).getText();
                }
            }
            yqmap.put(((CheckBox) year_quar_LinearLayout.getChildAt(i)).getText().toString(), ((CheckBox) year_quar_LinearLayout.getChildAt(i)).isChecked());
        }
        summary.put("yq", yqmap);
        searchResult.add(yq);

        String ver = "";
        HashMap<String, Boolean> vermap = new HashMap<>();
        for (int i = 0; i < version_linearlayout.getChildCount(); i++) {
            if (((CheckBox) version_linearlayout.getChildAt(i)).isChecked()) {
                if (ver.equals("")) {
                    ver += ((CheckBox) version_linearlayout.getChildAt(i)).getText();
                } else {
                    ver = ver + "," + ((CheckBox) version_linearlayout.getChildAt(i)).getText();
                }
            }
            vermap.put(((CheckBox) version_linearlayout.getChildAt(i)).getText().toString(), ((CheckBox) version_linearlayout.getChildAt(i)).isChecked());
        }
        summary.put("version", vermap);
        searchResult.add(ver);

        String ver2 = "";
        HashMap<String, Boolean> ver2map = new HashMap<>();
        for (int i = 0; i < version_linearlayout2.getChildCount(); i++) {
            if (((CheckBox) version_linearlayout2.getChildAt(i)).isChecked()) {
                if (ver2.equals("")) {
                    ver2 += ((CheckBox) version_linearlayout2.getChildAt(i)).getText();
                } else {
                    ver2 = ver2 + "," + ((CheckBox) version_linearlayout2.getChildAt(i)).getText();
                }
            }
            ver2map.put(((CheckBox) version_linearlayout2.getChildAt(i)).getText().toString(), ((CheckBox) version_linearlayout2.getChildAt(i)).isChecked());
        }
        summary.put("version2", ver2map);
        searchResult.add(ver2);

        LinearLayout lobll = findViewById(R.id.e2e_lob);
        String lob = "";
        HashMap<String, Boolean> lobmap = new HashMap<>();
        for (int i = 0; i < lobll.getChildCount(); i++) {
            if (((CheckBox) lobll.getChildAt(i)).isChecked()) {
                if (lob.equals("")) {
                    lob += ((CheckBox) lobll.getChildAt(i)).getText();
                } else {
                    lob = lob + "," + ((CheckBox) lobll.getChildAt(i)).getText();
                }
            }
            lobmap.put(((CheckBox) lobll.getChildAt(i)).getText().toString(), ((CheckBox) lobll.getChildAt(i)).isChecked());

        }
        summary.put("lob", lobmap);
        searchResult.add(lob);

        LinearLayout sfll = findViewById(R.id.e2e_sf);
        HashMap<String, Boolean> sfmap = new HashMap<>();
        String sf = "";
        for (int i = 0; i < sfll.getChildCount(); i++) {
            if (((CheckBox) sfll.getChildAt(i)).isChecked()) {
                if (sf.equals("")) {
                    sf += ((CheckBox) sfll.getChildAt(i)).getText();
                } else {
                    sf = sf + "," + ((CheckBox) sfll.getChildAt(i)).getText();
                }
            }
            sfmap.put(((CheckBox) sfll.getChildAt(i)).getText().toString(), ((CheckBox) sfll.getChildAt(i)).isChecked());
        }
        summary.put("sf", sfmap);
        searchResult.add(sf);

        LinearLayout ptll = findViewById(R.id.e2e_pt);
        HashMap<String, Boolean> ptmap = new HashMap<>();
        String pt = "";
        for (int i = 0; i < ptll.getChildCount(); i++) {
            if (((CheckBox) ptll.getChildAt(i)).isChecked()) {
                if (pt.equals("")) {
                    pt += ((CheckBox) ptll.getChildAt(i)).getText();
                } else {
                    pt = pt + "," + ((CheckBox) ptll.getChildAt(i)).getText();
                }
            }
            ptmap.put(((CheckBox) ptll.getChildAt(i)).getText().toString(), ((CheckBox) ptll.getChildAt(i)).isChecked());
        }
        summary.put("pt", ptmap);
        searchResult.add(pt);

        LinearLayout lgll = findViewById(R.id.e2e_lg);
        HashMap<String, Boolean> lgmap = new HashMap<>();
        String lg = "";
        for (int i = 0; i < lgll.getChildCount(); i++) {
            if (((CheckBox) lgll.getChildAt(i)).isChecked()) {
                if (lg.equals("")) {
                    lg += ((CheckBox) lgll.getChildAt(i)).getText();
                } else {
                    lg = lg + "," + ((CheckBox) lgll.getChildAt(i)).getText();
                }
            }
            lgmap.put(((CheckBox) lgll.getChildAt(i)).getText().toString(), ((CheckBox) lgll.getChildAt(i)).isChecked());
        }
        summary.put("lg", lgmap);
        searchResult.add(lg);

        LinearLayout regionll = findViewById(R.id.e2e_region);
        HashMap<String, Boolean> regionmap = new HashMap<>();
        String region = "";
        for (int i = 0; i < regionll.getChildCount(); i++) {
            if (((CheckBox) regionll.getChildAt(i)).isChecked()) {
                if (region.equals("")) {
                    region += ((CheckBox) regionll.getChildAt(i)).getText();
                } else {
                    region = region + "," + ((CheckBox) regionll.getChildAt(i)).getText();
                }
            }
            regionmap.put(((CheckBox) regionll.getChildAt(i)).getText().toString(), ((CheckBox) regionll.getChildAt(i)).isChecked());

        }
        summary.put("region", regionmap);
        searchResult.add(region);

        HashMap<String, Boolean> segmap = new HashMap<>();
        LinearLayout segll = findViewById(R.id.e2e_segment);
        String seg = "";
        for (int i = 0; i < segll.getChildCount(); i++) {
            if (((CheckBox) segll.getChildAt(i)).isChecked()) {
                if (seg.equals("")) {
                    seg += ((CheckBox) segll.getChildAt(i)).getText();
                } else {
                    seg = seg + "," + ((CheckBox) segll.getChildAt(i)).getText();
                }
            }
            segmap.put(((CheckBox) segll.getChildAt(i)).getText().toString(), ((CheckBox) segll.getChildAt(i)).isChecked());

        }
        summary.put("segment", segmap);
        searchResult.add(seg);

        HashMap<String, Boolean> sitemap = new HashMap<>();
        LinearLayout sitell = findViewById(R.id.e2e_site);
        String site = "";
        for (int i = 0; i < sitell.getChildCount(); i++) {
            if (((CheckBox) sitell.getChildAt(i)).isChecked()) {
                if (site.equals("")) {
                    site += ((CheckBox) sitell.getChildAt(i)).getText();
                } else {
                    site = site + "," + ((CheckBox) sitell.getChildAt(i)).getText();
                }
            }
            sitemap.put(((CheckBox) sitell.getChildAt(i)).getText().toString(), ((CheckBox) sitell.getChildAt(i)).isChecked());

        }
        summary.put("site", sitemap);
        searchResult.add(site);
        return searchResult;
    }


}
