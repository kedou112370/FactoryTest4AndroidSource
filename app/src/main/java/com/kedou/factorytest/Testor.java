package com.kedou.factorytest;

/**
 * @author kedou
 */
public class Testor<T> {
    private final int nvIndex;
    private final Class<T> testCla;
    private final boolean testEnable;
    private final int localNameId;
    private int resultCode;

    public Testor(int nvIndex, Class<T> testCla, boolean testEnable, int localNameId){
        this.nvIndex = nvIndex;
        this.testCla = testCla;
        this.testEnable = testEnable;
        this.localNameId = localNameId;
    }

    public final int getNvIndex() {
        return nvIndex;
    }

    public final Class<T> getTestCla() {
        return testCla;
    }

    public final boolean getTestEnabled() {
        return testEnable;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public final int getLocalNameId() {
        return localNameId;
    }

    public static <T> Testor createTestor(int nvIndex, Class<T> testCla, boolean testEnable, int localNameId) {
        return new Testor(nvIndex, testCla, testEnable, localNameId);
    }
}
