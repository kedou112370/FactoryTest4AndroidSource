package com.kedou.factorytest.util;

import java.util.ArrayList;
import java.util.List;

public class NvramUtils {
    private static String TAG = "NvramUtils";
    public static String PRIZE_USER_INFO_PATH = "/mnt/vendor/nvdata/APCFG/APRDEB/PRIZE_USER_INFO";

    public static int PRIZE_USER_PUBLIC_INFO_OFFSET = 0;
    public static int PRIZE_USER_CUSTOM_IMEI1_OFFSET = PRIZE_USER_PUBLIC_INFO_OFFSET+0;
    public static int PRIZE_USER_CUSTOM_IMEI1_LENGTH = 15;
    public static int PRIZE_USER_CUSTOM_IMEI1_END = 14;
    public static int PRIZE_USER_CUSTOM_IMEI2_OFFSET = 15;
    public static int PRIZE_USER_CUSTOM_IMEI2_LENGTH = 15;
    public static int PRIZE_USER_CUSTOM_IMEI2_END = 29;
    public static int PRIZE_USER_CUSTOM_MEID_OFFSET = 30;
    public static int PRIZE_USER_CUSTOM_MEID_LENGTH = 14;
    public static int PRIZE_USER_CUSTOM_MEID_END = 43;
    public static int PRIZE_USER_PUBLIC_INFO_END = 149;

    public static int PRIZE_USER_OS_INFO_OFFSET = 150;
    public static int PRIZE_USER_OS_INFO_END = 299;

    public static int PRIZE_USER_PCBA_INFO_OFFSET = 300;
    public static int PRIZE_USER_PCBA_INFO_END = 349;

    public static int PRIZE_USER_KOOBEE_INFO_OFFSET = 350;
    public static int PRIZE_USER_KOOBEE_INFO_END = 399;

    public static int PRIZE_USER_ODM_INFO_OFFSET = 400;
    public static int PRIZE_USER_ODM_INFO_END = 449;

    public static String PRIZE_FACTORY_INFO_PATH = "/mnt/vendor/nvdata/APCFG/APRDEB/PRIZE_FACTORY_INFO";
    public static int PRIZE_FACTORY_BARCODE_OFFSET = 0;
    public static int PRIZE_FACTORY_BBARCODE_END= 149;
    public static int PRIZE_FACTORY_FACTORY_INFO_OFFSET = 150;
    public static int PRIZE_FACTORY_FACTORY_INFO_END= 349;
    private static List<String> WHITE_LIST_PACKAGE = new ArrayList<>();
    static {
        WHITE_LIST_PACKAGE.add("com.kedou.factorytest");
        WHITE_LIST_PACKAGE.add("com.prize.autotest");
    }

    public static boolean writeFactoryNvramInfo(int offset,int length,String info){
        return writeNvramInfo(PRIZE_FACTORY_INFO_PATH,offset,length,info);
    }

    public static boolean writeUserNvramInfo(int offset,int length,String info){
        return writeNvramInfo(PRIZE_USER_INFO_PATH,offset,length,info);
    }

    public static String readFactoryNvramInfo(int offset,int length){
        return getNvramInfo(PRIZE_FACTORY_INFO_PATH,offset,length);
    }

    public static String readUserNvramInfo(int offset,int length){
        return getNvramInfo(PRIZE_USER_INFO_PATH,offset,length);
    }

    public static boolean writeNvramInfo(String fileName,int offset,int length,String info){
        /*boolean writeFlag = false;
        String buff = null;
        try{
            INvram agent = INvram.getService();
            if (agent == null) {
                Log.e(TAG, "NvRAMAgent is null write failed");
                return writeFlag;
            }

            byte[] infoByte = info.getBytes();
            try {
                buff = agent.readFileByName(
                        fileName, offset + length);
            } catch (Exception e) {
                e.printStackTrace();

            }
            // Remove \0 in the end
            byte[] buffArr = HexDump.hexStringToByteArray(
                    buff.substring(0, buff.length() - 1));
            for (int i = 0; i < length; i ++) {
                buffArr[i + offset] = infoByte[i];
            }

            ArrayList<Byte> dataArray = new ArrayList<Byte>(
                    offset + length);
            for (int i = 0; i < offset + length; i++) {
                dataArray.add(i, new Byte(buffArr[i]));
            }

            try {
                int flag = agent.writeFileByNamevec(fileName,
                        offset + length, dataArray);
                writeFlag = !(flag < 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch(Exception e){

        }
        Log.d(TAG,"write "+fileName +" reslut:"+writeFlag);
        return  writeFlag;*/
        return true;
    }

    public static String getNvramInfo(String fileName,int offset,int length){
        /*String result = null;
        String buff = null;
        StringBuffer sb = null;
        try {
            INvram agent = INvram.getService();
            if (agent == null) {
                Log.e(TAG, "NvRAMAgent is null read failed");
                return result;
            }
            try {
                buff = agent.readFileByName(
                        fileName, offset + length);
                *//*result = new String(HexDump.hexStringToByteArray(
                        buff.substring(0, buff.length() - 1)));
                result = result.substring(offset, result.length() - 1);*//*
                byte[] buffArr = HexDump.hexStringToByteArray(buff.substring(0, buff.length() - 1));
                char c = 0;
                sb = new StringBuffer();
                for (byte b : buffArr) {
                    c = (char) b;
                    sb.append(String.valueOf(c));
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, " getNvramInfo buff=" + String.valueOf(buff) *//*+ ",result=" + result*//*);
        return sb == null ? null : sb.toString();*/
        return  "";
    }
}
