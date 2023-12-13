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
}
