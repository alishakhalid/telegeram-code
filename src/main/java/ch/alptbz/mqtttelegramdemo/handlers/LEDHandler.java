package ch.alptbz.mqtttelegramdemo.handlers;

/**
 * -----------------------------------------------------
 * Author:          Alisha Khalid
 * Datum:           Date
 * Projekt:         project name
 * Beschreibung:    Kurzbeschreibung
 * ------------------------------------------------------
 **/
import ch.alptbz.mqtttelegramdemo.telegram.TelegramSenderInterface;
import org.eclipse.paho.client.mqttv3.*;

import java.util.Scanner;

public class LEDHandler {

    private MqttClient mqttClient;
    private String mqttBroker;
    private String mqttTopic;
    private boolean isSubscribed;

    private String mqttRoot;
    private TelegramSenderInterface telegramSend;


    public static final String TEMP_TOPIC = "temp";

    public LEDHandler(String mqttRoot, TelegramSenderInterface telegramSend) {
        this.mqttRoot = mqttRoot;
        this.telegramSend = telegramSend;
        isSubscribed = false;
    }


    private void handleColor(String color) {
        // Implement the logic to control the LED based on the received color
        if (color.equals("red")) {
            turnOnRed();
        } else if (color.equals("blue")) {
            turnOnBlue();
        } else if (color.equals("green")) {
            turnOnGreen();
        } else {
            System.out.println("Unsupported color: " + color);
        }
    }

    private void turnOnRed() {
        // Code to turn on the LED with red color
        System.out.println("Turning on the LED with red color.");
    }

    private void turnOnBlue() {
        // Code to turn on the LED with blue color
        System.out.println("Turning on the LED with blue color.");
    }

    private void turnOnGreen() {
        // Code to turn on the LED with green color
        System.out.println("Turning on the LED with green color.");
    }

    private void promptUserInput() {
        Scanner scanner = new Scanner(System.in);
        String input;

        // Subscribe loop
        while (!isSubscribed) {
            System.out.println("Enter '/subscribe' to subscribe or '/exit' to quit:");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("/subscribe")) {
                subscribe();
            } else if (input.equalsIgnoreCase("/exit")) {
                System.out.println("Bye!");
                scanner.close();
                return;
            }
        }

        // Room selection loop
        String selectedRoom = "";
        while (isSubscribed && selectedRoom.isEmpty()) {
            System.out.println("Choose a room (/bedroom | /livingroom | /attic):");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("/bedroom") || input.equalsIgnoreCase("/livingroom") ||
                    input.equalsIgnoreCase("/attic")) {
                selectedRoom = input;
                System.out.println("Room selected: " + selectedRoom);
            }
        }

        // Color selection loop
        while (isSubscribed) {
            System.out.println("Choose a color (red, blue, green) or '/unsubscribe' to stop:");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("/unsubscribe")) {
                unsubscribe();
            } else if (input.equalsIgnoreCase("red") || input.equalsIgnoreCase("blue") ||
                    input.equalsIgnoreCase("green")) {
                publishColor(selectedRoom, input);
            }
        }

        scanner.close();
    }

    private void subscribe() {
        try {
            mqttClient.subscribe(mqttTopic);
            isSubscribed = true;
            System.out.println("Subscribed to topic: " + mqttTopic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void unsubscribe() {
        try {
            mqttClient.unsubscribe(mqttTopic);
            isSubscribed = false;
            System.out.println("Unsubscribed from topic: " + mqttTopic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void publishColor(String room, String color) {
        try {
            String topic = mqttTopic + "/" + room + "/" + color;
            MqttMessage message = new MqttMessage(color.getBytes());
            mqttClient.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}

