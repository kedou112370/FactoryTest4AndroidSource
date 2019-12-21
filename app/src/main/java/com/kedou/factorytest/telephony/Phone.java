package com.kedou.factorytest.Phone;

import android.content.Intent;
import android.net.Uri;
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
import com.kedou.factorytest.R;
import com.kedou.factorytest.annotation.TestorLabel;

/**
 * @author kedou
 */
@OrderIndex(orde = 11)
@NvIndex(index = 21)
@TestorLabel(labelId = R.string.phone_name)
@AutoService(BaseFragment.class)
public class Phone extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.phone, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL_PRIVILEGED");
        intent.setData(Uri.parse("tel:" + 112));
        startActivity(intent);
    }
}
