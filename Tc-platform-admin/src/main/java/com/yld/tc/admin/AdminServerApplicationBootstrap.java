package com.yld.tc.admin;


import de.codecentric.boot.admin.config.EnableAdminServer;
import de.codecentric.boot.admin.model.Application;
import de.codecentric.boot.admin.model.StatusInfo;
import de.codecentric.boot.admin.registry.ApplicationIdGenerator;
import de.codecentric.boot.admin.registry.ApplicationRegistry;
import de.codecentric.boot.admin.registry.store.ApplicationStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@EnableAdminServer
@SpringBootApplication
@EnableDiscoveryClient
public class AdminServerApplicationBootstrap implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplicationBootstrap.class, args);
    }


    public  ApplicationStore store;


    @Override
    public void run(String... strings) throws Exception {
        Executors.newScheduledThreadPool(2).scheduleWithFixedDelay(() -> {
            for(Application app: store.findAll()) {
                if( app.getStatusInfo().isOffline()) {
                    store.delete(app.getId());
                }
            }
        }, 1, 5000, TimeUnit.MILLISECONDS);
    }


    @Bean
    @ConditionalOnMissingBean
    public ApplicationRegistry applicationRegistry(ApplicationStore applicationStore, ApplicationIdGenerator applicationIdGenerator) {
        return new ApplicationRegistry(applicationStore, applicationIdGenerator) {

            @Override
            public Application register(Application application) {
                if(application.getHealthUrl().contains("127.0.0.1")
                        || application.getHealthUrl().contains("localhost")
                        || application.getHealthUrl().contains("::1")) {
                    Application.Builder builder = Application.copyOf(application).withId(String.valueOf(System.currentTimeMillis())).withStatusInfo(StatusInfo.ofUnknown());
                    Application registering = builder.build();
                    return registering;
                }
                return super.register(application);
            }
        };
    }


}
