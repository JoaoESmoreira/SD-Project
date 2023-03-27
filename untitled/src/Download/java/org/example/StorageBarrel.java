package org.example;//package src.Download.java.org.example;

import java.util.Random;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.ArrayList;


public class StorageBarrel extends UnicastRemoteObject implements Binterface {

    StorageBarrel() throws RemoteException{
        super();
    }

    public String Infos(String s) throws RemoteException{
        System.out.println("In barrel: "+s);
        return "Info from barrel";
    }

    public static void main(String[] args) {


        try {
            Inter server = (Inter) LocateRegistry.getRegistry(5000).lookup("barrel");
            Binterface client = new StorageBarrel();
            System.out.println(server.registerBarrel(client));

        } catch (Exception e) {
            //System.out.println("Exception : " + e);
            e.printStackTrace();
        }



        MulticastSocket socket = null;
        try {
            int PORT = 4321;
            socket = new MulticastSocket(PORT);  // create socket and bind it
            String MULTICAST_ADDRESS = "224.3.2.1";
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {

                byte[] buffer = new byte[1024];
                socket.receive(new DatagramPacket(buffer, 1024));
                System.out.println("Datagram received!");

                ByteArrayInputStream byteIn = new ByteArrayInputStream(buffer);
                ObjectInputStream objIn = new ObjectInputStream(byteIn);
                ArrayList<String> arrayList = (ArrayList<String>) objIn.readObject();

                System.out.println("ArrayList contents:");
                for (String s : arrayList) {
                    System.out.println(s);
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ne) {
            throw new RuntimeException(ne);
        } finally {
            assert socket != null;
            socket.close();
        }
    }
}
