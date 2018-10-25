package com.andy.lisa;

import java.util.List;

/**
 * 类描述：
 * 创建人：yekh
 * 创建时间：2018/10/24 11:40
 */
public interface LCallback {
    /**
     * 成功
     * @param items
     */
    void ok(List<String> items);

    /**
     * 失败
     * @param errMsg
     */
    void err(String errMsg);
}
