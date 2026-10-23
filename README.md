\# MCmovil Care



Aplicación Android desarrollada para la asignatura \*\*Desarrollo de Aplicaciones Móviles (DSY1105)\*\*, como parte de la Evaluación Parcial 2.



\## Integrantes



\- Matías Muñoz

\- Moisés Pontigo



\## Descripción



MCmovil Care es una aplicación académica orientada a clientes de un servicio técnico. Permite consultar equipos, registrar solicitudes y realizar seguimiento de atenciones y cotizaciones.



El proyecto utiliza información ficticia y no se conecta al sistema productivo Gestioo.



\## Tecnologías



\- Kotlin

\- Jetpack Compose

\- Material Design 3

\- Arquitectura MVVM

\- ViewModel y StateFlow

\- Navegación con NavController y NavHost

\- Git y GitHub



La persistencia con Room se encuentra en proceso de integración.



\## Funcionalidades



\### Equipos y solicitudes — Matías



\- Listado de equipos ficticios.

\- Consulta del detalle de un equipo.

\- Navegación entre equipos, detalle y solicitudes.

\- Formulario de solicitud de atención.

\- Validaciones de descripción.

\- Gestión del formulario mediante ViewModel y StateFlow.



\### Seguimiento de atenciones — Moisés



\- Listado de atenciones ficticias.

\- Visualización del estado y diagnóstico.

\- Consulta de cotizaciones.

\- Aceptación y rechazo de cotizaciones mediante ViewModel y StateFlow.

\- Selección de imágenes desde la galería de Android.

\- Visualización de evidencias fotográficas.

\- Animación para mostrar y ocultar el diagnóstico.



\## Funcionalidades pendientes de integración



\- Persistencia local con Room para solicitudes y decisiones.

\- Captura de fotografías mediante cámara.

\- Pruebas finales de navegación y funcionamiento integrado.

\- Confirmación del alcance de la API REST de prueba.



\## Requisitos para ejecutar



\- Android Studio.

\- SDK de Android correspondiente al proyecto.

\- Emulador Android compatible con API 26 o superior.

\- Conexión a Internet para descargar dependencias cuando sea necesario.



\## Instrucciones de ejecución



1\. Clonar el repositorio:



&#x20;  `git clone https://github.com/Skeeeeewb/mcmovil-care.git`



2\. Abrir Android Studio.



3\. Seleccionar \*\*Open\*\* y abrir la carpeta `MCmovilCare`.



4\. Permitir la sincronización de Gradle.



5\. Seleccionar un emulador Android.



6\. Ejecutar la configuración `app` utilizando \*\*Run\*\*.



\## Organización del proyecto



El código de la aplicación se encuentra en:



`MCmovilCare/app/src/main/java/com/example/mcmovilcare/`



Los modelos representan los datos de equipos, solicitudes y atenciones. Las pantallas se implementan con Jetpack Compose y los ViewModel gestionan el estado y la lógica correspondiente.



\## Control de versiones



El desarrollo se realiza mediante ramas de Git, conservando los aportes individuales de ambos integrantes.



\- `matias/equipos`

\- `moises/seguimiento-atenciones`

\- `integracion/ev2`



Las funcionalidades se integran y prueban antes de publicarse en la rama principal.



\## Consideraciones



Este proyecto tiene fines exclusivamente académicos. Los equipos, diagnósticos, solicitudes y cotizaciones presentados son ficticios.

