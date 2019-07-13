package com.example.smartcontroltower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.google.android.material.navigation.NavigationView;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class E2E extends AppCompatActivity {

    private DrawerLayout drawerl;
    private ActionBarDrawerToggle toggle;
    private SmartTable<Object> table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e2_e);

        Toolbar toolbar = findViewById(R.id.e2e_toolbar);
        setSupportActionBar(toolbar);

        /////////////////Table//////////////////////////////////////////////////

        table = findViewById(R.id.table);

        //表格数据 datas 是需要填充的数据
        List<Object> maplist = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Map<String, String> a = new LinkedHashMap<>();
            a.put("city", "Shenyang");
            a.put("name", "Peter");
            a.put("no", "12");
            a.put("count", "2");
            a.put("no12", "1222");
            a.put("count2", "2cc");
            Map<String, String> b = new LinkedHashMap<>();
            b.put("city", "Xiamen");
            b.put("name", "Jason");
            b.put("no", "12");
            b.put("count", "2");
            b.put("no12", "1233");
            b.put("count2", "2ff");
            maplist.add(a);
            maplist.add(b);
        }

        MapTableData tableData = MapTableData.create("E2E OLK Version", maplist);
        Column groupColumn = new Column("组合", tableData.getColumns().get(2), tableData.getColumns().get(3));
        table.getConfig().setFixedTitle(true);//first row
        tableData.getColumns().get(0).setFixed(true);//first column
        table.setZoom(true, 2, 1);//zoom in & out
        table.getConfig().setShowXSequence(false);//hide
        table.getConfig().setShowYSequence(false);//hide
//设置数据
        table.setTableData(tableData);
//        table.getConfig().setContentStyle(new FontStyle(50, Color.BLUE));
        table.getConfig().setColumnTitleStyle(new FontStyle(35, Color.DKGRAY));
        table.getConfig().setTableTitleStyle(new FontStyle(43,Color.rgb(0,0,0)));

        //////////////////////////////////drop-down list//////////////////////////////////////
        NiceSpinner niceSpinner = findViewById(R.id.nice_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("QuarView", "VersionView"));
        niceSpinner.attachDataSource(dataset);

        niceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                Toast.makeText(E2E.this, item, Toast.LENGTH_SHORT).show();
            }
        });
////////////////////////////////////////////left side///////////////////////////////////////////
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

        toggle = new ActionBarDrawerToggle(this, drawerl,
                toolbar, R.string.draw_open, R.string.draw_close) {
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);//抽屉关闭后
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);//抽屉打开后
            }
        };
//

        Button search = findViewById(R.id.e2e_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRightSliding();
            }
        });


        //Lob
        LinearLayout e2elob = findViewById(R.id.e2e_lob);
        String[] lob = getResources().getStringArray(R.array.Lob);
        for (int i = 0; i < lob.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(lob[i]);
            cb.setTextSize(15);
            e2elob.addView(cb);

        }
        //System Flag
        LinearLayout e2esf = findViewById(R.id.e2e_sf);
        String[] sf = getResources().getStringArray(R.array.system_flag);
        for (int i = 0; i < sf.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(sf[i]);
            cb.setTextSize(15);
            e2esf.addView(cb);
        }

        //Product Type
        LinearLayout e2ept = findViewById(R.id.e2e_pt);
        String[] pt = getResources().getStringArray(R.array.Product_Type);
        for (int i = 0; i < pt.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(pt[i]);
            cb.setTextSize(15);
            e2ept.addView(cb);

        }
        //Lob Group
        LinearLayout e2elg = findViewById(R.id.e2e_lg);
        String[] lg = getResources().getStringArray(R.array.LOB_Group);
        for (int i = 0; i < lg.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(lg[i]);
            cb.setTextSize(15);
            e2elg.addView(cb);

        }
        //Region
        LinearLayout e2eregion = findViewById(R.id.e2e_region);
        String[] region = getResources().getStringArray(R.array.Region);
        for (int i = 0; i < region.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(region[i]);
            cb.setTextSize(15);
            e2eregion.addView(cb);

        }
        //Segment
        LinearLayout e2esegment = findViewById(R.id.e2e_segment);
        String[] segment = getResources().getStringArray(R.array.Segment);
        for (int i = 0; i < segment.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(segment[i]);
            cb.setTextSize(15);
            e2esegment.addView(cb);

        }
        //Site
        LinearLayout e2esite = findViewById(R.id.e2e_site);
        String[] site = getResources().getStringArray(R.array.Site);
        for (int i = 0; i < site.length; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(site[i]);
            cb.setTextSize(15);
            e2esite.addView(cb);

        }

        Button refresh=findViewById(R.id.e2e_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Object> maplist2 = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    Map<String, String> a = new LinkedHashMap<>();
                    a.put("city", "Zhangzhou");
                    a.put("name", "Cindy");
                    a.put("no", "12");
                    a.put("count", "2");
                    a.put("no12", "1222");
                    a.put("count2", "2cc");
                    Map<String, String> b = new LinkedHashMap<>();
                    b.put("city", "Beijing");
                    b.put("name", "Helen");
                    b.put("no", "12");
                    b.put("count", "2");
                    b.put("no12", "1233");
                    b.put("count2", "2ff");
                    maplist2.add(a);
                    maplist2.add(b);
                }

                MapTableData tableData = MapTableData.create("E2E OLK Version", maplist2);
                //Column groupColumn = new Column("组合", tableData.getColumns().get(2), tableData.getColumns().get(3));
                tableData.getColumns().get(0).setFixed(true);//first column
//设置数据
                table.setTableData(tableData);
//        table.getConfig().setContentStyle(new FontStyle(50, Color.BLUE));
                table.getConfig().setColumnTitleStyle(new FontStyle(35, Color.DKGRAY));
                table.getConfig().setTableTitleStyle(new FontStyle(43,Color.rgb(0,0,0)));
                toggleRightSliding();
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////
    }

    //手动控制抽屉开关的
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
}
