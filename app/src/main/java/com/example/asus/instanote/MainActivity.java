package com.example.asus.instanote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView mrecyclerView;
    FileAdapter mfileAdapter;
    File file;
//    EditText searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //        searchView = findViewById(R.id.searchView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  final EditText editText = textEntryView.findViewById(R.id.renamefile);
                SharedPreferences sp = getSharedPreferences("MySharedPreferences2",MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putBoolean("isNewFile", true);
                ed.apply();

                Intent intent = new Intent(MainActivity.this , Main2Activity.class);
                startActivity(intent);
            }
        });

//        searchView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        mrecyclerView = findViewById(R.id.recyclerview);
        // Create a File object to get all the files stored in the MyDir created
        file = new File(getFilesDir(), "MyDir");
        // Get all the files in MyDir in a String array
        String[] Files = MainActivity.this.fileList();
        /*String[] Files = file.list();*/

        // Create a new arraylist
        ArrayList<String> FILES = new ArrayList<>();
        // Copy the elements of the array into an ArrayList to make it compatible with the adapter
        Collections.addAll(FILES, Files);
        mfileAdapter = new FileAdapter(FILES,createHashMapOfColors(), MainActivity.this);
        mrecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mrecyclerView.setAdapter(mfileAdapter);
    }

    Map<Integer, Integer> createHashMapOfColors() {
        Map<Integer, Integer> colorHashMap = new HashMap<>();
        colorHashMap.put(getResources().getColor(R.color.darkGreenColor), getResources().getColor(R.color.lightGreenColor));
        colorHashMap.put(getResources().getColor(R.color.darkBlackColor), getResources().getColor(R.color.lightBlackColor));
        colorHashMap.put(getResources().getColor(R.color.darkBlueColor), getResources().getColor(R.color.lightBlueColor));
        colorHashMap.put(getResources().getColor(R.color.darkRedColor), getResources().getColor(R.color.lightRedColor));
        colorHashMap.put(getResources().getColor(R.color.darkOrangeColor), getResources().getColor(R.color.lightOrangeColor));
        colorHashMap.put(getResources().getColor(R.color.darkPurpleColor), getResources().getColor(R.color.lightPurpleColor));
        colorHashMap.put(R.color.darkGreenColor, R.color.lightGreenColor);
        colorHashMap.put(R.color.darkBlackColor, R.color.lightBlackColor);
        colorHashMap.put(R.color.darkBlueColor, R.color.lightBlueColor);
        colorHashMap.put(R.color.darkRedColor, R.color.lightRedColor);
        colorHashMap.put(R.color.darkOrangeColor, R.color.lightOrangeColor);
        colorHashMap.put(R.color.darkPurpleColor, R.color.lightPurpleColor);
        new Colors(colorHashMap);
        return colorHashMap;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
