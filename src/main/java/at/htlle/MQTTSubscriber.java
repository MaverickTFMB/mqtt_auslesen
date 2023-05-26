package at.htlle;

import org.eclipse.paho.client.mqttv3.*;

public class MQTTSubscriber {
    private static final String TOPIC = "$SYS/#";
    private static final String BROKER = "tcp://itsp.htl-leoben.at:1883";
    private static final String CLIENT_ID = "MqttSubscriber";

    public static void main(String[] args) {
        try {
            MqttClient client = new MqttClient(BROKER, CLIENT_ID);
            client.connect();
            client.subscribe(TOPIC);
            client.setCallback(new MqttCallback() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Topic: " + topic);
                    System.out.println("Message: " + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    //just because you need it
                }

                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause);
                }
            });
        } catch (MqttException e) {
            System.out.println("Error subscribing to topic");
        }
    }
}
