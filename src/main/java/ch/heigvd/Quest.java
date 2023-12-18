package ch.heigvd;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Quest {

    private final String uuid;
    private final String name;
    private final String description;
    private final int reward;

    public Quest(String name, String description, int reward, String uuid) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.reward = reward;
    }

    public Quest(String name, String description, int reward) {
        uuid = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.reward = reward;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getReward() {
        return reward;
    }

    public String toGuildPostMessage() {
        return ("POST " + uuid + "|" + name + "|" + description + "|" + reward);
    }

    /*
     * Parse a quest from a message received from a guild
     * returns null if the message is not a valid quest
     */
    public static Quest fromGuildPostMessage(String message) {
        String[] parts = message.split(" ", 2);
        if(!parts[0].equals("POST")) {
            return null;
        }

        String[] questParts = parts[1].split("\\|");
        return new Quest(
                questParts[1],
                questParts[2],
                Integer.parseInt(questParts[3]),
                questParts[0]
        );


    }

    /*
     * Parse a quest from a message stocked on billboard
     * returns null if the message is not a valid quest
     */
    public static Quest fromBillboardToAdventurer(String message) {
        String[] parts = message.split(" ", 2);
        if(!parts[0].equals("GIVE")) {
            return null;
        }

        String[] questParts = parts[1].split("\\|");
        return new Quest(
                questParts[1],
                questParts[2],
                Integer.parseInt(questParts[3]),
                questParts[0]
        );


    }
}
