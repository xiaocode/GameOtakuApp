/**
 * Copyright (c) www.bugull.com
 */
package com.gameotaku.app.db;

public class DownloadConstants {

    public static final String SERVICE_ACTION = "com.dudulu.app.download.service.DownloadService";
    public static final String RECEIVER_ACTION = "com.dudulu.downloader.receiver";
    public static final String INSTALL_ACTION = "com.dudulu.installer.receiver";
    public static final String NETWORK_CHANGE_ACTION = "com.dudulu.network";

    public static final int INSTALL_ACTION_UNINSTALLED = 0;
    public static final int INSTALL_ACTION_INSTALLED = 1;

    /**
     * 还没有下载 初始默认状态
     */
    public static final int STATUS_DEFAULT = 0;//这种状态要注意加一个判断：是否已经下载完成并安装完成
    /**
     * 添加到下载列表中 但未开始下载
     */
    public static final int STATUS_WAIT = 1;
    /**
     * 正在下载
     */
    public static final int STATUS_DOWNLOADING = 2;
    /**
     * 下载完成等待安装
     */
    public static final int STATUS_COMPLETED = 3;
    /**
     * 暂停下载
     */
    public static final int STATUS_PAUSE = 4;

    /**
     * 下载完成并安装完成  注：这种状态不在数据库中体现，需要结合数据库和安装情况来决定
     */
    public static final int STATUS_INSTALLED = 5;

    /**
     * 提示更新
     */
    public static final int STATUS_UPDATE = 6;

    /**
     * 下载错误
     */
    public static final int STATUS_ERROR = 9;

    /**
     * 已卸载
     */
    public static final int STATUS_UNINSTALLED = -1;

}
