package com.kedou.factorytest;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kedou.factorytest.util.Utils;

/**
 * @author kedou
 */
public class MainActivity extends FragmentActivity {
    private final static String TAG = "PrizeBaseActivity";
    protected FactoryTestApplication mApp;

    @Override
    protected void attachBaseContext(Context newBase) {
        String language = Utils.CURRENT_LAN;
        Utils.setGlobalContext(newBase);
        Context context = Utils.createConfigurationResources(language);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (FactoryTestApplication) getApplication();
        setContentView(R.layout.activity_main);

        addFragment(new MainFragment(), "MainFrag");
    }

    public void addFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container_main, fragment, tag);
        transaction.addToBackStack("back");
        transaction.commit();
    }

    public void addFragmentClass(Class<? extends BaseFragment> clazz, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container_main, clazz, null, tag);
        transaction.addToBackStack("back");
        transaction.commit();
    }

    public void removeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    public void popBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment f = getSupportFragmentManager().findFragmentByTag("MainFrag");
        if (f == null) {
            addFragment(new MainFragment(), "MainFrag");
        }
    }
}
