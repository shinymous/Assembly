## Como rodar ##
###Java 11
###Instale ou já tenha instaldo o docker.
###Instale ou já tenha instalado o maven.

* Na raiz do projeto rode o comando "cd docker && docker-compose up" para subir o mysql e o rabbitmq local
* Na raiz do projeto rode o comando "mvn clean install"
* Rode o projeto Spring local com profile default

###Senhas, usuarios e portas estarão em src/resoruces/application-dev.properties
###Documentação da api http://localhost:8081/assembly/swagger-ui.html

Caso queira utilizar/testar a api sem ter que rodar local, acesse:
https://south-assembly.herokuapp.com/assembly/swagger-ui.html