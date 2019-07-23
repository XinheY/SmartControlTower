package com.example.smartcontroltower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class WelcomePage extends AppCompatActivity {

    private DrawerLayout drawerl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.welcome_toolbar);
        setSupportActionBar(toolbar);

        drawerl = findViewById(R.id.welcome_drawer);
        ActionBar actionb = getSupportActionBar();
        NavigationView nv = findViewById(R.id.nav_view);
        if (actionb != null) {
            actionb.setDisplayHomeAsUpEnabled(true);
            actionb.setHomeAsUpIndicator(R.drawable.menu);
        }

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_E2E:
                       Intent intent=new Intent(WelcomePage.this,E2E.class);
                        TextView tt=findViewById(R.id.nav_E2E);
                       startActivity(intent);
                       finish();
                        drawerl.closeDrawers();
                        break;
                    case R.id.nav_Dynamic:
                        Intent intent2=new Intent(WelcomePage.this,Dynamic.class);
                        startActivity(intent2);
                        finish();
                        drawerl.closeDrawers();
                        break;
                    case R.id.nav_DirectBL:
                        Intent intent4=new Intent(WelcomePage.this,DirectBL.class);
                        startActivity(intent4);
                        finish();
                        drawerl.closeDrawers();
                        break;
                    case R.id.nav_analysis:
                        Intent intent3=new Intent(WelcomePage.this,Analysis.class);
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
