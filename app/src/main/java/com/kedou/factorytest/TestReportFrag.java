package com.kedou.factorytest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kedou.factorytest.util.Utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.kedou.factorytest.util.Utils.isReadNv;

/**
 * @author kedou
 */
public class TestReportFrag extends BaseFragment {
    private TextView mTestReport;
    private ListView testReportListView;
    private TestReportAdapter testReportAdapter;
    private String mLastOperation = "";
    private Button mClearReport = null;
    private List<Testor> mTestors = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.testreport, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View root) {
        mTestReport = root.findViewById(R.id.testreport_show);
        testReportListView = root.findViewById(R.id.testreport_lv);
        mClearReport = root.findViewById(R.id.clear_report);
        mClearReport.setVisibility(isReadNv() ? View.VISIBLE : View.GONE);
        mTestReport.setText(showCurrentTime());
        mClearReport.setOnClickListener((view) -> {
            String spaceChars94 = String.format("%94s", "");
            Utils.writeProInfo(spaceChars94, Utils.PRIZE_FACTORY_FACTORY_INFO_OFFSET);

            testReportDisplay();
        });
    }

    @Override
    protected int interceptKey() {
        return KEYCODE_BACK & KEYCODE_HOME & KEYCODE_MENU;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTestors = mApp.getFilterEnabledTest() == null ? mApp.getEnabledTest() : mApp.getFilterEnabledTest();
        testReportDisplay();
    }

    private String showCurrentTime() {
        String temp = "";
        SimpleDateFormat formatter = new SimpleDateFormat(getResources().getString(R.string.date_format));
        Date curDate = new Date(System.currentTimeMillis());
        temp = formatter.format(curDate);
        return temp;
    }

    private void testReportDisplay() {
        String nvInfo = null;
        if (isReadNv()) {
            Testor testor = mTestors.stream().max(Comparator.comparing(Testor::getNvIndex)).get();
            nvInfo = Utils.readProInfo(Utils.PRIZE_FACTORY_FACTORY_INFO_OFFSET + testor.getNvIndex(), 1);
        }
        ArrayList<HashMap<String, String>> testReportList = getListViewData(nvInfo);
        testReportAdapter = new TestReportAdapter(mContext, testReportList);
        testReportListView.setAdapter(testReportAdapter);
    }

    private ArrayList<HashMap<String, String>> getListViewData(String nvInfo) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        final String fail = getString(R.string.result_error);
        final String pass = getString(R.string.pass);
        final String noTest = getString(R.string.no_test);
        for (int index = 0; index < mTestors.size(); index++) {
            Testor t = mTestors.get(index);
            String iResult = Optional.ofNullable(nvInfo).map(x -> {
                int nvIndex = Utils.PRIZE_FACTORY_FACTORY_INFO_OFFSET + t.getNvIndex();
                if (nvInfo.length() <= nvIndex) {
                    return noTest;
                }
                String str = String.valueOf(x.charAt(nvIndex)).trim();
                return "".equals(str) ? noTest : TResult.P.equals(str) ? pass : fail;
            }).orElse(t.getResultCode() == TResult.SUCCES ? pass
                    : t.getResultCode() == TResult.FAILED ? fail : noTest);

            HashMap<String, String> testReportInfo = new HashMap<String, String>();
            testReportInfo.put("key", getString(t.getLocalNameId()) + ":");
            testReportInfo.put("value", iResult);

            list.add(testReportInfo);
        }
        return list;
    }

    public class TestReportAdapter extends BaseAdapter {
        Context context;
        ArrayList<HashMap<String, String>> list;
        String fail = getString(R.string.result_error);
        String pass = getString(R.string.pass);

        public TestReportAdapter(Context context, ArrayList<HashMap<String, String>> snList) {
            this.context = context;
            list = snList;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.testreport_item, parent, false);
                holder = new ViewHolder();
                holder.textViewItem01 = convertView.findViewById(R.id.key);
                holder.textViewItem02 = convertView.findViewById(R.id.value);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            HashMap<String, String> item = list.get(position);
            String key = item.get("key");
            String value = item.get("value");
            holder.textViewItem01.setText(key);
            holder.textViewItem02.setText(value);
            if (pass.equals(value)) {
                holder.textViewItem01.setTextColor(Color.WHITE);
                holder.textViewItem02.setTextColor(Color.GREEN);
            } else if (fail.equals(value)) {
                holder.textViewItem01.setTextColor(Color.WHITE);
                holder.textViewItem02.setTextColor(Color.RED);
            } else {
                holder.textViewItem01.setTextColor(Color.WHITE);
                holder.textViewItem02.setTextColor(Color.GRAY);
            }
            return convertView;
        }
    }

    public class ViewHolder {
        TextView textViewItem01;
        TextView textViewItem02;
    }
}
