package com.leyou.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Si6x
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.leyou.search.client")
public class LeyouSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouSearchApplication.class,args);
    }
}