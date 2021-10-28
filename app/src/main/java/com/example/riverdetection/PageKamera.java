package com.example.riverdetection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PageKamera extends AppCompatActivity {

    private Intent intent;
    private ImageView imageView;
    private Button backKamera, getImage, saveImage, detectImage;
    private TextView textView;
    private Bitmap picture;
    private int CAMERA_PIC = 1;
    private String mCurrentPhotoPath = "";

    /*
    Nothing!
     */

    OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        ActivityCompat.requestPermissions(PageKamera.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(PageKamera.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        final Animation buttonEfect = AnimationUtils.loadAnimation(this, R.anim.button_animation);

        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        backKamera = (Button) findViewById(R.id.backKamera);
        getImage = (Button) findViewById(R.id.getImage);
        saveImage = (Button) findViewById(R.id.saveImage);
        detectImage = (Button) findViewById(R.id.detectImage);

        getImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent kameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(kameraIntent, CAMERA_PIC);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                File filepath = Environment.getExternalStorageDirectory();
                File dir = new File(filepath.getAbsolutePath() + "/DCIM/Camera");
                dir.mkdir();
                File file = new File(dir, System.currentTimeMillis() + ".jpg");
                try {
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                Toast.makeText(getApplicationContext(), "Foto Tersimpan ke Galeri!",
                        Toast.LENGTH_SHORT).show();
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        detectImage.setOnClickListener(v -> {
            intent = new Intent(PageKamera.this, PageGalery.class);
            startActivity(intent);
            v.startAnimation(buttonEfect);
            finish();
        });

        backKamera.setOnClickListener(v -> {
            intent = new Intent(PageKamera.this, SecondActivity.class);
            startActivity(intent);
            v.startAnimation(buttonEfect);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC && resultCode == RESULT_OK) {
            Bitmap picture = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(picture);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

    /*
        Nothing!
     */

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_picture);
//
//        imageView = findViewById(R.id.imageView);
//        button = findViewById(R.id.getImage);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                captureImage();
//            }
//        });
//    }
//
//    private void captureImage() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
//                PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//        } else {
//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if (takePictureIntent.resolveActivity(getPackageManager()) != null){
//                try {
//                    photoFile = createImageFile();
//                    displayMessage(getBaseContext(), photoFile.getAbsolutePath());
//                    Log.i("River", photoFile.getAbsolutePath());
//
//                    if (photoFile != null){
//                        photoURI = FileProvider.getUriForFile(this,
//                              "com.example.riverdetection.fileprovider", photoFile);
////                        photoURI = FileProvider.getUriForFile(this,
////                                getApplicationContext().getPackageName()+".provider", photoFile);
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
//                    }
//                } catch (Exception ex){
//                    displayMessage(getBaseContext(), ex.getMessage().toString());
//                }
//            }
//        }
//    }
//
//    private File createImageFile() throws IOException{
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,
//                ".jpg",
//                storageDir
//        );
//
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        Bundle extras = data.getExtras();
////        Bitmap imageBitmap = (Bitmap) extras.get("data");
////        imageView.setImageBitmap(imageBitmap);
//        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK){
//            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(myBitmap);
//        } else{
//            displayMessage(getBaseContext(), "Gagal atau Sesuatu Bermasalah!");
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == 0){
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
//            && grantResults[1] == PackageManager.PERMISSION_GRANTED){
//                captureImage();
//            } else{
//                displayMessage(getBaseContext(), "Aplikasi Ini Membutuhkan Izin " +
//                        "untuk Penggunaan Kamera!");
//            }
//        }
//    }
//
//    private String galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//        return null;
//    }
//
//    private void displayMessage(Context context, String message) {
//        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//    }

    /*
        Nothing!
     */

//    public static final int IMAGE_GALLERY_REQUEST = 20;
//    public static final int CAMERA_REQUEST_CODE = 228;
//    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4192;
//    private ImageView imageView;
//    private Button getImage, saveImage;
//
//    //        getImage = (Button) findViewById(R.id.getImage);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_picture);
//        imageView = (ImageView) findViewById(R.id.imageView);
//        getImage = (Button) findViewById(R.id.getImage);
//        saveImage = (Button) findViewById(R.id.saveImage);
//
//        getImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
//                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                        invokeCamera();
//                    } else {
//                        // let's request permission.
//                        String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                        requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
//                    }
//                }
//            }
//        });
//
//        /**
//         * This method will be invoked when the user clicks a button
//         * @param v
//         */
//        saveImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // invoke the image gallery using an implict intent.
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//
//                // where do we want to find the data?
//                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                String pictureDirectoryPath = pictureDirectory.getPath();
//                // finally, get a URI representation
//                Uri data = Uri.parse(pictureDirectoryPath);
//
//                // set the data and type.  Get all image types.
//                photoPickerIntent.setDataAndType(data, "image/*");
//
//                // we will invoke this activity, and get something back from it.
//                startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
//            }
//        });
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
//            // we have heard back from our request for camera and write external storage.
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                invokeCamera();
//            } else {
//                Toast.makeText(this, "Tidak Bisa Menggunakan Kamera Tanpa Izin", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private void invokeCamera(){
//        Uri pictureUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() +
//                ".provider", createImageFile());
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
//        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        startActivityForResult(intent, CAMERA_REQUEST_CODE);
//
//    }
//
//    private File createImageFile() {
//        File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String timestamp = sdf.format(new Date());
//        File imageFile = new File(picturesDirectory, "picture" + timestamp + ".jpg");
//
//        return imageFile;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == CAMERA_REQUEST_CODE) {
//                Toast.makeText(this, "Image Saved.", Toast.LENGTH_LONG).show();
//            }
//            if (requestCode == IMAGE_GALLERY_REQUEST) {
//                Uri imageUri = data.getData();
//                InputStream inputStream;
//                try {
//                    inputStream = getContentResolver().openInputStream(imageUri);
//                    Bitmap image = BitmapFactory.decodeStream(inputStream);
//                    imageView.setImageBitmap(image);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }
//    }
