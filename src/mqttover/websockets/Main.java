package mqttover.websockets;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.net.URISyntaxException;

public class Main extends Application {
    private static TextArea textMessage =  new TextArea();
    private  static  TextField subscribeField = new TextField();
    private static Subscriber subscriber;

    public GridPane cratePane(){
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Subscriber panel");
        sceneTitle.setFont(Font.font("Arial", FontWeight.NORMAL,20));
        pane.add(sceneTitle, 0, 0, 2, 1);
        return pane;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Subscriber");
        GridPane pane = cratePane();
        Scene scene = new Scene(pane, 400, 400);

        Label total = new Label("topic:");
        pane.add(total, 0, 1);
        pane.add(subscribeField, 1, 1);

        Button subscribeBtn = new Button("subscribe");
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        hbox.getChildren().add(subscribeBtn);

        Button unsubscribeBtn = new Button("unsubscribe");
        hbox.getChildren().add(unsubscribeBtn);
        pane.add(hbox, 1, 4);

        pane.add(textMessage, 1, 6);

        subscribeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    subscriber.subscribe(subscribeField.getText());
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        unsubscribeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    subscriber.unsubscribe(subscribeField.getText());
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) throws MqttException, URISyntaxException {

        // connected with websockets protocol can be replaced with tcp on port 1883
        textMessage.setEditable(false);
        subscriber = new Subscriber("ws://127.0.0.1:9001","panel",textMessage);
        subscribeField.setText(subscriber.getTopic());
        launch(args);
    }
}