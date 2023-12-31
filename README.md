# Of Guilds and Adventurers

This application simulates a fantasy-like universe.
More specifically it focuses on a network of guilds offering quests to
adventurers.
Those quests are all posted on a billboards.
Adventurers can get a random quest from a billboard.

More complex features such as listing all quests from a given category
or communicating across billboards to ensure a quest is properly
completed were scraped due to lack of time.

This application uses the following
container : https://github.com/GuggisbergSimon/dai-pw3/pkgs/container/dai-pw3

## Instructions

### Building the application

Either use the "Package as JAR file" run configuration or :

```bash
maven dependency:resolve clean compile package
```

Then build the docker image with :

```bash
docker build -t dai-pw3:v1.0 .
```

Tag the image with the proper version to use.
Here 1.0 is the finished work for this practical work.
Use a github container repository on which you have access.

```bash
docker tag dai-pw3:v1.0 ghcr.io/guggisbergsimon/dai-pw3:v1.0
```

Push the new image to the github container repository.
This step is optional if working locally.

```bash
docker push ghcr.io/guggisbergsimon/dai-pw3:v1.0
```

### Running the application

#### Docker compose

Depending on values adjusted during the build of the application (such
as the github container repository, the version number),
make sure those changes are reflected in the docker-compose.yaml.

Run docker compose with everything setup in it :

```bash
docker compose up
```

#### Individual commands

Or, alternatively, the individual commands available are the
following.

Running the application, it returns a help message.

```bash
java -jar target/dai-pw3-1.0-SNAPSHOT.jar
```

Running a billboard with all parameters.

```bash
java -jar target/dai-pw3-1.0-SNAPSHOT.jar billboard -H 239.1.1.1 -mp 42000 -up 32000 -i eth0
```

Running an adventurer with all parameters.

```bash
java -jar target/dai-pw3-1.0-SNAPSHOT.jar adventurer -H localhost -up 32000
```

Running a guild with all parameters.

```bash
java -jar target/dai-pw3-1.0-SNAPSHOT.jar guild -H 239.1.1.1 -mp 42000 -i eth0
```

# Application Protocol

![udp-dai-pw3.drawio.png](doc%2Fudp-dai-pw3.drawio.png)

## Overview

This protocol is meant to :

- ease guilds into sharing quests
- ease adventurers into picking up quests

## Transport protocol

The protocol uses UDP. Therefore there is no end of connection since
UDP is connectionless.

Guilds communicate with Billboards through Multicast with a pattern
fire-and-forget.
The communication is initiated by Guilds.
Port used between Guild and BillBoard is : 42000

Adventurers and Billboards communicate through Unicast with a pattern
request-reply.
The communication is initiated by Adventurers.
Port used between BillBoard and Adventurer is : 32000

## Messages

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

The new line character `\n` is used to end a communication.

## Examples

### Working Example

A guild posts a quest :

- POST 1280f219-bdb8-49db-bb1c-6123c951be19|Cursed Mines|Lift the
  curse plaguing a once-prosperous mining town.|80

A billboard receives it and stores it.

An adventurer asks for a random quest :

- GET

The billboard responds to the adventurer with a quest :

- SEND Cursed Mines|Lift the curse plaguing a once-prosperous mining
  town.|80

The adventurer leaves for the quest and, a while later sends a message
concerning the completion of said quest :

- COMPLETE 1280f219-bdb8-49db-bb1c-6123c951be19

The billboard receives it, deletes the matching entry and acknowledges
that :

- COMPLETE

### Error example

An adventurer asks for a random quest but coffee had been spilled on
their keyboard and keys stick to their fingers :

- GETTT

The billboard responds to the adventurer with an error message :

- ERROR WRONG_REQUEST
