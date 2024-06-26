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

## Crea un nuevo realm y un client en Keycloak

Para crear un nuevo `realm` seguimos los pasos de la imagen de abajo. El nuevo `realm` que crearemos tendrá el nombre
de `book-social-network`.

![03.new-realm.png](assets/03.new-realm.png)

En la esquina superior izquierda seleccionamos el realm que acabamos de crear y luego en el menú del lateral izquierdo
seleccionamos `clients`, para crear un nuevo cliente.

El cliente que vamos a crear será para conectar nuestro frontend de `Angular` con el `realm`, así como también con
nuestro backend de `Spring Boot`.

![04.new-client.png](assets/04.new-client.png)

La siguiente imagen muestra las configuraciones generales que le vamos a dar a este cliente:

![05.general-settings-client.png](assets/05.general-settings-client.png)

En la siguiente imagen dejamos todas las configuraciones por defecto:

![06.capability-config.png](assets/06.capability-config.png)

Finalmente, en la etapa de `Loging Settings` agregamos las urls según el campo solicitado:

![07.loging-settings.png](assets/07.loging-settings.png)

**DONDE**

- `Root URL`, debe ser la URL raíz de nuestra aplicación de frontend, en nuestro caso es `http://localhost:4200`.


- `Valid redirect URIs`, patrón de URI válido al que un navegador puede redirigir después de un inicio de sesión
  exitoso. Se permiten comodines simples como `http://example.com/*`. También se puede especificar la ruta relativa,
  como `/my/relative/path/*`. Las rutas relativas son relativas a la URL raíz del cliente o, si no se especifica
  ninguna, se utiliza la URL raíz del servidor de autenticación.


- `Valid post logout redirect URIs`, patrón de URI válido al que un navegador puede redirigir después de cerrar sesión
  correctamente. Un valor de `+` o un campo vacío utilizará la lista de uris de redireccionamiento válidos. Un valor
  de `-` no permitirá ningún URI de redireccionamiento posterior al cierre de sesión. Se permiten comodines simples
  como `http://example.com/*`. También se puede especificar la ruta relativa, como `/my/relative/path/*`. Las rutas
  relativas son relativas a la URL raíz del cliente o, si no se especifica ninguna, se utiliza la URL raíz del servidor
  de autenticación.


- `Web origins`, orígenes `CORS` permitidos. Para permitir todos los orígenes de URI de redireccionamiento válidos,
  agregue `+`. Sin embargo, esto no incluye el comodín `*`. Para permitir todos los orígenes, agregue
  explícitamente `*`.

Finalmente, veremos que nuestro cliente ha sido creado exitosamente.

![08.created-client.png](assets/08.created-client.png)

## Crea un usuario

Seleccionando el realm `book-social-network`, vamos a crear un usuario que lo usaremos más adelante.

![09.create-user.png](assets/09.create-user.png)

Crearemos el usuario tal como se ve en la imagen inferior. Este usuario solo nos servirá para mostrar el progreso
que vayamos realizando a nuestra aplicación. Nótese además que en el campo `email verified` estamos marcando como `Yes`
para decirle a `Keycloak` que el email del usuario lo coloque como un email verificado.

**¡Importante!**
> Nuestro objetivo final no es usar keycloak para crear usuarios, sino que queremos registrar usuarios a través de
> Keycloak

![10.new-user.png](assets/10.new-user.png)

Una vez creado el usuario, seremos redireccionados a la siguiente página:

![11.user-created.png](assets/11.user-created.png)

Ahora, iremos a la pestaña `Credentials` para asignarle una contraseña al usuario:

![12.set-password-to-user.png](assets/12.set-password-to-user.png)

---

# Integrando Keycloak con Angular

---

## Instalando dependencia: keycloak-js

Agregaremos la siguiente dependencia `keycloak-js` a nuestra aplicación de Angular. **Esta dependencia es una
biblioteca JavaScript `OpenID Connect` del lado del cliente que se puede utilizar para proteger aplicaciones web.**

**¡Importante!**
> La versión de `keycloak-js` que debemos instalar debe ser la misma versión del servidor de `Keycloak` que estemos
> usando. En nuestro caso, recordemos que hemos dockerizado nuestro servidor de `Keycloack` y la versión que estamos
> usando es la `24.0.2`, por lo tanto, la dependencia `keycloak-js` que debemos instalar debe ser la misma.

````bash
M:\PROGRAMACION\DESARROLLO_JAVA_SPRING\02.youtube\18.bouali_ali\08.full_web_application\book-social-network-03\book-network-frontend (main -> origin)
$ npm i keycloak-js@24.0.2
````

## Crea y configura el servicio para keycloak

Crearemos el servicio `KeyclockService` a quien configuraremos para que se ejecute cuando arranque nuestra aplicación.
Al iniciar nuestra aplicación debemos decirle a Angular que tenemos un proveedor de autenticación, en nuestro
caso `Keycloack`, que queremos usar y que debe realizar todas las comprobaciones en dicho servidor.

````typescript
@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  async init() {
    console.log('Inicializando Keycloak');
  }

}
````

**DONDE**

- Notar que a la función `init()` le hemos antepuesto el `async`, con esto estamos indicando que es una función
  asíncrona. Más adelante utilizaremos el `await` dentro de la función `init` para interactuar con el servidor de
  keycloak.
- Al tener definida la función con `async`, la función se convierte automáticamente en una función que devuelve una
  `promesa`, independientemente de si utiliza `await` internamente o no. El uso de `await` dentro de la función
  simplemente permite manejar otras promesas de manera más legible.
- Si la función `async` retorna un valor, ese valor se convierte en la resolución de la promesa.
- Si una función `async` no retorna explícitamente un valor, la promesa que se resuelve tendrá un valor de `undefined`,
  lo que en términos prácticos equivale a `void`.
- Si la función `async` lanza una excepción, esa excepción se convierte en el rechazo de la promesa.

## ¿Qué es APP_INITIALIZER?

`Angular` viene con un token `APP_INITIALIZER`, lo que permite a los desarrolladores ejecutar algún código antes de que
la aplicación comience a ejecutarse. Proporciona una forma de cargar datos, realizar la autenticación o configurar el
entorno de la aplicación antes de que la aplicación se represente en el explorador.

`Angular` **ejecuta la función asociada con un token durante la carga de la aplicación. Si la función devuelve una
promesa, Angular esperará a que la promesa se resuelva antes de inicializar la aplicación. Esto lo convierte en un
lugar ideal para ejecutar la lógica de inicialización.**

El token `APP_INITIALIZER` se define como proveedor en el módulo `@angular/core`. Toma una función como argumento y
devuelve un `Promise` o un `Observable`. Cuando se inicia la aplicación, `Angular` invoca al proveedor `APP_INITIALIZER`
y espera a que se resuelva la promesa o el observable antes de renderizar la aplicación.

Para utilizar `APP_INITIALIZER` en la aplicación, debe definir una función que realice las tareas de inicialización y
devuelva un `Promise` u `Observable`. Por ejemplo, en nuestro caso, deseamos comprobar usando kecyclock si un usuario
está autenticado o no antes de que inicie la aplicación. Puede definir un proveedor de `APP_INITIALIZER` en el
archivo `app.config.ts` (para Angular clásico sería en el archivo `app.module.ts`) de la siguiente manera:

````typescript
export function initialize(kcService: KeycloakService) {
  return () => kcService.init();
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(APP_ROUTES),
    provideHttpClient(withInterceptors([httpTokenInterceptor])),
    {
      provide: APP_INITIALIZER,
      useFactory: initialize,   //<-- Define el método initialize
      deps: [KeycloakService],  //<-- Dependencias que serán inyectadas al método initialize()
      multi: true,
    }
  ]
};
````

**DONDE**

- `multi: true`, esto indica que puede haber múltiples inicializadores registrados. Angular ejecutará todas las
  funciones de inicialización y esperará a que todas las promesas u observables se resuelvan antes de continuar con la
  inicialización de la aplicación.
- `useFactory`, permite especificar una función de fábrica que crea la función de inicialización. Esto es útil para
  inyectar servicios en la función de inicialización.

Con la configuración anterior, cuando se inicie la aplicación, `Angular` invocará la función `initialize` a quien le
estamos pasando o inyectando como `deps (dependencias)` la clase `KeycloakService`. El método `initialize` utilizará
el método `init()` del servicio inyectado antes de renderizar la aplicación.