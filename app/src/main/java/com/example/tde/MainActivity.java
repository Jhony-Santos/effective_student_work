package com.example.tde;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    static final int CAMERA_PERMISSION_CODE=2001;
    static final int CAMERA_INTENT_CODE=3001;

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





}