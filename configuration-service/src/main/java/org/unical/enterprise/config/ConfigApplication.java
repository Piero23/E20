package org.unical.enterprise.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigApplication {

    ///TODO mettere tutto su git (chiedere a scalzo o dodaro)
    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }

}
