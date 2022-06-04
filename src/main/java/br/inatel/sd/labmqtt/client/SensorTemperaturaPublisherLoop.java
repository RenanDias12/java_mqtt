package br.inatel.sd.labmqtt.client;

import java.util.Random;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SensorTemperaturaPublisherLoop {
    public static void main(String[] args) {

        try {
            String publisherId = UUID.randomUUID().toString();
            IMqttClient publisher = new MqttClient(MyConstants.URI_BROKER, publisherId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            publisher.connect(options);

            for (int i = 0; i < 10; i++) {
                MqttMessage msg = getTemperaturaSolo();
                msg.setQos(0);
                msg.setRetained(true);

                publisher.publish(MyConstants.TOPIC_1, msg);
                Thread.sleep(2000);
            }

            publisher.disconnect();
        } catch (MqttException e) {
            e.getStackTrace();
        }
        catch (InterruptedException e) {
            e.fillInStackTrace();
        }
        catch (Exception e) {
            e.fillInStackTrace();
        }

    }

    private static MqttMessage getTemperaturaSolo() {
        Random r = new Random();
        double temperatura = 80 + r.nextDouble() * 20.0;
        byte[] payload = String.format("T:%04.2f", temperatura).getBytes();
        return new MqttMessage(payload);
    }
}
