#FROM maven
#WORKDIR /workdir/server
#COPY pom.xml /workdir/server/pom.xml
#RUN mvn dependency:go-offline
#
#COPY src /workdir/server/src
#RUN mvn install
#
#FROM openjdk
#
#WORKDIR /app
#
#COPY target/fake-bank-0.0.1-SNAPSHOT.jar /app/fake-bank.jar
#
#ENTRYPOINT ["java", "-jar", "fake-bank.jar"]

# Use uma imagem base com o Maven
FROM maven:3.8-openjdk-17 AS build

# Crie um diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo POM do projeto para o contêiner
COPY pom.xml .

# Baixe as dependências do Maven (isso permite que as dependências sejam cacheadas)
RUN mvn dependency:go-offline -B

# Copie o restante do código-fonte do projeto para o contêiner
COPY src ./src

# Compile o projeto usando o comando "mvn install"
RUN mvn install -DskipTests -X

# Agora, construa a imagem final com o OpenJDK
FROM openjdk:17

# Crie um diretório de trabalho para a aplicação
WORKDIR /app

# Copie o arquivo JAR gerado pelo Maven para o contêiner
COPY --from=build /app/target/fake-bank-0.0.1-SNAPSHOT.jar ./fake-bank.jar

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "fake-bank.jar"]
