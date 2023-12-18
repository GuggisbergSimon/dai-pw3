package ch.heigvd;

import picocli.CommandLine;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@CommandLine.Command(
        name = "billboard",
        description = "Start a billboard"
)
public class BillBoard extends AbstractMulticast {
    //TODO find a way to refactor this as java doesn't allow multiple inheritance
    @CommandLine.Option(
            names = {"-up", "--unicastPort"},
            description = "Port to use (default: 32000).",
            defaultValue = "32000",
            scope = CommandLine.ScopeType.INHERIT
    )
    protected int unicastPort;

    private ArrayList<Quest> quests = new ArrayList<Quest>();

    @Override
    public Integer call() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try {
            executorService.submit(this::multicast);
            executorService.submit(this::unicast);
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        } finally {
            executorService.shutdown();
        }

        return 0;
    }

    private Integer multicast() {
        try (MulticastSocket socket = new MulticastSocket(multicastPort)) {
            String myself = InetAddress.getLocalHost().getHostAddress() + ":" + multicastPort;
            System.out.println("[BillBoard] Multicast receiver started (" + myself + ")");

            InetAddress multicastAddress = InetAddress.getByName(String.valueOf(host));
            InetSocketAddress group = new InetSocketAddress(multicastAddress, multicastPort);
            NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
            socket.joinGroup(group, networkInterface);

            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(
                        receiveData,
                        receiveData.length
                );

                socket.receive(packet);

                String message = new String(
                        packet.getData(),
                        packet.getOffset(),
                        packet.getLength(),
                        StandardCharsets.UTF_8
                );

                System.out.println("[Billboard] Multicast receiver (" + myself + ") received message: " + message);

                Quest quest = Quest.fromGuildPostMessage(message);
                if (quest != null) {
                    quests.add(quest);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    private Integer unicast() {
        try (DatagramSocket socket = new DatagramSocket(unicastPort)) {
            String myself = InetAddress.getLocalHost().getHostAddress() + ":" + unicastPort;
            System.out.println("[Billboard] Unicast receiver started (" + myself + ")");

            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(
                        receiveData,
                        receiveData.length
                );

                socket.receive(packet);

                String message = new String(
                        packet.getData(),
                        packet.getOffset(),
                        packet.getLength(),
                        StandardCharsets.UTF_8
                );

                System.out.println("[Billboard] Unicast receiver (" + myself + ") received message: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
}
