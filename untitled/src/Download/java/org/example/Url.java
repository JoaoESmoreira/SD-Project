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
    //public ConcurrentHashMap<String, CopyOnWriteArraySet<String>> invertedIndex;
    public SynchronizedQueue<String> urlQueue;
    public CopyOnWriteArraySet<Object> urlSet;
    // public HashMap<String, String> titles;
    // public ConcurrentHashMap<String, CopyOnWriteArraySet<String>> relevanteIndex;
    private int maxLinks;

    public Url() {
        super();
        urlQueue = new SynchronizedQueue<>();
        urlSet = new CopyOnWriteArraySet<>();
        // invertedIndex = new ConcurrentHashMap<>();
        // relevanteIndex = new ConcurrentHashMap<>();
        // titles = new HashMap<>();
        maxLinks = 0;
    }

    /*synchronized public CopyOnWriteArraySet<String> getIndex(String index) {
        return this.invertedIndex.get(index);
    }*/
    /*synchronized public int getRelevantIndexSize(String index) {
        return this.relevanteIndex.get(index).size();
    }*/

    /*synchronized public CopyOnWriteArraySet<String> getRelevantIndex (String index) {
        return this.relevanteIndex.get(index);
    }*/

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

    /*@Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (CopyOnWriteArraySet<String> set:invertedIndex.values()) {
            for (Object token: set)
                text.append(token).append("\n");
        }
        return text.toString();
    }*/

    /*public void printRelevanteIndex () {
        for (Object url:relevanteIndex.keySet()) {
            CopyOnWriteArraySet<String> set = relevanteIndex.get(url);
            System.out.println("\t\t" + url);
            for (String whoPoints:set) {
                System.out.println("\t\t\t" + whoPoints);
            }
        }
    }*/

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
            // titles.put(url, title);
            // System.out.println("Title: " + title);

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

                     /*CopyOnWriteArraySet<String> index = invertedIndex.get(token);
                     if (index == null) {
                         index = new CopyOnWriteArraySet<>();
                         index.add(url);
                         invertedIndex.put(token, index);
                     } else {
                         index.add(url);
                     }*/
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

                    /*CopyOnWriteArraySet<String> value = relevanteIndex.get(aux);
                    if (value == null) {
                        CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<>();
                        set.add(url);
                        relevanteIndex.put(aux, set);
                    } else {
                        value.add(url);
                        relevanteIndex.put(aux, value);
                    }*/
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
