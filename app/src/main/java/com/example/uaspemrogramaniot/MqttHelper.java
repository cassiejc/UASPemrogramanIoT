package com.example.uaspemrogramaniot;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttHelper {
    private static final String MQTT_BROKER = "tcp://broker.emqx.io:1883";
    private static final String MQ5_SENSOR_TOPIC = "mq5Sensor";
    private static final String IR_SENSOR_TOPIC = "irSensor";
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
                        mqttClient.subscribe(MQ5_SENSOR_TOPIC,0);
                        mqttClient.subscribe(IR_SENSOR_TOPIC, 0);
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
//                    System.out.println("MQ-5 Sensor Value: " + payload);
//                    mainActivity.updateSensorValue(payload);

                    if (topic.equals(MQ5_SENSOR_TOPIC)) {
                        mainActivity.updateMq5SensorValue(payload);
                    } else if (topic.equals(IR_SENSOR_TOPIC)) {
                        mainActivity.updateIrSensorValue(payload);
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
