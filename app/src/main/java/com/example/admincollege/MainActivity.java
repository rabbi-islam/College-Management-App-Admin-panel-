package com.example.admincollege;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialCardView uploadNotice,addGalleryImage,addEbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadNotice = findViewById(R.id.addNotice);
        addGalleryImage = findViewById(R.id.addGallery);
        addEbook = findViewById(R.id.addEbook);
        uploadNotice.setOnClickListener(this);
        addGalleryImage.setOnClickListener(this);
        addEbook.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addNotice:
                startActivity(new Intent(MainActivity.this,UploadNoticeActivity.class));
                break;
            case R.id.addGallery:
                startActivity(new Intent(MainActivity.this,UploadImage.class));
                break;
            case R.id.addEbook:
                startActivity(new Intent(MainActivity.this,UploadPdf.class));
                break;
        }

    }
}