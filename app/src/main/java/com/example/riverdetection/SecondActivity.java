package com.example.riverdetection;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {

    Button btnBack;
    Button btnGalery;
    Button btnPicture;
    Intent pindah1;
    Intent pindah2;
    Intent pindah3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final Animation buttonEfect = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        btnBack = (Button)findViewById(R.id.back);
        btnGalery = (Button)findViewById(R.id.galery);
        btnPicture = (Button)findViewById(R.id.picture);

        btnBack.setOnClickListener(v -> {
            pindah1 = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(pindah1);
            finish();
            v.startAnimation(buttonEfect);
        });

        btnGalery.setOnClickListener(v -> {
            pindah2 = new Intent(SecondActivity.this, PageGalery.class);
            startActivity(pindah2);
            v.startAnimation(buttonEfect);
            finish();
        });

        btnPicture.setOnClickListener(v -> {
            pindah3 = new Intent(SecondActivity.this, PageKamera.class);
            startActivity(pindah3);
            v.startAnimation(buttonEfect);
            finish();
        });
    }
}
