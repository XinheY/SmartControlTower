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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
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
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.data.table.TableData;
import com.example.smartcontroltower.Info.FirstInfo;
import com.example.smartcontroltower.Info.ForthInfo;
import com.example.smartcontroltower.Info.SecondInfo;
import com.example.smartcontroltower.Info.ThirdInfo;
import com.google.android.material.navigation.NavigationView;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class DirectBL extends AppCompatActivity {

    private DrawerLayout drawerl;
    private ActionBarDrawerToggle toggle;
    public static MySmartTable<Object> table1, table2, table3, table4;
    private HashMap<String, HashMap<String, CheckBox>> summary = new HashMap<>();//所有checkbox的集合
    private HashMap<String, HashMap<String, CheckBox>> summaryOld = new HashMap<>();//之前所有checkbox的集合
    private ArrayList<String> allCondition = new ArrayList<>();
    private ArrayList<String[]> allContent = new ArrayList<>();
    private static ArrayList<LinkedHashMap<String, String>> answerDir;
    private InitializeInfo info = null;
    private Button YearBtn, QuarBtn, WeekBtn;
    private Button b1, b2, b3, b4;
    private int count = 0;
    private LoadingDialog ld;
    private RadioGroup diryear, dirquar, dirweek;
    private HashMap<String, ArrayList<RadioButton>> radioSummary = new LinkedHashMap<>();
    private HashMap<String, ArrayList<RadioButton>> radioOld = new LinkedHashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            answerDir = (ArrayList<LinkedHashMap<String, String>>) savedInstanceState.getSerializable("initial");
            summaryOld = (HashMap<String, HashMap<String, CheckBox>>) savedInstanceState.getSerializable("initial2");
            radioOld = (LinkedHashMap<String, ArrayList<RadioButton>>) savedInstanceState.getSerializable("initial3");

        } else {
            answerDir = new ArrayList<>();
        }
        ld = new LoadingDialog(this);
        ld.setLoadingText("Loading...").setSuccessText("Success").setFailedText("Failed")
                .closeSuccessAnim();

        setContentView(R.layout.activity_directbl);
        table1 = findViewById(R.id.dir_table1);
        table2 = findViewById(R.id.dir_table2);
        table3 = findViewById(R.id.dir_table3);
        table4 = findViewById(R.id.dir_table4);
        YearBtn = findViewById(R.id.dir_year_btn);
        QuarBtn = findViewById(R.id.dir_qr_btn);
        WeekBtn = findViewById(R.id.dir_wk_btn);
        b1 = findViewById(R.id.dir_b1);
        b2 = findViewById(R.id.dir_b2);
        b3 = findViewById(R.id.dir_b3);
        b4 = findViewById(R.id.dir_b4);

        info = (InitializeInfo) getIntent().getSerializableExtra("InitializeInfo");
        /////////////////Table//////////////////////////////////////////////////
        //设置初始值
        if (answerDir.size() != 0) {
            setNumber1();
            setNumber2();
            setNumber3();
            setNumber4();
        } else {
            test("EXEC [SP_CTO_DAILY_REPORT] '','',''");
        }

////////////////////////////////////////////left side///////////////////////////////////////////
        Toolbar toolbar = findViewById(R.id.dir_toolbar);
        setSupportActionBar(toolbar);

        drawerl = findViewById(R.id.dir_drawer);
        ActionBar actionb = getSupportActionBar();
        NavigationView nv = findViewById(R.id.dir_view);
        if (actionb != null) {
            actionb.setDisplayHomeAsUpEnabled(true);
            actionb.setHomeAsUpIndicator(R.drawable.menu);
        }
        nv.setCheckedItem(R.id.nav_DirectBL);
        Resources resource = getBaseContext().getResources();
        ColorStateList csl = resource.getColorStateList(R.color.nav_status_color);
        nv.setItemTextColor(csl);

        //界面切换
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_E2E:
                        Intent intent4 = new Intent(DirectBL.this, E2E.class);
                        Bundle bundle4 = new Bundle();
                        bundle4.putSerializable("InitializeInfo", info);
                        intent4.putExtras(bundle4);
                        startActivity(intent4);
                        finish();
                        break;
                    case R.id.nav_Dynamic:
                        Intent intent2 = new Intent(DirectBL.this, Dynamic.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("InitializeInfo", info);
                        intent2.putExtras(bundle2);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_DirectBL:
                        break;
                    case R.id.nav_analysis:
                        Intent intent3 = new Intent(DirectBL.this, Analysis.class);
                        Bundle bundle3 = new Bundle();
                        bundle3.putSerializable("InitializeInfo", info);
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
        final Button search = findViewById(R.id.dir_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRightSliding();
            }
        });

        ///////////////////////RadioBox列表/////////////////////////////////////////

        diryear = findViewById(R.id.dir_rg_year);
        String[] year = getResources().getStringArray(R.array.year);
        Log.e("year", year.toString());
        ConstructRadio("Year", year, diryear, "---");

        dirquar = findViewById(R.id.dir_rg_qr);
        String[] quar = getResources().getStringArray(R.array.quar);
        Log.e("quar", quar.toString());
        ConstructRadio("Quar", quar, dirquar, "---");

        dirweek = findViewById(R.id.dir_rg_wk);
        String[] week = getResources().getStringArray(R.array.week);
        Log.e("week", week.toString());
        ConstructRadio("Week", week, dirweek, "---");

        ////////////////////////Checkbox列表结束////////////////////////////////////

        Button refresh = findViewById(R.id.dir_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {//Refresh的动态监控
            @Override
            public void onClick(View view) {
                LinearLayout l1, l2, l3;
                toggleRightSliding();
                answerDir.clear();
                RadioButton y = findViewById(diryear.getCheckedRadioButtonId());
                RadioButton q = findViewById(dirquar.getCheckedRadioButtonId());
                RadioButton w = findViewById(dirweek.getCheckedRadioButtonId());
                Log.e("y/q/w", y.getText() + "/" + q.getText() + "/" + w.getText());
                if (!y.getText().toString().equals("---") && !q.getText().toString().equals("---") && !w.getText().toString().equals("---")) {
                    test("EXEC [SP_CTO_DAILY_REPORT] '" + y.getText().toString().replace("FY", "") +
                            "','" + q.getText().toString() + "','" + w.getText().toString().replace("WK", "") + "'");
                    Log.e("Notice", y.getText().toString().replace("FY", "") +
                            " " + q.getText().toString() + " " + w.getText().toString().replace("WK", ""));
                } else if (y.getText().toString().equals("---") && q.getText().toString().equals("---") && w.getText().toString().equals("---")) {
                    test("EXEC [SP_CTO_DAILY_REPORT] '','',''");
                } else {
                    Toast.makeText(view.getContext(), "Filter Condition Wrong", Toast.LENGTH_SHORT).show();
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

    /**
     * 右侧列表按钮监听：打开选项列表
     *
     * @param view
     */
    public void showMutilAlertDialog(View view) {
        LinearLayout ll = null;
        switch (view.getId()) {

            case R.id.dir_qr_btn:
                ll = findViewById(R.id.dir_qr);
                break;
            case R.id.dir_wk_btn:
                ll = findViewById(R.id.dir_wk);
                break;
            case R.id.dir_year_btn:
                ll = findViewById(R.id.dir_year);
                break;
            case R.id.dir_b1:
                if (table1.getVisibility() == View.VISIBLE) {
                    table1.setVisibility(View.GONE);
                } else {
                    table1.setVisibility(View.VISIBLE);
                }
                table2.setVisibility(View.GONE);
                table3.setVisibility(View.GONE);
                table4.setVisibility(View.GONE);
                break;
            case R.id.dir_b2:
                if (table2.getVisibility() == View.VISIBLE) {
                    table2.setVisibility(View.GONE);
                } else {
                    table2.setVisibility(View.VISIBLE);
                }
                table1.setVisibility(View.GONE);
                table3.setVisibility(View.GONE);
                table4.setVisibility(View.GONE);
                break;
            case R.id.dir_b3:
                if (table3.getVisibility() == View.VISIBLE) {
                    table3.setVisibility(View.GONE);
                } else {
                    table3.setVisibility(View.VISIBLE);
                }
                table2.setVisibility(View.GONE);
                table1.setVisibility(View.GONE);
                table4.setVisibility(View.GONE);
                break;
            case R.id.dir_b4:
                if (table4.getVisibility() == View.VISIBLE) {
                    table4.setVisibility(View.GONE);
                } else {
                    table4.setVisibility(View.VISIBLE);
                }
                table2.setVisibility(View.GONE);
                table3.setVisibility(View.GONE);
                table1.setVisibility(View.GONE);
                break;
            default:

        }
        if (ll != null) {
            if (ll.getVisibility() == View.VISIBLE) {

                ll.setVisibility(View.GONE);
            } else {
                ll.setVisibility(View.VISIBLE);
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 左侧菜单栏
     *
     * @param item
     * @return
     */
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
                answerDir = DBUtil.QuerySQL(sql);
                //Log.e("SQL",sql);
                //answerDirDir = DBUtil.sendRequestWithOkHttp();
                if (answerDir.size() != 0) {
                    setNumber1();
                    setNumber2();
                    setNumber3();
                    setNumber4();
                }
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
                    // ld.loadSuccess();
                    break;
                default:
                    break;
            }
        }
    };

    /////////////////////////////////////////////////////////////////////////////////////////

    public void ConstructRadio(String title, String[] items, RadioGroup rg, String ini) {
        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        allContent.add(items);
        for (int i = 0; i < items.length; i++) {
            RadioButton rb = new RadioButton(rg.getContext());
            rb.setText(items[i]);
            Log.e("construct", title + " : " + rb.getText());
            rb.setTextSize(18);
            rb.setId(count++);
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


    //在界面刷新之前保存旧数据
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("initial", answerDir);
        outState.putSerializable("initial2", summary);
        outState.putSerializable("initial3", radioSummary);
    }

    public static void setNumber1() {

        //表格数据 datas 是需要填充的数据
        List<Object> list = new ArrayList<>();
        final int[][] ColorGrid = new int[5][25];
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 25; y++) {
                ColorGrid[x][y] = 0;
            }
        }
        String[] title1 = {"Direct", "Client", "ISG System", "ISG Non-System", "ISG-Overall"};
        for (int i = 0; i < 5; i++) {
            FirstInfo firstInfo = new FirstInfo();
            firstInfo.setTitle(title1[i]);
            firstInfo.setEBL_GOAL0(answerDir.get(i).get("EBL_GOAL0"));
            firstInfo.setEBL0(answerDir.get(i).get("EBL0"));
            firstInfo.setUNIT0(answerDir.get(i).get("UNIT0"));
            firstInfo.setEBL_GOAL1(answerDir.get(i).get("EBL_GOAL1"));
            firstInfo.setEBL1(answerDir.get(i).get("EBL1"));
            firstInfo.setUNIT1(answerDir.get(i).get("UNIT1"));
            firstInfo.setEBL_GOAL2(answerDir.get(i).get("EBL_GOAL2"));
            firstInfo.setEBL2(answerDir.get(i).get("EBL2"));
            firstInfo.setUNIT2(answerDir.get(i).get("UNIT2"));
            firstInfo.setEBL_GOAL3(answerDir.get(i).get("EBL_GOAL3"));
            firstInfo.setEBL3(answerDir.get(i).get("EBL3"));
            firstInfo.setUNIT3(answerDir.get(i).get("UNIT3"));
            firstInfo.setEBL_GOAL4(answerDir.get(i).get("EBL_GOAL4"));
            firstInfo.setEBL4(answerDir.get(i).get("EBL4"));
            firstInfo.setUNIT4(answerDir.get(i).get("UNIT4"));
            firstInfo.setEBL_GOAL5(answerDir.get(i).get("EBL_GOAL5"));
            firstInfo.setEBL5(answerDir.get(i).get("EBL5"));
            firstInfo.setUNIT5(answerDir.get(i).get("UNIT5"));
            firstInfo.setEBL_GOAL6(answerDir.get(i).get("EBL_GOAL6"));
            firstInfo.setEBL6(answerDir.get(i).get("EBL6"));
            firstInfo.setUNIT6(answerDir.get(i).get("UNIT6"));
            firstInfo.setEBL_GOAL7(answerDir.get(i).get("EBL_GOAL7"));
            firstInfo.setEBL7(answerDir.get(i).get("EBL7"));
            firstInfo.setUNIT7(answerDir.get(i).get("UNIT7"));
            list.add(firstInfo);
        }
        table1.setData(list);

        final Column<String> TitleC = new Column<>("", "Title");
        final Column<String> Egoal0 = new Column<>("Days Goal", "EBL_GOAL0");
        final Column<String> E0 = new Column<>("Days Act", "EBL0");
        final Column<String> Unit0 = new Column<>("Unit Act", "UNIT0");
        Column group1 = new Column(answerDir.get(0).get("WEEK"), Egoal0, E0, Unit0);
        final Column<String> Egoal1 = new Column<>("Days Goal", "EBL_GOAL1");
        final Column<String> E1 = new Column<>("Days Act", "EBL1");
        final Column<String> Unit1 = new Column<>("Unit Act", "UNIT1");
        Column group2 = new Column("Sun", Egoal1, E1, Unit1);
        final Column<String> Egoal2 = new Column<>("Days Goal", "EBL_GOAL2");
        final Column<String> E2 = new Column<>("Days Act", "EBL2");
        final Column<String> Unit2 = new Column<>("Unit Act", "UNIT2");
        Column group3 = new Column("Mon", Egoal2, E2, Unit2);
        final Column<String> Egoal3 = new Column<>("Days Goal", "EBL_GOAL3");
        final Column<String> E3 = new Column<>("Days Act", "EBL3");
        final Column<String> Unit3 = new Column<>("Unit Act", "UNIT3");
        Column group4 = new Column("Tue", Egoal3, E3, Unit3);
        final Column<String> Egoal4 = new Column<>("Days Goal", "EBL_GOAL4");
        final Column<String> E4 = new Column<>("Days Act", "EBL4");
        final Column<String> Unit4 = new Column<>("Unit Act", "UNIT4");
        Column group5 = new Column("Wed", Egoal4, E4, Unit4);
        final Column<String> Egoal5 = new Column<>("Days Goal", "EBL_GOAL5");
        final Column<String> E5 = new Column<>("Days Act", "EBL5");
        final Column<String> Unit5 = new Column<>("Unit Act", "UNIT5");
        Column group6 = new Column("Thur", Egoal5, E5, Unit5);
        final Column<String> Egoal6 = new Column<>("Days Goal", "EBL_GOAL6");
        final Column<String> E6 = new Column<>("Days Act", "EBL6");
        final Column<String> Unit6 = new Column<>("Unit Act", "UNIT6");
        Column group7 = new Column("Fri", Egoal6, E6, Unit6);
        final Column<String> Egoal7 = new Column<>("Days Goal", "EBL_GOAL7");
        final Column<String> E7 = new Column<>("Days Act", "EBL7");
        final Column<String> Unit7 = new Column<>("Unit Act", "UNIT7");
        Column group8 = new Column("Sat", Egoal7, E7, Unit7);

        for (int j = 0; j < 5; j++) {
            for (int h = 0; h < 8; h++) {
                if (!answerDir.get(j).get("EBL" + h).equals("-") && !answerDir.get(j).get("EBL_GOAL" + h).equals("-")
                        && !answerDir.get(j).get("UNIT" + h).equals("-") && !answerDir.get(j).get("UNIT_GOAL" + h).equals("-")) {
                    double ebl = Double.parseDouble(answerDir.get(j).get("EBL" + h));
                    double eblgoal = Double.parseDouble(answerDir.get(j).get("EBL_GOAL" + h));
                    double unit = Double.parseDouble(answerDir.get(j).get("UNIT" + h));
                    double unitgoal = Double.parseDouble(answerDir.get(j).get("UNIT_GOAL" + h));

                    if (ebl > eblgoal) {
                        if (ebl > eblgoal * 1.1) {
                            ColorGrid[j][h * 3 + 2] = 2;
                        } else {
                            ColorGrid[j][h * 3 + 2] = 1;
                        }
                    }
                    if (unit > unitgoal) {
                        if (unit > unitgoal * 1.1) {
                            ColorGrid[j][h * 3 + 3] = 2;
                        } else {
                            ColorGrid[j][h * 3 + 3] = 1;
                        }
                    }
                }
            }
        }
//        for(int xx=0;xx<5;xx++){
//            for(int yy=0;yy<24;yy++){
//                System.out.print(ColorGrid[xx][yy]+" ");
//            }
//            System.out.println();
//        }

        TableData tableData = new TableData<>("ISG Backlog Break down", list, TitleC, group1, group2, group3, group4, group5, group6, group7, group8);
        table1.getConfig().setFixedTitle(true);
        TitleC.setFixed(true);
        table1.setZoom(true, 2, 1);
        table1.getConfig().setShowXSequence(false);
        table1.getConfig().setShowYSequence(false);
        table1.getConfig().setTableTitleStyle(new FontStyle(50, Color.rgb(115, 135, 156)));
        table1.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(Color.rgb(115, 135, 156)));
        table1.getConfig().setContentStyle(new FontStyle(45, Color.BLACK));
        table1.getConfig().setVerticalPadding(40);
        table1.getConfig().setColumnTitleStyle(new FontStyle(45, Color.WHITE));

        table1.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {
            @Override
            public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                Log.e("位置", cellInfo.row + "," + cellInfo.col);
                if ((cellInfo.col % 3 == 2 || cellInfo.col % 3 == 0) && cellInfo.col != 0) {
                    paint.setColor(Color.GREEN);
                    canvas.drawRect(rect, paint);
                }
                if (ColorGrid[cellInfo.row][cellInfo.col] == 2) {
                    paint.setColor(Color.RED);
                    canvas.drawRect(rect, paint);
                } else if (ColorGrid[cellInfo.row][cellInfo.col] == 1) {
                    paint.setColor(Color.YELLOW);
                    canvas.drawRect(rect, paint);
                }
            }

            @Override
            public int getTextColor(CellInfo cellInfo) {
//                if (cellInfo.row % 4 == 2) {
//                    return Color.WHITE;
//                }
                return 0;
            }
        });

        table1.setTableData(tableData);
        table1.invalidate();
    }

    public static void setNumber2() {


        //表格数据 datas 是需要填充的数据
        List<Object> list = new ArrayList<>();
        final int[][] ColorGrid2 = new int[12][26];
        for (int x = 0; x < 12; x++) {
            for (int y = 0; y < 26; y++) {
                ColorGrid2[x][y] = 0;
            }
        }

        String[] title1 = {"OPR", "STBL", "E-Mgt/EOL", "Invalid", "APCC", "CCC", "ICC", "APCC", "CCC", "ICC", "APCC", "CCC", "ICC"};
        String[] title2 = {"Un-\nShippable BL", "MFG BL(Overall)", "MFG BL(System)", "MFG BL(Non - System)"};
        for (int i = 5; i < 17; i++) {
            SecondInfo secondInfo = new SecondInfo();
            if (i >= 5 && i < 9) secondInfo.setTitle(title2[0]);
            else if (i >= 9 && i < 12) secondInfo.setTitle(title2[1]);
            else if (i >= 12 && i < 15) secondInfo.setTitle(title2[2]);
            else if (i >= 15) secondInfo.setTitle(title2[3]);
            secondInfo.setFactory(title1[i - 5]);
            secondInfo.setEBL_GOAL0(answerDir.get(i).get("EBL_GOAL0"));
            secondInfo.setEBL0(answerDir.get(i).get("EBL0"));
            secondInfo.setUNIT0(answerDir.get(i).get("UNIT0"));
            secondInfo.setEBL_GOAL1(answerDir.get(i).get("EBL_GOAL1"));
            secondInfo.setEBL1(answerDir.get(i).get("EBL1"));
            secondInfo.setUNIT1(answerDir.get(i).get("UNIT1"));
            secondInfo.setEBL_GOAL2(answerDir.get(i).get("EBL_GOAL2"));
            secondInfo.setEBL2(answerDir.get(i).get("EBL2"));
            secondInfo.setUNIT2(answerDir.get(i).get("UNIT2"));
            secondInfo.setEBL_GOAL3(answerDir.get(i).get("EBL_GOAL3"));
            secondInfo.setEBL3(answerDir.get(i).get("EBL3"));
            secondInfo.setUNIT3(answerDir.get(i).get("UNIT3"));
            secondInfo.setEBL_GOAL4(answerDir.get(i).get("EBL_GOAL4"));
            secondInfo.setEBL4(answerDir.get(i).get("EBL4"));
            secondInfo.setUNIT4(answerDir.get(i).get("UNIT4"));
            secondInfo.setEBL_GOAL5(answerDir.get(i).get("EBL_GOAL5"));
            secondInfo.setEBL5(answerDir.get(i).get("EBL5"));
            secondInfo.setUNIT5(answerDir.get(i).get("UNIT5"));
            secondInfo.setEBL_GOAL6(answerDir.get(i).get("EBL_GOAL6"));
            secondInfo.setEBL6(answerDir.get(i).get("EBL6"));
            secondInfo.setUNIT6(answerDir.get(i).get("UNIT6"));
            secondInfo.setEBL_GOAL7(answerDir.get(i).get("EBL_GOAL7"));
            secondInfo.setEBL7(answerDir.get(i).get("EBL7"));
            secondInfo.setUNIT7(answerDir.get(i).get("UNIT7"));
            list.add(secondInfo);
        }
        table2.setData(list);

        for (int j = 5; j < 17; j++) {
            for (int h = 0; h < 8; h++) {
                if (!answerDir.get(j).get("EBL" + h).equals("-") && !answerDir.get(j).get("EBL_GOAL" + h).equals("-")) {
                    double ebl = Double.parseDouble(answerDir.get(j).get("EBL" + h));
                    double eblgoal = Double.parseDouble(answerDir.get(j).get("EBL_GOAL" + h));
                    if (ebl > eblgoal) {
                        if (ebl > eblgoal * 1.1) {
                            ColorGrid2[j - 5][h * 3 + 3] = 2;
                        } else {
                            ColorGrid2[j - 5][h * 3 + 3] = 1;
                        }
                    }
                }
                if (!answerDir.get(j).get("UNIT" + h).equals("-") && !answerDir.get(j).get("UNIT_GOAL" + h).equals("-")) {
                    double unit = Double.parseDouble(answerDir.get(j).get("UNIT" + h));
                    double unitgoal = Double.parseDouble(answerDir.get(j).get("UNIT_GOAL" + h));
                    if (unit > unitgoal) {
                        if (unit > unitgoal * 1.1) {
                            ColorGrid2[j - 5][h * 3 + 4] = 2;
                        } else {
                            ColorGrid2[j - 5][h * 3 + 4] = 1;
                        }
                    }
                }
            }
        }

        final Column<String> TitleC = new Column<>("", "Title");
        TitleC.setAutoMerge(true);
        TitleC.setWidth(200);
        final Column<String> Fac = new Column<>("", "Factory");
        final Column<String> Egoal0 = new Column<>("Days\nGoal", "EBL_GOAL0");
        final Column<String> E0 = new Column<>("Days\nAct", "EBL0");
        final Column<String> Unit0 = new Column<>("Unit\nAct", "UNIT0");
        Column group1 = new Column(answerDir.get(0).get("WEEK"), Egoal0, E0, Unit0);
        final Column<String> Egoal1 = new Column<>("Days\nGoal", "EBL_GOAL1");
        final Column<String> E1 = new Column<>("Days\nAct", "EBL1");
        final Column<String> Unit1 = new Column<>("Unit\nAct", "UNIT1");
        Column group2 = new Column("Sun", Egoal1, E1, Unit1);
        final Column<String> Egoal2 = new Column<>("Days\nGoal", "EBL_GOAL2");
        final Column<String> E2 = new Column<>("Days\nAct", "EBL2");
        final Column<String> Unit2 = new Column<>("Unit\nAct", "UNIT2");
        Column group3 = new Column("Mon", Egoal2, E2, Unit2);
        final Column<String> Egoal3 = new Column<>("Days Goal", "EBL_GOAL3");
        final Column<String> E3 = new Column<>("Days Act", "EBL3");
        final Column<String> Unit3 = new Column<>("Unit Act", "UNIT3");
        Column group4 = new Column("Tue", Egoal3, E3, Unit3);
        final Column<String> Egoal4 = new Column<>("Days Goal", "EBL_GOAL4");
        final Column<String> E4 = new Column<>("Days Act", "EBL4");
        final Column<String> Unit4 = new Column<>("Unit Act", "UNIT4");
        Column group5 = new Column("Wed", Egoal4, E4, Unit4);
        final Column<String> Egoal5 = new Column<>("Days Goal", "EBL_GOAL5");
        final Column<String> E5 = new Column<>("Days Act", "EBL5");
        final Column<String> Unit5 = new Column<>("Unit Act", "UNIT5");
        Column group6 = new Column("Thur", Egoal5, E5, Unit5);
        final Column<String> Egoal6 = new Column<>("Days Goal", "EBL_GOAL6");
        final Column<String> E6 = new Column<>("Days Act", "EBL6");
        final Column<String> Unit6 = new Column<>("Unit Act", "UNIT6");
        Column group7 = new Column("Fri", Egoal6, E6, Unit6);
        final Column<String> Egoal7 = new Column<>("Days Goal", "EBL_GOAL7");
        final Column<String> E7 = new Column<>("Days Act", "EBL7");
        final Column<String> Unit7 = new Column<>("Unit Act", "UNIT7");
        Column group8 = new Column("Sat", Egoal7, E7, Unit7);

        TableData tableData = new TableData<>("Direct backlog break down", list, TitleC, Fac, group1, group2, group3, group4, group5, group6, group7, group8);
        table2.getConfig().setFixedTitle(true);
        TitleC.setFixed(true);
        Fac.setFixed(true);
        table2.setZoom(true, 2, 1);
        table2.getConfig().setShowXSequence(false);
        table2.getConfig().setShowYSequence(false);
        table2.getConfig().setTableTitleStyle(new FontStyle(50, Color.rgb(115, 135, 156)));
        table2.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(Color.rgb(115, 135, 156)));
        table2.getConfig().setContentStyle(new FontStyle(45, Color.BLACK));
        table2.getConfig().setVerticalPadding(40);
        table2.getConfig().setColumnTitleStyle(new FontStyle(45, Color.WHITE));

        table2.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {
            @Override
            public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                if ((cellInfo.col % 3 == 0 || cellInfo.col % 3 == 1) && cellInfo.col != 1 && cellInfo.col != 0) {
                    paint.setColor(Color.GREEN);
                    canvas.drawRect(rect, paint);
                }
                if (ColorGrid2[cellInfo.row][cellInfo.col] == 2) {
                    paint.setColor(Color.RED);
                    canvas.drawRect(rect, paint);
                } else if (ColorGrid2[cellInfo.row][cellInfo.col] == 1) {
                    paint.setColor(Color.YELLOW);
                    canvas.drawRect(rect, paint);
                }
            }

            @Override
            public int getTextColor(CellInfo cellInfo) {
                return 0;
            }
        });

        table2.setTableData(tableData);
        table2.invalidate();
    }

    public static void setNumber3() {


        //表格数据 datas 是需要填充的数据
        List<Object> list = new ArrayList<>();
        final int[][] ColorGrid3 = new int[12][26];
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 26; y++) {
                ColorGrid3[x][y] = 0;
            }
        }

        String[] title1 = {"OPR", "STBL", "E-Mgt/EOL", "Invalid", "APCC", "CCC", "ICC"};
        String[] title2 = {"Un-\nShippable BL", "MFG BL"};
        for (int i = 18; i < 25; i++) {
            ThirdInfo firstInfo = new ThirdInfo();
            firstInfo.setFactory(title1[i - 18]);
            if (i >= 18 && i < 22) firstInfo.setTitle(title2[0]);
            else if (i >= 22 && i < 25) firstInfo.setTitle(title2[1]);
            firstInfo.setEBL_GOAL0(answerDir.get(i).get("EBL_GOAL0"));
            firstInfo.setEBL0(answerDir.get(i).get("EBL0"));
            firstInfo.setUNIT0(answerDir.get(i).get("UNIT0"));
            firstInfo.setEBL_GOAL1(answerDir.get(i).get("EBL_GOAL1"));
            firstInfo.setEBL1(answerDir.get(i).get("EBL1"));
            firstInfo.setUNIT1(answerDir.get(i).get("UNIT1"));
            firstInfo.setEBL_GOAL2(answerDir.get(i).get("EBL_GOAL2"));
            firstInfo.setEBL2(answerDir.get(i).get("EBL2"));
            firstInfo.setUNIT2(answerDir.get(i).get("UNIT2"));
            firstInfo.setEBL_GOAL3(answerDir.get(i).get("EBL_GOAL3"));
            firstInfo.setEBL3(answerDir.get(i).get("EBL3"));
            firstInfo.setUNIT3(answerDir.get(i).get("UNIT3"));
            firstInfo.setEBL_GOAL4(answerDir.get(i).get("EBL_GOAL4"));
            firstInfo.setEBL4(answerDir.get(i).get("EBL4"));
            firstInfo.setUNIT4(answerDir.get(i).get("UNIT4"));
            firstInfo.setEBL_GOAL5(answerDir.get(i).get("EBL_GOAL5"));
            firstInfo.setEBL5(answerDir.get(i).get("EBL5"));
            firstInfo.setUNIT5(answerDir.get(i).get("UNIT5"));
            firstInfo.setEBL_GOAL6(answerDir.get(i).get("EBL_GOAL6"));
            firstInfo.setEBL6(answerDir.get(i).get("EBL6"));
            firstInfo.setUNIT6(answerDir.get(i).get("UNIT6"));
            firstInfo.setEBL_GOAL7(answerDir.get(i).get("EBL_GOAL7"));
            firstInfo.setEBL7(answerDir.get(i).get("EBL7"));
            firstInfo.setUNIT7(answerDir.get(i).get("UNIT7"));
            list.add(firstInfo);
        }
        table3.setData(list);

        for (int j = 18; j <25 ; j++) {
            Log.e("数据",answerDir.get(j).toString());
            for (int h = 0; h < 8; h++) {
                if (!answerDir.get(j).get("EBL" + h).equals("-") && !answerDir.get(j).get("EBL_GOAL" + h).equals("-")) {
                    double ebl = Double.parseDouble(answerDir.get(j).get("EBL" + h));
                    double eblgoal = Double.parseDouble(answerDir.get(j).get("EBL_GOAL" + h));
                    if (ebl > eblgoal) {
                        if (ebl > eblgoal * 1.1) {
                            ColorGrid3[j - 18][h * 3 + 3] = 2;
                        } else {
                            ColorGrid3[j - 18][h * 3 + 3] = 1;
                        }
                    }
                }
                if (!answerDir.get(j).get("UNIT" + h).equals("-") && !answerDir.get(j).get("UNIT_GOAL" + h).equals("-")) {
                    double unit = Double.parseDouble(answerDir.get(j).get("UNIT" + h));
                    double unitgoal = Double.parseDouble(answerDir.get(j).get("UNIT_GOAL" + h));
                    if (unit > unitgoal) {
                        if (unit > unitgoal * 1.1) {
                            ColorGrid3[j - 18][h * 3 + 4] = 2;
                        } else {
                            ColorGrid3[j - 18][h * 3 + 4] = 1;
                        }
                    }
                }
            }
        }

        final Column<String> TitleC = new Column<>("", "Title");
        TitleC.setAutoMerge(true);
        TitleC.setWidth(200);
        final Column<String> Fac = new Column<>("", "Factory");
        final Column<String> Egoal0 = new Column<>("Days Goal", "EBL_GOAL0");
        final Column<String> E0 = new Column<>("Days Act", "EBL0");
        final Column<String> Unit0 = new Column<>("Unit Act", "UNIT0");
        Column group1 = new Column(answerDir.get(0).get("WEEK"), Egoal0, E0, Unit0);
        final Column<String> Egoal1 = new Column<>("Days Goal", "EBL_GOAL1");
        final Column<String> E1 = new Column<>("Days Act", "EBL1");
        final Column<String> Unit1 = new Column<>("Unit Act", "UNIT1");
        Column group2 = new Column("Sun", Egoal1, E1, Unit1);
        final Column<String> Egoal2 = new Column<>("Days Goal", "EBL_GOAL2");
        final Column<String> E2 = new Column<>("Days Act", "EBL2");
        final Column<String> Unit2 = new Column<>("Unit Act", "UNIT2");
        Column group3 = new Column("Mon", Egoal2, E2, Unit2);
        final Column<String> Egoal3 = new Column<>("Days Goal", "EBL_GOAL3");
        final Column<String> E3 = new Column<>("Days Act", "EBL3");
        final Column<String> Unit3 = new Column<>("Unit Act", "UNIT3");
        Column group4 = new Column("Tue", Egoal3, E3, Unit3);
        final Column<String> Egoal4 = new Column<>("Days Goal", "EBL_GOAL4");
        final Column<String> E4 = new Column<>("Days Act", "EBL4");
        final Column<String> Unit4 = new Column<>("Unit Act", "UNIT4");
        Column group5 = new Column("Wed", Egoal4, E4, Unit4);
        final Column<String> Egoal5 = new Column<>("Days Goal", "EBL_GOAL5");
        final Column<String> E5 = new Column<>("Days Act", "EBL5");
        final Column<String> Unit5 = new Column<>("Unit Act", "UNIT5");
        Column group6 = new Column("Thur", Egoal5, E5, Unit5);
        final Column<String> Egoal6 = new Column<>("Days Goal", "EBL_GOAL6");
        final Column<String> E6 = new Column<>("Days Act", "EBL6");
        final Column<String> Unit6 = new Column<>("Unit Act", "UNIT6");
        Column group7 = new Column("Fri", Egoal6, E6, Unit6);
        final Column<String> Egoal7 = new Column<>("Days Goal", "EBL_GOAL7");
        final Column<String> E7 = new Column<>("Days Act", "EBL7");
        final Column<String> Unit7 = new Column<>("Unit Act", "UNIT7");
        Column group8 = new Column("Sat", Egoal7, E7, Unit7);

        TableData tableData = new TableData<>("Client Backlog Break down", list, TitleC, Fac, group1, group2, group3, group4, group5, group6, group7, group8);
        table3.getConfig().setFixedTitle(true);
        TitleC.setFixed(true);
        Fac.setFixed(true);
        table3.setZoom(true, 2, 1);
        table3.getConfig().setShowXSequence(false);
        table3.getConfig().setShowYSequence(false);
        table3.getConfig().setTableTitleStyle(new FontStyle(50, Color.rgb(115, 135, 156)));
        table3.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(Color.rgb(115, 135, 156)));
        table3.getConfig().setContentStyle(new FontStyle(45, Color.BLACK));
        table3.getConfig().setVerticalPadding(40);
        table3.getConfig().setColumnTitleStyle(new FontStyle(45, Color.WHITE));

        table3.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {
            @Override
            public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                if ((cellInfo.col % 3 == 0 || cellInfo.col % 3 == 1) && cellInfo.col != 1 && cellInfo.col != 0) {
                    paint.setColor(Color.GREEN);
                    canvas.drawRect(rect, paint);
                }
                if (ColorGrid3[cellInfo.row][cellInfo.col] == 2) {
                    paint.setColor(Color.RED);
                    canvas.drawRect(rect, paint);
                } else if (ColorGrid3[cellInfo.row][cellInfo.col] == 1) {
                    paint.setColor(Color.YELLOW);
                    canvas.drawRect(rect, paint);
                }
            }

            @Override
            public int getTextColor(CellInfo cellInfo) {
                return 0;
            }
        });

        table3.setTableData(tableData);
        table3.invalidate();
    }

    public static void setNumber4() {


//        //表格数据 datas 是需要填充的数据
        List<Object> list = new ArrayList<>();
        final int[][] ColorGrid4 = new int[12][26];
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 26; y++) {
                ColorGrid4[x][y] = 0;
            }
        }

        String[] title1 = {"OPR", "STBL", "E-Mgt/EOL", "GPT", "Illegal", "IT issue", "CL/Re-cut", "EGH", "APCC", "CCC", "ICC", "ODM"};
        String[] title2 = {"Un-\nShippable BL", "MFG BL"};
        for (int i = 25; i < answerDir.size(); i++) {
            ForthInfo firstInfo = new ForthInfo();
            firstInfo.setFactory(title1[i - 25]);
            if (i >= 25 && i < 33) firstInfo.setTitle(title2[0]);
            else if (i >= 32) firstInfo.setTitle(title2[1]);
            firstInfo.setEBL_GOAL0(answerDir.get(i).get("EBL_GOAL0"));
            firstInfo.setEBL0(answerDir.get(i).get("EBL0"));
            firstInfo.setUNIT0(answerDir.get(i).get("UNIT0"));
            firstInfo.setEBL_GOAL1(answerDir.get(i).get("EBL_GOAL1"));
            firstInfo.setEBL1(answerDir.get(i).get("EBL1"));
            firstInfo.setUNIT1(answerDir.get(i).get("UNIT1"));
            firstInfo.setEBL_GOAL2(answerDir.get(i).get("EBL_GOAL2"));
            firstInfo.setEBL2(answerDir.get(i).get("EBL2"));
            firstInfo.setUNIT2(answerDir.get(i).get("UNIT2"));
            firstInfo.setEBL_GOAL3(answerDir.get(i).get("EBL_GOAL3"));
            firstInfo.setEBL3(answerDir.get(i).get("EBL3"));
            firstInfo.setUNIT3(answerDir.get(i).get("UNIT3"));
            firstInfo.setEBL_GOAL4(answerDir.get(i).get("EBL_GOAL4"));
            firstInfo.setEBL4(answerDir.get(i).get("EBL4"));
            firstInfo.setUNIT4(answerDir.get(i).get("UNIT4"));
            firstInfo.setEBL_GOAL5(answerDir.get(i).get("EBL_GOAL5"));
            firstInfo.setEBL5(answerDir.get(i).get("EBL5"));
            firstInfo.setUNIT5(answerDir.get(i).get("UNIT5"));
            firstInfo.setEBL_GOAL6(answerDir.get(i).get("EBL_GOAL6"));
            firstInfo.setEBL6(answerDir.get(i).get("EBL6"));
            firstInfo.setUNIT6(answerDir.get(i).get("UNIT6"));
            firstInfo.setEBL_GOAL7(answerDir.get(i).get("EBL_GOAL7"));
            firstInfo.setEBL7(answerDir.get(i).get("EBL7"));
            firstInfo.setUNIT7(answerDir.get(i).get("UNIT7"));
            list.add(firstInfo);
        }
        table4.setData(list);

        for (int j = 25; j <37 ; j++) {
            Log.e("数据",answerDir.get(j).toString());
            for (int h = 0; h < 8; h++) {
                if (!answerDir.get(j).get("EBL" + h).equals("-") && !answerDir.get(j).get("EBL_GOAL" + h).equals("-")) {
                    double ebl = Double.parseDouble(answerDir.get(j).get("EBL" + h));
                    double eblgoal = Double.parseDouble(answerDir.get(j).get("EBL_GOAL" + h));
                    if (ebl > eblgoal) {
                        if (ebl > eblgoal * 1.1) {
                            ColorGrid4[j - 25][h * 3 + 3] = 2;
                        } else {
                            ColorGrid4[j - 25][h * 3 + 3] = 1;
                        }
                    }
                }
                if (!answerDir.get(j).get("UNIT" + h).equals("-") && !answerDir.get(j).get("UNIT_GOAL" + h).equals("-")) {
                    double unit = Double.parseDouble(answerDir.get(j).get("UNIT" + h));
                    double unitgoal = Double.parseDouble(answerDir.get(j).get("UNIT_GOAL" + h));
                    if (unit > unitgoal) {
                        if (unit > unitgoal * 1.1) {
                            ColorGrid4[j - 25][h * 3 + 4] = 2;
                        } else {
                            ColorGrid4[j - 25][h * 3 + 4] = 1;
                        }
                    }
                }
            }
        }

        final Column<String> TitleC = new Column<>("", "Title");
        TitleC.setAutoMerge(true);
        TitleC.setWidth(200);
        final Column<String> Fac = new Column<>("", "Factory");
        final Column<String> Egoal0 = new Column<>("Days Goal", "EBL_GOAL0");
        final Column<String> E0 = new Column<>("Days Act", "EBL0");
        final Column<String> Unit0 = new Column<>("Unit Act", "UNIT0");
        Column group1 = new Column(answerDir.get(0).get("WEEK"), Egoal0, E0, Unit0);
        final Column<String> Egoal1 = new Column<>("Days Goal", "EBL_GOAL1");
        final Column<String> E1 = new Column<>("Days Act", "EBL1");
        final Column<String> Unit1 = new Column<>("Unit Act", "UNIT1");
        Column group2 = new Column("Sun", Egoal1, E1, Unit1);
        final Column<String> Egoal2 = new Column<>("Days Goal", "EBL_GOAL2");
        final Column<String> E2 = new Column<>("Days Act", "EBL2");
        final Column<String> Unit2 = new Column<>("Unit Act", "UNIT2");
        Column group3 = new Column("Mon", Egoal2, E2, Unit2);
        final Column<String> Egoal3 = new Column<>("Days Goal", "EBL_GOAL3");
        final Column<String> E3 = new Column<>("Days Act", "EBL3");
        final Column<String> Unit3 = new Column<>("Unit Act", "UNIT3");
        Column group4 = new Column("Tue", Egoal3, E3, Unit3);
        final Column<String> Egoal4 = new Column<>("Days Goal", "EBL_GOAL4");
        final Column<String> E4 = new Column<>("Days Act", "EBL4");
        final Column<String> Unit4 = new Column<>("Unit Act", "UNIT4");
        Column group5 = new Column("Wed", Egoal4, E4, Unit4);
        final Column<String> Egoal5 = new Column<>("Days Goal", "EBL_GOAL5");
        final Column<String> E5 = new Column<>("Days Act", "EBL5");
        final Column<String> Unit5 = new Column<>("Unit Act", "UNIT5");
        Column group6 = new Column("Thur", Egoal5, E5, Unit5);
        final Column<String> Egoal6 = new Column<>("Days Goal", "EBL_GOAL6");
        final Column<String> E6 = new Column<>("Days Act", "EBL6");
        final Column<String> Unit6 = new Column<>("Unit Act", "UNIT6");
        Column group7 = new Column("Fri", Egoal6, E6, Unit6);
        final Column<String> Egoal7 = new Column<>("Days Goal", "EBL_GOAL7");
        final Column<String> E7 = new Column<>("Days Act", "EBL7");
        final Column<String> Unit7 = new Column<>("Unit Act", "UNIT7");
        Column group8 = new Column("Sat", Egoal7, E7, Unit7);

        TableData tableData = new TableData<>("", list, TitleC, Fac, group1, group2, group3, group4, group5, group6, group7, group8);
        table4.getConfig().setFixedTitle(true);
        TitleC.setFixed(true);
        Fac.setFixed(true);
        table4.setZoom(true, 2, 1);
        table4.getConfig().setShowXSequence(false);
        table4.getConfig().setShowYSequence(false);
        table4.getConfig().setTableTitleStyle(new FontStyle(50, Color.rgb(115, 135, 156)));
        table4.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(Color.rgb(115, 135, 156)));
        table4.getConfig().setContentStyle(new FontStyle(45, Color.BLACK));
        table4.getConfig().setVerticalPadding(40);
        table4.getConfig().setColumnTitleStyle(new FontStyle(45, Color.WHITE));

        table4.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {
            @Override
            public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                if ((cellInfo.col % 3 == 0 || cellInfo.col % 3 == 1) && cellInfo.col != 1 && cellInfo.col != 0) {
                    paint.setColor(Color.GREEN);
                    canvas.drawRect(rect, paint);
                }
                if (ColorGrid4[cellInfo.row][cellInfo.col] == 2) {
                    paint.setColor(Color.RED);
                    canvas.drawRect(rect, paint);
                } else if (ColorGrid4[cellInfo.row][cellInfo.col] == 1) {
                    paint.setColor(Color.YELLOW);
                    canvas.drawRect(rect, paint);
                }
            }

            @Override
            public int getTextColor(CellInfo cellInfo) {
                return 0;
            }
        });

        table4.setTableData(tableData);
        table4.invalidate();
    }


}
