package de.beooo79;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;

public class TestFlatMap {

    @Data
    public static class Numbers {
        List<Integer> l1 = List.of(1, 2, 3, 4, 5);
        List<Integer> l2 = List.of(10, 2, 8, 4, 6, 1, 2, 1, 9, 10);
        String name = "foobar";
    }

    public static void main(String[] args) {
        var t = new TestFlatMap.Numbers();

        var l = Stream.of(t.l1, t.l2).flatMap(s -> s.stream().filter(n -> n > 3)).sorted().collect(Collectors.toList());

        System.out.println(l);
    }

}
