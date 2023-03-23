package org.example;

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
        System.out.println("Size: " + this.queue.size());
        while (this.queue.size() == this.capacity) {
            System.out.println("==== Full queue =====");
            wait();
        }
        if (this.queue.size() == 0) {
            notifyAll();
        }
        this.queue.add(element);
    }

    public synchronized E pop () throws InterruptedException {
        System.out.println("Size: " + this.queue.size());
        while (this.queue.size() == 0) {
            System.out.println("==== Empty queue =====");
            wait();
        }
        if (this.queue.size() == this.capacity) {
            notifyAll();
        }
        return this.queue.remove();
    }

    public synchronized boolean isFull () throws InterruptedException {
        return this.queue.size() == this.capacity;
    }

    public synchronized boolean isEmpty () throws InterruptedException {
        return this.queue.size() == 0;
    }

    public synchronized void erraseAll () throws InterruptedException {
        System.out.println("===============================");
        for (E element:this.queue) {
            this.queue.remove();
        }
        notifyAll();
    }
}
