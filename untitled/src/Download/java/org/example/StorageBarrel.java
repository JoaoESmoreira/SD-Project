package org.example;

import sun.misc.Signal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;

import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


public class StorageBarrel extends UnicastRemoteObject implements Binterface {
    public static ConcurrentHashMap<String, CopyOnWriteArraySet<String>> invertedIndex;
    public static HashMap<String, String> titles;
    public static ConcurrentHashMap<String, CopyOnWriteArraySet<String>> relevanteIndex;

    StorageBarrel() throws RemoteException{
        super();
        invertedIndex = new ConcurrentHashMap<>();
        titles = new HashMap<>();
        relevanteIndex = new ConcurrentHashMap<>();
    }

    public String getSearch(String search) throws RemoteException {
        //System.out.println("In barrel: "+s);
        String output = "";
        String[] tokens = search.split(" ");

        ArrayList<CopyOnWriteArraySet<String>> relevantIndexArray = new ArrayList<>();
        for (String string:tokens) {
            CopyOnWriteArraySet<String> set = invertedIndex.get(string);
            relevantIndexArray.add(set);
        }

        for (int i = 1; i < relevantIndexArray.size(); ++i)
            relevantIndexArray.get(0).retainAll(relevantIndexArray.get(i));


        ArrayList<String> relevantUrl = new ArrayList<>();
        if (relevantIndexArray.get(0) != null) {
            relevantUrl = new ArrayList<>(relevantIndexArray.get(0));
            relevantUrl.sort((s, t1) -> relevanteIndex.get(s).size() > relevanteIndex.get(t1).size() ? -1 : (relevanteIndex.get(s).size() > relevanteIndex.get(t1).size()) ? 1 : 0);
        }

        if (relevantUrl.size() > 0) {
            for (String urlFromRelevant:relevantUrl)
                output = output.concat("Enjoy: " + urlFromRelevant + " . " + relevanteIndex.get(urlFromRelevant).size() + "\n");
        } else {
            output = "No results found";
        }
        return output;
    }


    public String getPointToLink (String point) throws RemoteException {
        CopyOnWriteArraySet<String> aux = relevanteIndex.get(point);
        String output = "";
        if (aux != null) {
            for (String str : aux) {
                output = output.concat("Poited by: " + str + "\n");
            }
        }
        return output;
    }

    public static void main(String[] args) {

        // receives file path
        if (args.length != 1) {
            System.out.println("Wrong number of arguments.");
            System.exit(0);
        }
        // read data file
        try {
            File dataFile = new File(args[0]);
            if (dataFile.exists()) {
                Scanner scn = new Scanner(dataFile);
                while (scn.hasNextLine()) {
                    String data = scn.nextLine();
                    System.out.println(data + " c");
                }
                scn.close();
            } else {
                if (!dataFile.createNewFile())
                    System.exit(0);
                else
                    System.out.println("Created file");
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // open a file to write
        FileWriter myWriter;
        try {
            myWriter = new FileWriter(args[0]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Inter server = (Inter) LocateRegistry.getRegistry(5000).lookup("barrel");
            StorageBarrel client = new StorageBarrel();
            String r = server.registerBarrel(client);
            System.out.println(r);
            Signal.handle(new Signal("INT"), sig -> {
                try {
                    server.logoutBarrel(client);
                    myWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
            });
        } catch (Exception e) {
            //System.out.println("Exception : " + e);
            e.printStackTrace();
        }

        MulticastSocket socket = null;
        try {
            int PORT = 4321;
            String MULTICAST_ADDRESS = "224.3.2.1";
            socket = new MulticastSocket(PORT);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String title, url, type, token, mUrl;
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                type = new String(packet.getData(), 0, packet.getLength());
                myWriter.write(type + "\n");

                String[] tokens = type.split(" ");

                switch (tokens[0]){
                    case "Title":
                        url = tokens[1];
                        title = tokens[2];
                        for (int i = 3; i < tokens.length; ++i)
                            title = title.concat(" " + tokens[i]);

                        titles.put(url, title);
                        System.out.println(" url " + url + " title " + title );
                        break;
                    case "Token":
                        try {
                            url = tokens[1];
                            token = tokens[2];
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
                        break;
                    case "Url":
                        url = tokens[1];
                        mUrl = tokens[2];
                        CopyOnWriteArraySet<String> value = relevanteIndex.get(mUrl);
                        if (value == null) {
                            CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<>();
                            set.add(url);
                            relevanteIndex.put(mUrl, set);
                        } else {
                            value.add(url);
                            relevanteIndex.put(mUrl, value);
                        }
                        // System.out.println(" url " + url + " token " + mUrl );
                        break;
                }
            }
            // myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert socket != null;
            socket.close();
        }
    }
}
