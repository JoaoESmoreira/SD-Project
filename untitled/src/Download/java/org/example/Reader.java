package org.example;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

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
        MulticastSocket socket;
        InetAddress group;
        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            String MULTICAST_ADDRESS = "224.3.2.1";
            group = InetAddress.getByName(MULTICAST_ADDRESS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            // System.out.println("Work starting on: " + this.n );
            if (!url.work(socket, group)){
                System.out.println("I'm tired of working! " + this.n);
                break;
            }
            // System.out.println("Work done on: " + this.n );
        }

        System.out.println("I'm getting out: " + this.n);
    }
}

