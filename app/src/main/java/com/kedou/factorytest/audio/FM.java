package com.kedou.factorytest.audio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.kedou.factorytest.BaseFragment;
import com.kedou.factorytest.annotation.NvIndex;
import com.kedou.factorytest.annotation.OrderIndex;
import com.kedou.factorytest.annotation.TestorLabel;
import com.kedou.factorytest.annotation.TypeTestor;
import com.kedou.factorytest.R;

import static com.kedou.factorytest.util.Utils.hasComponentPkg;

@TypeTestor(type = "MOBILE")
@TypeTestor(type = "PCBA")
@TypeTestor(type = "MANUAL")
@OrderIndex(orde = 10)
@NvIndex(index = 20)
@TestorLabel(labelId = R.string.fm_name)
@AutoService(BaseFragment.class)
public class FM extends BaseFragment {
    public static final boolean TESTOR_ENABLED = hasComponentPkg("com.android.fmradio");
    private final int REQUEST_CODE = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startFM();
    }

    private void startFM() {
        Intent intent = new Intent();
        intent.setClassName("com.android.fmradio",
                "com.android.fmradio.FmMainActivity");
        intent.putExtra("FACTORYFM", "factoryfm");
        startActivityForResult(intent, REQUEST_CODE);
    }
}
