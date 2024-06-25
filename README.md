# [KEYCLOAK with Spring Boot & Angular | Step by Step Guide](https://www.youtube.com/watch?v=Ppkys9dKadA&list=PL41m5U3u3wwk0xrfl0FK--idljxVR2Dnx&index=4)

Tomado del canal de youtube de **Bouali Ali**.

---

En este curso de Spring Boot Angular, aprenderá cómo integrar `Keycloak` con `Spring Boot` y la aplicación `Angular`.
Recorriendo todas las configuraciones: creating a realm, client, users, roles, groups, localization,
social authentication, keycloak events, etc.

---

## Flujo de autenticación de Keycloak

A continuación se muestra de manera general cómo van a interactuar nuestras aplicaciones de Angular, Spring Boot y
Keycloak.

![01.keycloak_authentication_flow.png](assets/01.keycloak_authentication_flow.png)

## Configura Keycloak con Docker

En nuestro `compose.yml` agregaremos el servicio para `keycloak`, de tal manera que cuando ejecutemos docker compose se
nos cree el contenedor de `Keycloak` a partir de su imagen. A continuación mostramos todo el contenido de nuestro
archivo `compose.yml` que incluye el servicio para `Keycloak`:

````yml
services:
  postgres:
    image: postgres:15.2-alpine
    container_name: c-postgres-bsn
    restart: unless-stopped
    environment:
      POSTGRES_DB: db_book_social_network
      POSTGRES_USER: magadiflo
      POSTGRES_PASSWORD: magadiflo
    ports:
      - 5435:5432
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - spring-net

  mail-dev:
    image: maildev/maildev
    container_name: c-mail-dev-bsn
    restart: unless-stopped
    ports:
      - 1080:1080
      - 1025:1025
    networks:
      - spring-net

  keycloak:
    image: quay.io/keycloak/keycloak:24.0.2
    container_name: c-keycloak-bsn
    restart: unless-stopped
    command:
      - 'start-dev'
    ports:
      - 8181:8080
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
    networks:
      - spring-net

volumes:
  postgres_data:
    name: postgres_data

networks:
  spring-net:
    name: spring-net
````

Una vez definido el servicio para `Keycloak` ejecutamos el siguiente comando para levantar todos los contenedores
definidos en el archivo `compose.yml`:

````bash
M:\PROGRAMACION\DESARROLLO_JAVA_SPRING\02.youtube\18.bouali_ali\08.full_web_application\book-social-network-03 (main -> origin)

$ docker compose up -d              
[+] Running 0/0                     
[+] Running 4/4ng-net  Creating     
 ✔ Network spring-net        Created
 ✔ Container c-postgres-bsn  Started
 ✔ Container c-mail-dev-bsn  Started
 ✔ Container c-keycloak-bsn  Started
````

Comprobamos que los contenedores se hayan creado y estén ejecutándose:

````bash
$ docker container ls -a
CONTAINER ID   IMAGE                              COMMAND                  CREATED          STATUS                      PORTS                                            NAMES
e10bcc43e07e   postgres:15.2-alpine               "docker-entrypoint.s…"   53 seconds ago   Up 52 seconds               0.0.0.0:5435->5432/tcp                           c-postgres-bsn
961d80ea53ac   maildev/maildev                    "bin/maildev"            53 seconds ago   Up 52 seconds (unhealthy)   0.0.0.0:1025->1025/tcp, 0.0.0.0:1080->1080/tcp   c-mail-dev-bsn
e1986d4225f0   quay.io/keycloak/keycloak:24.0.2   "/opt/keycloak/bin/k…"   53 seconds ago   Up 52 seconds               8443/tcp, 0.0.0.0:8181->8080/tcp                 c-keycloak-bsn
````

Ahora accedemos mediante el navegador al servidor de `Keycloak` autenticándonos con las credenciales definidas en el
archivo `compose.yml`. Luego de iniciar sesión seremos redireccionados al siguiente panel de administrador.

![02.keycloak-ui.png](assets/02.keycloak-ui.png)

## 