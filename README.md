## Documentación de usuarios probando la página: 
https://drive.google.com/drive/folders/1FTyAS4qfaZ8tyk1S8Bl8w7BAvLJFtylD?usp=sharing

# Manual de Instalación - Furry Mate

Este manual te guiará a través del proceso de instalación de Maven y Spring Boot, así como los pasos para ejecutar la página de Furry Mate utilizando `mvnw.cmd spring-boot:run`.

## Requisitos Previos

Antes de comenzar con la instalación, asegúrate de cumplir con los siguientes requisitos:

- **Java Development Kit (JDK) 11 o superior**: Asegúrate de tener JDK instalado en tu sistema.
- **Git**: Asegúrate de tener Git instalado para clonar el repositorio.

## Paso 1: Instalación de Maven

### Windows

1. **Descargar Apache Maven**:
   - Ve a la página oficial de [Apache Maven](https://maven.apache.org/download.cgi).
   - Descarga la versión binaria ZIP más reciente.

2. **Extraer el archivo ZIP**:
   - Extrae el contenido del archivo ZIP en una ubicación deseada, por ejemplo, `C:\Program Files\Apache\maven`.

3. **Configurar las Variables de Entorno**:
   - Agrega `MAVEN_HOME` como una variable de entorno y establece su valor como la ruta de la carpeta de Maven, por ejemplo, `C:\Program Files\Apache\maven`.
   - Agrega `%MAVEN_HOME%\bin` a la variable de entorno `Path`.

4. **Verificar la Instalación**:
   - Abre una terminal y ejecuta el siguiente comando:
     ```sh
     mvn -version
     ```
   - Deberías ver la versión de Maven y la versión de Java.

### MacOS y Linux

1. **Usar Homebrew (MacOS)**:
   - Abre una terminal y ejecuta el siguiente comando:
     ```sh
     brew install maven
     ```

2. **Usar el Gestor de Paquetes (Linux)**:
   - En distribuciones basadas en Debian, ejecuta:
     ```sh
     sudo apt-get install maven
     ```

   - En distribuciones basadas en Red Hat, ejecuta:
     ```sh
     sudo yum install maven
     ```

3. **Verificar la Instalación**:
   - Abre una terminal y ejecuta el siguiente comando:
     ```sh
     mvn -version
     ```
   - Deberías ver la versión de Maven y la versión de Java.

## Paso 2: Clonar el Repositorio del Proyecto

1. Abre una terminal y clona el repositorio del proyecto desde GitHub:
   ```sh
   git clone <URL_del_repositorio>

2. Navega al directorio del proyecto:
   ```sh
   cd <nombre_del_proyecto>

## Paso 3: Ejecutar el Proyecto con Spring Boot

1. Usar el Wrapper de Maven (mvnw):
   - En Windows:
   ```sh
   mvnw.cmd spring-boot:run
   ```

   - En MacOS y Linux:
   ```sh
   mvnw.cmd spring-boot:run
   ```
2. Esperar a que el Servidor Inicie:
   El comando anterior iniciará el servidor de Spring Boot y la aplicación estará disponible en `http://localhost:8080`.

# ¡Listo! 

Ahora deberías tener la aplicación de Furry Mate en funcionamiento. Si tienes algún problema o necesitas más ayuda, no dudes en contactar con nuestro soporte técnico.

¡Disfruta utilizando Furry Mate!

# Manual de Instrucciones - Furry Mate

## Requisitos para usar la página
Para poder utilizar la página de Furry Mate, asegúrate de cumplir con los siguientes requisitos:

- Utilizar un navegador actualizado (Google Chrome, Firefox, Microsoft Edge, etc.).
- Tener una conexión a internet estable.

## Registro de Usuario
Para comenzar a utilizar Furry Mate, primero necesitas registrar un usuario. Sigue estos pasos:

1. Dirígete a la página de **Registro**.
2. Completa el formulario con tu nombre de usuario y contraseña.
3. Haz clic en el botón **Registrar**.
4. Una vez registrado, serás redirigido a la página de registro de tu perro.

## Registro de tu Perro
Después de registrar tu usuario, necesitas registrar a tu perro para recibir recomendaciones. Sigue estos pasos:

1. Completa el formulario con los datos de tu perro: nombre, raza, ubicación, edad, peso, tamaño, sexo, etc.
2. Haz clic en el botón **Registrar Perro**.
3. Una vez registrado tu perro, serás redirigido a la página de recomendaciones.

## Recomendaciones de Perros
Después de registrar a tu perro, recibirás recomendaciones de otros perros que podrían ser compatibles. Aquí te explicamos cómo funciona:

1. En la página de **Recomendaciones**, verás el perfil del perro recomendado.
2. Puedes darle **Like** o **Dislike** al perro recomendado.
3. Si le das **Like**, el perro será añadido a tu lista de likes y recibirás una nueva recomendación.
4. Si le das **Dislike**, el perro será añadido a tu lista de dislikes y recibirás una nueva recomendación que excluya perros similares.

## Otras Funcionalidades
Además de las recomendaciones, la página de Furry Mate ofrece otras funcionalidades:

- **Visión:** Conoce la visión de Furry Mate.
- **Misión:** Conoce la misión de Furry Mate.
- **Conócenos:** Información sobre el equipo detrás de Furry Mate.
- **Contáctanos:** Formulario de contacto para enviar tus dudas o comentarios.
- **Chat:** Funcionalidad de chat para comunicarte con otros usuarios.
- **Buscar:** Herramienta de búsqueda para encontrar perros específicos.

## Consejos y Sugerencias
Para sacar el máximo provecho de Furry Mate, te recomendamos lo siguiente:

- Completa todos los campos al registrar a tu perro para obtener mejores recomendaciones.
- Revisa regularmente la página de recomendaciones para encontrar el mejor match para tu perro.
- Utiliza la funcionalidad de chat para interactuar con otros usuarios y compartir experiencias.

## Soporte Técnico
Si tienes algún problema o duda, no dudes en contactar con nuestro soporte técnico a través de la página de **Contáctanos**. Estaremos encantados de ayudarte.

## ¡Disfruta de Furry Mate!
Esperamos que disfrutes utilizando Furry Mate y encuentres el mejor compañero para tu perro.
