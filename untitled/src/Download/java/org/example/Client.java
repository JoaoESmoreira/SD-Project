package org.example;

import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        
        try {
			Inter h = (Inter) LocateRegistry.getRegistry(7000).lookup("ALGO");

			Scanner sc = new Scanner(System.in);

			while(true) {
				System.out.print("1 - Indexar URL\n2 - Pesquisa\n: ");
				String choice = sc.nextLine();

				if ("1".equals(choice)) {

					System.out.print("URL: ");
					String urll = sc.nextLine();

					String message = h.sayURL(urll);
					System.out.println("Server message: " + message);
				} else if ("2".equals(choice)) {
					System.out.print("Search: ");
					String search = sc.nextLine();

					String message = h.saySearch(search);
					System.out.println("Server message: " + message);
				}
				else {break;}
			}

		} catch (Exception e) {
			System.out.println("Exception in client: " + e);
			e.printStackTrace();
		}

	}


}

