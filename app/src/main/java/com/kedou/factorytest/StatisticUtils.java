package com.kedou.factorytest;

import com.kedou.factorytest.annotation.NvIndex;
import com.kedou.factorytest.annotation.OrderIndex;
import com.kedou.factorytest.annotation.TestorLabel;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author kedou
 */
public class StatisticUtils {
    private final static String FIELD_TESTOR_ENABLED = "TESTOR_ENABLED";

    public static List<Testor> getAllTestorInSystem(){
        StatisticOfTestItem statistic = new StatisticOfTestItemImpl(BaseFragment.class);
        return statistic.getStatisticValues();
    }

    public static int getTestorNvIndex(Class<?> zz) {
        if (zz.isAnnotationPresent(NvIndex.class)) {
            NvIndex nvIndex = zz.getAnnotation(NvIndex.class);
            return nvIndex.index();
        } else {
            android.util.Log.e("FactoryTest4And", "The " + zz.getClass().getName() + " nvIndex is not has," +
                    " Must be have with NvIndex Annotation!");
            return -1;
        }
    }

    public static int getTestorName(Class<?> zz) {
        if (zz.isAnnotationPresent(TestorLabel.class)) {
            TestorLabel label = zz.getAnnotation(TestorLabel.class);
            return label.labelId();
        } else {
            android.util.Log.e("FactoryTest4And", "The " + zz.getClass().getName() + " name is not has, " +
                    "strongly recommend add it with TestorLabel Annotation");
            return -1;
        }
    }

    public static int getTestorOrderInd(Class<?> zz) {
        if (zz.isAnnotationPresent(OrderIndex.class)) {
            OrderIndex order = zz.getAnnotation(OrderIndex.class);
            return order.orde();
        } else {
            android.util.Log.e("FactoryTest4And", "The " + zz.getClass().getName() + " order is not has, " +
                    "recommend add it with OrderIndex Annotation");
            return Integer.MAX_VALUE;
        }
    }

    public static boolean getTestorEnabled(Class<?> zz) {
        try {
            Field field = zz.getField(FIELD_TESTOR_ENABLED);
            return (Boolean) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

}
