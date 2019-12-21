package com.kedou.factorytest.util;

import android.text.TextUtils;

/**
 * @author kedou
 * @data 2019/12/18
 */
public class SystemConfig {

    private static String getProp(String propName){
        return "";//SystemProperties.get(propName);
    }

    public static final String DEFAULT_LANG_EN = getProp("ro.pri_factory_default_lang_en");

    public static final String CUSTOMER_PROJECT = getProp("ro.pri_customer");

    public static final String GSM_SERIAL = getProp("vendor.gsm.serial");

    public static final String NETWORK_BAND = getProp("ro.pri_board_network_type");

    public static final String NETWORK_TYPE = TextUtils.isEmpty(NETWORK_BAND.trim()) ? "CB/LfB/LtB/WB/TB/GB" : NETWORK_BAND;

    public static final String HOR_CALI = getProp("ro.hor_cali");
}
