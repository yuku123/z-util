package com.zifang.util.core.lang.collection;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class StreamsTest {

    // --- streamOf(Iterator) ---

    @Test
    public void testStreamOfIterator_Normal() {
        List<String> list = Arrays.asList("a", "b", "c");
        Iterator<String> iterator = list.iterator();
        Stream<String> stream = Streams.streamOf(iterator);
        List<String> result = stream.collect(Collectors.toList());
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testStreamOfIterator_SingleElement() {
        List<String> list = new ArrayList<>();
        list.add("x");
        Iterator<String> iterator = list.iterator();
        Stream<String> stream = Streams.streamOf(iterator);
        List<String> result = stream.collect(Collectors.toList());
        assertEquals(1, result.size());
    }

    @Test
    public void testStreamOfIterator_Empty() {
        List<String> list = new ArrayList<>();
        Iterator<String> iterator = list.iterator();
        Stream<String> stream = Streams.streamOf(iterator);
        List<String> result = stream.collect(Collectors.toList());
        assertTrue(result.isEmpty());
    }

    // --- streamOf(Iterable) ---

    @Test
    public void testStreamOfIterable_Normal() {
        List<String> list = Arrays.asList("a", "b", "c");
        Stream<String> stream = Streams.streamOf((Iterable<String>) list);
        List<String> result = stream.collect(Collectors.toList());
        assertEquals(3, result.size());
    }

    @Test
    public void testStreamOfIterable_SingleElement() {
        List<String> list = new ArrayList<>();
        list.add("x");
        Stream<String> stream = Streams.streamOf((Iterable<String>) list);
        List<String> result = stream.collect(Collectors.toList());
        assertEquals(1, result.size());
    }

    @Test
    public void testStreamOfIterable_Empty() {
        List<String> list = new ArrayList<>();
        Stream<String> stream = Streams.streamOf((Iterable<String>) list);
        List<String> result = stream.collect(Collectors.toList());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testStreamOfIterable_Set() {
        Set<String> set = new HashSet<>(Arrays.asList("a", "b"));
        Stream<String> stream = Streams.streamOf((Iterable<String>) set);
        List<String> result = stream.collect(Collectors.toList());
        assertEquals(2, result.size());
    }

    // --- parallelStreamOf(Iterator) ---

    @Test
    public void testParallelStreamOfIterator_Normal() {
        List<String> list = Arrays.asList("a", "b", "c");
        Iterator<String> iterator = list.iterator();
        Stream<String> stream = Streams.parallelStreamOf(iterator);
        assertTrue(stream.isParallel());
        List<String> result = stream.collect(Collectors.toList());
        assertEquals(3, result.size());
    }

    @Test
    public void testParallelStreamOfIterator_Empty() {
        List<String> list = new ArrayList<>();
        Iterator<String> iterator = list.iterator();
        Stream<String> stream = Streams.parallelStreamOf(iterator);
        assertTrue(stream.isParallel());
        List<String> result = stream.collect(Collectors.toList());
        assertTrue(result.isEmpty());
    }

    // --- parallelStreamOf(Iterable) ---

    @Test
    public void testParallelStreamOfIterable_Normal() {
        List<String> list = Arrays.asList("a", "b", "c");
        Stream<String> stream = Streams.parallelStreamOf((Iterable<String>) list);
        assertTrue(stream.isParallel());
        List<String> result = stream.collect(Collectors.toList());
        assertEquals(3, result.size());
    }

    @Test
    public void testParallelStreamOfIterable_SingleElement() {
        List<String> list = new ArrayList<>();
        list.add("x");
        Stream<String> stream = Streams.parallelStreamOf((Iterable<String>) list);
        assertTrue(stream.isParallel());
        List<String> result = stream.collect(Collectors.toList());
        assertEquals(1, result.size());
    }

    @Test
    public void testParallelStreamOfIterable_Empty() {
        List<String> list = new ArrayList<>();
        Stream<String> stream = Streams.parallelStreamOf((Iterable<String>) list);
        assertTrue(stream.isParallel());
        List<String> result = stream.collect(Collectors.toList());
        assertTrue(result.isEmpty());
    }
}
