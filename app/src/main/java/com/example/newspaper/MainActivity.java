package com.example.newspaper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int CAMERA_PERMISSION_CODE = 2001;
    static final int CAMERA_INTENT_CODE = 3001;

    RecyclerView recyclerView;
    ImagesArrayAdapter adapter;
    String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataModel.getInstance().setContext(MainActivity.this);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ImagesArrayAdapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        adapter.setClickListenner(new ImagesArrayAdapter.ClickListenner() {
            @Override
            public void onItemClick(int position, View view) {
                DataModel.getInstance().indexToDetails = position;
                Intent intent = new Intent(MainActivity.this, MovieDetails.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }


    public void takePictureClicked(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestCameraPermission();
        }
        else {
            sendCameraIntent();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    void requestCameraPermission() {
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{ Manifest.permission.CAMERA }, CAMERA_PERMISSION_CODE);
            }
            else {
                sendCameraIntent();
            }
        }
        else {
            Toast.makeText(MainActivity.this,"Câmera não disponível", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendCameraIntent();
            }
            else {
                Toast.makeText(MainActivity.this, "Câmera não disponível", Toast.LENGTH_LONG).show();
            }
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

                Uri photoUri = FileProvider.getUriForFile(MainActivity.this, "com.example.newspaper.fileProvider", pictureFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, CAMERA_INTENT_CODE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_INTENT_CODE) {
            if(resultCode == RESULT_OK) {
                File file = new File(picturePath);
                if(file.exists()) {
                    DataModel.getInstance().addPhotoToken(new Foto(new Date().toString(), "Nova foto", file.getAbsolutePath()));

                    adapter.notifyDataSetChanged();
                }
            }
            else {
                Toast.makeText(MainActivity.this, "Problema na busca de imagens", Toast.LENGTH_LONG).show();
            }
        }
    }






}



