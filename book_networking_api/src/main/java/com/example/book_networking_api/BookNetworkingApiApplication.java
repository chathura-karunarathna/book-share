package com.example.book_networking_api;

import com.example.book_networking_api.entity.Role;
import com.example.book_networking_api.repo.RoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class BookNetworkingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetworkingApiApplication.class, args);
		}

	@Bean
	public CommandLineRunner runner(RoleRepo roleRepo){
		return args -> {
			if(roleRepo.findByName("USER").isEmpty()){
				roleRepo.save(Role.builder().name("USER").build());
			}
		};
	}

}
