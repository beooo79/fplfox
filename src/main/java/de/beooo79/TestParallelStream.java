package de.beooo79;

import java.util.Random;
import java.util.stream.IntStream;

import org.apache.commons.lang3.time.StopWatch;

import lombok.SneakyThrows;

public class TestParallelStream {

    @SneakyThrows
    public static void main(String[] args) {
        parallelVsSequential2();
    }

    private static void parallelVsSequential1() {
        System.out.println(Thread.currentThread().toString() + " started");
        StopWatch w = StopWatch.create();

        w.start();
        IntStream.range(0, 200).boxed().parallel().map(i -> doWork()).forEach(TestParallelStream::printValue);
        w.stop();
        System.out.println("Parallel: " + w.toString());
        System.out.println(
                "--------------------------------------------------------------------------------------------");
        w.reset();
        w.start();
        IntStream.range(0, 200).boxed().sequential().map(i -> doWork()).forEach(TestParallelStream::printValue);
        w.stop();
        System.out.println("Sequential: " + w.toString());

        System.out.println(Thread.currentThread().toString() + " finished");
    }

    private static void parallelVsSequential2() {
        System.out.println(Thread.currentThread().toString() + " started");
        StopWatch w = StopWatch.create();

        w.start();
        IntStream.range(0, 200).boxed().parallel().map(TestParallelStream::doEasyWork)
                .forEachOrdered(TestParallelStream::printValue);
        w.stop();
        System.out.println("Parallel: " + w.toString());
        System.out.println(
                "--------------------------------------------------------------------------------------------");
        w.reset();
        w.start();
        IntStream.range(0, 200).boxed().sequential().map(TestParallelStream::doEasyWork)
                .forEach(TestParallelStream::printValue);
        w.stop();
        System.out.println("Sequential: " + w.toString());

        System.out.println(Thread.currentThread().toString() + " finished");
    }

    @SneakyThrows
    private static void printValue(Double value) {
        System.out.print(value + " ");
        // System.out.print("|");
    }

    @SneakyThrows
    private static void printProgress(Double value) {
        System.out.print("|");
    }

    @SneakyThrows
    private static Double doWork() {
        // System.out.println(Thread.currentThread().toString() + " doWork");
        Thread.sleep(50);
        return new Random().nextDouble();
    }

    @SneakyThrows
    private static Double doEasyWork(Integer number) {
        Thread.sleep(50);
        return number.doubleValue();
    }

}
