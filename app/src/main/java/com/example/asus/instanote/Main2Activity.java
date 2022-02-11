package com.example.asus.instanote;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {

    private EditText notes;
    private EditText title;
    private ImageButton colorchangebutton, texttospeech;
    private SpeechRecognizer speech = null;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private Intent recognizerIntent;
    private int selectedColor;
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setStatusBarColor(R.color.darkOrangeColor);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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
        boolean isNewFile = sp.getBoolean("isNewFile", true);
        if (!isNewFile) {
            int darkColor = new DatabaseManagement(Main2Activity.this).getColor(Name_Of_The_File);
            int lightColor = new Colors().getColorHashMap().get(darkColor);
            setColorsOfTheWidgets(darkColor, lightColor, true);
        } else {
            selectedColor = getResources().getColor(R.color.darkOrangeColor);
            setStatusBarColor(getResources().getColor(R.color.darkOrangeColor));
        }

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
                builder.create();
                builder.show();
                ImageButton blue = view.findViewById(R.id.imageButton);
                blue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setColorsOfTheWidgets(R.color.darkBlueColor, R.color.lightBlueColor, false);
                    }
                });
                ImageButton black = view.findViewById(R.id.imageButton4);
                black.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setColorsOfTheWidgets(R.color.darkBlackColor, R.color.lightBlackColor, false);
                    }
                });
                ImageButton orange = view.findViewById(R.id.imageButton3);
                orange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setColorsOfTheWidgets(R.color.darkOrangeColor, R.color.lightOrangeColor, false);
                    }
                });
                ImageButton green = view.findViewById(R.id.imageButton2);
                green.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setColorsOfTheWidgets(R.color.darkGreenColor, R.color.lightGreenColor, false);
                    }
                });
                ImageButton red = view.findViewById(R.id.imageButton5);
                red.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setColorsOfTheWidgets(R.color.darkRedColor, R.color.lightRedColor, false);
                    }
                });
                ImageButton purple = view.findViewById(R.id.imageButton6);
                purple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setColorsOfTheWidgets(R.color.darkPurpleColor, R.color.lightPurpleColor, false);
                    }
                });


            }
        });

        texttospeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start speech recogniser
                resetSpeechRecognizer();

                // check for permission
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
                    return;
                }

                setRecogniserIntent();
                speech.startListening(recognizerIntent);
            }
        });
    }

    public void setStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    void setColorsOfTheWidgets(int darkColorId, int lightColorId, boolean isColor) {
        int darkColor = darkColorId;
        int lightColor = lightColorId;
        if (!isColor) {
            darkColor = getResources().getColor(darkColorId);
            lightColor = getResources().getColor(lightColorId);
        }
        toolbar.setBackgroundColor(darkColor);
        notes.setBackgroundColor(lightColor);
        linedEditText.setColorOfLinedEditText(darkColor);
        setStatusBarColor(darkColor);
        selectedColor = darkColor;
        texttospeech.setBackgroundColor(darkColor);
        colorchangebutton.setBackgroundColor(darkColor);
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
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (FileHandling.deleteFile(title.getText().toString(),Main2Activity.this)) {
                                    Toast.makeText(Main2Activity.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sp = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
                                    sp.edit().remove("FileName").remove("Contents").remove("isNewFile").apply();
                                    finish();
                                }
                                else
                                    Toast.makeText(Main2Activity.this, "Error in deletion!", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    void createFile(String filename, String res) {
        SharedPreferences sp = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
        if (!sp.getBoolean("isNewFile", true)) {
            if (FileHandling.renameFile(getFilesDir(), sp.getString("FileName", "ERROR"), filename, res, selectedColor, Main2Activity.this)) {
                Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                // To remove the contents of the SharedPreferences so that on creation of the another file the same values do not appear
                sp.edit().remove("FileName").remove("Contents").remove("isNewFile").apply();
            } else
                Toast.makeText(this, "Error in renaming file!", Toast.LENGTH_SHORT).show();
        }
        else {
            if (FileHandling.createFile(getFilesDir(), filename, res, selectedColor, Main2Activity.this)) {
                Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Error in saving file!", Toast.LENGTH_SHORT).show();
        }
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
                createFile(filename, res);
            } else {
                Toast.makeText(getBaseContext(), "Press back once again to save your note!", Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (speech != null) {
            speech.destroy();
        }
    }

    private void resetSpeechRecognizer() {
        if(speech != null)
            speech.destroy();
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        if(SpeechRecognizer.isRecognitionAvailable(this))
            speech.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {
                    speech.stopListening();
                }

                @Override
                public void onError(int i) {
                    // rest voice recogniser
                    resetSpeechRecognizer();
                    speech.startListening(recognizerIntent);
                    new CountDownTimer(4000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            //do nothing, just let it tick
                        }

                        public void onFinish() {
                            speech.destroy();
                        }
                    }.start();
                }

                @Override
                public void onResults(Bundle bundle) {
                    ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    switch (matches.get(0)) {
                        case "save my note":
                            createFile(title.getText().toString(), notes.getText().toString());
                            finish();
                            break;
                        case "share my note":
                            FileHandling.shareFile(null, Main2Activity.this, notes.getText().toString());
                            break;
                        case "delete my note":
                            FileHandling.deleteFile(title.getText().toString(), Main2Activity.this);
                            Toast.makeText(Main2Activity.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                            SharedPreferences sp = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
                            sp.edit().remove("FileName").remove("Contents").remove("isNewFile").apply();
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
                            notes.setText(String.format("%s %s", notes.getText().toString(), matches.get(0)));
                            speech.startListening(recognizerIntent);
                            notes.setSelection(notes.getText().length());
                            break;
                    }
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        else
            finish();
    }

    private void setRecogniserIntent() {

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                speech.startListening(recognizerIntent);
            } else {
                Toast.makeText(Main2Activity.this, "Permission Denied!", Toast
                        .LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
