package com.example.uaspemrogramaniot;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private MqttHelper mqttHelper;
    private TextView ldrSensorValueTextView;
    private ImageView irSensorStatusImageView1;
    private ImageView irSensorStatusImageView2;
    private ImageView irSensorStatusImageView3;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ldrSensorValueTextView = findViewById(R.id.ldrSensorValueTextView);
        irSensorStatusImageView1 = findViewById(R.id.irSensorStatusImageView);
        irSensorStatusImageView2 = findViewById(R.id.irSensorStatusImageView2); // Initialize irSensorStatusImageView2
        irSensorStatusImageView3 = findViewById(R.id.irSensorStatusImageView3); // Initialize irSensorStatusImageView3
        mqttHelper = new MqttHelper(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttHelper.disconnect();
    }

    public void updateLdrSensorValue(String sensorValue){
        runOnUiThread(() -> {
            ldrSensorValueTextView.setText("LDR Sensor Value: " + sensorValue);
            uploadLdrSensorValueToFirebase(sensorValue);
        });
    }

    private void uploadLdrSensorValueToFirebase(String sensorValue) {
        try {
            databaseReference.child("ldrSensor").setValue(sensorValue);
        } catch (Exception e) {
            Log.e(TAG, "Error uploading MQ-5 sensor value to Firebase", e);
        }
    }

    public void updateIrSensorValue1(String sensorValue) {
        runOnUiThread(() -> {
            if (sensorValue.equals("Car Detected")) {
                irSensorStatusImageView1.setImageResource(R.drawable.red_indicator);
            } else {
                irSensorStatusImageView1.setImageResource(R.drawable.green_indicator);
            }
        });
    }

    public void updateIrSensorValue2(String sensorValue) {
        runOnUiThread(() -> {
            if (sensorValue.equals("Car Detected")) {
                irSensorStatusImageView2.setImageResource(R.drawable.red_indicator);
            } else {
                irSensorStatusImageView2.setImageResource(R.drawable.green_indicator);
            }
        });
    }

    public void updateIrSensorValue3(String sensorValue) {
        runOnUiThread(() -> {
            if (sensorValue.equals("Car Detected")) {
                irSensorStatusImageView3.setImageResource(R.drawable.red_indicator);
            } else {
                irSensorStatusImageView3.setImageResource(R.drawable.green_indicator);
            }
        });
    }

}