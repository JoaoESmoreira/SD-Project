package com.example.demo;

import com.example.demo.model.UrlModel;

import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        
        try {
			Inter h = (Inter) LocateRegistry.getRegistry(7000).lookup("ALGO");

			Scanner sc = new Scanner(System.in);

			boolean flag= false;
			while(true) {


				if(!flag) {
					System.out.print("0 - QUIT\n1 - Index URL\n2 - Search\n3 - Register\n4 - Log in\n5 - Get Stats\n:");
				} else{
					System.out.print("0 - QUIT\n1 - Index URL\n2 - Search\n5 - Get Stats\n6 - Get pointed links\n: ");
				}

				String choice = sc.next();
				sc.nextLine();

				if ("1".equals(choice)) {

					System.out.print("URL: ");
					String urll = sc.next();

					String message = h.sayURL(urll);
					System.out.println("Server message: " + message);
				} else if ("2".equals(choice)) {
					System.out.print("Search: ");
					String search = sc.nextLine();

					// ArrayList<UrlModel> message = h.saySearch(search);
					// System.out.println(message);
				} else if ("3".equals(choice) && !flag) {
					System.out.print("Username: ");
					String username = sc.next();
					sc.nextLine();
					System.out.print("password: ");
					String password = sc.next();
					sc.nextLine();

					String message = h.Register(username,password);
					System.out.println("Server message: " + message);

				}  else if ("4".equals(choice) && !flag) {
					System.out.print("Username: ");
					String username = sc.next();
					sc.nextLine();
					System.out.print("password: ");
					String password = sc.next();
					sc.nextLine();

					String message = h.Login(username,password);
					System.out.println("Server message: " + message);
					if (message.equals("LOGGED IN")){
						flag =  true;
					}
				} else if ("6".equals(choice) && flag) {
					System.out.print("URL: ");
					String link = sc.next();
					sc.nextLine();
					String message = h.pointToLink(link);
					System.out.println(message);
				} else if ("5".equals(choice) ) {
					String message = h.Stats();
					System.out.println(message);
				}
				else if ("0".equals(choice) ) {
					break;
				}
				else {
					System.out.println("Not a valid key\n");
				}
			}

		} catch (Exception e) {
			System.out.println("Exception in client: " + e);
			e.printStackTrace();
		}

	}


}

