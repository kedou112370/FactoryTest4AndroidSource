package com.kedou.factorytest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kedou.factorytest.util.Utils;

import java.util.Comparator;
import java.util.List;

import static com.kedou.factorytest.util.Utils.isReadNv;

/**
 * @author kedou
 */
public class ListTestFragment extends BaseFragment implements OnItemClickListener {
    private static final String TAG = "FactoryTestListActivity";
    private FunnyLookingAdapter mFunnyLookingAdapter = null;
    private String mNvInfo = null;

    @Override
    protected int interceptKey() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        GridView grid = getView().findViewById(R.id.grid);
        List<Testor> enabledTest = mApp.getEnabledTest();
        if (isReadNv()) {
            int maxNvIndex = enabledTest.stream().max(Comparator.comparing(Testor::getNvIndex)).get().getNvIndex();
            mNvInfo = Utils.readProInfo(Utils.PRIZE_FACTORY_FACTORY_INFO_OFFSET + maxNvIndex, 1);
        }
        mFunnyLookingAdapter = new FunnyLookingAdapter(enabledTest);
        grid.setAdapter(mFunnyLookingAdapter);
        grid.setOnItemClickListener(this);
        mFunnyLookingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        List<Testor> enabledTest = mApp.getEnabledTest();
        mContext.addFragmentClass(enabledTest.get(position).getTestCla(), "");
    }

    private class FunnyLookingAdapter extends BaseAdapter {
        private List<Testor> testors;
        private LayoutInflater mInflater;

        FunnyLookingAdapter(List<Testor> list) {
            testors = list;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return testors.size();
        }

        @Override
        public Object getItem(int position) {
            return testors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item, null);
                viewHolder = new ViewHolder();
                viewHolder.label = convertView.findViewById(R.id.item_red);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Testor t = testors.get(position);
            viewHolder.label.setText(getString(t.getLocalNameId()));
            String nvValue = null;
            if (isReadNv()) {
                int nvIndex = Utils.PRIZE_FACTORY_FACTORY_INFO_OFFSET + t.getNvIndex();
                nvValue = String.valueOf(mNvInfo.charAt(nvIndex)).trim();
            }
            int resultCode = t.getResultCode();
            if (resultCode == TResult.SUCCES || TResult.P.equals(nvValue)) {
                viewHolder.label.setBackgroundColor(Color.GREEN);
            } else if (resultCode == TResult.FAILED || TResult.F.equals(nvValue)) {
                viewHolder.label.setBackgroundColor(Color.RED);
            } else if (resultCode == TResult.NOT_TEST || TResult.NT.equals(nvValue)) {
                viewHolder.label.setBackgroundColor(Color.GRAY);
            }
            return convertView;
        }
    }

    private class ViewHolder {
        TextView label;
    }
}
