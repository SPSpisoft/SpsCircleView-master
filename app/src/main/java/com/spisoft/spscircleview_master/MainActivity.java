package com.spisoft.spscircleview_master;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.spisoft.spcircleview.CircleView;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CircleView CV = findViewById(R.id.cv);
        CV.setBackgroundColor(R.color.purple_500);
        CV.setViewSize(50);
//
    }
}