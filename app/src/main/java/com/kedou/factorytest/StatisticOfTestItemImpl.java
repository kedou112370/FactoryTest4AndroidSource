package com.kedou.factorytest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * @author kedou
 */
public class StatisticOfTestItemImpl<T> implements StatisticOfTestItem {
    private final List<Testor> sTestClass;

    @Override
    public List<Testor> getStatisticValues() {
        return sTestClass;
    }

    public StatisticOfTestItemImpl(Class<T> testorParent) {
        List<T> testorClassList = getChildClassByAutoService(testorParent);
        List<Testor> testors = testorClassList.stream().map(T::getClass)
                .sorted(Comparator.comparing(StatisticUtils::getTestorOrderInd))
                .map(StatisticOfTestItemImpl::createTestor).collect(Collectors.toList());
        sTestClass = new ArrayList<>();
        sTestClass.addAll(testors);
    }

    private static <T> List<T> getChildClassByAutoService(Class<T> superCla) {
        ServiceLoader<T> sLoader = ServiceLoader.load(superCla);
        List<T> childList = new ArrayList<>();
        Iterator<T> iterator = sLoader.iterator();
        while (iterator.hasNext()) {
            T t = iterator.next();
            childList.add(t);
        }
        return childList;
    }

    private static <T> Testor<T> createTestor(Class<T> c) {
        return Testor.createTestor(StatisticUtils.getTestorNvIndex(c), c, StatisticUtils.getTestorEnabled(c),
                StatisticUtils.getTestorName(c));
    }
}
