package ch.heigvd;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "adventurer",
        description = "Start an Adventurer"
)
public class Adventurer implements Callable<Integer> {
    @Override
    public Integer call() {
        //TODO
        System.out.println("Adventurer here !");
        return 0;
    }
}