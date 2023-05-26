package org.example;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTSubscriber {

    private static final String BROKER = "tcp://mqtt-broker:1883"; // Modify MQTT broker URL
    private static final String TOPIC = "$SYS/#";
    private static final String USERNAME = "your-username"; // Modify MQTT broker credentials
    private static final String PASSWORD = "your-password";

    private static final String SERVER_URL = "https://itsp.htl-leoben.at"; // Modify school server URL
    private static final int INTERVAL = 30; // Interval in seconds for status update

    private static int messageCount = 0;

    public static void main(String[] args) {
        // MQTT client initialization
        MqttClient client;
        try {
            client = new MqttClient(BROKER, MqttClient.generateClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());
            client.connect(options);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection to MQTT broker lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // Process incoming messages
                    System.out.println("Message received - Topic: " + topic + ", Message: " + new String(message.getPayload()));
                    messageCount++;
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            // Subscribe to the Sys topics
            client.subscribe(TOPIC);

            // Start status update thread
            StatusUpdateThread statusUpdateThread = new StatusUpdateThread();
            statusUpdateThread.start();

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private static class StatusUpdateThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    // Send message count to the school server
                    sendStatusUpdate();
                    Thread.sleep(INTERVAL * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendStatusUpdate() {
            // Code to send message count to the school server goes here
            // Use HTTP requests or an appropriate library to send POST requests
            // Note that this part will vary depending on the chosen method of communicating with the school server
        }
    }
}
