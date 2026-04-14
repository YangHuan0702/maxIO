package com.halosky;

import lombok.Data;

/**
 * packageName com.halosky
 *
 * @author huan.yang
 * @className ConfigObjectItem
 * @date 2026/4/14
 * @description 元数据中的block信息
 */
@Data
public class ConfigObjectItem {

    private String fileName;
    private String hostName;
    private int num;
    private boolean isOrigin;
    private String filePath;


}
