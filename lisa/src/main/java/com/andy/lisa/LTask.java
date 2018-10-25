package com.andy.lisa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：yekh
 * 创建时间：2018/10/24 11:25
 */
public class LTask implements LCompress, Runnable {
    private List<File> images;
    private LConfig config;
    private LCallback callback;

    public LTask(List<File> images, LConfig config) {
        this.images = images;
        this.config = config;
    }

    @Override
    public void compress(LCallback callback) {
        this.callback = callback;
        Lisa.getInstance().execute(this);
    }

    @Override
    public void run() {
        compress();
    }

    private void compress() {
        if (!Lisa.getInstance().isCancel()) {
            try {
                List<String> listResult = compressImage();
                if (listResult != null) {
                    ok(listResult);
                } else {
                    error("解析图片失败");
                }
            } catch (Exception e) {
                L.e("压缩图片异常：" + e);
                error("压缩图片异常");
            }
        }
    }

    private void ok(final List<String> listResult) {
        Lisa.getInstance().post(new Runnable() {
            @Override
            public void run() {
                if (callback != null && !Lisa.getInstance().isCancel()){
                    callback.ok(listResult);
                }
            }
        });
    }

    private void error(final String errMsg) {
        Lisa.getInstance().post(new Runnable() {
            @Override
            public void run() {
                if (callback != null && !Lisa.getInstance().isCancel()){
                    callback.err(errMsg);
                }
            }
        });
    }

    private List<String> compressImage() throws Exception {
        List<String> list = new ArrayList<>(images.size());
        File dir = config.descDir;
        if (dir == null) {
            dir = images.get(0).getParentFile();
        }
        for (File srcFile : images) {
            String item = compressImage(dir, srcFile);
            if (item == null) {
                return null;
            }
            list.add(item);
        }
        return list;
    }

    private String compressImage(File dir, File srcFile) throws Exception {
        File descFile = new File(dir, "1-" + srcFile.getName());
        if (descFile.exists())
            descFile.delete();
        descFile.createNewFile();
        Bitmap bitmap = null;
        FileOutputStream fos = null;
        try {
            bitmap = decodeBitmap(srcFile, config.maxWidth, config.maxHeight);
            fos = new FileOutputStream(descFile);
            bitmap.compress(config.compressFormat, config.quality, fos);
            return descFile.getAbsolutePath();
        } catch (Exception e) {
            L.e("解析bitmap异常：" + e);
            return null;
        } finally {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    private Bitmap decodeBitmap(File imageFile, int reqWidth, int reqHeight) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        Bitmap scaledBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
        Matrix matrix = new Matrix();
        if (orientation == 6) {
            matrix.postRotate(90);
        } else if (orientation == 3) {
            matrix.postRotate(180);
        } else if (orientation == 8) {
            matrix.postRotate(270);
        }
        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return scaledBitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
