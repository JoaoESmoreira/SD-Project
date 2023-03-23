package org.example;

public class Reader extends Thread {
    Url url;
    int n;

    public Reader(Url worker, int n) {
        this.url = worker;
        this.n = n;
    }

    @Override
    public void run() {
        System.out.println("Starting working");

        while (true) {
            System.out.println("Work starting on: " + this.n );
            if (!url.work(this.n)){
                System.out.println("I'm tired of working! " + this.n);
                break;
            }
            System.out.println("Work done on: " + this.n );
        }

        System.out.println("I'm getting out: " + this.n);
    }
}

