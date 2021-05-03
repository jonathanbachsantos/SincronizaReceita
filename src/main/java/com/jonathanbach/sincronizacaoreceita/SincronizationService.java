package com.jonathanbach.sincronizacaoreceita;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class SincronizationService {
	
	
	//Tarefa assíncrona que valida com pelo ReceitaService.
    @Async
	public CompletableFuture<String[]> receita(String[] line) throws RuntimeException, InterruptedException {
		ReceitaService receita = new ReceitaService();
		
		//Validando alguns dados
		double str2 = Double.parseDouble(line[2].replace(",", ".")); 
		String str1 = line[1].replace("-", "");
		
		//Simulando enquando ocorrer um erro tenta novamente por no maximo 3 vezes.

		boolean receitaCheck = false;
		Integer i = 0;
		while((!receitaCheck) && (i <3)){
			try {
				receitaCheck = receita.atualizarConta(line[0], str1, str2, line[3]);
			} catch (RuntimeException myErro){
				System.out.println(myErro);
			}
			
			i++;
		}

		//Retornando array de String.
		String receitaCheckStr = receitaCheck + "";
		String[] line2 = new String[] {line[0], line[1], line[2], line[3], receitaCheckStr};
		
		return CompletableFuture.completedFuture(line2);
	}
	
	
}
