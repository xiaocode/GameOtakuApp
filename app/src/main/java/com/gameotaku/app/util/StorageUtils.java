package com.gameotaku.app.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;

/**
 * Created by Vincent on 8/29/14.
 */
public class StorageUtils {

    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/";
    public static String FILE_ROOT = SDCARD_ROOT + "DuduluGame/";

    public static final String ICON_ROOT = FILE_ROOT + "cache_images/";

    private static final long LOW_STORAGE_THRESHOLD = 1024 * 1024 * 10;

    public static boolean isSdCardWrittenable() {

        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 设置文件保存的路径 例如 /mnt/sdcard/bugull/meeting/apk/
     */
    public static void setFilePath(String path) {
        FILE_ROOT = path;
    }

    public static long getAvailableStorage() {

        String storageDirectory = null;
        storageDirectory = Environment.getExternalStorageDirectory().toString();

        try {
            StatFs stat = new StatFs(storageDirectory);
            long avaliableSize = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
            return avaliableSize;
        } catch (RuntimeException ex) {
            return 0;
        }
    }

    public static boolean checkAvailableStorage() {

        if (getAvailableStorage() < LOW_STORAGE_THRESHOLD) {
            return false;
        }

        return true;
    }

    public static boolean isSDCardPresent() {

        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void mkdir() throws IOException {

        File file = new File(FILE_ROOT);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static Bitmap getLoacalBitmap(String url) {

        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String size(long size) {

        if (size / (1024 * 1024) > 0) {
            float tmpSize = (float) (size) / (float) (1024 * 1024);
            DecimalFormat df = new DecimalFormat("#.##");
            return "" + df.format(tmpSize) + "MB";
        } else if (size / 1024 > 0) {
            return "" + (size / (1024)) + "KB";
        } else
            return "" + size + "B";
    }

    public static void installAPK(Context context, final String url) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        String fileName = FILE_ROOT + NetworkUtils.getFileNameFromUrl(url);
        intent.setDataAndType(Uri.fromFile(new File(fileName)),
                "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName("com.android.packageinstaller",
                "com.android.packageinstaller.PackageInstallerActivity");
        context.startActivity(intent);
    }

    public static void openAPK(PackageManager pm, String packageName, Context context) {
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    public static boolean delete(File path) {

        boolean result = true;
        if (path.exists()) {
            if (path.isDirectory()) {
                for (File child : path.listFiles()) {
                    result &= delete(child);
                }
                result &= path.delete(); // Delete empty directory.
            }
            if (path.isFile()) {
                result &= path.delete();
            }
            if (!result) {
                Log.e(null, "Delete failed;");
            }
            return result;
        } else {
            Log.e(null, "File does not exist.");
            return false;
        }
    }

    public static void deleteTempAndFile(String url) {
        String mUrl = null;
        String tempFileName = null;

        try {
            mUrl = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            tempFileName = new File(new URL(url).getFile()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (mUrl != null && tempFileName != null) {
            String baseName = FilenameUtils.getBaseName(mUrl);
            String extension = FilenameUtils.getExtension(mUrl);
            String fileName = baseName + "." + extension;
            File file = new File(StorageUtils.FILE_ROOT, fileName);
            File tempFile = new File(StorageUtils.FILE_ROOT, tempFileName + ".download");

            if (file.exists())
                file.delete();

            if (tempFile.exists())
                file.delete();
        }
    }
}
