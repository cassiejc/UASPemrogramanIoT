package com.example.uaspemrogramaniot;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttHelper {
    private static final String MQTT_BROKER = "tcp://192.168.1.152:1883";
    private static final String LDR_SENSOR_TOPIC = "ldrSensor";
    private static final String IR_SENSOR_TOPIC_1 = "irSensor1";
    private static final String IR_SENSOR_TOPIC_2 = "irSensor2";
    private static final String IR_SENSOR_TOPIC_3 = "irSensor3";
    private static final String GATE_COUNT_TOPIC = "gateCount";
    private static final String CLIENT_ID = "android_client";

    private MqttClient mqttClient;
    private MainActivity mainActivity;

    public MqttHelper(MainActivity mainActivity){
        this.mainActivity = mainActivity;

        try {
            mqttClient = new MqttClient(MQTT_BROKER, CLIENT_ID, new MemoryPersistence());
            mqttClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    try {
                        mqttClient.subscribe(LDR_SENSOR_TOPIC,0);
                        mqttClient.subscribe(IR_SENSOR_TOPIC_1, 0);
                        mqttClient.subscribe(IR_SENSOR_TOPIC_2, 0);
                        mqttClient.subscribe(IR_SENSOR_TOPIC_3, 0);
                        mqttClient.subscribe(GATE_COUNT_TOPIC, 0);
                    } catch (MqttException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());

                    if (topic.equals(LDR_SENSOR_TOPIC)) {
                        mainActivity.updateLdrSensorValue(payload);
                    } else if (topic.equals(IR_SENSOR_TOPIC_1)) {
                        mainActivity.updateIrSensorValue1(payload);
                    } else if (topic.equals(IR_SENSOR_TOPIC_2)) {
                        mainActivity.updateIrSensorValue2(payload);
                    } else if (topic.equals(IR_SENSOR_TOPIC_3)) {
                        mainActivity.updateIrSensorValue3(payload);
                    } else if (topic.equals(GATE_COUNT_TOPIC)) {
                        mainActivity.updateCarCount(payload);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            mqttClient.connect();
        } catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            mqttClient.disconnect();
        } catch (MqttException e){
            e.printStackTrace();
        }
    }
}
