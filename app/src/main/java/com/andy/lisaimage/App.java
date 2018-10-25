package com.andy.lisaimage;

import android.app.Application;

import com.andy.lisa.Lisa;

/**
 * 类描述：
 * 创建人：yekh
 * 创建时间：2018/10/24 14:37
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Lisa.getInstance()
                .maxWidth(540)
                .maxHeight(960)
                .quality(60)
                .descDir(getExternalFilesDir("lisa"));
    }
}
