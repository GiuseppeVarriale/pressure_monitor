# Pressure Monitor

Este é um projeto de monitoramento de pressão que permite criar, atualizar e monitorar medidores de pressão. O sistema gera notificações de alerta com base nas leituras de pressão e notifica um sistema externo em caso de variações significativas ou leituras fora do intervalo esperado.

## Funcionalidades

- Criação e atualização de medidores de pressão
- Processamento de leituras de pressão
- Geração de notificações de alerta
- Notificação de um sistema externo em caso de alertas

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- Mockito (para testes unitários)
- Maven

## Pré-requisitos

- Java 17
- Maven
- Docker (opcional, para rodar o banco de dados em um contêiner)

## Configuração do Ambiente

### Banco de dados necessários
Você precisará instalar ou rodar via docker o `PostgreSQL` e o `Redis`

No PostgreSQL será necessário criar 2 tabelas:
* pressure_monitor_development
* pressure_monitor_test

Você pode fazer isso com os seguintes comandos no terminal onde o PostgreSQL está instalado

```bash
psql -U seu_usuario -h localhost
```
vai pedir sua senha se tiver configurada, digite-a e tecle enter.

depois rode:

```bash
CREATE DATABASE pressure_monitor_development;
CREATE DATABASE pressure_monitor_test;
GRANT ALL PRIVILEGES ON DATABASE pressure_monitor_development TO seu_usuario;
GRANT ALL PRIVILEGES ON DATABASE pressure_monitor_test TO seu_usuario;
GRANT ALL PRIVILEGES ON DATABASE pressure_monitor_development TO postgres;
GRANT ALL PRIVILEGES ON DATABASE pressure_monitor_test TO postgres;
\q
```

### Arquivo `.env`

Copie o arquivo `.env.sample` na raiz do projeto para `.env` e subistitua pelas configurações corretas.

## Executando o Projeto

### Usando Maven

1. Clone o repositório:

```bash
git clone https://github.com/seu-usuario/pressure-monitor.git
cd pressure-monitor
```

2.Compile e execute o projeto:

```bash
mvn clean install
mvn spring-boot:run
```
