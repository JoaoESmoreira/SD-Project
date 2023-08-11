package com.example.demo;

import com.example.demo.model.Connection;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {
	@Autowired
	private Connection loginService;
	private static String RMI_HOST = "localhost";
	private static final int RMI_PORT = 6000;
	private static final String RMI_NAME = "LoginService";

	public static void main(String[] args) {
		// if (args.length != 1) {
		// 	System.out.println("Wrong number of arguments.");
		// 	System.exit(0);
		// }
		// RMI_HOST = args[0];
		SpringApplication.run(DemoApplication.class, args);
	}

	@PostConstruct
	public void init()  {
		try {
			Registry registry = LocateRegistry.getRegistry(RMI_HOST, RMI_PORT);
			loginService.setConnection((Inter) registry.lookup(RMI_NAME));
		} catch (RemoteException | NotBoundException ignored) {
			System.out.println("ERROR");
		}
	}

}
