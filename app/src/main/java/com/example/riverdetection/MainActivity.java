package com.example.riverdetection;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    private Button btnDetect;
    private Intent pindah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Animation buttonEfect = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        btnDetect = (Button) findViewById(R.id.detect);

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pindah = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(pindah);
                v.startAnimation(buttonEfect);
                finish();
            }
        });

    }
}