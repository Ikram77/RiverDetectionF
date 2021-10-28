package com.example.riverdetection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.riverdetection.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class PageGalery extends AppCompatActivity {

    private Intent intent;
    private ImageView imageView;
    private Button backGalery, selectImage, detectImage;
    private TextView textView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galery);

        final Animation buttonEfect = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        selectImage = (Button) findViewById(R.id.selectImage);
        detectImage = (Button) findViewById(R.id.detectImage);
        backGalery = (Button) findViewById(R.id.backGalery);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });

        backGalery.setOnClickListener(v -> {
            intent = new Intent(PageGalery.this, SecondActivity.class);
            startActivity(intent);
            v.startAnimation(buttonEfect);
            finish();
        });

        detectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
                try {
                    Model model = Model.newInstance(getApplicationContext());
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);

                    TensorImage tensorImage = new TensorImage(DataType.UINT8);
                    tensorImage.load(bitmap);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();
                    inputFeature0.loadBuffer(byteBuffer);

                    Model.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    model.close();

                    float total = 256;
                    textView.setText(
                             ("Persentase\nBersih   :  "+(100 * outputFeature0.getFloatArray()[0]) / total) +"%"+
                            "\n"+"Kotor     :  "+((100 * outputFeature0.getFloatArray()[1]) / total)+"%"
                     );
                } catch (IOException e) {
                    // TODO Handle the exception
                }
                v.startAnimation(buttonEfect);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100)
        {
            assert data != null;
            imageView.setImageURI(data.getData());
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}