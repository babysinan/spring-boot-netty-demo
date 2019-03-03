package com.sinostage.socket;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author timkobe
 */
@SpringBootApplication
@ComponentScan({"com.sinostage.socket.server"})
public class ApplicationServer {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationServer.class, args);
    }

}
