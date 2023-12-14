package ch.heigvd;

import picocli.CommandLine;

public abstract class AbstractUnicast extends AbstractCallable {
    @CommandLine.Option(
            names = {"-up", "--unicastPort"},
            description = "Port to use (default: 32000).",
            defaultValue = "32000",
            scope = CommandLine.ScopeType.INHERIT
    )
    protected int unicastPort;
}
