package com.example.bsyoo.jwc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bsyoo.jwc.model.Model_Notice;

import uk.co.senab.photoview.PhotoViewAttacher;

public class EventInfoActivity extends AppCompatActivity {

    private Model_Notice event = new Model_Notice();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        getSupportActionBar().setTitle("이벤트");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.RED);
        }
        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        event = (Model_Notice) intent.getSerializableExtra("event");
        ImageView event_info = (ImageView) findViewById(R.id.event_info);

        if (event.getImg_info() != null) {
            Glide.with(this).load(event.getImg_info()).override(720, 4000).fitCenter().into(event_info);
        }

        // 이미지 줌인, 아웃 (build.gradle 추가)
        PhotoViewAttacher photoview = new PhotoViewAttacher(event_info);
        photoview.update();

    }
}