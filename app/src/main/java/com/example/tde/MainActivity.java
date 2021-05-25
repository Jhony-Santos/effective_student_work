package com.example.tde;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int CAMERA_PERMISSION_CODE = 2001;
    static final int CAMERA_INTENT_CODE = 3001;
    static final int GALLERY_INTENT_CODE = 100;


    String picturePath;
    ImageView imageView;
    Uri imageUri;
    Bitmap imageBitMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
    }


    public void tirarFoto(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestCameraPermission();
        } else {
            sendCameraIntent();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    void requestCameraPermission() {
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] { Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            } else {
                sendCameraIntent();
            }
        }
        else Toast.makeText(MainActivity.this, "A câmera não está disponível", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendCameraIntent();
            } else Toast.makeText(MainActivity.this, "A câmera não está disponível", Toast.LENGTH_LONG).show();
        }
    }


    void sendCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        if(intent.resolveActivity(getPackageManager()) != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String picName = "pic_" + timeStamp;
            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File pictureFile = null;
            try {
                pictureFile = File.createTempFile(picName, ".jpg", dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(pictureFile != null) {
                picturePath = pictureFile.getAbsolutePath();

                Uri photoUri = FileProvider.getUriForFile(MainActivity.this, "com.example.tde", pictureFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, CAMERA_INTENT_CODE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        resetGrayscale();

        if(requestCode == CAMERA_INTENT_CODE) {
            if(resultCode == RESULT_OK){
                File file = new File(picturePath);
                if(file.exists()) {
                    imageView.setImageURI(Uri.fromFile(file));
                }
            } else Toast.makeText(MainActivity.this,"Não é possível acessar tal imagem!", Toast.LENGTH_LONG).show();
        }
        else if(requestCode == GALLERY_INTENT_CODE) {
            if(resultCode == RESULT_OK){
                imageUri = data.getData();
                try {
                    imageBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(imageBitMap);
            } else Toast.makeText(MainActivity.this, "Não foi possível acessar tal imagem!", Toast.LENGTH_LONG).show();
        }
    }


    public void galeria(View v) {
        Intent myIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(myIntent, 100);
    }


    public void converter(View v) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(cf);
    }


    public void resetGrayscale() {
        imageView.setColorFilter(null);
    }





}