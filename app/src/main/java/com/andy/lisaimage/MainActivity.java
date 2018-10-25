package com.andy.lisaimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.lisa.L;
import com.andy.lisa.LCallback;
import com.andy.lisa.LTask;
import com.andy.lisa.Lisa;

import java.io.File;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView srcImage;
    private ImageView descImage;
    private TextView srcTv;
    private TextView descTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        srcImage = findViewById(R.id.image_src);
        descImage = findViewById(R.id.image_desc);
        srcTv = findViewById(R.id.tv_src);
        descTv = findViewById(R.id.tv_desc);
        srcImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (srcTv.getTag() != null)
                    PicActivity.toPic(MainActivity.this, String.valueOf(srcTv.getTag()));
            }
        });
        descImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (descTv.getTag() != null)
                    PicActivity.toPic(MainActivity.this, String.valueOf(descTv.getTag()));
            }
        });
        findViewById(R.id.choose_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        findViewById(R.id.lisa_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lisaImage();
            }
        });
        clear();
    }

    private void clear() {
        srcImage.setBackgroundColor(getRandomColor());
        descImage.setImageDrawable(null);
        descImage.setBackgroundColor(getRandomColor());
        srcTv.setText("");
        descTv.setText("");
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void lisaImage() {
        Object tag = srcTv.getTag();
        if (tag != null) {
            LTask task = Lisa.getInstance().compress((File) tag);
            task.compress(new LCallback() {
                @Override
                public void ok(List<String> items) {
                    L.e(items);
                    setBitmap(descImage, descTv, new File(items.get(0)));
                }

                @Override
                public void err(String errMsg) {
                    L.e("errMsg:" + errMsg);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                L.e("Failed to open picture!");
                return;
            }
            Uri uri = data.getData();
            L.e(uri);
            try {
                File imageFile = FileUtil.from(this, uri);
                L.e(imageFile);
                clear();
                setBitmap(srcImage, srcTv, imageFile);
            } catch (Exception e) {
                L.e(e);
            }
        }
    }

    private void setBitmap(ImageView image, TextView tv, File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        image.setImageBitmap(bitmap);
        tv.setTag(file);
        tv.setText(bitmap.getWidth() + "*" + bitmap.getHeight() + "---"
                + Formatter.formatFileSize(this, bitmap.getRowBytes() * bitmap.getHeight()) + "---" + Formatter.formatFileSize(this, file.length()));
    }

    private int getRandomColor() {
        Random rand = new Random();
        return Color.argb(100, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }
}
