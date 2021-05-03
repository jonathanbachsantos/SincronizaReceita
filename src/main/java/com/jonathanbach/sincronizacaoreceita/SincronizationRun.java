package com.jonathanbach.sincronizacaoreceita;

import java.util.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class SincronizationRun implements CommandLineRunner {

	private final SincronizationService sincronizationService;
	
	public SincronizationRun(SincronizationService sincronizationService) {
		this.sincronizationService = sincronizationService;
	}

	
    @Override
	public void run(String... args) throws IOException, RuntimeException, InterruptedException, ExecutionException{
        
		//Lendo e transformando os dados do arquivo CSV em uma lista.

		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
			String line;
			int i = 0;
			
			while ((line = br.readLine()) != null) {
				
				String[] values = line.split(";");
				records.add(Arrays.asList(values));
				i++;
			}
			
		}

		//Declarando array de tarefas assíncronas.
		CompletableFuture<?>[] arrayFuture = new CompletableFuture<?>[records.size()-1];

		//Preenchendo array de tarefaz assíncronas 
		try {
			
			int i = 0;
		
			while (i < records.size()) {
				if (i != 0){
					String[] myArray = new String[records.get(i).size()];
					records.get(i).toArray(myArray);
					arrayFuture[i-1] = sincronizationService.receita(myArray);
				}
				i++;
			}
			

		} catch (Exception e) {
			System.out.println(e);
		}

		//Executando array de tarefas assíncronas.
		CompletableFuture.allOf(arrayFuture).join();

		//Criando novo arquivo CSV
		Long timestamp = System.currentTimeMillis();
		File file = new File(timestamp + args[0]);
	
		if(!file.exists()){
			file.createNewFile();
			System.out.println("Arquivo criado com sucesso");
		}else{
			System.out.println("Arquivo ja existe");
		}
		
		
		FileWriter fw = new FileWriter(file, true);
		PrintWriter pw = new PrintWriter(fw);
		BufferedWriter bw = new BufferedWriter(pw);
		
		int count = 0;
		

		//Criando um lista com o resultado do ReceitaService.
		while (count < records.size()) {
			List<String> newLine = records.get(count);
			String concat = "";


			if(count == 0){
				for(String s : newLine){
					concat = concat + s + ";";
				}
				concat = concat + "resultado;";
			}else{
				Object objArr = arrayFuture[count-1].get();
				
				if (objArr instanceof String[]) {
					String[] strArray = (String[]) objArr;
					
					for(String s : strArray){
						concat = concat + s + ";";
					}
				}
			}
			
			//Escrevendo no arquivo
			bw.write(concat);
			bw.newLine();

			count++;
		}
		//Fechando arquivo
		bw.close();
					
    }

}
