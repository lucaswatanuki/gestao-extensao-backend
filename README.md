# GAS - Gestão de Atividades Simultâneas

## O projeto
<p align="justify">O sistema GAS foi desenvolvido para auxiliar na 
gestão das atividades simultâneas de extensão, além de facilitar a consulta às horas 
alocadas pelo docente em cada atividade.</p>

## Conteúdo
<!--ts-->
* [Instalação](#instalacao)
* [Como usar](#como-usar)
    * [Pre Requisitos](#pre-requisitos)
* [Testes](#testes)
* [Tecnologias](#tecnologias)
<!--te-->

### Features

- [x] Cadastro de regência concomitante
- [x] Cadastro de cursos de extensão
- [x] Cadastro de atividades convênios
- [x] Consulta de atividades
- [x] Consulta de alocações
- [x] Update de atividades de atividades
- [ ] Cadastro de atividades Univesp
- [x] Notificações via email
- [x] Extração de relatórios
- [x] Upload/Download de arquivos
- [ ] Filtros/Paginações (API)
    - [ ] Filtro e paginação de docente
    - [ ] Filtro e paginação de atividades
    - [ ] Filtro e paginação de alocações

### Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina as seguintes ferramentas:
[Git](https://git-scm.com), 
[Node.js](https://nodejs.org/en/), 
[Maven](https://maven.apache.org/), 
[Docker](https://www.docker.com/), 
[Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
Além disto é bom ter um editor para trabalhar com o código como 
[VSCode](https://code.visualstudio.com/) e o 
[IntelliJ](https://www.jetbrains.com/pt-br/idea/).

### 🎲 Rodando o Back End (API)

```bash
# Clone este repositório
$ git clone <https://github.com/lucaswatanuki/gestao-extensao-backend.git>

# Acesse a pasta do projeto com um terminal (pode ser o Git Bash)
$ cd gestao-extensao-backend

# Rode o seguinte comando para baixar as dependências do projeto
$ mvn clean install

# Rode o seguinte comando para iniciar o conteiner docker com o banco de dados
$ docker-compose up
```

Ao terminar de baixar as dependências e iniciar o conteiner do Docker, abra o projeto pelo IntelliJ.
Crie um arquivo nomeado "application-local.properties" e vá em "Edit Configurations", em seguida
"Environment variables" e coloque "PROFILE=local". No arquivo criado, você deverá colocar
as seguintes propriedades de configuração para estar apto a rodar o projeto localmente:

```bash
spring.profiles.active=${PROFILE}
server.port=8080
spring.datasource.url=#sua url de conexão do banco de dados
spring.datasource.username=#seu BD username
spring.datasource.password=#seu BD password
spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto=update
spring.servlet.multipart.max-file-size=2MB
spring.servlet.multipart.max-request-size=2MB

gestao.extensao.jwtSecret=#sua chave secreta
gestao.extensao.tempoExpiracaoJwt=3600000

spring.mail.host=smtp.mailgun.org
spring.mail.port=587
spring.mail.username=postmaster@mail.unicamp-extensao.com.br
spring.mail.from.username=comissao.extensao.ft@gmail.com
spring.mail.password=#consultar senha no painel de controle do Heroku
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.smtp.socketFactory.port=587
spring.mail.smtp.socketFactory.fallback=true
spring.mail.smtp.starttls.enable=true
spring.mail.smtp.starttls.required=true
spring.mail.smtp.ssl.enable=false
spring.mail.domain=mail.unicamp-extensao.com.br
spring.mail.api.key=#consultar chave no painel de controle do Heroku

coordenador.nome=#nome do coordenador atual

frontend.url=http://localhost:4200/
```
