package org.example;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;

import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.HashMap;
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

    public String Infos(String s) throws RemoteException{
        System.out.println("In barrel: "+s);
        return "Info from barrel";
    }

    public static void main(String[] args) {

        try {
            Binterface h = new StorageBarrel();
            LocateRegistry.createRegistry(9000).rebind("barrel", h);
            System.out.println("Storage Barrel ready.");

        } catch (RemoteException re) {
            System.out.println("Exception in barrel: " + re);
        }


        /*
        try {
            Inter server = (Inter) Naming.lookup("barrel");
            Binterface client = new StorageBarrel();
            System.out.println(server.registerBarrel(client));

        } catch (Exception e) {
            e.printStackTrace();
        }
        */


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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert socket != null;
            socket.close();
        }
    }
}
