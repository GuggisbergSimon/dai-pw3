package ch.heigvd;

import picocli.CommandLine;

import javax.xml.crypto.Data;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@CommandLine.Command(
        name = "adventurer",
        description = "Start an Adventurer"
)
public class Adventurer extends AbstractUnicast {

    @Override
    public Integer call() {
        try (DatagramSocket socket = new DatagramSocket()) {
            String myself = InetAddress.getLocalHost().getHostAddress() + " mp :" + unicastPort + " up : " + unicastPort;
            System.out.println("[Adventurer] started (" + myself + ")");
            InetAddress serverAddress = InetAddress.getByName(host);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("[Adventurer] Enter a command: ");
                String message = scanner.nextLine().toUpperCase();

                // TODO check input
                String[] arguments = message.split(" ");
                if (arguments.length == 0) {
                    continue;
                } else if (arguments[0].equalsIgnoreCase("SUMMARY")) {
                    System.out.println("I'm checking the billboard");
                } else if (arguments[0].equalsIgnoreCase("GET")) {
                    System.out.println("I'm getting the quest number " + arguments[1]);
                } else if (arguments[0].equalsIgnoreCase("COMPLETE")){
                    System.out.println("I completed the quest number " + arguments[1]);
                } else if (arguments[0].equalsIgnoreCase("EXIT")) {
                    break;
                }

                //Envoi du datagram
                byte[] payload = message.getBytes(StandardCharsets.UTF_8);

                DatagramPacket datagram = new DatagramPacket(
                        payload,
                        payload.length,
                        serverAddress,
                        unicastPort
                );

                socket.send(datagram);

                //Reception du datagram
                byte[] receiveDatagram = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveDatagram, receiveDatagram.length);
                socket.receive(receivePacket);

                String receivedMessage = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength(), StandardCharsets.UTF_8);

                String[] receivedArguments = receivedMessage.split(" ");

                // TODO Faire les différents cas dépendant de la réponse de Billboard.

            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }
}