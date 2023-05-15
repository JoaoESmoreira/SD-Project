package com.example.demo;

import com.example.demo.model.Connection;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@SpringBootApplication
public class DemoApplication {
	@Autowired
	private Connection loginService;
	private static final String RMI_HOST = "localhost";
	private static final int RMI_PORT = 6000;
	private static final String RMI_NAME = "LoginService";

	public static void main(String[] args) {
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
