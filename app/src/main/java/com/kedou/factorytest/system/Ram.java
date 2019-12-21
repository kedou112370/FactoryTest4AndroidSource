package com.kedou.factorytest.system;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.kedou.factorytest.BaseFragment;
import com.kedou.factorytest.annotation.NvIndex;
import com.kedou.factorytest.annotation.OrderIndex;
import com.kedou.factorytest.R;
import com.kedou.factorytest.annotation.TestorLabel;
import com.kedou.factorytest.annotation.TypeTestor;

import static com.kedou.factorytest.util.Utils.EIGHT_G;
import static com.kedou.factorytest.util.Utils.FOUR_G;
import static com.kedou.factorytest.util.Utils.ONE_G;
import static com.kedou.factorytest.util.Utils.SIX_G;
import static com.kedou.factorytest.util.Utils.THR_G;
import static com.kedou.factorytest.util.Utils.TWO_G;
import static com.kedou.factorytest.util.Utils.formatDataSize;

/**
 * @author kedou
 */
@TypeTestor(type = "MOBILE")
@OrderIndex(orde = 12)
@NvIndex(index = 22)
@TestorLabel(labelId = R.string.ram_name)
@AutoService(BaseFragment.class)
public class Ram extends BaseFragment {
    public static final boolean TESTOR_ENABLED = true;
    private ActivityManager mAm = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAm = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ram, container, false);
        TextView totalRam = view.findViewById(R.id.ram_total);
        TextView freeRam = view.findViewById(R.id.ram_free);
        totalRam.setText(getString(R.string.total_memory) + formatDataSize(getTotalMemory()));
        freeRam.setText(getString(R.string.available_memory) + formatDataSize(getAvailableMemory()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private long getTotalMemory() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mAm.getMemoryInfo(memoryInfo);
        long totalMem = memoryInfo.totalMem;
        long totalCustom = 0L;
        if (totalMem <= ONE_G) {
            totalCustom = ONE_G;
        } else if (totalMem > ONE_G && totalMem <= TWO_G) {
            totalCustom = TWO_G;
        } else if (totalMem > TWO_G && totalMem <= THR_G) {
            totalCustom = THR_G;
        } else if (totalMem > THR_G && totalMem <= FOUR_G) {
            totalCustom = FOUR_G;
        } else if (totalMem > FOUR_G && totalMem <= SIX_G) {
            totalCustom = SIX_G;
        } else if (totalMem > SIX_G && totalMem <= EIGHT_G) {
            totalCustom = EIGHT_G;
        }
        return totalCustom;
    }

    private long getAvailableMemory() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mAm.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }
}