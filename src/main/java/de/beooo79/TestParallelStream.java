package de.beooo79;

import java.util.Random;
import java.util.stream.IntStream;

import org.apache.commons.lang3.time.StopWatch;

import lombok.SneakyThrows;

public class TestParallelStream {

    @SneakyThrows
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().toString() + " started");
        StopWatch w = StopWatch.create();

        w.start();
        IntStream.range(0, 200).boxed().parallel().map(i -> doWork()).forEach(TestParallelStream::printValue);
        w.stop();
        System.out.println("Parallel: " + w.toString());

        w.reset();
        w.start();
        IntStream.range(0, 200).boxed().sequential().map(i -> doWork()).forEach(TestParallelStream::printValue);
        w.stop();
        System.out.println("Sequential: " + w.toString());

        System.out.println(Thread.currentThread().toString() + " finished");
    }

    @SneakyThrows
    private static void printValue(Double value) {
        // System.out.println(Thread.currentThread().toString() + " printIt");
        // System.out.print(value);
        System.out.print("|");
    }

    @SneakyThrows
    private static Double doWork() {
        // System.out.println(Thread.currentThread().toString() + " doWork");
        Thread.sleep(100);
        return new Random().nextDouble();
    }

}
