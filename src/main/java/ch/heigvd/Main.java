package ch.heigvd;

import lombok.Getter;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        description = "Of Guilds and Adventurers",
        version = "1.0.0",
        subcommands = {
                Guild.class,
                Adventurer.class,
                BillBoard.class,
        },
        scope = CommandLine.ScopeType.INHERIT,
        mixinStandardHelpOptions = true
)
@Getter
public class Main {
    public static void main(String... args) {
        // Source: https://stackoverflow.com/a/11159435
        String commandName = new java.io.File(
                Main.class.getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .getPath()
        ).getName();

        int exitCode = new CommandLine(new Main())
                .setCommandName(commandName)
                .execute(args);
        System.exit(exitCode);
    }
}
