package com.example.asus.instanote;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import java.io.File;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

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

                // Creating an edittext for the dialog box
                final EditText editText = new EditText(MainActivity.this);
                editText.setHint("Give your note a name");

                // Save the below three lines in NIIT24 app
//                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
//                linearLayout.setOrientation(LinearLayout.VERTICAL);
//                linearLayout.addView(editText);

                // Creating the AlertDialog Box for the user to enter the name of the note
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Give your note a name..");

                // Save the below three lines in NIIT24 app
//                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
//                final View textEntryView = factory.inflate(R.layout.rename_file_layout, null);
//                builder.setView(textEntryView);
                builder.setIcon(R.drawable.appicon);
                builder.setView(editText);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        //  final EditText editText = textEntryView.findViewById(R.id.renamefile);
                        SharedPreferences sp = getSharedPreferences("MySharedPreferences2",MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putString("NaamFileKa",editText.getText().toString());
                        // lola
                        ed.putBoolean("isNewFile", true);
                        ed.apply();
                        Intent intent = new Intent(MainActivity.this , Main2Activity.class);
                        startActivity(intent);


                        ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.INVISIBLE);
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (s.toString().isEmpty())
                                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.INVISIBLE);
                                else
                                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();



                builder.show();
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
        mfileAdapter = new FileAdapter(FILES,MainActivity.this);
        mrecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mrecyclerView.setAdapter(mfileAdapter);
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