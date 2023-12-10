package ch.heigvd;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "guild",
        description = "Start a Guild"
)
public class Guild implements Callable<Integer> {
    @Override
    public Integer call() {
        // TODO
        System.out.println("Guild here !");
        return 0;
    }
}
