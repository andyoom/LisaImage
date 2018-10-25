package com.andy.lisaimage;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.andy.lisa.L;

public class PicActivity extends AppCompatActivity {

    public static void toPic(Context context,String path){
        Intent intent = new Intent(context,PicActivity.class);
        intent.putExtra("urlPath",path);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        ImageView image = findViewById(R.id.image);
        String path = getIntent().getStringExtra("urlPath");
        L.e(path);
        if(path!=null){
            image.setImageBitmap(BitmapFactory.decodeFile(path));
        }
    }
}
