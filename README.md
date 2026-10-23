# MCmovil Care

Aplicación Android desarrollada para la asignatura **Desarrollo de Aplicaciones Móviles (DSY1105)** de Duoc UC, como parte de la Evaluación Parcial 2.

## Integrantes

- Matías Muñoz
- Moisés Pontigo

## Descripción del proyecto

MCmovil Care es una aplicación móvil académica orientada a clientes de un servicio técnico de dispositivos electrónicos.

Permite consultar equipos registrados, crear solicitudes de atención, revisar diagnósticos y gestionar decisiones sobre cotizaciones.

El proyecto utiliza información ficticia y no se conecta al sistema productivo Gestioo.

## Tecnologías utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- Arquitectura MVVM
- ViewModel y StateFlow
- Room Database 2.8.4 (SQLite)
- Kotlin Symbol Processing (KSP)
- Navigation Compose
- Activity Result API
- Git y GitHub
- Trello para planificación colaborativa

## Funcionalidades implementadas

### Gestión de equipos y solicitudes — Matías Muñoz

- Visualización de un listado de equipos ficticios.
- Consulta de información individual de cada equipo.
- Navegación entre equipos, detalles y solicitudes.
- Formulario para registrar solicitudes de atención.
- Validaciones y mensajes visuales ante datos incorrectos.
- Gestión del estado mediante ViewModel y StateFlow.
- Almacenamiento local de solicitudes con Room.
- Consulta de solicitudes registradas.
- Captura de fotografías utilizando la cámara del dispositivo.
- Visualización y eliminación de la vista previa de la fotografía capturada.

### Seguimiento de atenciones — Moisés Pontigo

- Listado de atenciones ficticias.
- Visualización del estado, diagnóstico y cotización de cada atención.
- Aceptación y rechazo de cotizaciones.
- Gestión de decisiones mediante ViewModel y StateFlow.
- Persistencia de decisiones con Room mediante Repository y DAO.
- Recuperación de decisiones al cerrar y volver a abrir la aplicación.
- Selección de fotografías desde la galería de Android.
- Visualización de imágenes seleccionadas como evidencias temporales.
- Animación para mostrar y ocultar el diagnóstico utilizando AnimatedVisibility.

## Persistencia local

La aplicación utiliza **Room Database** sobre SQLite.

La base de datos se denomina `mcmovil_care.db` y centraliza el almacenamiento local de solicitudes y decisiones de cotización.

Los componentes principales son:

- `AppDatabase.kt`: configura y proporciona acceso a Room.
- `SolicitudEntity.kt` y `SolicitudDao.kt`: representan y administran las solicitudes.
- `DecisionCotizacionEntity.kt` y `DecisionCotizacionDao.kt`: representan y administran las decisiones de cotización.
- `DecisionCotizacionRepository.kt`: conecta la lógica del módulo de atenciones con el DAO.
- `AtencionesViewModel.kt`: gestiona la actualización y recuperación de las decisiones.

La persistencia se ha probado cerrando y volviendo a abrir la aplicación.

Las fotografías capturadas y seleccionadas se utilizan como vistas previas temporales; no se almacenan permanentemente como parte de las solicitudes o atenciones.

## Recursos nativos y animaciones

Se integraron dos recursos nativos del dispositivo:

1. **Cámara:** captura de fotografías y visualización del resultado.
2. **Galería:** selección y visualización de imágenes existentes.

También se incorporó una animación mediante `AnimatedVisibility`, con transiciones para mostrar y ocultar el diagnóstico de una atención.

## Requisitos para ejecutar

- Android Studio.
- SDK de Android compatible con el proyecto.
- Emulador o dispositivo con Android 8.0 (API 26) o superior.
- Conexión a Internet para descargar dependencias durante la configuración inicial.

## Instrucciones de ejecución

1. Clonar el repositorio:

   `git clone https://github.com/Skeeeeewb/mcmovil-care.git`

2. Abrir Android Studio.
3. Seleccionar **Open** y abrir la carpeta `MCmovilCare`.
4. Esperar a que finalice la sincronización de Gradle.
5. Seleccionar un emulador compatible.
6. Elegir la configuración `app` y presionar **Run**.
7. Desde el inicio de la aplicación, acceder a **Mis equipos** o **Mis atenciones**.

Para comprobar la persistencia, se puede registrar una solicitud o aceptar/rechazar una cotización, cerrar la aplicación y volver a abrirla.

## Organización del proyecto

El código principal se encuentra en:

`MCmovilCare/app/src/main/java/com/example/mcmovilcare/`

La implementación separa responsabilidades mediante clases de modelos, pantallas Compose, ViewModels, Repository, entidades y DAO de Room.

La navegación se administra mediante `NavegacionApp.kt`.

## Trabajo colaborativo

El proyecto se desarrolló utilizando GitHub y Trello para distribuir tareas, registrar avances e integrar los aportes de ambos estudiantes.

Ramas utilizadas:

- `matias/equipos`
- `moises/seguimiento-atenciones`
- `integracion/ev2`

Repositorio:

https://github.com/Skeeeeewb/mcmovil-care

La planificación del desarrollo y sus responsables se encuentran registrados en el tablero de Trello del equipo.

## Consideraciones

Este proyecto tiene fines exclusivamente académicos.

Los equipos, diagnósticos y cotizaciones de demostración son ficticios. Las solicitudes y decisiones almacenadas localmente corresponden a datos de prueba.

No se utiliza información real del sistema productivo Gestioo.