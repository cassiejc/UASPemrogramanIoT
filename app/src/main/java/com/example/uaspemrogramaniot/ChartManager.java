package com.example.uaspemrogramaniot;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartManager {
    private LineChart ldrChart;
    private ArrayList<Float> ldrData;

    public ChartManager(LineChart chart) {
        this.ldrChart = chart;
        this.ldrData = new ArrayList<>();
        setupLineChart();
    }

    public void addLdrData(float value) {
        ldrData.add(value);
        setupLineChart();;
    }

    private List<ILineDataSet> getLineDataset(){
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

    public void updateChart() {
        LineData lineData = ldrChart.getData();
        if (lineData != null) {
            LineDataSet dataSet = (LineDataSet) lineData.getDataSetByIndex(0);
            if (dataSet != null) {
                dataSet.clear();
                for (int i = 0; i < ldrData.size(); i++) {
                    dataSet.addEntry(new Entry(ldrData.get(i), i));
                }
            }
            ldrChart.notifyDataSetChanged();
            ldrChart.invalidate();
        }
    }
}

