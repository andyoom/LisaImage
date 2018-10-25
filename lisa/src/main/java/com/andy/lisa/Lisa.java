package com.andy.lisa;

import android.graphics.Bitmap.CompressFormat;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类描述：
 * 创建人：yekh
 * 创建时间：2018/10/24 9:38
 */
public class Lisa {
    private static Lisa _instance;
    private LConfig commConfig;
    private Handler mainHandler;
    private ExecutorService executor;

    private Lisa() {
        commConfig = new LConfig();
        mainHandler = new Handler(Looper.getMainLooper());
        executor = Executors.newSingleThreadExecutor();
    }

    public LTask compress(String imagePath) {
        return compress(imagePath, null);
    }

    public LTask compress(String imagePath, LConfig config) {
        return compress(new File(imagePath), config);
    }

    public LTask compress(File imageFile) {
        return compress(imageFile, null);
    }

    public LTask compress(File imageFile, LConfig config) {
        List<File> list = new ArrayList<>();
        list.add(imageFile);
        return compressImages(list, config);
    }

    public LTask compress(List<String> images) {
        return compress(images, commConfig);
    }

    public LTask compress(List<String> images, LConfig config) {
        List<File> list = new ArrayList<>();
        for (String image : images)
            list.add(new File(image));
        return compressImages(list, config);
    }

    private LTask compressImages(List<File> images, LConfig config) {
        return new LTask(images, config == null ? commConfig : config);
    }

    public static Lisa getInstance() {
        if (_instance == null) {
            synchronized (Lisa.class) {
                if (_instance == null) {
                    _instance = new Lisa();
                }
            }
        }
        return _instance;
    }

    protected void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    public void cancel() {
        executor.shutdown();
        executor.shutdownNow();
    }

    protected boolean isCancel() {
        return executor.isShutdown();
    }

    protected void post(Runnable runnable) {
        mainHandler.post(runnable);
    }

    public LConfig copyImageConfig() {
        return commConfig.copy();
    }

    public Lisa maxWidth(int maxWidth) {
        commConfig.setMaxWidth(maxWidth);
        return this;
    }

    public Lisa maxHeight(int maxHeight) {
        commConfig.setMaxHeight(maxHeight);
        return this;
    }

    public Lisa compressFormat(CompressFormat compressFormat) {
        commConfig.setCompressFormat(compressFormat);
        return this;
    }

    public Lisa quality(int quality) {
        commConfig.setQuality(quality);
        return this;
    }

    public Lisa descDir(File descDir) {
        commConfig.setDescDir(descDir);
        return this;
    }
}
