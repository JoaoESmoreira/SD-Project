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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


public class StorageBarrel extends UnicastRemoteObject implements Binterface {
    public static ConcurrentHashMap<String, CopyOnWriteArraySet<String>> invertedIndex;
    public static HashMap<String, String> titles;

    public static HashMap<String, Integer> searches;
    public static ConcurrentHashMap<String, CopyOnWriteArraySet<String>> relevanteIndex;

    static HashMap<String, String> Regists;
    String filename;

    StorageBarrel() throws RemoteException{
        super();
        invertedIndex = new ConcurrentHashMap<>();
        titles = new HashMap<>();
        relevanteIndex = new ConcurrentHashMap<>();
        searches = new HashMap<>();
        Regists = new HashMap<>();

    }

    public void Setfilename(String name){
        this.filename = name;
    }

    public String Getfilename(){
        return this.filename;
    }

    public String Regist(String username, String password) throws RemoteException{

        FileWriter Writer;
        String output = "";
        try {
            Writer = new FileWriter(filename, true);

            if (Regists.get(username) == null) {
                Regists.put(username, password);
                String msgregist = "Regist " + username + " " + password;
                Writer.write(msgregist + "\n");
                output =  "Regist Done";
            } else {
                output =  "Username already registed";
            }

            Writer.close();
            return output;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String Log_in(String username, String password) throws RemoteException{

        String output = "";

        if(Regists.get(username)== null){
            output = "Need to register";
        } else{
            if(Objects.equals(Regists.get(username), password)){
                output =  "LOGGED IN";
            }
            else {
                output = "Wrong password";
            }
        }

        return output;

    }

    public String GetInfos() throws RemoteException{

        String output = "Most searched:\n";

        if(searches.isEmpty()){
            return "No searches done";
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(searches.entrySet());
        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        int con=1;
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()){
            output = output.concat( String.valueOf(con) + " - '"  + entry.getKey() + "'\n");
            con++;
            if(con>10) break;

        }



        return output;
    }

    public String getSearch(String search) throws RemoteException {

        FileWriter Writer;
        try {
            Writer = new FileWriter(filename,true);
        
        
        
            if (searches.get(search)!=null){
                searches.replace(search,searches.get(search),searches.get(search)+1);
            }
            else{
                searches.put(search,1);
            }
            String msgsearch = "Search " + search + " " + searches.get(search);
            Writer.write(msgsearch + "\n");
            

            String output = "";
            String[] tokens = search.split(" ");
    
            ArrayList<CopyOnWriteArraySet<String>> relevantIndexArray = new ArrayList<>();
            for (String string:tokens) {
                CopyOnWriteArraySet<String> set = invertedIndex.get(string);
                relevantIndexArray.add(set);
                //System.out.println(set);
            }
    
    
    
            for (int i = 1; i < relevantIndexArray.size(); ++i) {
                if(relevantIndexArray.get(i)==null){
                    try {
                        Writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return "No results found";
                }
                relevantIndexArray.get(0).retainAll(relevantIndexArray.get(i));
            }
    
            ArrayList<String> relevantUrl = new ArrayList<>();
            if (relevantIndexArray.get(0) != null) {
                relevantUrl = new ArrayList<>(relevantIndexArray.get(0));
                relevantUrl.sort((s, t1) -> relevanteIndex.get(s).size() > relevanteIndex.get(t1).size() ? -1 : (relevanteIndex.get(s).size() > relevanteIndex.get(t1).size()) ? 1 : 0);
            }
    
            if (relevantUrl.size() > 0) {
                for (String urlFromRelevant:relevantUrl) {
                    System.out.println(urlFromRelevant);
                    output = output.concat("Enjoy: " + urlFromRelevant + "\n");
                }
            } else {
                output = "No results found";
            }

        
            Writer.close();
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
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


        // open a file to write
        FileWriter myWriter;
        try {
            myWriter = new FileWriter(args[0],true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Inter server = (Inter) LocateRegistry.getRegistry(5000).lookup("barrel");
            StorageBarrel client = new StorageBarrel();
            client.Setfilename(args[0]);
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

        // read data file
        try {
            File dataFile = new File(args[0]);
            if (dataFile.exists()) {
                String title, url, type, token, mUrl, pesquisa, username, pass;
                int npesquisa;
                Scanner scn = new Scanner(dataFile);
                while (scn.hasNextLine()) {
                    String data = scn.nextLine();
                    System.out.println(data + " c");
                    String[] tokens = data.split(" ");

                    if(tokens[0] != null && tokens[1] != null && tokens[2] != null){

                        switch (tokens[0]){
                            case "Title":
                                url = tokens[1];
                                title = tokens[2];
                                for (int i = 3; i < tokens.length; ++i)
                                    title = title.concat(" " + tokens[i]);

                                titles.put(url, title);
                                //System.out.println(" url " + url + " title " + title );
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
                            case "Search":
                                pesquisa = tokens[1];
                                npesquisa = Integer.parseInt(tokens[tokens.length -1]);

                                for (int i = 2; i < tokens.length -1; ++i)
                                    pesquisa = pesquisa.concat(" " + tokens[i]);
                                if (searches.get(pesquisa)!=null){
                                    searches.replace(pesquisa,searches.get(pesquisa),npesquisa);
                                }
                                else{
                                    searches.put(pesquisa,npesquisa);
                                }
                                break;
                            case "Regist":
                                username = tokens[1];
                                pass = tokens[2];

                                Regists.putIfAbsent(username, pass);
                                break;
                        }
                    }
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
                boolean test = true;
                String[] tokens = type.split(" ");

                if(tokens[0] != null && tokens[1] != null && tokens[2] != null){
                    myWriter.write(type + "\n");
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

            }
            //myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert socket != null;
            socket.close();
        }
    }
}
