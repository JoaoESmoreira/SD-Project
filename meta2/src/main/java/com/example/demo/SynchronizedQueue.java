package com.example.demo;

import java.util.LinkedList;
import java.util.Queue;

public class SynchronizedQueue<E> {
    private final Queue<E> queue;
    private final int capacity;

    public SynchronizedQueue() {
        this.capacity = 1024;
        this.queue = new LinkedList<>();
    }
    public SynchronizedQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public synchronized void add (E element) throws InterruptedException {
        if (this.queue.size() == this.capacity)
            return;
        if (this.queue.size() == 0) {
            notifyAll();
        }
        this.queue.add(element);
    }

    public synchronized E pop () throws InterruptedException {
        while (this.queue.size() == 0) {
            wait();
        }
        return this.queue.remove();
    }

    public synchronized int size () {
        return this.queue.size();
    }
}
