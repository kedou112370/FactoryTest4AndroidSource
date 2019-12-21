package com.kedou.factorytest.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.kedou.factorytest.TResult;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.kedou.factorytest.util.SystemConfig.CUSTOMER_PROJECT;
import static com.kedou.factorytest.util.SystemConfig.DEFAULT_LANG_EN;
import static com.kedou.factorytest.util.SystemConfig.NETWORK_TYPE;

public class Utils {
    private static boolean DEBUG = true;
    private static String TAG = "PrizeFactoryTestUtils";
    public static final String PRODUCT_INFO_FILENAME = "/vendor/nvdata/APCFG/APRDEB/PRODUCT_INFO";

    private static long lastClickTime;

    public static boolean toStartAutoTest = false;

    public static final int PRIZE_FACTORY_FACTORY_INFO_OFFSET = 100;

    public static String CURRENT_LAN = "1".equals(DEFAULT_LANG_EN) ? "en_US" : "zh_CN";
    private static Context mContext;

    public static long ONE_G = 1 * 1024 * 1024 * 1024L;
    public static long TWO_G = 2 * 1024 * 1024 * 1024L;
    public static long THR_G = 3 * 1024 * 1024 * 1024L;
    public static long FOUR_G = 4 * 1024 * 1024 * 1024L;
    public static long SIX_G = 6 * 1024 * 1024 * 1024L;
    public static long EIGHT_G = 8 * 1024 * 1024 * 1024L;
    private static long KB = 1024L;
    private static long MB = 1024L * KB;
    private static long GB = 1024L * MB;

    public synchronized static boolean isNoNFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            if (DEBUG) {
                Log.i("Utils", "------fast click-----");
            }
            return false;
        }
        lastClickTime = time;
        if (DEBUG) {
            Log.i("Utils", "-----NON-fast click-----");
        }
        return true;
    }

    public static void setGlobalContext(Context context) {
        mContext = context;
    }

    public static String convertNvChars(String conStr) {
        String str = Optional.ofNullable(conStr).orElse("");
        int strLength = str.length();
        for (int i = 0; i < strLength; i++) {
            char ch = str.charAt(i);
            if ((ch >= 0x00 && ch <= 0x08)
                    || (ch >= 0x0b && ch <= 0x0c)
                    || (ch >= 0x0e && ch <= 0x1f)) {
                str = str.replace(ch, ' ');
            }
        }

        return str;
    }

    /**
     * @param sn
     * @param index
     */
    public static void writeProInfo(String sn, int index) {
        if (null == sn || sn.length() < 1) {
            Log.e("Utils", "---writeProInfo result length is less 1,not collect ");
            return;
        }
        NvramUtils.writeFactoryNvramInfo(index, sn.length(), sn);
        if (!Stream.of(36, 37, 45, 49).anyMatch(x -> x == index)) {
            return;
        }
        NvramUtils.writeNvramInfo(PRODUCT_INFO_FILENAME, index, sn.length(), sn);
    }

    /**
     * @param offset
     * @param length
     * @return
     */
    public static String readProInfo(int offset, int length) {
        return convertNvChars(NvramUtils.readFactoryNvramInfo(offset, length));
    }

    public static boolean isPhoneCalibration() {
        if (CUSTOMER_PROJECT.contains("pcba")) {
            return true;
        }
        String antResult = readProInfo(41, 8);
        boolean wbg = getAntPass(antResult, 41);
        boolean tb = true;
        boolean wb = true;
        boolean cb = true;
        boolean lte = true;
        boolean gb = true;
        if (!wbg) {
            return false;
        }
        if (NETWORK_TYPE.contains("TB")) {
            tb = getAntPass(antResult, 43);
        }
        if (NETWORK_TYPE.contains("WB")) {
            wb = getAntPass(antResult, 44);
        }
        if (NETWORK_TYPE.contains("CB")) {
            cb = getAntPass(antResult, 46);
        }
        if (NETWORK_TYPE.contains("LtB") || NETWORK_TYPE.contains("LfB")) {
            lte = getAntPass(antResult, 47);
        }
        if (NETWORK_TYPE.contains("GB")) {
            gb = getAntPass(antResult, 48);
        }
        return wb && cb && lte && gb;
    }

    public static boolean getAntPass(String antResult, int index) {
        String result = null;
        if (antResult.length() > index) {
            result = String.valueOf(antResult.charAt(index));
            result = result.toUpperCase();
        }
        return TResult.P.equals(result);
    }

    public static boolean isFileExists(String path) {
        return Optional.ofNullable(path).map(File::new).map(File::exists).orElse(false);
    }

    public static String readContextFromStream(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = bis.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, length));
            }
            bis.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static Context createConfigurationResources(String language) {
        Resources resources = mContext.getResources();
        Configuration configuration = resources.getConfiguration();

        switch (language) {
            case "en_US":
                Log.i(TAG, "---createConfigurationResources-en_us");
                configuration.setLocale(Locale.ENGLISH);
                break;
            case "zh_CN":
                Log.i(TAG, "---createConfigurationResources-zh_cn");
                configuration.setLocale(Locale.SIMPLIFIED_CHINESE);
                break;
            default:
                configuration.setLocale(Locale.ENGLISH);
                break;
        }
        return mContext.createConfigurationContext(configuration);
    }

    public static boolean hasComponentPkg(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = mContext.getPackageManager().getApplicationInfo(packageName, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasSystemFeature(String name) {
        PackageManager pm = mContext.getPackageManager();
        return pm.hasSystemFeature(name);
    }

    public static boolean isSupportDoubuleCameraStand() {
        PackageManager pm = mContext.getPackageManager();
        Intent intent = new Intent("com.kb.action.FACTORY_CAMERA");
        List<ResolveInfo> list = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return Optional.ofNullable(list).map(x -> x.size() > 0).orElse(false);
    }

    public static boolean isWiredHeadsetPluggedIn(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);
        Collection<Integer> deviceColl =
                Arrays.stream(devices).map(AudioDeviceInfo::getType).collect(Collectors.toList());
        return Stream.of(AudioDeviceInfo.TYPE_WIRED_HEADPHONES,
                AudioDeviceInfo.TYPE_WIRED_HEADSET, AudioDeviceInfo.TYPE_USB_HEADSET,
                AudioDeviceInfo.TYPE_USB_DEVICE).anyMatch(x -> deviceColl.contains(x));
    }

    public static void writeFile(String filePath, String value) {
        writeFile(filePath, value.getBytes(Charset.defaultCharset()));
    }

    public static void writeFile(String filePath, byte[] data) {
        try {
            FileOutputStream fout = new FileOutputStream(filePath);
            fout.write(data);
            fout.flush();
            fout.close();
            Log.d(TAG, "writeFile succcess");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * recommand use this to global read file util fun
     *
     * @param filePath
     * @param onlyReadFirstLine
     * @return
     */
    public static String readFile(String filePath, boolean onlyReadFirstLine) {
        try (BufferedReader bufferedReader =
                     new BufferedReader(new FileReader(new File(filePath)))) {
            if (onlyReadFirstLine) {
                return bufferedReader.readLine();
            } else {
                Stream<String> linesStream = bufferedReader.lines();
                StringBuilder stringBuilder = new StringBuilder();
                linesStream.forEach(x -> stringBuilder.append(x));
                return stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getStackTrace()[0].toString();
        }
    }

    public static boolean isFrontFlashSupport() {
        CameraManager cameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] ids = cameraManager.getCameraIdList();
            for (String id : ids) {
                CameraCharacteristics c = cameraManager.getCameraCharacteristics(id);
                Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                Integer lensFacing = c.get(CameraCharacteristics.LENS_FACING);
                //Log.i(TAG, "----id:" + id + "||flashAvailable:" + flashAvailable.booleanValue() + "||lensFacing:" + lensFacing.intValue());
                if ("1".equals(id) && flashAvailable != null && flashAvailable
                        && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                    return true;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isReadNv() {
        return Stream.of("pcba-sea").anyMatch(x -> x.equals(CUSTOMER_PROJECT));
    }

    public static int string2IntDissSpace(String str) {
        String initString = null;
        if (!TextUtils.isEmpty(str)) {
            char[] charArray = str.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (char c : charArray) {
                if (c >= 0x30 && c <= 0x39) {
                    sb.append(c);
                }
            }
            initString = sb.toString();
        }
        try {
            return Integer.parseInt(initString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatDataSize(long size) {
        String sizeString;
        if (size < KB && size > 0) {
            sizeString = size + "B";
        } else if (size < MB) {
            sizeString = Math.round(size * 1.0 / KB) + "KB";
        } else if (size < GB) {
            sizeString = Math.round(size * 1.0 / MB) + "MB";
        } else {
            sizeString = Math.round(size * 1.0 / GB) + "GB";
        }
        return sizeString;
    }

    public static String filterNumChars(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        char[] charArray = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : charArray) {
            if (c >= 0x30 && c <= 0x39) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static void toastShow(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }
}
