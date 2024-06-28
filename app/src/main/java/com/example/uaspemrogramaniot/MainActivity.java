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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private MqttHelper mqttHelper;
    private TextView ldrSensorValueTextView;
    private TextView tvFull;
    private ImageView irSensorStatusImageView1;
    private ImageView irSensorStatusImageView2;
    private ImageView irSensorStatusImageView3;
    private FirebaseFirestore db;
    private LineChart ldrChart;
    private ArrayList<Float> ldrData;
    private static final String TAG = "MainActivity";
    private Handler handler;
    private final int UPDATE_INTERVAL = 2000;

    private boolean isCarDetected1 = false;
    private boolean isCarDetected2 = false;
    private boolean isCarDetected3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ldrSensorValueTextView = findViewById(R.id.ldrSensorValueTextView);
        tvFull = findViewById(R.id.tvFull);
        irSensorStatusImageView1 = findViewById(R.id.irSensorStatusImageView);
        irSensorStatusImageView2 = findViewById(R.id.irSensorStatusImageView2);
        irSensorStatusImageView3 = findViewById(R.id.irSensorStatusImageView3);
        ldrChart = findViewById(R.id.ldrChart);

        mqttHelper = new MqttHelper(this);

        db = FirebaseFirestore.getInstance();

        ldrData = new ArrayList<>();
        setupLineChart();

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
            LineData lineData = ldrChart.getData();
            if (lineData != null) {
                LineDataSet dataSet = (LineDataSet) lineData.getDataSetByIndex(0);
                if (dataSet != null) {
                    dataSet.clear();
                    for (int i = 0; i< ldrData.size(); i++) {
                        dataSet.addEntry(new Entry(ldrData.get(i), i));
                    }
                }
                ldrChart.notifyDataSetChanged();
                ldrChart.invalidate();
            }
//            setupLineChart();
            handler.postDelayed(this, UPDATE_INTERVAL);
        }
    };

    public void updateLdrSensorValue(String sensorValue){
        runOnUiThread(() -> {
            ldrSensorValueTextView.setText("LDR Sensor Value: " + sensorValue);
            ldrData.add(Float.parseFloat(sensorValue));
            setupLineChart();
        });
        Map<String, Object> data = new HashMap<>();
        data.put("value", sensorValue);
        data.put("timestamp", System.currentTimeMillis());

        db.collection("ldrSensorValues")
                .add(data)
                .addOnSuccessListener(documentReference ->  Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    private List<ILineDataSet> getLineDataset(){
//        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        ArrayList<Entry> valueset = new ArrayList<>();

        for (int i = 0; i < ldrData.size(); i++) {
            valueset.add(new Entry(ldrData.get(i), i));
        }

        LineDataSet lineDataSet = new LineDataSet(valueset, "LDR Sensor Data");
        lineDataSet.setColor(Color.rgb(0, 200, 0));
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setCircleColor(Color.rgb(0, 200, 0));
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setFillAlpha(65);
        lineDataSet.setFillColor(Color.rgb(0, 200, 0));
        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        lineDataSet.setDrawCircleHole(false);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        return dataSets;
    }

    private List<String> getLineLabel(){
        ArrayList<String> xlabel = new ArrayList<>();
        for (int i = 0; i < ldrData.size(); i++) {
            xlabel.add("Data " + (i + 1));
        }
        return xlabel;
    }

    private void setupLineChart() {
        LineData lineData = new LineData(getLineLabel(), getLineDataset());
        ldrChart.setData(lineData);
        ldrChart.setDescription("LDR Sensor Data");
        ldrChart.animateX(2000);
        ldrChart.invalidate();
    }

    public void updateIrSensorValue1(String sensorValue) {
        runOnUiThread(() -> {
            isCarDetected1 = sensorValue.equals("Car Detected");
            if (sensorValue.equals("Car Detected")) {
                irSensorStatusImageView1.setImageResource(R.drawable.red_indicator);
            } else {
                irSensorStatusImageView1.setImageResource(R.drawable.green_indicator);
            }
            updateTvFullStatus();
        });
    }

    public void updateIrSensorValue2(String sensorValue) {
        runOnUiThread(() -> {
            isCarDetected2 = sensorValue.equals("Car Detected");
            if (sensorValue.equals("Car Detected")) {
                irSensorStatusImageView2.setImageResource(R.drawable.red_indicator);
            } else {
                irSensorStatusImageView2.setImageResource(R.drawable.green_indicator);
            }
            updateTvFullStatus();
        });
    }

    public void updateIrSensorValue3(String sensorValue) {
        runOnUiThread(() -> {
            isCarDetected3 = sensorValue.equals("Car Detected");
            if (sensorValue.equals("Car Detected")) {
                irSensorStatusImageView3.setImageResource(R.drawable.red_indicator);
            } else {
                irSensorStatusImageView3.setImageResource(R.drawable.green_indicator);
            }
            updateTvFullStatus();
        });
    }

    private void updateTvFullStatus() {
        if (isCarDetected1 && isCarDetected2 && isCarDetected3) {
            tvFull.setVisibility(TextView.VISIBLE);
        } else {
            tvFull.setVisibility(TextView.INVISIBLE);
        }
    }

}