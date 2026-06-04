# Proyecto TT1 - Simulador de Pandemias

Este proyecto es una aplicación web basada en Spring Boot que simula la propagación de una enfermedad, para la asignatura de TT1. 

La simulación incluye persistencia de datos, gestión de usuarios y una interfaz interactiva dinámica (Grid) para visualizar el avance de las generaciones del virus sobre diferentes entidades poblacionales (Sanos, Infectados, Recuperados, Vacunados y Fallecidos).
Incluye también logout y filtro de autenticación para que los usuarios no identificados no puedan realizar simulaciones. 

---

## Tecnologías Utilizadas
* **Backend:** Java 21, Spring Boot 3.2.4, Spring Data JPA.
* **Frontend:** HTML5, CSS3, JavaScript (Thymeleaf).
* **Base de Datos:** Oracle Database 19c.
* **Otros:** Maven, JavaMailSender.

---

## Setup necesario para poder probar la aplicación

Para poder ejecutar, probar y evaluar esta aplicación en un entorno local, es estrictamente necesario contar con el siguiente software y configurar **dos Máquinas Virtuales (MV)** en VirtualBox:

### 1. Máquina Virtual del Servicio (API)
Esta máquina aloja el servicio base requerido para la asignatura.
* **Disco duro:** Es necesario descargar e importar el archivo `Servicio.vdi` correspondiente a la Práctica 5.
* **Memoria RAM:** Asignar **4 GB** (4096 MB).
* **Procesador:** Asignar **1 o 2 núcleos** de CPU.
* **Red:** Configurar el adaptador de red como *Adaptador Sólo-Anfitrión (Host-Only)* para garantizar la conectividad desde la máquina host.

### 2. Máquina Virtual de Base de Datos (Oracle 19c)
Esta máquina aloja el servidor de base de datos Oracle donde la aplicación persistirá los usuarios, solicitudes, historial de correos y estadísticas.
* Es necesario tener una MV con **Oracle 19c Database** instalada y configurada.
* Asegurarse de que el *Listener* de Oracle está activo antes de lanzar la aplicación.
* Comprobar que la base de datos es accesible desde el host (generalmente en la IP `192.168.56.101` por el puerto `1521`).

---

## Configuración del Proyecto

Antes de compilar y ejecutar el proyecto, es necesario ajustar las credenciales en el archivo de propiedades local.

1. Navega hasta el archivo `src/main/resources/application.properties`.
2. **Conexión a Base de Datos:** Verifica que la IP de la conexión JDBC (`spring.datasource.url`) coincide con la IP de tu máquina virtual de Oracle. (realmente no es necesario crear el DataSource, a nosotros nos iba sin eso)
3. También hay que hacer un maven:clean y maven:compile para que se regeneren las clases del proyecto del trabajo individual.

## Despliegue y Pruebas

Una vez que ambas máquinas virtuales estén arrancadas y la red configurada:

Inicia el proyecto desde el IDE (recomendamos usar IntelliJ)
No es necesario que crees el DataSource si no quieres, funciona con tener las 2 MV arrancadas. 
Accede a localhost:8081 para probar. Entraras al login. 


## Flujo de Prueba Recomendado
Login: Introduce un nombre de usuario y un correo electrónico. Si quieres mandar mensajes a otros usuarios, deberá existir el usuario previamente (si no quieres crear nuevos, puedes mandarte a ti mismo) 

Configuración de la Pandemia: Configura los parámetros de la simulación desde el formulario dinámico (tamaño del grid, infectados iniciales, porcentaje de movilidad de la población).

Notificación: Al procesar la solicitud, el sistema te enviará un token de acceso con el que puedes acceder a la simulación mediante la URL: /grid?tok={token} 

Visualización (Grid): Utiliza el reproductor automático de la vista del Grid para observar el comportamiento dinámico y la propagación de la enfermedad a lo largo de las distintas generaciones.
También puedes utilizar el slider de JS. Para guiarte, hemos añadido una leyenda encima del grid, indicando a que corresponde cada color con su tipo de entidad. 

## Autores
- Desarrollado por: Hugo Moncada, Ahmed Mando, Javier Rincón y Mario Pascual.


