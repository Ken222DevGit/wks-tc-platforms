package com.yld.tc.eureka;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;



@SpringBootApplication
@EnableEurekaServer
public class EurekaDiscoveryApplicationBootstrap {


    public static void main(String[] args) {
        SpringApplication.run(EurekaDiscoveryApplicationBootstrap.class, args);

    }

}
