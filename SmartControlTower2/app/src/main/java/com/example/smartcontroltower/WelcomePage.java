package com.example.smartcontroltower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class WelcomePage extends AppCompatActivity {

    private DrawerLayout drawerl;
    private InitializeInfo info = null;//初始化EOQ 数据
    private InitializeInfo info2 = null;//初始化IDC 数据
    private int[] AccessRight = new int[4];//存储该用户权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        //从MainPage继承数据
        info = (InitializeInfo) getIntent().getSerializableExtra("InitializeInfo");
        info2 = (InitializeInfo) getIntent().getSerializableExtra("InitializeInfo2");
        AccessRight = (int[]) getIntent().getSerializableExtra("AccessRight");
        String username=getIntent().getStringExtra("username");

        TextView welcome=findViewById(R.id.welcome_welcome);
        welcome.setText("ASIA-PACIFIC/"+username);

        Toolbar toolbar = findViewById(R.id.welcome_toolbar);
        setSupportActionBar(toolbar);

        //右侧filter page
        drawerl = findViewById(R.id.welcome_drawer);
        ActionBar actionb = getSupportActionBar();

        //左侧导航栏，根据用户权限判断能被显示的功能
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

        //左侧导航栏界面转换
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_E2E:
                        Intent intent = new Intent(WelcomePage.this, E2E.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("InitializeInfo", info);
                        bundle.putSerializable("InitializeInfo2", info2);
                        bundle.putSerializable("AccessRight",AccessRight);
                        intent.putExtras(bundle);
                        TextView tt = findViewById(R.id.nav_E2E);
                        startActivity(intent);
                        finish();
                        drawerl.closeDrawers();
                        break;
                    case R.id.nav_Dynamic:
                        Intent intent2 = new Intent(WelcomePage.this, Dynamic.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("InitializeInfo", info);
                        bundle2.putSerializable("InitializeInfo2", info2);
                        bundle2.putSerializable("AccessRight",AccessRight);
                        intent2.putExtras(bundle2);
                        startActivity(intent2);
                        finish();
                        drawerl.closeDrawers();
                        break;
                    case R.id.nav_DirectBL:
                        Intent intent4 = new Intent(WelcomePage.this, DirectBL.class);
                        Bundle bundle4 = new Bundle();
                        bundle4.putSerializable("InitializeInfo", info);
                        bundle4.putSerializable("InitializeInfo2", info2);
                        bundle4.putSerializable("AccessRight",AccessRight);
                        intent4.putExtras(bundle4);
                        startActivity(intent4);
                        finish();
                        drawerl.closeDrawers();
                        break;
                    case R.id.nav_analysis:
                        Intent intent3 = new Intent(WelcomePage.this, Analysis.class);
                        Bundle bundle3 = new Bundle();
                        bundle3.putSerializable("InitializeInfo", info);
                        bundle3.putSerializable("InitializeInfo2", info2);
                        bundle3.putSerializable("AccessRight",AccessRight);
                        intent3.putExtras(bundle3);
                        startActivity(intent3);
                        finish();
                        drawerl.closeDrawers();
                        break;
                    default:
                }
                return true;
            }
        });


    }

    /**
     * 点开左上角按钮出现导航栏
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
}
