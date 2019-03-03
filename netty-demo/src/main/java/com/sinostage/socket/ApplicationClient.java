package com.sinostage.socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author timkobe
 */
@SpringBootApplication
@ComponentScan({"com.sinostage.socket.client"})
public class ApplicationClient {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationClient.class, args);
    }
}
