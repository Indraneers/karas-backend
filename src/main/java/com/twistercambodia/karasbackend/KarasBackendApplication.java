package com.twistercambodia.karasbackend;

import com.twistercambodia.karasbackend.inventory.repositories.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KarasBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KarasBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(CategoryRepository categoryRepo) {
        return runner -> {
            createCategory(categoryRepo);
        };
    }

    private void createCategory(CategoryRepository categoryRepo) {

        // create the student object
    }
}
