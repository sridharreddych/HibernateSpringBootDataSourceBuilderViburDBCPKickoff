package com.app;

import com.app.repository.NumberRepository;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private static final Logger logger = Logger.getLogger(MainApplication.class.getName());
        
    private static final ExecutorService executor = Executors.newFixedThreadPool(25);

    private final NumberRepository numberRepository;    
    private final DataSource dataSource;

    public MainApplication(NumberRepository numberRepository, DataSource dataSource) {
        this.numberRepository = numberRepository;
        this.dataSource = dataSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            logger.info("-------------------------------------------------------");
            logger.log(Level.INFO, "DataSource: {0}", dataSource);
            logger.info("-------------------------------------------------------");
            
            while (true) {
                executor.execute(numberRepository);

                Thread.sleep((int) (Math.random() * 250));
            }

        };
    }
}

/*
 * How To Customize ViburDBCP Settings Via Properties And DataSourceBuilder

Note: The best way to tune the connection pool parameters consist in using Flexy Pool by Vlad Mihalcea. Via Flexy Pool you can find the optim settings that sustain high-performance of your connection pool.

Description: This is a kickoff application that set up ViburDBCP via DataSourceBuilder. The jdbcUrl is set up for a MySQL database. For testing purposes, the application uses an ExecutorService for simulating concurrent users.

Key points:

in pom.xml add the ViburDBCP dependency
in application.properties, configure ViburDBCP via a custom prefix, e.g., app.datasource.*
write a @Bean that returns the DataSource
 * 
 */
