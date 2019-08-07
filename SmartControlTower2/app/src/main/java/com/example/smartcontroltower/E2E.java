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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
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
    private HashMap<String, HashMap<String, CheckBox>> summary = new HashMap<>();//所有checkbox的集合
    private HashMap<String, HashMap<String, CheckBox>> summaryOld = new HashMap<>();//之前所有checkbox的集合
    private ArrayList<String> allCondition = new ArrayList<>();
    private ArrayList<String[]> allContent = new ArrayList<>();
    private ArrayList<LinkedHashMap<String, String>> answerE2E;
    private LoadingDialog ld = null;
    private Button verbtn;
    private Button yqbtn;
    private Button expand;
    private LinearLayout yqll;
    private LinearLayout verll;
    private RadioGroup viewType;
    private RadioGroup radioGroup;
    private String expandTitle = "Expand";
    private InitializeInfo info=null;

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

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            answerE2E = (ArrayList<LinkedHashMap<String, String>>) savedInstanceState.getSerializable("initial");
            summaryOld = (HashMap<String, HashMap<String, CheckBox>>) savedInstanceState.getSerializable("initial2");
            expandTitle = savedInstanceState.getString("initial3");
        } else {
            answerE2E = new ArrayList<>();
        }

        setContentView(R.layout.activity_e2_e);
        radioGroup = findViewById(R.id.e2e_range);
        viewType = findViewById(R.id.e2e_spinner);
        verbtn = findViewById(R.id.e2e_ver_btn);
        yqbtn = findViewById(R.id.e2e_yq_btn);
        yqll = findViewById(R.id.e2e_yq);
        verll = findViewById(R.id.e2e_ver);
        table = findViewById(R.id.table);

        info  = (InitializeInfo) getIntent().getSerializableExtra("InitializeInfo");
        Log.e("info",info.getVersion().toString());
        /////////////////Table//////////////////////////////////////////////////
        //设置初始值
        if (answerE2E.size() != 0) {
            setNumber(selectRadioBtn(radioGroup), expandTitle);
        } else {
            ld = new LoadingDialog(this);
            ld.setLoadingText("Loading...").setSuccessText("Success").setFailedText("Failed")
                    .closeSuccessAnim().show();
            test("EXEC [SP_IDC_EOQ_SUMMARY] '" + "EoQ" + "','" + "QuarView" + "','" + "FY20Q2" + "','" + "" + "','" + "overall" + "','" + "system" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "'");
            //Log.e("SQL","EXEC [SP_IDC_EOQ_SUMMARY] '" + "EoQ" + "','" + "QuarView" + "','" + "FY20Q2" + "','" + "" + "','" + "overall" + "','" + "system" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "'");
        }
        ///////////////////////////////Expand Button//////////////////////////////////////////
        expand = findViewById(R.id.e2e_isexpand);
        expand.setText(expandTitle);
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expandTitle.equals("Expand")) {
                    expandTitle = "Collapse";
                }
                else if(expandTitle.equals("Collapse")) {
                    expandTitle = "Expand";
                }
                expand.setText(expandTitle);
                Log.e("expand",expandTitle);
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

        RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (selectRadioBtn(viewType).equals("VersionView")) {
                    verbtn.setVisibility(View.VISIBLE);
                    yqbtn.setVisibility(View.GONE);
                    yqll.setVisibility(View.GONE);
                    for (String checkb : summary.get("yq").keySet()) {
                        CheckBox cbb = summary.get("yq").get(checkb);
                        cbb.setChecked(false);
                    }
                } else {
                    verbtn.setVisibility(View.GONE);
                    verll.setVisibility(View.GONE);
                    yqbtn.setVisibility(View.VISIBLE);
                    for (String checkb : summary.get("version").keySet()) {
                        CheckBox cbb = summary.get("version").get(checkb);
                        cbb.setChecked(false);
                    }
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
                        Bundle bundle2=new Bundle();
                        bundle2.putSerializable("InitializeInfo",info);
                        intent2.putExtras(bundle2);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_DirectBL:
                        Intent intent4 = new Intent(E2E.this, DirectBL.class);
                        Bundle bundle4=new Bundle();
                        bundle4.putSerializable("InitializeInfo",info);
                        intent4.putExtras(bundle4);
                        startActivity(intent4);
                        finish();
                        break;
                    case R.id.nav_analysis:
                        Intent intent3 = new Intent(E2E.this, Analysis.class);
                        Bundle bundle3=new Bundle();
                        bundle3.putSerializable("InitializeInfo",info);
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
        String[] ver = ((String[])info.getVersion().toArray(new String[info.getVersion().size()]));
        ConstructCheck("version", ver, e2ever, "");

        LinearLayout e2eyq = findViewById(R.id.e2e_yq);
        String[] yq = ((String[])info.getVersion_year_quar().toArray(new String[info.getVersion_year_quar().size()]));
        ConstructCheck("yq", yq, e2eyq, yq[0]);

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
                for (int i = 0; i < summary.size(); i++) {
                    String oneCondition = "";
                    HashMap<String, CheckBox> innerMap = summary.get(allCondition.get(i));
                    int count = 0;
                    for (int j = 0; j < innerMap.size(); j++) {
                        CheckBox cbc = innerMap.get((allContent.get(i))[j]);
                        if (cbc.isChecked()) {
                            count++;
                            if (oneCondition.equals("")) {
                                oneCondition += cbc.getText();
                            } else {
                                oneCondition += "," + cbc.getText();
                            }
                        }
                    }
                    if (count == 0 && i > 1) {
                        canrun = false;
                        Toast.makeText(E2E.this, "Blank " + allCondition.get(i), Toast.LENGTH_LONG).show();
                        ld.closeFailedAnim().loadFailed();
                        break;
                    }
                    searchSum.add(oneCondition);
                }
                if (canrun) {
                    if (searchSum.get(2) != "" || searchSum.get(3) != "") {
                        String sql = "EXEC [SP_IDC_EOQ_SUMMARY] '" + searchSum.get(0) + "','" + searchSum.get(1) + "','" + searchSum.get(3) + "','" + searchSum.get(2) + "','" + searchSum.get(4) + "','" + searchSum.get(5) + "','" + searchSum.get(6) + "','" + searchSum.get(7) + "','" + searchSum.get(8) + "','" + searchSum.get(9) + "','" + searchSum.get(10) + "'";
                        toggleRightSliding();
                        test(sql);
                    } else {
                        Toast.makeText(E2E.this, "Blank Error", Toast.LENGTH_LONG).show();
                        ld.closeFailedAnim().loadFailed();
                    }
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
                answerE2E = DBUtil.QuerySQL(sql,1);
                //Log.e("SQL",sql);
                //answerE2E = DBUtil.sendRequestWithOkHttp();
                setNumber(selectRadioBtn(radioGroup), expandTitle);
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
            if (summaryOld.size() != 0) {
                if (summaryOld.get(title).get(items[i]).isChecked()) {
                    cb.setChecked(true);
                }
            } else {
                for (int j = 0; j < initial.length; j++) {
                    if (items[i].equals(ini)) {
                        cb.setChecked(true);
                    }
                }
            }
            ll.addView(cb);
        }
        summary.put(title, map);
        allCondition.add(title);
    }

    //将数字放进table里
    public void setNumber(String unit, String title) {
        List<Object> maplist = new ArrayList<>();
        List<Object> collapsemap = new ArrayList<>();
        for (LinkedHashMap<String, String> a : answerE2E) {
            LinkedHashMap<String, String> lhm = new LinkedHashMap<>();
            LinkedHashMap<String,String> lhm2=new LinkedHashMap<>();
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
                } else {
                    collapsemap.add(lhm);
                }
                maplist.add(lhm);
            } else {
                for (String b : a.keySet()) {
                        lhm2.put(b, a.get(b));
                }
                Log.e("a",a.toString());
                if (a.get("Item").contains("Direct_")) {
                    String temp = a.get("Item");
                    temp = temp.replace("Direct_", "    ");
                    lhm2.put("Item", temp.replace("_", " "));
                } else if (a.get("Item").contains("Scheduled_")) {
                    String temp = a.get("Item");
                    temp = temp.replace("Scheduled_", "    ");
                    lhm2.put("Item", temp.replace("_", " "));
                } else {
                    collapsemap.add(lhm2);
                }
                Log.e("a-after",a.toString());
                maplist.add(lhm2);
            }

        }

        if (maplist.size() != 0 && collapsemap.size() != 0) {
            Log.e("size",maplist.size()+" "+collapsemap.size());
            MapTableData tableData;
            if (title.equals("Expand")) {
                tableData = MapTableData.create("EoQ && IDC", collapsemap);
                table.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {
                    @Override
                    public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                        if (cellInfo.row==4||cellInfo.row==5) {
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
                tableData = MapTableData.create("EoQ && IDC", maplist);
                table.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {
                    @Override
                    public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                        if (cellInfo.row==4||cellInfo.row==11) {
                            paint.setColor(getResources().getColor(R.color.rowgray));
                            canvas.drawRect(rect, paint);
                        }
                        else if((cellInfo.row>4&&cellInfo.row<11)||(cellInfo.row>11&&cellInfo.row<21)){
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

            table.getConfig().setFixedTitle(true);
            tableData.getColumns().get(0).setFixed(true);
            tableData.getColumns().get(0).setTextAlign(Paint.Align.LEFT);
            table.setZoom(true, 2, 1);
            table.getConfig().setShowXSequence(false);
            table.getConfig().setShowYSequence(false);
            table.getConfig().setTableTitleStyle(new FontStyle(50, getResources().getColor(R.color.table_gray)));
            table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(getResources().getColor(R.color.table_gray)));
            table.getConfig().setContentStyle(new FontStyle(45, getResources().getColor(R.color.table_gray)));
            table.getConfig().setColumnTitleStyle(new FontStyle(45, getResources().getColor(R.color.white)));
            table.getConfig().setVerticalPadding(10);
            table.setTableData(tableData);
            table.invalidate();
        }
    }

    //在界面刷新之前保存旧数据
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("initial", answerE2E);
        outState.putSerializable("initial2", summary);
        outState.putString("initial3", expandTitle);
    }

    private String selectRadioBtn(RadioGroup rg) {
        RadioButton rb = E2E.this.findViewById(rg.getCheckedRadioButtonId());
        return rb.getText() + "";

    }


}
