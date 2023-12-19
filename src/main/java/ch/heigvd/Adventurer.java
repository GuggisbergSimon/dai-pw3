package ch.heigvd;

import picocli.CommandLine;

import javax.xml.crypto.Data;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

@CommandLine.Command(
        name = "adventurer",
        description = "Start an Adventurer"
)
public class Adventurer extends AbstractUnicast {
    private Quest acceptedQuest;
    private boolean hasAQuest = false;

    private enum Requests {
        GET,
        COMPLETE
    }

    @Override
    public Integer call() {
        try (DatagramSocket socket = new DatagramSocket()) {
            String myself = InetAddress.getLocalHost().getHostAddress() + ":" + unicastPort;
            System.out.println("[Adventurer] started (" + myself + ")");
            InetAddress serverAddress = InetAddress.getByName(host);

            String message;
            // 3% chance to exit every time.
            while (Math.random() * 100 >= 3) {
                System.out.print("[Adventurer] arrives to the village and ");
                Thread.sleep((long) (Math.random() * 5000 + 10000));
                if (hasAQuest) {
                    System.out.println("sends word of the completion of a quest.");
                    message = Requests.COMPLETE.name() + " " + acceptedQuest.getUuid();
                } else {
                    System.out.println("picks a quest from the billboard.");
                    message = Requests.GET.name();
                }

                byte[] payload = message.getBytes(StandardCharsets.UTF_8);

                DatagramPacket sentDatagram = new DatagramPacket(payload, payload.length, serverAddress, unicastPort);
                socket.send(sentDatagram);

                byte[] receivedPayload = new byte[1024];

                DatagramPacket receivedDatagram = new DatagramPacket(receivedPayload, receivedPayload.length);
                socket.receive(receivedDatagram);

                String receivedMessage = new String(
                        receivedDatagram.getData(),
                        receivedDatagram.getOffset(),
                        receivedDatagram.getLength(),
                        StandardCharsets.UTF_8);

                String[] receivedArguments = receivedMessage.split(" ");

                if (receivedArguments[0].equalsIgnoreCase("COMPLETE") && receivedArguments.length == 1) {
                    System.out.println("[Adventurer] receives " + acceptedQuest.getReward() + " pieces of gold for completing the quest !");
                    hasAQuest = false;
                    System.out.println("[Adventurer] rests after this epic quest.");
                } else if (receivedArguments[0].equalsIgnoreCase("GIVE")) {
                    acceptedQuest = Quest.fromBillboardToAdventurer(receivedMessage);
                    hasAQuest = true;
                    System.out.println("[Adventurer] takes the quest \"" + acceptedQuest.getName() + "\" and leaves the village for a moment.");
                }
                else if (receivedArguments[0].equalsIgnoreCase("ERROR")) {
                    if (receivedArguments[1].equalsIgnoreCase("WRONG_REQUEST")) {
                        System.out.println("[ERROR] Invalid request");
                    } else if (receivedArguments[1].equalsIgnoreCase("NO_QUESTS")) {
                        System.out.println("[ERROR] No such quest");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }
}