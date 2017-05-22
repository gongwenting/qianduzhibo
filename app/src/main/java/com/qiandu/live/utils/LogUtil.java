package com.qiandu.live.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import com.qiandu.live.BuildConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * @description: Log 工具类
 *
 * @author: Andruby
 * @time: 2016/12/17 10:23
 */
public class LogUtil {
    //关闭debug
    private static final boolean DEBUBABLE = BuildConfig.DEBUG;
    private static HashMap<String, Long> timeHashMap;

    public static void v(String tag, String msg) {
        if (DEBUBABLE) {
            Log.e(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUBABLE) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUBABLE) {
            Log.e(tag, msg);
        }
    }
    public static void e(String tag, String msg,Exception e) {
        if (DEBUBABLE) {
            Log.e(tag, msg,e);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUBABLE) {
            Log.i(tag, msg);
        }
    }

    /**
     * Enables strict mode. This should only be called when debugging the application and is useful
     * for finding some potential bugs or best practice violations.
     */
    @TargetApi(11)
    public static void enableStrictMode() {
        // Strict mode is only available on gingerbread or later
        if (LogUtil.hasGingerbread()) {

            // Enable all thread strict mode policies
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            // Enable all VM strict mode policies
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            // Honeycomb introduced some additional strict mode features
            if (LogUtil.hasHoneycomb()) {
                // Flash screen when thread policy is violated
                threadPolicyBuilder.penaltyFlashScreen();
                // For each activity class, set an instance limit of 1. Any more instances and
                // there could be a memory leak.
                //                vmPolicyBuilder
                //                        .setClassInstanceLimit(HomeActivity.class, 1);
            }

            // Use builders to enable strict mode policies
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    /**
     * Uses static final constants to detect if the device's platform version is Gingerbread or
     * later.
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * Uses static final constants to detect if the device's platform version is Honeycomb or
     * later.
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }


    public static boolean IS_WRITE = false;

    public static HashMap<String, Long> mMap = new HashMap<String, Long>();
    public static File mfile;

    public synchronized static void write(String key, String tag, long value) {
        if (!IS_WRITE) {
            mfile = new File(getLogDir());
            if (mfile.exists()) {
                mfile.delete();
            }
            IS_WRITE = !IS_WRITE;
        }
        Long pre = mMap.get(key);
        if (pre == null) {
            pre = 0l;
        }
        Long minus = value - pre;
        if (minus > 10000000) {
            minus = 0L;
        }
        try {
            if (mfile == null) {
                mfile = new File(getLogDir());
                if (!mfile.exists()) {
                    createFile(mfile);
                }
            }
            long length = mfile.length();
            RandomAccessFile output = new RandomAccessFile(mfile, "rw");
            //            DataOutputStream output = new DataOutputStream(o);
            String sss = key + "        " + tag + "        " + minus + '\n';
            output.seek(length);
            output.write(sss.getBytes());
            //            output.write(minus.);
            //            output.writeUTF(tag+"\n");
            output.close();
            //TODO:这个输出流不关闭，影响速度，这里只是测试
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.put(key, value);
    }

    /**
     * @param key  标志
     * @param tag  标志
     * @param time
     */
    public static void writeLog(String key, String tag, long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String strTime = format.format(time);

        String logInfo = key + " \t - " + tag + " \t -  " + time + " - " + strTime + "\n";
        Log.e("yanbin ", "logInfo = " + logInfo);

        File logFile = new File(getLogDir());
        if (!logFile.exists()) {
            try {
                createFile(logFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(logFile, "rw");
            randomAccessFile.seek(logFile.length());
            randomAccessFile.write(logInfo.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param key  用于排重
     * @param tag
     * @param time
     */
    public static void writeLogInterval(String key, String tag, long time) {

        long interval = 0L;
        if (timeHashMap == null) {
            timeHashMap = new HashMap<>();
        }
        if (timeHashMap.containsKey(key)) {
            Long before = timeHashMap.get(key);
            interval = time - before;
        }

        timeHashMap.put(key, time);

        String logInfo = tag + " \t -  " + interval + "\n";
        Log.e("yanbin ", "logInfo = " + logInfo);

        File logFile = new File(getLogDir());
        if (!logFile.exists()) {
            try {
                createFile(logFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(logFile, "rw");
            randomAccessFile.seek(logFile.length());
            randomAccessFile.write(logInfo.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getLogDir() {
        String path = "";
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        path = absolutePath + File.separator + "sou_log" + File.separator + "log.txt";
        return path;
    }

    public static void createFile(File _file) throws IOException {
        if (!_file.exists()) {
            File parent = _file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            _file.createNewFile();
        }
    }
}
