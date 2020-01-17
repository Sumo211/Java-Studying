package com.leon.experimental;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class ListPartitioner {

    public static void main(String[] args) {
        //Ref: https://e.printstacktrace.blog/divide-a-list-to-lists-of-n-size-in-Java-8
        var src = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        System.out.println(Partition.ofSize(src, 3));
        System.out.println(Partition.ofSize(src, 4));
        System.out.println(Partition.ofSize(src, 2));
    }

    static class Partition<T> extends AbstractList<List<T>> {

        private final List<T> src;

        private final int batchSize;

        private Partition(List<T> src, int batchSize) {
            this.src = src;
            this.batchSize = batchSize;
        }

        static <T> Partition<T> ofSize(List<T> src, int batchSize) {
            return new Partition<>(src, batchSize);
        }

        @Override
        public List<T> get(int index) {
            int start = index * batchSize;
            int end = Math.min(start + batchSize, src.size());

            if (start > end) {
                throw new IndexOutOfBoundsException("Index " + index + " is out of the list range <0," + (size() - 1) + ">");
            }

            return new ArrayList<>(src.subList(start, end));
        }

        @Override
        public int size() {
            return (int) Math.ceil(src.size() * 1.0 / batchSize);
        }

    }

}