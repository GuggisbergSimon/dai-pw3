package ch.heigvd;

import picocli.CommandLine;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@CommandLine.Command(
        name = "guild",
        description = "Start a Guild"
)
public class Guild extends AbstractMulticast {
    @Override
    public Integer call() {
        try (MulticastSocket socket = new MulticastSocket(parent.getPort())) {
            String myself = InetAddress.getLocalHost().getHostAddress() + ":" + parent.getPort();
            System.out.println("[Guild] started (" + myself + ")");

            InetAddress multicastAddress = InetAddress.getByName(host);
            InetSocketAddress group = new InetSocketAddress(multicastAddress, parent.getPort());
            NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
            socket.joinGroup(group, networkInterface);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("[Guild] Enter a command: ");
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
                        group
                );

                socket.send(datagram);
            }

            socket.leaveGroup(group, networkInterface);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }
}
