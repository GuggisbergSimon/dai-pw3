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

            Quest[] quests = new Quest[20];
            quests[0] = new Quest(
                    "Goblin Infestation",
                    "Clear the goblin den threatening a nearby village.",
                    50
            );

            quests[1] = new Quest(
                    "Enchanted Forest Mystery",
                    "Investigate strange occurrences in the mystical woods.",
                    75
            );

            quests[2] = new Quest(
                    "Lost Relic Retrieval",
                    "Recover a valuable ancient artifact from a hidden tomb.",
                    100
            );

            quests[3] = new Quest(
                    "Dragon Menace",
                    "Slay the dragon terrorizing the countryside.",
                    200
            );

            quests[4] = new Quest(
                    "Cursed Mines",
                    "Lift the curse plaguing a once-prosperous mining town.",
                    80
            );

            quests[5] = new Quest(
                    "Bandit King's Lair",
                    "Infiltrate the bandit king's stronghold and bring him to justice.",
                    120
            );

            quests[6] = new Quest(
                    "Elemental Disturbance",
                    "Restore balance to the disrupted elemental nexus.",
                    90
            );

            quests[7] = new Quest(
                    "Ghost Ship Haunting",
                    "Investigate a haunted ship and lay the spirits to rest.",
                    70
            );

            quests[8] = new Quest(
                    "Bounty on the Werewolf",
                    "Hunt down the notorious werewolf terrorizing the town.",
                    150
            );

            quests[9] = new Quest(
                    "Mystic Crystal Theft",
                    "Retrieve stolen mystical crystals from a thieves' guild.",
                    110
            );

            quests[10] = new Quest(
                    "Abandoned Wizard's Tower",
                    "Explore a long-forgotten wizard's tower for lost magical knowledge.",
                    60
            );

            quests[11] = new Quest(
                    "Sunken City Exploration",
                    "Dive into the depths to uncover the secrets of a sunken city.",
                    130
            );

            quests[12] = new Quest(
                    "Forest Guardian’s Request",
                    "Aid the ancient forest guardian in protecting its realm.",
                    85
            );

            quests[13] = new Quest(
                    "Time-Shifted Ruins",
                    "Navigate a time-distorted ruin to recover a valuable artifact.",
                    180
            );

            quests[14] = new Quest(
                    "Celestial Conundrum",
                    "Solve a puzzle in a celestial observatory to avert a cosmic disaster.",
                    95
            );

            quests[15] = new Quest(
                    "Minotaur Maze Expedition",
                    "Navigate a labyrinth to defeat the minotaur at its heart.",
                    140
            );

            quests[16] = new Quest(
                    "Shadow Realm Incursion",
                    "Close a portal leaking dark entities into the realm of light.",
                    160
            );

            quests[17] = new Quest(
                    "Rogue Alchemist's Laboratory",
                    "Shut down an outlaw alchemist’s dangerous experiments.",
                    110
            );

            quests[18] = new Quest(
                    "Elemental Crystal Heist",
                    "Retrieve stolen elemental crystals from a cunning thief.",
                    75
            );

            quests[19] = new Quest(
                    "Sphinx Riddles Challenge",
                    "Answer the riddles posed by a wise sphinx guarding a treasure.",
                    125
            );

            // 5% chance to exit every time
            while (Math.random() * 100 >= 5) {

                // wait random time between 1 and 5 seconds
                Thread.sleep((long) (Math.random() * 4000 + 1000));

                Quest quest = quests[(int) (Math.random() * quests.length)];

                String message = quest.toGuildPostMessage();

                System.out.println("[Guild] Sending: " + message);

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
