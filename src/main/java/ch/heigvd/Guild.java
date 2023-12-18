package ch.heigvd;

import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;

@CommandLine.Command(
        name = "guild",
        description = "Start a Guild"
)
public class Guild extends AbstractMulticast {
    @Override
    public Integer call() {
        try (MulticastSocket socket = new MulticastSocket(multicastPort)) {
            String myself = InetAddress.getLocalHost().getHostAddress() + ":" + multicastPort;
            System.out.println("[Guild] started (" + myself + ")");

            InetAddress multicastAddress = InetAddress.getByName(host);
            InetSocketAddress group = new InetSocketAddress(multicastAddress, multicastPort);
            NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
            socket.joinGroup(group, networkInterface);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("[Guild] Enter a command");
                System.out.println(">> ");
                String message = reader.readLine();

                // TODO check input
                String[] arguments = message.split(" ");
                if (arguments.length == 0) {
                    continue;
                } else if (arguments[0].equalsIgnoreCase("EXIT")) {
                    break;
                } else if (!arguments[0].equalsIgnoreCase("POST")) {
                    System.out.println("Unknown command");
                    continue;
                }

                if(arguments.length != 5) {
                    System.out.println("Wrong number of arguments: POST <uuid> <name> <description> <reward>");
                    continue;
                }

                UUID uuid;

                try{
                    uuid = UUID.fromString(arguments[1]);
                } catch (IllegalArgumentException exception){
                    System.out.println("Wrong uuid format");
                    continue;
                }

                String name = arguments[2];
                String description = arguments[3];
                int reward;

                try{
                    reward = Integer.parseInt(arguments[4]);
                } catch (NumberFormatException exception){
                    System.out.println("Wrong reward format");
                    continue;
                }

                Quest quest = new Quest(name, description, reward, uuid.toString());

                String messageToSend = quest.toGuildPostMessage();

                byte[] payload = messageToSend.getBytes(StandardCharsets.UTF_8);

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
