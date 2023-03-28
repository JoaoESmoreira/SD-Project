package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class Url {
    public SynchronizedQueue<String> urlQueue;
    public CopyOnWriteArraySet<Object> urlSet;
    private int maxLinks;

    public Url() {
        super();
        urlQueue = new SynchronizedQueue<>();
        urlSet = new CopyOnWriteArraySet<>();
    }

    synchronized public int getSizeUrlSet() { return this.urlSet.size(); }
    public void addUrl (String url) throws InterruptedException {
        if (this.maxLinks == this.urlSet.size()) {
            int value = getMaxLinks();
            setMaxLinks(value + 1024);
            this.urlQueue.add(url);
        }
    }

    synchronized public int getMaxLinks() {
        return maxLinks;
    }

    synchronized public void setMaxLinks(int maxLinks) {
        this.maxLinks = maxLinks;
    }

    boolean work(MulticastSocket socket, InetAddress group) {
        String url;
        String aux;
        String message;
        byte[] buffer;
        DatagramPacket packet;

        try {
            if ((url = urlQueue.pop()) == null) {
                System.out.println("Null url");
                return true;
            }

            Connection connection = Jsoup.connect(url);
            Document doc = connection.get();

            StringTokenizer tokens = new StringTokenizer(doc.text());

            String title = doc.title();

            message = "Title" + " " + url + " " + title;

            buffer = message.getBytes();
            int PORT = 4321;
            packet = new DatagramPacket(buffer, buffer.length, group, PORT);
            socket.send(packet);

            int countTokens = 0;
            String token;
            while (tokens.hasMoreElements() && countTokens++ < 100) {
                token = tokens.nextToken().toLowerCase();
                 // try {

                     message = "Token" + " " + url + " " + token;
                     buffer = message.getBytes();
                     packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                     socket.send(packet);

                 // } catch (Exception e) {
                 //     throw new RuntimeException(e);
                 // }
                 // System.out.println(token);
            }

            System.out.println("Size: " + urlSet.size() + " Capacity: " + getMaxLinks());
            System.out.println("Size: " + urlQueue.size());
            if (getSizeUrlSet() < getMaxLinks()) {
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    aux = link.attr("abs:href");

                    message = "Url" + " " + url + " " + aux;
                    buffer = message.getBytes();
                    packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);

                    if (!urlSet.contains(aux) && getSizeUrlSet() < getMaxLinks()) {
                        urlQueue.add(aux);
                        urlSet.add(aux);
                    }
                }
            }
        } catch (IOException e) {
            // e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
