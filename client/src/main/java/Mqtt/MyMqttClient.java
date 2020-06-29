package Mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
 * @author baopc@tuya.com
 * @date 2020/1/14
 */

class MqttRunable implements Runnable{

    public int maxRunTime = 1000;

    public void run(){
        String productKey = "a1EkUy9hQED";
        String devName = "mqtt_client";
        String devSecret = "8CsJnsLMDohk51VxpLTTZTKDxGhzOnEh";
        String port = "443";
        String broker = "ssl://" + productKey + ".iot-as-mqtt.cn-shanghai.aliyuncs.com" + ":" + port;

        MqttSign sign = new MqttSign();
        sign.calculate(productKey, devName, devSecret);

        MemoryPersistence memoryPersistence = new MemoryPersistence();

        try {
            MqttClient client = new MqttClient(broker, sign.getClientid(), memoryPersistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setKeepAliveInterval(180);
            options.setUserName(sign.getUsername());
            options.setPassword(sign.getPassword().toCharArray());

            client.connect(options);

            System.out.println("client connected");

            String topic = "/a1EkUy9hQED/" + devName + "/user/update";
            String content = "Hello World";

            MqttMessage msg = new MqttMessage(content.getBytes());
            msg.setQos(0);

            while (maxRunTime-- > 0){
                client.publish(topic,msg);

                System.out.println("send msg");

                Thread.sleep(1000);
            }

            client.disconnect();
            System.out.println("Disconnect");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

public class MyMqttClient {



    public static void main(String[] args){
        MqttRunable runnable = new MqttRunable();

        try {
            Thread thread = new Thread(runnable);
            thread.start();
            thread.join();
        } catch (Exception e){
            e.printStackTrace();
        }

        System.exit(0);
    }
}
