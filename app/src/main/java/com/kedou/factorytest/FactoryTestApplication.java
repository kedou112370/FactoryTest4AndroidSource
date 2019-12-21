package com.kedou.factorytest;

import android.app.Application;

import com.kedou.factorytest.util.SharedPreferencesHelper;
import com.kedou.factorytest.util.Utils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kedou
 */
public class FactoryTestApplication extends Application {
    private static final String TAG = "FactoryTestApplication";
    private SharedPreferencesHelper shared_pref;
    private String defDuration = "00:00:00";

    private List<Testor> mEnabledTest = null;
    private List<Testor> mFilterEnabledTest = null;

    @Override
    public void onCreate() {
        super.onCreate();
        shared_pref = new SharedPreferencesHelper(this, "com.kedou.factorytest");
        Utils.setGlobalContext(getApplicationContext());
        initAllTest();
        loadDefaultDuration();
        loadDefaultAgingTestItem();
        loadDefaultAgingTestTime();
        loadCurrentTimeMillis();
        loadDefaultTestReport();
    }

    private void initAllTest() {
        List<Testor> allTest = StatisticUtils.getAllTestorInSystem();
        mEnabledTest = allTest.stream().filter(Testor::getTestEnabled).collect(Collectors.toList());
    }

    public List<Testor> getEnabledTest() {
        return mEnabledTest;
    }

    public synchronized void setEnabledTest(List<Testor> lists) {
        mEnabledTest = lists;
    }

    public List<Testor> getFilterEnabledTest() {
        return mFilterEnabledTest;
    }

    public void setFilterEnabledTest(List<Testor> mFilterEnabledTest) {
        this.mFilterEnabledTest = mFilterEnabledTest;
    }

    private void loadDefaultTestReport() {
        if (shared_pref.getValue("reboot_result") == null) {
            shared_pref.putValue("reboot_result", "untest");
        }
        if (shared_pref.getValue("sleep_result") == null) {
            shared_pref.putValue("sleep_result", "untest");
        }
    }

    private void loadDefaultAgingTestItem() {
        if (shared_pref.getValue("reboot_selected") == null) {
            shared_pref.putValue("reboot_selected", "0");
        }
        if (shared_pref.getValue("sleep_selected") == null) {
            shared_pref.putValue("sleep_selected", "0");
        }
        if (shared_pref.getValue("video_speaker_selected") == null) {
            shared_pref.putValue("video_speaker_selected", "1");
        }
        if (shared_pref.getValue("video_receiver_selected") == null) {
            shared_pref.putValue("video_receiver_selected", "1");
        }
        if (shared_pref.getValue("vibrate_selected") == null) {
            shared_pref.putValue("vibrate_selected", "1");
        }
        if (shared_pref.getValue("mic_loop_selected") == null) {
            shared_pref.putValue("mic_loop_selected", "1");
        }
        if (shared_pref.getValue("front_camera_selected") == null) {
            shared_pref.putValue("front_camera_selected", "1");
        }
        if (shared_pref.getValue("back_camera_selected") == null) {
            shared_pref.putValue("back_camera_selected", "1");
        }
        ///ddr select init
        if (shared_pref.getValue("ddr_test_start") == null) {
            shared_pref.putValue("ddr_test_start", "1");
        }

        if (shared_pref.getValue("ddr_test") == null) {
            shared_pref.putValue("ddr_test", "0");
        }
        if (shared_pref.getValue("ddr_test_circles") == null) {
            shared_pref.putValue("ddr_test_circles", "0");
        }
        if (shared_pref.getValue("ddr_test_count") == null) {
            shared_pref.putValue("ddr_test_count", "1");
        }
        if (Utils.isFileExists("/proc/hall1/m1120_up") || Utils.isFileExists("/proc/hall2/m1120_down")) {
            if (shared_pref.getValue("front_motor_test_start") == null) {
                shared_pref.putValue("front_motor_test_start", "1");
            }
            if (shared_pref.getValue("front_motor_test_count") == null) {
                shared_pref.putValue("front_motor_test_count", "350");
            }
        }
    }

    //time is value seconds
    private void loadDefaultAgingTestTime() {
        if (shared_pref.getValue("reboot_time") == null) {
            shared_pref.putValue("reboot_time", "1800");
        }
        if (shared_pref.getValue("sleep_time") == null) {
            shared_pref.putValue("sleep_time", "1200");
        }
        if (shared_pref.getValue("parallel_time") == null) {
            shared_pref.putValue("parallel_time", "1800");
        }
    }

    private void loadCurrentTimeMillis() {
        if (shared_pref.getValue("reboot_currenttimemillis") == null) {
            shared_pref.putValue("reboot_currenttimemillis", String.valueOf(System.currentTimeMillis()));
        }
    }

    private void loadDefaultDuration() {
        if (shared_pref.getValue("video_speaker_duration") == null) {
            shared_pref.putValue("video_speaker_duration", defDuration);
        }
        if (shared_pref.getValue("video_receiver_duration") == null) {
            shared_pref.putValue("video_receiver_duration", defDuration);
        }
        if (shared_pref.getValue("vibrate_duration") == null) {
            shared_pref.putValue("vibrate_duration", defDuration);
        }
        if (shared_pref.getValue("mic_loop_duration") == null) {
            shared_pref.putValue("mic_loop_duration", defDuration);
        }
        if (shared_pref.getValue("front_camera_duration") == null) {
            shared_pref.putValue("front_camera_duration", defDuration);
        }
        if (shared_pref.getValue("back_camera_duration") == null) {
            shared_pref.putValue("back_camera_duration", defDuration);
        }
    }

    public SharedPreferencesHelper getSharePref() {
        return shared_pref;
    }

}
