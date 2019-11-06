package fi.laituri;

import fi.laituri.game.GameEngine;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

@Configuration
@SpringBootApplication
@Slf4j
@SuppressWarnings("Duplicates")
public class App implements CommandLineRunner {
    
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    
    @Override
    public void run(String... args) {
        RedisServer rs = null;
        try {
            rs = new RedisServer(6379);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Starting redis");
        rs.start();
        log.info("Redis started");
    }

    @Bean
    public GameEngine getGameEngine() {
        return new GameEngine();
    }

}
