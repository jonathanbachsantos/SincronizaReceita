package com.jonathanbach.sincronizacaoreceita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class SincronizacaoreceitaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SincronizacaoreceitaApplication.class, args).close();
	}

	@Bean
	public Executor taskExecutor() {
		
		//Configuração da Thread

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(6);
		executor.setMaxPoolSize(6);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("SincronizacaoReceita-");
		executor.initialize();
		return executor;
	}

}
