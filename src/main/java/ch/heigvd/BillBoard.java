package ch.heigvd;

import picocli.CommandLine;

import java.net.*;
import java.nio.charset.StandardCharsets;
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

            InetAddress multicastAddress = InetAddress.getByName(String.valueOf(multicastPort));
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
                //Reception de la requete
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

                //Process les requetes
                byte[] sendData = message.getBytes();
                String[] arguments = message.split(" ");
                if (arguments[0] == "GET" && arguments.length == 2){
                    String uuid = arguments[1];
                    // TODO à modifier dépendant du système de mappage des quetes.
                    String questInfo = "SEND [questName] [questDesc] [sum]"; //Dépendant du système de billboard.
                    if (questInfo != null){
                        //renvois l'info à l'aventurier.
                        sendData = questInfo.getBytes();
                    }
                    else {
                        System.out.println("Quête pas trouvé");
                    }
                }
                else if (arguments[0] == "COMPLETE" && arguments.length == 2){
                    // TODO set dans le billboard que la quête est terminé.
                    String questComplete = "La quete " + arguments[1] + " a ete termine";
                    sendData = questComplete.getBytes();
                }
                else if (arguments[0] == "SUMMARY" && arguments.length == 1){
                    // TODO afficher toutes ou une partie des quêtes.
                    String listQuests = "LIST";
                }
                else {
                    System.out.println("Requete invalide");
                }
                //Faut voir à quel point c'est robuste ou pas
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                socket.send(sendPacket);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
}
