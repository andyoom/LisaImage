package com.andy.lisa;

import android.graphics.Bitmap.CompressFormat;

import java.io.File;

/**
 * 类描述：
 * 创建人：yekh
 * 创建时间：2018/10/24 10:31
 */
public class LConfig {
    public int maxWidth;//宽
    public int maxHeight;//高
    public CompressFormat compressFormat;//图片格式
    public int quality;//压缩率
    public File descDir;//压缩目标目录

    protected LConfig() {
        maxWidth = 540;
        maxHeight = 960;
        compressFormat = CompressFormat.JPEG;
        quality = 50;
    }

    protected LConfig(LConfig config) {
        maxWidth = config.maxWidth;
        maxHeight = config.maxHeight;
        compressFormat = config.compressFormat;
        quality = config.quality;
        descDir = config.descDir;
    }

    public LConfig setMaxWidth(int maxWidth) {
        if (maxWidth > 0)
            this.maxWidth = maxWidth;
        return this;
    }

    public LConfig setMaxHeight(int maxHeight) {
        if (maxHeight > 0)
            this.maxHeight = maxHeight;
        return this;
    }

    public LConfig setCompressFormat(CompressFormat compressFormat) {
        if (compressFormat != null)
            this.compressFormat = compressFormat;
        return this;
    }

    public LConfig setQuality(int quality) {
        if (quality >= 30)
            this.quality = quality;
        return this;
    }

    public LConfig setDescDir(File descDir) {
        if (descDir != null) {
            if (descDir.exists()) {
                if (descDir.isDirectory()) {
                    this.descDir = descDir;
                }
            } else {
                if (descDir.mkdir()) {
                    this.descDir = descDir;
                }
            }
        }
        return this;
    }

    public LConfig copy() {
        return new LConfig(this);
    }
}
