# Of Guilds and Adventurers

## Overview

This application simulates a fantasy-like universe. More specifically
it focuses on a network of guilds offering quests to adventurers.
Those quests are all posted on a billboards. Adventurers can get a
random quest from a billboard.

## Protocol

![udp-dai-pw3.drawio.png](doc%2Fudp-dai-pw3.drawio.png)

Messages sent by a Guild

- POST [uuid]|[questName]|[questDesc]|[sum] : posts a quest

Messages sent by a BillBoard

- GIVE [questName]|[questDesc]|[sum] : returns a specific quest
- COMPLETE : acknowledge the quest completed by an adventurer
- ERROR
    - WRONG_REQUEST : no such request exists
    - NO_QUESTS : no UUID matching the one asked by the adventurer or
      no
      quests at all

Messages sent by an Adventurer

- GET : returns a random quest
- COMPLETE [uuid] : completes the specified quest

## Code

```yaml
  adventurer:
    image: ghcr.io/guggisbergsimon/dai-pw3:v1.0
    command:
      - adventurer
      - --host=billboard
      - --unicastPort=32000
#...
  billboard:
    image: ghcr.io/guggisbergsimon/dai-pw3:v1.0
    command:
      - billboard
      - --host=239.1.1.1
      - --multicastPort=42000
      - --unicastPort=32000
      - --interface=eth0
```

```java
//Quest
uuid = UUID.randomUUID().toString();
//Billboard
private ArrayList<Quest> quests = new ArrayList<Quest>();
//...
Quest quest = Quest.fromGuildPostMessage(message);
if (quest != null) {
    quests.add(quest);
}
//...
quests.removeIf(quest -> quest.getUuid() == arguments[1]);
```

## Demo

```bash
docker compose up
```

## Conclusion

More complex features such as listing all quests from a given category
or communicating across billboards to ensure a quest is properly
completed were scraped due to lack of time. But the architecture would
easily them to be added.