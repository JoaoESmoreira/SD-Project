package com.example.demo.model;

import com.example.demo.Inter;
import org.springframework.stereotype.Service;

@Service
public class Connection {

    private Inter connection;

    public Inter getConnection() {
        return connection;
    }

    public void setConnection(Inter connection) {
        this.connection = connection;
    }
}
