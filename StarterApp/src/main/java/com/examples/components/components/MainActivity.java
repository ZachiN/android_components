package com.examples.components.components;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.examples.components.MESSAGE";

    private LinearLayout background;
    private Button btnFirst;
    private Button btnSecond;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.background = (LinearLayout)findViewById(R.id.background);
        this.btnFirst = (Button)findViewById(R.id.btnFirst);
        this.btnSecond = (Button)findViewById(R.id.btnSec);

        this.btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                background.setBackgroundColor(Color.parseColor("#00FF00"));
            }
        });

        this.btnSecond.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                background.setBackgroundColor(Color.parseColor("#0000FF"));
            }
        });
    }

    @Override protected void onStart() {
        super.onStart();
        System.out.println("MainActivity::onStart");
    }

    @Override  protected void onResume() {
        super.onResume();
        System.out.println("MainActivity::onResume");
    }

    @Override protected void onPause() {
        // Used for releasing resources that may be needed by the current focused Activity
        // e.g. Camera, GPS, Animations, Video/Sound etc...
        // Save any data if any
        super.onPause();
        System.out.println("MainActivity::onPause");
    }

    @Override protected void onStop() {
        // Release all resources
        // Save any data if any
        super.onStop();
        System.out.println("MainActivity::onStop");
    }

    @Override protected void onRestart() {
        super.onRestart();
        System.out.println("MainActivity::onRestart");
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        System.out.println("MainActivity::onDestroy");
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editMessage);
//         Optional:
//        String msg = getResources().getString(R.string.edit_txt_insertHere);
//        editText.setText(R.string.edit_txt_insertHere);

        String msg = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, msg);
        startActivity(intent);
    }
}
