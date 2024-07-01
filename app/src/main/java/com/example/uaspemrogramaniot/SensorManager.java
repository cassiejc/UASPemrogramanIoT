package com.example.uaspemrogramaniot;

import android.widget.ImageView;
import android.widget.TextView;

public class SensorManager {
    private boolean isCarDetected1 = false;
    private boolean isCarDetected2 = false;
    private boolean isCarDetected3 = false;
    private ImageView irSensorStatusImageView1;
    private ImageView irSensorStatusImageView2;
    private ImageView irSensorStatusImageView3;
    private TextView tvFull;

    public SensorManager(ImageView irSensor1, ImageView irSensor2, ImageView irSensor3, TextView tvFull) {
        this.irSensorStatusImageView1 = irSensor1;
        this.irSensorStatusImageView2 = irSensor2;
        this.irSensorStatusImageView3 = irSensor3;
        this.tvFull = tvFull;

    }

    public void updateIrSensorValue1(String sensorValue) {
            isCarDetected1 = sensorValue.equals("Car Detected");
            if (sensorValue.equals("Car Detected")) {
                irSensorStatusImageView1.setImageResource(R.drawable.red_indicator);
            } else {
                irSensorStatusImageView1.setImageResource(R.drawable.green_indicator);
            }
            updateTvFullStatus();

    }

    public void updateIrSensorValue2(String sensorValue) {
            isCarDetected2 = sensorValue.equals("Car Detected");
            if (sensorValue.equals("Car Detected")) {
                irSensorStatusImageView2.setImageResource(R.drawable.red_indicator);
            } else {
                irSensorStatusImageView2.setImageResource(R.drawable.green_indicator);
            }
            updateTvFullStatus();
    }

    public void updateIrSensorValue3(String sensorValue) {
            isCarDetected3 = sensorValue.equals("Car Detected");
            if (sensorValue.equals("Car Detected")) {
                irSensorStatusImageView3.setImageResource(R.drawable.red_indicator);
            } else {
                irSensorStatusImageView3.setImageResource(R.drawable.green_indicator);
            }
            updateTvFullStatus();
    }


    private void updateTvFullStatus() {
        if (isCarDetected1 && isCarDetected2 && isCarDetected3) {
            tvFull.setVisibility(TextView.VISIBLE);
        } else {
            tvFull.setVisibility(TextView.INVISIBLE);
        }
    }

}
