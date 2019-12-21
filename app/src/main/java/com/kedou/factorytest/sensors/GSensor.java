package com.kedou.factorytest.sensors;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.kedou.factorytest.BaseFragment;
import com.kedou.factorytest.MainActivity;
import com.kedou.factorytest.annotation.NvIndex;
import com.kedou.factorytest.annotation.OrderIndex;
import com.kedou.factorytest.R;
import com.kedou.factorytest.annotation.TestorLabel;
import com.kedou.factorytest.annotation.TypeTestor;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.SENSOR_SERVICE;
import static com.kedou.factorytest.util.SystemConfig.HOR_CALI;
import static com.kedou.factorytest.util.Utils.toastShow;

/**
 * @author kedou
 */
@TypeTestor(type = "MOBILE")
@TypeTestor(type = "PCBA")
@TypeTestor(type = "MANUAL")
@OrderIndex(orde = 13)
@NvIndex(index = 23)
@TestorLabel(labelId = R.string.gsensor_name)
@AutoService(BaseFragment.class)
public class GSensor extends BaseFragment {
    private static final String TAG = "GSensor_1";
    private SensorManager mSensorManager = null;
    private Sensor mGSensor = null;
    private GSensorListener mGSensorListener;
    private TextView mTextView;
    private ImageView imageView;
    private final double DEVIATION = 3;
    private final static int SENSOR_TYPE = Sensor.TYPE_ACCELEROMETER;
    private final static int SENSOR_DELAY = SensorManager.SENSOR_DELAY_FASTEST;

    private boolean bLeft = false;
    private boolean bRight = false;
    private boolean bsensor = true;
    private final static boolean HAS_HOR_CALI = "1".equals(HOR_CALI);

    private void getService() {
        mSensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        if (mSensorManager == null) {
            fail(getString(R.string.service_get_fail));
        }

        mGSensor = mSensorManager.getDefaultSensor(SENSOR_TYPE);
        if (mGSensor == null) {
            fail(getString(R.string.sensor_get_fail));
        }

        mGSensorListener = new GSensorListener();
        if (!mSensorManager.registerListener(mGSensorListener, mGSensor,
                SENSOR_DELAY)) {
            fail(getString(R.string.sensor_register_fail));
        }
    }

    private void updateView(String s) {
        mTextView.setText(getString(R.string.gsensor_name) + " : " + s);
        mButtonPass.setEnabled(bLeft && bRight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (HAS_HOR_CALI) {
            Intent intent = new Intent();
            intent.setClassName("com.android.HorCali",
                    "com.android.HorCali.sensor.SensorCalibration");
            intent.putExtra("gsensor_factorytest", true);
            intent.putExtra("gsensor_autotest", false);
            try {
                startActivityForResult(intent, 0);
            } catch (Exception e) {
                e.printStackTrace();
                getService();
            }
        } else {
            getService();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gsensor, container, false);
        mTextView = view.findViewById(R.id.gsensor_result);
        imageView = view.findViewById(R.id.gsensor_image);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getService();
        } else {
            mButtonPass.setEnabled(false);
            mTextView.setText(getString(R.string.gsensor_name) + " : " + "sensorCalibration fail!");
        }
    }

    private void fail(String msg) {
        toastShow(msg);
        mContext.removeFragment(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSensorManager == null || mGSensorListener == null
                || mGSensor == null) {
            return;
        }
        mSensorManager.unregisterListener(mGSensorListener, mGSensor);
    }

    public class GSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            synchronized (this) {
                if (event.sensor.getType() == SENSOR_TYPE) {
                    String value = "(x:" + event.values[0] + ", y:"
                            + event.values[1] + ", z:" + event.values[2] + ")";
                    Log.d(TAG, "value=" + value);

                    if (event.values[0] > 0 + DEVIATION && event.values[0] > Math.abs(event.values[1])) {
                        bLeft = true;
                        imageView.setBackgroundResource(R.drawable.gsensor_left);
                        Log.d(TAG, "bLeft=true");
                    } else if (event.values[0] < 0 - DEVIATION && Math.abs(event.values[0]) > Math.abs(event.values[1])) {
                        bRight = true;
                        imageView.setBackgroundResource(R.drawable.gsensor_right);
                        Log.d(TAG, "bRight=true");
                    } else if (event.values[1] < 0 - DEVIATION && Math.abs(event.values[0]) < Math.abs(event.values[1])) {
                        imageView.setBackgroundResource(R.drawable.gsensor_up);
                    } else if (event.values[1] > 0 + DEVIATION && Math.abs(event.values[0]) < event.values[1]) {
                        imageView.setBackgroundResource(R.drawable.gsensor_down);
                    }
                    if (bsensor) {
                        bsensor = false;
                        imageView.setBackgroundResource(R.drawable.gsensor_down);
                    }

                    updateView(value);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }
    }
}
