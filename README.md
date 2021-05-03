# SincronizaReceita
Executando uma receita fake de envio de dados ao banco central com função assíncrona no Spring Boot.
Os dados e o retorno são em arquivo CSV.

## Acesse a pasta do repositório
cd SincronizaReceita

## Gere o arquivo jar
mvn package

## Teste com o exemplo de CSV
java -jar target/sincronizacaoreceita-0.0.1-SNAPSHOT.jar teste.csv
