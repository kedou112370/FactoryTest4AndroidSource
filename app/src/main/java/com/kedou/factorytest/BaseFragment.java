package com.kedou.factorytest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kedou.factorytest.util.Utils;

import java.util.List;
import static com.kedou.factorytest.util.Utils.isReadNv;

/**
 * @author kedou
 */
public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";

    protected Button mButtonFail;
    protected Button mButtonPass;
    protected MainActivity mContext;
    protected FactoryTestApplication mApp;
    private List<Testor> mTestors = null;

    protected final static int KEYCODE_HOME = 1;
    protected final static int KEYCODE_BACK = 1 << 1;
    protected final static int KEYCODE_MENU = 1 << 2;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
        mApp = (FactoryTestApplication)(mContext.getApplication());
        mTestors = mApp.getEnabledTest();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBtnViews();

        getView().setFocusable(true);
        getView().setFocusableInTouchMode(true);
        getView().setOnKeyListener(mKeyListener);
    }

    private void initBtnViews() {
        mButtonPass = getView().findViewById(R.id.passButton);
        mButtonFail = getView().findViewById(R.id.failButton);
        if (mButtonFail == null || mButtonPass == null) {
            return;
        }
        mButtonPass.setOnClickListener(view -> buttonClickEvent(true));
        mButtonFail.setOnClickListener(view -> buttonClickEvent(false));
    }

    private void buttonClickEvent(boolean isPositiveType) {
        int i = matchTestIndexInEnabledList();
        if (i == -1) {
            Log.e(TAG, "ERROR not find the testItem in the EnabledList");
        }
        Testor t = mTestors.get(i);
        t.setResultCode(isPositiveType ? TResult.SUCCES : TResult.FAILED);
        writeItemTestResult2Nv(t.getNvIndex(), isPositiveType ? TResult.P : TResult.F);
        mTestors.set(i, t);
        mApp.setEnabledTest(mTestors);
        if (Utils.toStartAutoTest) {
            if (i < mTestors.size() - 1) {
                Class<? extends BaseFragment> nextCla = mTestors.get(i + 1).getTestCla();
                mContext.addFragmentClass(nextCla, "");
                mContext.removeFragment(this);
            } else {
                boolean autoResu = mTestors.stream().anyMatch(x -> x.getResultCode() == TResult.FAILED);
                int nv;
                switch (TCondition.TEST_TYPE) {
                    case TCondition.MOBILE:
                        nv = 45;
                        break;
                    case TCondition.PCBA:
                        nv = 49;
                        break;
                    case TCondition.MANUAL:
                        nv = 37;
                        break;
                    default:
                        nv = 45;
                        break;
                }
                TCondition.TEST_TYPE = "";
                Utils.writeProInfo(autoResu ? TResult.P : TResult.F, nv);
                mApp.setFilterEnabledTest(null);
                mContext.addFragment(new TestReportFrag(), "TestReportFrag");
            }
        } else {
            mContext.popBackStack();
        }
    }

    private int matchTestIndexInEnabledList() {
        for (int i = 0; i < mTestors.size(); i++) {
            if (this.getClass().getSimpleName().equals(mTestors.get(i).getTestCla().getSimpleName())) {
                return i;
            }
        }
        return -1;
    }

    private void writeItemTestResult2Nv(int index, String result) {
        if (isReadNv() && index >= 0) {
            Utils.writeProInfo(result, index);
        }
    }

    protected int interceptKey() {
        return KEYCODE_BACK;
    }

    private View.OnKeyListener mKeyListener = (view, keyCode, keyEvent) -> {
        int interceptKeys = interceptKey();
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                if ((interceptKeys & KEYCODE_HOME) == KEYCODE_HOME) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_BACK:
                if ((interceptKeys & KEYCODE_BACK) == KEYCODE_BACK) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                if ((interceptKeys & KEYCODE_MENU) == KEYCODE_MENU) {
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    };
}
