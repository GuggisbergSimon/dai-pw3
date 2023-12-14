package ch.heigvd;

import picocli.CommandLine;

public abstract class AbstractMulticast extends AbstractCallable {
    @CommandLine.Option(
            names = {"-i", "--interface"},
            description = "Interface to use",
            scope = CommandLine.ScopeType.INHERIT,
            required = true
    )
    protected String interfaceName;
    @CommandLine.Option(
            names = {"-mp", "--multicastPort"},
            description = "Port to use (default: 42000).",
            defaultValue = "42000",
            scope = CommandLine.ScopeType.INHERIT
    )
    protected int multicastPort;
}
