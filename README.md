# Of Guilds and Adventurers

## Instructions

### Building the application

Either use the "Package as JAR file" run configuration or :

```bash
maven dependency:resolve clean compile package
```

```bash
docker build -t dai-pw3:v1.0 .
```

```bash
docker tag dai-pw3:v1.0 ghcr.io/guggisbergsimon/dai-pw3:v1.0
```

Optional if working locally
```bash
docker push ghcr.io/guggisbergsimon/dai-pw3:v1.0
```

```bash
docker compose up
```

### Running the application

```bash
java -jar target/dai-pw3-1.0-SNAPSHOT.jar
```

```bash
java -jar target/dai-pw3-1.0-SNAPSHOT.jar billboard -H 224.0.0.1 -i lo
```

```bash
java -jar target/dai-pw3-1.0-SNAPSHOT.jar adventurer -H 224.0.0.1
```

```bash
java -jar target/dai-pw3-1.0-SNAPSHOT.jar guild -H 224.0.0.1 -i lo
```

# Application Protocol

Port used between Guild and BillBoard is : 42000

Port used between BillBoard and Adventurer is : 32000

EOT character to end a transmission

Guild
- POST [uuid] [questName] [questDesc] [sum]

BillBoard
- LIST
- SEND [questName] [questDesc] [sum]
  //- COMPLETED [uuid]
- ERROR
    - NO ID
      //  - ALREADY COMPLETE
    - WRONG COMMAND
    - WRONG ARGUMENT
    - OTHER

Adventurer
- SUMMARY
- GET [uuid]
  //- COMPLETE [uuid]
