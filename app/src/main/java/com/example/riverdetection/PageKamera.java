package com.example.riverdetection;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PageKamera extends AppCompatActivity {

    private Intent intent;
    private ImageView imageView;
    private Button backKamera, button, getImage, saveImage, detectImage;
    private TextView textView;
    private Bitmap picture;
    private int CAMERA_PIC = 1;
    static final int CAPTURE_IMAGE_REQUEST = 1;
    File photoFile = null;
    private String mCurrentPhotoPath = "";
    Uri photoURI = null;

//    OutputStream outputStream;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_picture);
//
//        ActivityCompat.requestPermissions(PageKamera.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//        ActivityCompat.requestPermissions(PageKamera.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
//        final Animation buttonEfect = AnimationUtils.loadAnimation(this, R.anim.button_animation);
//
//        imageView = (ImageView) findViewById(R.id.imageView);
//        textView = (TextView) findViewById(R.id.textView);
//        backKamera = (Button) findViewById(R.id.backKamera);
//        getImage = (Button) findViewById(R.id.getImage);
//        saveImage = (Button) findViewById(R.id.saveImage);
//        detectImage = (Button) findViewById(R.id.detectImage);
//
//        getImage.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//               if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
//                   Intent kameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                   startActivityForResult(kameraIntent, CAMERA_PIC);
//               }else{
//                   requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
//               }
//            }
//        });
//
//        saveImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
//                Bitmap bitmap = drawable.getBitmap();
//                File filepath = Environment.getExternalStorageDirectory();
//                File dir = new File(filepath.getAbsolutePath() + "/MyPics");
//                dir.mkdir();
//                File file = new File(dir, System.currentTimeMillis()+".jpg");
//                try {
//                    outputStream = new FileOutputStream(file);
//                } catch (FileNotFoundException e){
//                    e.printStackTrace();
//                }
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                Toast.makeText(getApplicationContext(), "Image Save to Internal!",
//                        Toast.LENGTH_SHORT).show();
//                try {
//                    outputStream.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        detectImage.setOnClickListener(v -> {
//            intent = new Intent(PageKamera.this, PageGalery.class);
//            startActivity(intent);
//            v.startAnimation(buttonEfect);
//            finish();
//        });
//
//        backKamera.setOnClickListener(v -> {
//            intent = new Intent(PageKamera.this, SecondActivity.class);
//            startActivity(intent);
//            v.startAnimation(buttonEfect);
//            finish();
//        });
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == CAMERA_PIC && resultCode == RESULT_OK){
//            Bitmap picture = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(picture);
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.getImage);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
    }

    private void captureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null){
                try {
                    photoFile = createImageFile();
                    displayMessage(getBaseContext(), photoFile.getAbsolutePath());
                    Log.i("River", photoFile.getAbsolutePath());

                    if (photoFile != null){
                        photoURI = FileProvider.getUriForFile(this,
                              "com.example.riverdetection.fileprovider", photoFile);
//                        photoURI = FileProvider.getUriForFile(this,
//                                getApplicationContext().getPackageName()+".fileprovider", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                    }
                } catch (Exception ex){
                    displayMessage(getBaseContext(), ex.getMessage().toString());
                }
            }
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Bundle extras = data.getExtras();
//        Bitmap imageBitmap = (Bitmap) extras.get("data");
//        imageView.setImageBitmap(imageBitmap);
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK){
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(myBitmap);
        } else{
            displayMessage(getBaseContext(), "Gagal atau Sesuatu Bermasalah!");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                captureImage();
            } else{
                displayMessage(getBaseContext(), "Aplikasi Ini Membutuhkan Izin " +
                        "untuk Penggunaan Kamera!");
            }
        }
    }

    private String galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        return null;
    }

    private void displayMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}