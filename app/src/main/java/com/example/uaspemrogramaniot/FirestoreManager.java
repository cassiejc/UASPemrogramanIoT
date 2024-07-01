package com.example.uaspemrogramaniot;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FirestoreManager {
    private FirebaseFirestore db;

    private static final String TAG = "FirestoreManager";

    public FirestoreManager() {
        db = FirebaseFirestore.getInstance();
    }

    public void addLdrData(String sensorValue) {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        Map<String, Object> data = new HashMap<>();
        data.put("time", currentTime);
        data.put("value", sensorValue);
        data.put("timestamp", System.currentTimeMillis());

        db.collection("ldrSensorValues")
                .add(data)
                .addOnSuccessListener(documentReference ->  Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    public void addCarCount(String carCount) {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        Map<String, Object> data = new HashMap<>();
        data.put("carCount", carCount);
        data.put("time", currentTime);
        data.put("timestamp", System.currentTimeMillis());

        db.collection("carCounts")
                .add(data)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

}
