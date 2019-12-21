package com.kedou.factorytest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.kedou.factorytest.annotation.TypeTestor;
import com.kedou.factorytest.annotation.TypesTestor;
import com.kedou.factorytest.util.Utils;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author kedou
 */
public class MainFragment extends BaseFragment {
    private static final String TAG = "MainFragment";

    private Button pcbaTestButton = null;
    private Button autoTestButton = null;
    private Button manualTestButton = null;
    private Button listtestButton = null;
    private Button testReportButton = null;
    private Button factorySetButton = null;
    private Button softInfoButton = null;
    private Button languageSwitchButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View main = inflater.inflate(R.layout.main, container, false);
        pcbaTestButton = main.findViewById(R.id.pcbatest);
        autoTestButton = main.findViewById(R.id.autotest);
        manualTestButton = main.findViewById(R.id.manualtest);
        listtestButton = main.findViewById(R.id.listtest);
        testReportButton = main.findViewById(R.id.testreport);
        factorySetButton = main.findViewById(R.id.factoryset);
        softInfoButton = main.findViewById(R.id.softinfo);
        languageSwitchButton = main.findViewById(R.id.languageswitch);
        return main;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        pcbaTestButton.setOnClickListener(view ->
                showDialog(R.string.pcbatest, R.string.pcbatest_confirm, (dialoginterface, i) -> startOverAllTest(TCondition.PCBA)));

        autoTestButton.setOnClickListener(view -> {
            if (!preStartAutoCit()) {
                return;
            }
            showDialog(R.string.phonetest, R.string.phonetest_confirm, (dialoginterface, i) -> startOverAllTest(TCondition.MOBILE));
        });

        manualTestButton.setOnClickListener(view -> {
            if (!preStartAutoCit()) {
                return;
            }
            showDialog(R.string.manualtest, R.string.manualtest_confirm, (dialoginterface, i) -> startOverAllTest(TCondition.MANUAL));
        });

        listtestButton.setOnClickListener(view -> {
            List<Testor> testors =
                    mApp.getEnabledTest().stream().map(x -> Testor.createTestor(x.getNvIndex(),
                            x.getTestCla(), x.getTestEnabled(), x.getLocalNameId())).collect(Collectors.toList());
            mApp.setEnabledTest(testors);
            mContext.addFragment(new ListTestFragment(), "ListTestFrag");
        });

        testReportButton.setOnClickListener(view -> {
            mContext.addFragment(new TestReportFrag(), "TestReportFrag");
        });

        factorySetButton.setOnClickListener(view ->
                showDialog(R.string.factoryset, R.string.factoryset_confirm, (dialoginterface, i) -> clearMaster()));

        softInfoButton.setOnClickListener(view -> {
        });

        languageSwitchButton.setOnClickListener(view -> switchLanguage());
    }

    private void startOverAllTest(String type) {
        TCondition.TEST_TYPE = type;
        Utils.toStartAutoTest = true;
        List<Testor> filterList = filterEnabledTestor(type);
        mApp.setFilterEnabledTest(filterList);
        Log.d(TAG, "--------INIT------items.length:" + filterList.size());

        mContext.addFragmentClass(filterList.get(0).getTestCla(), "");
    }

    private List<Testor> filterEnabledTestor(String type) {
        List<Testor> list = mApp.getEnabledTest();
        List<Testor> filterList= list.stream().filter(x -> {
            if (x.getTestCla().getClass().isAnnotationPresent(TypesTestor.class)) {
                TypesTestor types = x.getTestCla().getClass().getAnnotation(TypesTestor.class);
                for (TypeTestor t : types.value()) {
                    if(type.equals(t.type())){
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());
        return filterList;
    }

    @Override
    protected int interceptKey() {
        return 0;
    }

    private void clearMaster(){
        /*Intent intent = new Intent(Intent.ACTION_FACTORY_RESET);
        intent.setPackage("android");
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.putExtra(Intent.EXTRA_REASON, "MasterClearConfirm");
        intent.putExtra(Intent.EXTRA_WIPE_EXTERNAL_STORAGE,
                *//*mEraseSdCard*//*false);
        intent.putExtra(Intent.EXTRA_WIPE_ESIMS, *//*mEraseEsims*//*true);
        mContext.sendBroadcast(intent);*/
    }

    private void showDialog(int titleRes, int msgRes, DialogInterface.OnClickListener positListener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(
                mContext);
        dialog.setCancelable(false)
                .setTitle(titleRes)
                .setMessage(msgRes)
                .setPositiveButton(R.string.confirm, positListener)
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    /**
     * rebuild the process of the product,MMI test not limit the MT(BT/FT)
     *
     * @return TRUE:the MT nv test are all pass,false or else
     */
    private boolean preStartAutoCit() {
        if (!Utils.isPhoneCalibration()) {
            Utils.toastShow(getString(R.string.no_calibration));
            return false;
        }
        return true;
    }

    public void switchLanguage() {
        if (Utils.CURRENT_LAN.contains("zh")) {
            Configuration config = getResources().getConfiguration();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            config.locale = Locale.ENGLISH;
            Utils.CURRENT_LAN = "en_US";
            getResources().updateConfiguration(config, metrics);
        } else {
            Configuration config = getResources().getConfiguration();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            config.locale = Locale.SIMPLIFIED_CHINESE;
            Utils.CURRENT_LAN = "zh_CN";
            getResources().updateConfiguration(config, metrics);
        }
        /*Intent intent = new Intent();
        intent.setClass(this, PrizeFactoryTestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isAutoTest", true);
        startActivity(intent);*/
    }
}
