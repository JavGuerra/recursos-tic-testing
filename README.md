# Recursos TIC

## Proyecto transversal del Curso de Testing con Java
Adecco, diciembre 2024

[[Ver el PDF de la presentación](src/main/resources/static/doc/presentacion-recursos_tic.pdf)] · [[Ver el vídeo de la presentación](https://youtu.be/d_SeaUuKrMg)] · [[Ir al sitio web](https://recursos-tic.up.railway.app/)]


## La práctica

Empleando Java y el framework Spring Boot, se ha realizado la planificación e implementación de una aplicación web que permite gestionar recursos TIC y listas de recursos, para realizar los tests correspondientes, aplicando los conocimientos aprendidos durante la formación.

En su desarrollo se ha empleado Spring security, Hibernate, Thymeleaft y Bootstrap. Se ha codificado usando IntelliJ IDEA.

Los tests han sido realizados con JUnit, Mockito, Selenium, JaCoCo, GitHub Actions y SonarQube.

La planificación y codificación se han llevado a cabo usando Trello y GitHub.

## Destacados

Además de los requisitos del MVP, se han incluido las siguientes funcionalidades:

- Diseño responsive, "mobile first"
- Web accesible AA. Testada con WAVE
- Seguridad por diseño
- Confirmaciones de borrado
- Implementación de login de usuarios
- Integra el editor TinyMCE
- Provee API REST e interfaz Swagger
- Incluye aviso legal, privacidad y cookies
- Documentación la aplicación con JavaDoc
- Usa el protocolo OpenGraph para RR.SS.
- Manifest para móviles (PWA)
- Desarrollada con Linux + Docker LAMP
- Despliegue

# Indicaciones de uso

## Requisitos

Para ejecutar el proyecto se requiere:

- Java 23 o superior
- Maven
- MySQL
- Docker

## Configuración

Actualmente la aplicación se ejecuta localmente en el puerto 8082.

Puede construirse un contenedor Docker con la imagen de la aplicación ejecutando el fichero `docker.sh` que hace uso de docker-compose para lanzar la aplicación y la BBDD que requiere.

Para generar un informe de cobertura de testing, se puede ejecutar el comando `mvn site` y acceder a él en el directorio `target/site/jacoco/index.html`.

# Autores

Desarrollado por [Javier](https://github.com/JavGuerra), [Kevin](https://github.com/kevinzamoraa) y [Marina](https://github.com/MarinaVallejo89)

# Licencia

Este proyecto está licenciado bajo licencia [GPL v.3](https://www.gnu.org/licenses/gpl-3.0.html).
