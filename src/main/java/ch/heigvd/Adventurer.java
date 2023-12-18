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

            Scanner scanner = new Scanner(System.in);
            String message;
            // 3% chance to exit every time.
            while (Math.random() * 100 >= 3) {
                System.out.print("[Adventurer] is arrived to the village.");


                Thread.sleep((long) (Math.random() * 5000 + 10000));
                //Effectue la request.
                if (hasAQuest){
                    System.out.println("[Adventurer] will return the quest.");
                    message = Requests.COMPLETE.name() + " " + acceptedQuest.getUuid();
                }
                else {
                    System.out.println("[Adventurer] is going to pick a quest");
                    message = Requests.GET.name();
                }

                byte[] payload = message.getBytes(StandardCharsets.UTF_8);

                DatagramPacket sendedDatagram = new DatagramPacket(payload, payload.length,serverAddress, unicastPort);
                socket.send(sendedDatagram);

                // Attente de la réponse
                byte[] receivedPayload = new byte[1024];

                DatagramPacket receivedDatagram = new DatagramPacket(receivedPayload, receivedPayload.length);
                socket.receive(receivedDatagram);

                String receivedMessage = new String(
                        receivedDatagram.getData(),
                        receivedDatagram.getOffset(),
                        receivedDatagram.getLength(),
                        StandardCharsets.UTF_8);

                String[] receivedArguments = receivedMessage.split(" ");

                //Validation de réponses obtenu de Billboard

                if (receivedArguments[0] == "COMPLETE" && receivedArguments.length == 1){
                    System.out.println("[Adventurer] received " + acceptedQuest.getReward() + " golds for completing the quest !");
                    hasAQuest = false;
                    System.out.println("[Adventurer] will now take some rest.");
                }
                else if (receivedArguments[0] == "GIVE" && receivedArguments.length == 2){
                    acceptedQuest = Quest.fromBillboardToAdventurer(receivedMessage);
                    System.out.println("[Adventurer] take the quest \"" + acceptedQuest.getName() + "\" and leave the village for a moment.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }
}