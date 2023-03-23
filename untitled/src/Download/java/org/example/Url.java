package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Url {
    public ConcurrentHashMap<String, CopyOnWriteArraySet<String>> invertedIndex;
    public SynchronizedQueue<String> urlQueue;
    public Set<Object> urlSet;
    private int maxLinks;

    public Url() {
        super();
        urlQueue = new SynchronizedQueue<>();
        urlSet = Collections.synchronizedSet(new HashSet<>());
        invertedIndex = new ConcurrentHashMap<>();
        maxLinks = 0;
    }

    synchronized public int getSizeUrlSet() { return this.urlSet.size(); }
    public void addUrl (String url) throws InterruptedException {
        this.maxLinks += 1024;
        this.urlQueue.add(url);
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (CopyOnWriteArraySet<String> set:invertedIndex.values()) {
            for (Object token: set)
                text.append(token).append("\n");
        }
        return text.toString();
    }

    boolean work() {
        String url;
        String aux;

        try {
            if ((url = urlQueue.pop()) == null) {
                System.out.println("Null url");
                return true;
            }

            Connection connection = Jsoup.connect(url);
            Document doc = connection.get();

            StringTokenizer tokens = new StringTokenizer(doc.text());
            int countTokens = 0;
            String token;
            while (tokens.hasMoreElements() && countTokens++ < 100) {
                token = tokens.nextToken().toLowerCase();
                 try {
                     CopyOnWriteArraySet<String> index = invertedIndex.get(token);
                     if (index == null) {
                         index = new CopyOnWriteArraySet<>();
                         index.add(url);
                         invertedIndex.put(token, index);
                     } else {
                         index.add(url);
                     }
                 } catch (Exception e) {
                     throw new RuntimeException(e);
                 }
            }

            if (urlSet.size() != maxLinks) {
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    aux = link.attr("abs:href");
                    if (!urlSet.contains(aux) && getSizeUrlSet() < maxLinks) {
                        urlQueue.add(aux);
                        urlSet.add(aux);
                        // System.out.println("Worker: " + worker);
                        // System.out.println("Worker: " + worker + " Size: " + urlSet.size());
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
