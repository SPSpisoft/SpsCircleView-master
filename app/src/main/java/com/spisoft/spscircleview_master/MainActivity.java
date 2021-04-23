package com.spisoft.spscircleview_master;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.spisoft.spcircleview.CircleView;

public class MainActivity extends AppCompatActivity {

    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CircleView CV = findViewById(R.id.cv);
        CV.setBackgroundColor(Color.RED);
        CV.setViewSize(50);
        CV.setIcon(getResources().getDrawable(R.drawable.ic_baseline_account_circle_24));

        CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CV.setAlpha(50);
            }
        });

        CV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CV.setAlpha(255);
                return true;
            }
        });
//
    }
}