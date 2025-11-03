## Instrucciones de como inicializar proyecto 

Este proyeto es un microservicio de springboot-java.


## Requisitos de para su uso

Necesitarás las técnologias de
-> Git hub
-> Docker y Docker Compose 
-> Maven 

## Pasos 

1. Clonar repositorio con Git Clone (nombre de tu repositorio a utilizar) en VS
2. Utilizarás cd (nombre del proyecto) para entrar a la carpeta

## Despliegue

En este proyecto se recomienda utilizar docker para el despliegue, por lo tanto, debes tener instalado en tu pc, esta plataforma nos permite ejecutar aplicaciones en contenedores.

3. (Opcional) crear carpeta deployment para archivos de Docker 
4. Crear archivos para la ejecución del despliegue
-Dockerfile
-docker-compose.yml
5. Comandos: docker-compose up --build -d (construir imágen)
   docker compose up
   docker-compose ps 


## Pruebas automatizadas con pipeline 

Se utiliza un pipeline de github actions como herramienta para poder automatizar la calidad y trazabilidad del microservicio

1. Se utiliza Jacoco que ejecuta las pruebas unitarias, en POM.Xl se debe encontrar el código entre pluggins que nos permite ejecutar las pruebas   
   Comandos: mvn test, mvn verify.
   abrir archivo (target/site/jacoco/index.html)
2. Se utiliza SonarCloud para poder verificar la calidad de nuestro código mediante distintos colores. Se configuro con una cuenta en sonarcloud y token.
    verde: Código Limpio
    amarillo: Código intermedio puede obtener mejoras
    rojo: Código con errores graves.

## Trazabilidad 

Este se garantiza al momento de que el codigo sea vinculado con nuestro despliegue (Docker).

