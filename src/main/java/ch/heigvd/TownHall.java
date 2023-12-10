package ch.heigvd;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "townhall",
        description = "Start a townhall"
)
public class TownHall implements Callable<Integer> {
    @Override
    public Integer call() {
        //TODO
        System.out.println("Town Hall here !");
        return 0;
    }
}
