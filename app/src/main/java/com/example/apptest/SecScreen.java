package com.example.apptest;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.content.Intent;

public class SecScreen extends AppCompatActivity {
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sec_screen);
        Log.d("SecScreen", "onCreate: Starting.");


        mFab = (FloatingActionButton) findViewById(R.id.sec_screen_float);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SecScreen", "onClick: Clicked MainActivity.");

                Intent intent = new Intent(SecScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
