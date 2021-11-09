package com.example.riverdetection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.riverdetection.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class PageKamera extends AppCompatActivity {

    private Intent intent;
    private ImageView imageView;
    private Button backKamera, getImage, saveImage, detectImage;
    private TextView textView;
    private Bitmap picture;
    private int CAMERA_PIC = 1;
    private String mCurrentPhotoPath = "";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        final Animation buttonEfect = AnimationUtils.loadAnimation(this, R.anim.button_animation);

        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView2);
        backKamera = (Button) findViewById(R.id.backKamera);
        getImage = (Button) findViewById(R.id.getImage);
        detectImage = (Button) findViewById(R.id.detectImage);

        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureIntent();
                v.startAnimation(buttonEfect);
            }
        });

        detectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predictImage();
                v.startAnimation(buttonEfect);
            }
        });

        backKamera.setOnClickListener(v -> {
            intent = new Intent(PageKamera.this, SecondActivity.class);
            startActivity(intent);
            v.startAnimation(buttonEfect);
        });
    }

    private void predictImage() {
        picture = Bitmap.createScaledBitmap(picture, 224, 224, true);
        try {
            Model model = Model.newInstance(getApplicationContext());
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);

            TensorImage tensorImage = new TensorImage(DataType.UINT8);
            tensorImage.load(picture);
            ByteBuffer byteBuffer = tensorImage.getBuffer();
            inputFeature0.loadBuffer(byteBuffer);

            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            model.close();

            float total = 256;
            textView.setText(
                    ("Persentase\nBersih   :  "+(100 * outputFeature0.getFloatArray()[0]) / total)
                            +"%"+ "\n"+"Kotor     :  " +
                            ""+((100 * outputFeature0.getFloatArray()[1]) / total)+"%"
            );
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    private void pictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            picture = (Bitmap) extras.get("data");
            imageView.setImageBitmap(picture);
        }
    }
}
