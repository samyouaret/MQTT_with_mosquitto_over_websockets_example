package mqttover.websockets;


import javafx.scene.control.TextArea;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;



public class Subscriber implements MqttCallback {

    private final int qos = 1;
    private String topic = "local/+";
    private MqttClient client;
    private TextArea textMessage;

    public Subscriber(String uri, String clientId, TextArea textMessage) throws MqttException {


        this.client = new MqttClient(uri, clientId, new MemoryPersistence());
        this.client.setCallback(this);
        this.client.connect();
        this.client.subscribe(this.topic, qos);
        this.textMessage = textMessage;

    }


    public void subscribe(String topic) throws MqttException {
        this.client.subscribe(topic,qos);
    }

    public void unsubscribe(String topic) throws MqttException {
        this.client.unsubscribe(topic);
    }

    public void sendMessage(String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(qos);
        this.client.publish(this.topic, message); // Blocking publish
    }

    /**
     * @see MqttCallback#connectionLost(Throwable)
     */
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost because: " + cause);
        System.exit(1);
    }

    /**
     * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
     */
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    /**
     * @see MqttCallback#messageArrived(String, MqttMessage)
     */
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        StringBuilder messages =  new StringBuilder(message.toString() + "\n");
        messages.append(textMessage.getText());

        // delay to avoid exception raised by TextArea
        try {
            Thread.sleep(60);
            this.textMessage.setText(messages.toString());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getTopic() {
        return topic;
    }
}
