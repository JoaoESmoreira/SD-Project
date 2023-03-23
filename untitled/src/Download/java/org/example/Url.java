package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
// import java.util.concurrent.BlockingQueue;
// import java.util.concurrent.ConcurrentHashMap;
// import java.util.concurrent.ConcurrentLinkedQueue;
// import java.util.concurrent.CopyOnWriteArraySet;

public class Url {
    public SynchronizedQueue<String> urlQueue;
    public Set<Object> urlSet;
    private int maxLinks;
    //public ConcurrentHashMap<String, CopyOnWriteArraySet<String>> invertedIndex;

    public Url() throws InterruptedException {
        super();
        urlQueue = new SynchronizedQueue<>();
        urlSet = Collections.synchronizedSet(new HashSet<>());
        maxLinks = 0;
    }

    public int getMaxLinks() {
        return maxLinks;
    }

    synchronized public int getSizeUrlSet() { return this.urlSet.size(); }
    public void addUrl (String url) throws InterruptedException {
        this.maxLinks += 1024;
        this.urlQueue.add(url);
    }

    boolean work(int worker) {
        String url;
        String aux;

        try {
            System.out.println("SET SIZE: " + urlSet.size());
            if ((url = urlQueue.pop()) == null) {
                System.out.println("Null url");
                return true;
            }

            Connection connection = Jsoup.connect(url);
            Document doc = connection.get();

            // StringTokenizer tokens = new StringTokenizer(doc.text());
            // int countTokens = 0;
            // while (tokens.hasMoreElements() && countTokens++ < 100) {
            //      try {
            //          String token = tokens.nextToken().toLowerCase();
            //          CopyOnWriteArraySet<String> index = invertedIndex.get(token);
            //          if (index == null) {
            //              index = new CopyOnWriteArraySet<>();
            //              index.add(token);
            //              invertedIndex.put(url, index);
            //          } else {
            //              index.add(token);
            //          }
            //      } catch (Exception e) {
            //          throw new RuntimeException(e);
            //      }

            //      System.out.println(tokens.nextToken().toLowerCase());
            // }
            // System.out.println("\n\n");

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
