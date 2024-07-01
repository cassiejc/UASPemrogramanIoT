package com.example.uaspemrogramaniot;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private MqttHelper mqttHelper;
    private TextView ldrSensorValueTextView;
    private TextView tvFull;
    private TextView tvCarCount;
    private ImageView irSensorStatusImageView1;
    private ImageView irSensorStatusImageView2;
    private ImageView irSensorStatusImageView3;
    private ChartManager chartManager;
    private FirestoreManager firestoreManager;
    private SensorManager sensorManager;
    private Handler handler;
    private final int UPDATE_INTERVAL = 2000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ldrSensorValueTextView = findViewById(R.id.ldrSensorValueTextView);
        tvFull = findViewById(R.id.tvFull);
        tvCarCount = findViewById(R.id.tvCarCount);
        irSensorStatusImageView1 = findViewById(R.id.irSensorStatusImageView);
        irSensorStatusImageView2 = findViewById(R.id.irSensorStatusImageView2);
        irSensorStatusImageView3 = findViewById(R.id.irSensorStatusImageView3);
        LineChart ldrChart = findViewById(R.id.ldrChart);

        mqttHelper = new MqttHelper(this);


        chartManager = new ChartManager(ldrChart);
        firestoreManager = new FirestoreManager();
        sensorManager = new SensorManager(irSensorStatusImageView1, irSensorStatusImageView2, irSensorStatusImageView3, tvFull);

        handler = new Handler(Looper.getMainLooper());
        startRepeatingTask();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttHelper.disconnect();
    }

    private void startRepeatingTask() {
        updateChartRunnable.run();
    }

    private void stopRepeatingTask() {
        handler.removeCallbacks(updateChartRunnable);
    }

    private Runnable updateChartRunnable = new Runnable() {
        @Override
        public void run() {
            chartManager.updateChart();
            handler.postDelayed(this, UPDATE_INTERVAL);
        }

    };

    public void updateLdrSensorValue(String sensorValue){
        runOnUiThread(() -> {
            ldrSensorValueTextView.setText("LDR Sensor Value: " + sensorValue);
            chartManager.addLdrData(Float.parseFloat(sensorValue));
        });
        firestoreManager.addLdrData(sensorValue);
    }

    public void updateIrSensorValue1(String sensorValue) {
        runOnUiThread(() -> sensorManager.updateIrSensorValue1(sensorValue));
    }

    public void updateIrSensorValue2(String sensorValue) {
        runOnUiThread(() -> sensorManager.updateIrSensorValue2(sensorValue));
    }

    public void updateIrSensorValue3(String sensorValue) {
        runOnUiThread(() -> sensorManager.updateIrSensorValue3(sensorValue));
    }



    public void updateCarCount(String carCount) {
        runOnUiThread(() -> tvCarCount.setText("Car Count: " + carCount));
        firestoreManager.addCarCount(carCount);

    }

}