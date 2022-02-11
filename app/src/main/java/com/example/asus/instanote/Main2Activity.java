package com.example.asus.instanote;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {

    private EditText notes;
    private EditText title;
    private ImageButton colorchangebutton, texttospeech;
    private Menu menu;
    boolean hasUserChangedTitle = false;
    private static final int TIME_DELAY = 3000;
    private static long back_pressed;
    private LinedEditText linedEditText;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        * get the references of all the widgets
        */
        notes = findViewById(R.id.notes);
        title = findViewById(R.id.title);
        colorchangebutton = findViewById(R.id.colorchangebutton);
        texttospeech = findViewById(R.id.texttospeech);
        linedEditText = new LinedEditText(Main2Activity.this);

        /*
        * Retrieve the contents of the file made and set them in respective fields
        * */
        SharedPreferences sp = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
        String Name_Of_The_File = sp.getString("FileName", null);
        title.setText(Name_Of_The_File);
        String Contents_Of_The_File = sp.getString("Contents", null);
        notes.setText(Contents_Of_The_File);

        /*
        * Add listeners to notes and title
        * */
        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!hasUserChangedTitle) {
                    title.setTag("arbitrary value");
                    title.setText(editable.toString());
                    title.setTag(null);
                }
            }
        });

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (title.getTag() == null) {
                    // value changed by user
                    hasUserChangedTitle = true;
                }
            }
        });

        colorchangebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                LayoutInflater factory = LayoutInflater.from(Main2Activity.this);
                View view= factory.inflate(R.layout.color_chooser, null);
                builder.setView(view);

                ImageButton blue = view.findViewById(R.id.imageButton);
                blue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolbar.setBackgroundColor(getResources().getColor(R.color.darkBlueColor));
                        notes.setBackgroundColor(getResources().getColor(R.color.lightBlueColor));
                        linedEditText.setColorOfLinedEditText(R.color.darkBlueColor);
                    }
                });
                ImageButton black = view.findViewById(R.id.imageButton4);
                black.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolbar.setBackgroundColor(getResources().getColor(R.color.darkBlackColor));
                        notes.setBackgroundColor(getResources().getColor(R.color.lightBlackColor));
                        linedEditText.setColorOfLinedEditText(R.color.darkBlackColor);
                    }
                });
                ImageButton orange = view.findViewById(R.id.imageButton3);
                orange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolbar.setBackgroundColor(getResources().getColor(R.color.darkOrangeColor));
                        notes.setBackgroundColor(getResources().getColor(R.color.lightOrangeColor));
                        linedEditText.setColorOfLinedEditText(R.color.darkOrangeColor);
                    }
                });
                ImageButton green = view.findViewById(R.id.imageButton2);
                green.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        notes.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        linedEditText.setColorOfLinedEditText(R.color.colorPrimaryDark);
                    }
                });
                ImageButton red = view.findViewById(R.id.imageButton5);
                red.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolbar.setBackgroundColor(getResources().getColor(R.color.darkRedColor));
                        notes.setBackgroundColor(getResources().getColor(R.color.lightRedColor));
                        linedEditText.setColorOfLinedEditText(R.color.darkRedColor);
                    }
                });
                ImageButton purple = view.findViewById(R.id.imageButton6);
                purple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolbar.setBackgroundColor(getResources().getColor(R.color.darkPurpleColor));
                        notes.setBackgroundColor(getResources().getColor(R.color.lightPurpleColor));
                        linedEditText.setColorOfLinedEditText(R.color.darkPurpleColor);
                    }
                });

                builder.create();
                builder.show();
            }
        });

        // Should write on the notes edittext by clicking on this button
        texttospeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAudioPermissions();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                // the below line could be skipped
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getPackageManager())!=null) {
                    startActivityForResult(intent, 10);
                } else {
                    Toast.makeText(Main2Activity.this, "This feature is not supported on your android device", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("noteTitleSavedInstanceState", title.getText().toString());
        if (menu != null){
            MenuItem deleteMenuItem = menu.findItem(R.id.delete);
            outState.putBoolean("DeleteMenuItem", deleteMenuItem.isVisible());
            MenuItem discardMenuItem = menu.findItem(R.id.discard);
            outState.putBoolean("DiscardMenuItem", discardMenuItem.isVisible());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        title.setText(savedInstanceState.getString("noteTitleSavedInstanceState"));
        if (menu != null) {
            menu.findItem(R.id.delete).setVisible(savedInstanceState.getBoolean("DeleteMenuItem"));
            menu.findItem(R.id.discard).setVisible(savedInstanceState.getBoolean("DiscardMenuItem"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main2_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.discard :
                Toast.makeText(Main2Activity.this, "Note Discarded", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.share :
                FileHandling.shareFile(null,Main2Activity.this, notes.getText().toString());
                break;
            case R.id.delete :
                new AlertDialog.Builder(Main2Activity.this).setTitle("Are you sure you want to delete this note?")
                        .setPositiveButton("Yeah, pretty sure..!!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (FileHandling.deleteFile(title.getText().toString(),Main2Activity.this)) {
                                    Toast.makeText(Main2Activity.this, "Deleted Successfully..!!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else
                                    Toast.makeText(Main2Activity.this, "Error in deletion..!!", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Nope..!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                break;
//            case R.id.lock :
//                break;
//            case R.id.reminder :
//                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
//                startActivity(intent);
//                break;
        }
        return true;
    }

    // If note already exists, only have the delete option
    // Else, only have the discard option
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SharedPreferences sp = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
        if (!sp.getBoolean("isNewFile", true)) {
            menu.findItem(R.id.discard).setVisible(false);
        } else {
            menu.findItem(R.id.delete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    // If nothing is done by the user, simply go back
    // If user has left the title empty, make first 15 characters of notes as title
    // If user has opened an old file, rename it
    @Override
    public void onBackPressed() {
        if (notes.getText().toString().isEmpty() && title.getText().toString().isEmpty()) {
            super.onBackPressed();
        } else {
            if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed();
                String res = notes.getText().toString();
                String filename = title.getText().toString();
                if (filename.isEmpty()) {
                    if (res.length() >= 15) {
                        filename = res.substring(0, 15);
                    } else {
                        filename = res;
                    }
                }
                SharedPreferences sp1 = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
                if (!sp1.getBoolean("isNewFile", true)) {
                    if (FileHandling.renameFile(sp1.getString("FileName", "ERROR"), filename, res, Main2Activity.this)) {
                        Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                        // To remove the contents of the SharedPreferences so that on creation of the another file the same values do not appear
                        sp1.edit().remove("FileName").remove("Contents").remove("isNewFile").apply();
                    } else
                        Toast.makeText(this, "Error in renaming file..!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (FileHandling.createFile(filename, res, Main2Activity.this)) {
                        Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Error in saving file..!!", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(getBaseContext(), "Press back once again to save your note!",
                        Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }

    // Text To Speech feature
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    switch (result.get(0)) {
                        case "save my note":
                            FileHandling.createFile(title.getText().toString(), notes.getText().toString(), Main2Activity.this);
                            Toast.makeText(Main2Activity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case "share my note":
                            FileHandling.shareFile(null, Main2Activity.this, notes.getText().toString());
                            break;
                        case "delete my note":
                            FileHandling.deleteFile(title.getText().toString(), Main2Activity.this);
                            Toast.makeText(Main2Activity.this, "Deleted Successfully..!!", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case "discard my note":
                            finish();
                            break;
                        case "set a reminder":
                            break;
                        case "lock my note":
                            break;
                        case "clear my note":
                            notes.setText("");
                            break;
                        default:
                            notes.setText(String.format("%s%s", notes.getText().toString(), result.get(0)));
                            break;
                    }
                }
                break;
        }
    }



    //Requesting run-time permissions
    //Create placeholder for user's consent to record_audio permission.
    //This will be used in handling callback
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_SHORT).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
        }
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
