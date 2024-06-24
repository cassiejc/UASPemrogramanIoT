package com.example.uaspemrogramaniot;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private MqttHelper mqttHelper;
    private TextView mq5SensorValueTextView;
//    private TextView irSensorValueTextView;
    private ImageView irSensorStatusImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mq5SensorValueTextView = findViewById(R.id.mq5SensorValueTextView);
//        irSensorValueTextView = findViewById(R.id.irSensorValueTextView);
        irSensorStatusImageView = findViewById(R.id.irSensorStatusImageView);
        mqttHelper = new MqttHelper(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttHelper.disconnect();
    }

    public void updateMq5SensorValue(String sensorValue){
        runOnUiThread(() -> {
            mq5SensorValueTextView.setText("MQ-5 Sensor Value: " + sensorValue);
        });
    }

    public void updateIrSensorValue(String sensorValue) {
        runOnUiThread(() -> {
//            irSensorValueTextView.setText("IR Sensor Value: " + sensorValue);
            if (sensorValue.equals("Car Detected")) {
//                irSensorValueTextView.setTextColor(Color.RED);
                irSensorStatusImageView.setImageResource(R.drawable.red_indicator);
            } else {
//                irSensorValueTextView.setTextColor(Color.GREEN);
                irSensorStatusImageView.setImageResource(R.drawable.green_indicator);
            }
        });
    }

}