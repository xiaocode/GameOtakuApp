package com.gameotaku.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.gameotaku.app.BootstrapApplication;

import java.util.UUID;

/**
 * Created by Vincent on 8/29/14.
 */
public class NetworkUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED
                            || info[i].getState() == NetworkInfo.State.CONNECTING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isWiFi(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            boolean isWiFi = activeNetwork != null &&
                    activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            if (isConnected && isWiFi) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isOnlyWiFi() {
        String isOnlyWiFi = ConfigUtils.getString(BootstrapApplication.getInstance(), "isOnlyWiFi");
        return isOnlyWiFi.equals("") || isOnlyWiFi.equals("true");
    }

    public static void setOnlyWiFi(boolean is_only) {
        String field = is_only ? "true" : "false";
        ConfigUtils.setString(BootstrapApplication.getInstance(), "isOnlyWiFi", field);
    }

    public static String getFileNameFromUrl(String url) {
        // 通过 ‘？’ 和 ‘/’ 判断文件名
        int index = url.lastIndexOf('?');
        String filename;
        if (index > 1) {
            filename = url.substring(url.lastIndexOf('/') + 1, index);
        } else {
            filename = url.substring(url.lastIndexOf('/') + 1);
        }

        if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
            filename = UUID.randomUUID() + ".apk";// 默认取一个文件名
        }
        return filename;
    }
}
