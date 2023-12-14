package ch.heigvd;

import picocli.CommandLine;

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
                } else if (arguments[0].equalsIgnoreCase("EXIT")) {
                    break;
                }

                byte[] payload = message.getBytes(StandardCharsets.UTF_8);

                DatagramPacket datagram = new DatagramPacket(
                        payload,
                        payload.length,
                        serverAddress,
                        unicastPort
                );

                socket.send(datagram);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }
}