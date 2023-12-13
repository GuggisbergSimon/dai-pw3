package ch.heigvd;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "billboard",
        description = "Start a billboard"
)
public class BillBoard implements Callable<Integer> {
    @Override
    public Integer call() {
        //TODO
        System.out.println("Town Hall here !");
        return 0;
    }
}
