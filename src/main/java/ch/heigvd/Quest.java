package ch.heigvd;

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
        return ("POST '" + uuid + "' '" + name + "' '" + description + "' '" + reward + "'");
    }
}
