package Nsq;

import com.github.brainlag.nsq.NSQProducer;
/**
 * @author baopc@tuya.com
 * @date 2020/1/13
 */
public class NsqClient {
    public static void main(String[] args){

        String nsqAddress = "172.16.248.157:4161";
        String topic = "smart-dp-report-1";
        String data = "Hello world";

        try{
            NSQProducer nsqProducer = new NSQProducer();
            nsqProducer.addAddress("172.16.248.157",4161);
            nsqProducer.start();

            nsqProducer.produce(topic,data.getBytes());
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Hello world");
    }
}
