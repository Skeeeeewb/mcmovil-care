# MCmovil Care — Evaluación Parcial 4 (DSY1105)

Aplicación móvil académica para un servicio técnico: consulta de equipos, registro de solicitudes, seguimiento de atenciones y decisiones sobre cotizaciones. La observación de la interfaz se realiza en tiempo real contra un microservicio Spring Boot propio; la API externa DummyJSON entrega datos de referencia que se muestran en el detalle del equipo.

## Integrantes

| Integrante | Correo | Rol principal en EV4 |
| --- | --- | --- |
| Matías Muñoz | matias.munoz.c05@gmail.com | App Android, integración Retrofit, firma y APK |
| Moisés Pontigo | moisespontigo25@gmail.com | Microservicio, base de datos, pruebas unitarias y cobertura |

## Tecnologías

- Kotlin + Jetpack Compose + Material 3
- Arquitectura MVVM: `model/`, `repository/`, `ui/`, `viewmodel/` y `MainActivity.kt` en la raíz del paquete
- Room para solicitudes y decisiones locales
- Retrofit 3.0.0 + OkHttp 4.12.0 + Gson para el microservicio y DummyJSON
- Coil 2.7.0 para la imagen de referencia externa
- Spring Boot 3.5.3 + Java 21 + Gradle 8.14.8
- MySQL 8.4.3 (Laragon), base de datos `mcmovil_care`
- JUnit + MockK + Mockito + JaCoCo 0.8.13

## Funcionalidades

### Aplicación

- Listado de equipos y atenciones obtenidos desde el microservicio, con respaldo local si el servicio no responde.
- Detalle de equipo con marca, modelo y estado; muestra además marca, precio e imagen de un producto de DummyJSON.
- Formulario de solicitud de atención con la validación original de la app (descripción de al menos 10 caracteres).
- Solicitudes sincronizadas entre el microservicio y Room.
- Detalle de atención con diagnóstico plegable, cotización, aceptación/rechazo y evidencia fotográfica desde galería.
- Decisiones sincronizadas entre el microservicio y Room.
- Navegación Compose y pantallas stateless: la UI observa `StateFlow` y envía eventos al ViewModel.

### Microservicio

- CRUD de equipos, atenciones y solicitudes.
- Consulta por equipo para atenciones y solicitudes.
- Lectura y actualización de la decisión de cotización por atención.
- Validaciones y respuestas `200`, `201`, `204`, `400` y `404`.
- Datos iniciales de equipos y atenciones cargados desde `data.sql`.

## Endpoints utilizados

### Microservicio propio

Base: `http://10.0.2.2:8080/api` desde el emulador Android; `http://localhost:8080/api` desde el equipo.

| Método | Ruta | Uso en la app |
| --- | --- | --- |
| `GET` | `/equipos` | Listado de equipos |
| `GET` | `/equipos/{id}` | Detalle de equipo |
| `POST` | `/equipos` | Alta de equipos |
| `PUT` | `/equipos/{id}` | Actualización de equipos |
| `DELETE` | `/equipos/{id}` | Baja de equipos |
| `GET` | `/atenciones` | Listado de atenciones |
| `GET` | `/atenciones/{id}` | Detalle de atención |
| `GET` | `/atenciones/equipo/{equipoId}` | Atenciones filtradas por equipo |
| `POST` | `/atenciones` | Alta de atenciones |
| `PUT` | `/atenciones/{id}` | Actualización de atenciones |
| `DELETE` | `/atenciones/{id}` | Baja de atenciones |
| `GET` | `/solicitudes` | Sincronización de solicitudes con Room |
| `GET` | `/solicitudes/{id}` | Detalle de solicitud |
| `GET` | `/solicitudes/equipo/{equipoId}` | Solicitudes filtradas por equipo |
| `POST` | `/solicitudes` | Registro de solicitudes desde el formulario |
| `PUT` | `/solicitudes/{id}` | Actualización de solicitudes |
| `DELETE` | `/solicitudes/{id}` | Baja de solicitudes |
| `GET` | `/atenciones/{id}/decision` | Sincronización de la decisión |
| `PUT` | `/atenciones/{id}/decision` | Aceptar/rechazar cotización |

Cuerpos JSON:

```json
{ "id": 1, "nombre": "Notebook personal", "marca": "Lenovo", "modelo": "IdeaPad 3", "estado": "En diagnóstico" }
{ "id": 1001, "equipoId": 1, "estado": "En diagnóstico", "diagnostico": "Posible falla de almacenamiento", "cotizacion": 45000 }
{ "id": 5, "equipoId": 1, "descripcion": "Revisión del equipo completo", "estado": "Pendiente" }
{ "decision": "PENDIENTE|ACEPTADA|RECHAZADA" }
```

### API externa

| Método | Ruta | Dato mostrado |
| --- | --- | --- |
| `GET` | `https://dummyjson.com/products/{id}` | Marca, precio e imagen de referencia en `DetalleEquipoScreen` |

Se usa el identificador del equipo como `id` del producto. No requiere API key y no interfiere con Room ni con el microservicio propio.

## Pasos para ejecutar

### 1. Base de datos

Con MySQL de Laragon en `127.0.0.1:3306`, usuario `root` sin contraseña:

```powershell
mysql -h localhost -P 3306 -u root -e "CREATE DATABASE IF NOT EXISTS mcmovil_care CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

### 2. Microservicio

```powershell
cd backend\mcmovil-api
.\gradlew.bat bootRun
```

Verificación rápida:

```powershell
curl http://localhost:8080/api/equipos
```

### 3. Aplicación Android

1. Clonar `https://github.com/Skeeeeewb/mcmovil-care.git`.
2. Abrir la carpeta `MCmovilCare` con Android Studio y sincronizar Gradle.
3. Ejecutar la configuración `app` en un emulador con API 26 o superior.
4. El emulador apunta al microservicio mediante `http://10.0.2.2:8080/api/`. Si se usa dispositivo físico, reemplazar `10.0.2.2` por la IP de la máquina que ejecuta el backend.

### 4. Pruebas y cobertura

```powershell
cd backend\mcmovil-api
.\gradlew.bat test jacocoTestReport
```

```powershell
cd MCmovilCare
.\gradlew.bat :app:testDebugUnitTest :app:jacocoTestReport
```

Resultados verificados: 14 pruebas del backend y 40 pruebas de la app. La cobertura de la lógica Android (ViewModels, repositorios y mappers) es de **82,5%** de instrucciones, medida por JaCoCo. El informe HTML queda en `MCmovilCare/app/build/reports/jacoco/jacocoTestReport/html/index.html`.

## APK firmado y keystore

| Archivo | Descripción |
| --- | --- |
| `MCmovilCare/app/mcmovil-care.jks` | Keystore PKCS12 usado para firmar |
| `MCmovilCare/app/release/mcmovil-care-release.apk` | APK firmado incluido en el repositorio |
| `MCmovilCare/app/release/SHA256SUMS.txt` | Hash SHA-256 del APK |
| `MCmovilCare/app/release/verificacion-firma.txt` | Salida de `apksigner verify --verbose --print-certs` |

Datos de firma acordados como valores de ejemplo:

- Alias: `mcmovilcare`
- Contraseña del store y de la clave: `mcmovil2026`
- Validez: 9.125 días (25 años)
- Algoritmo: RSA 2048, SHA256withRSA
- Certificado: `CN=MCmovil Care, OU=DSY1105, O=Duoc UC, L=Santiago, ST=Region Metropolitana, C=CL`
- Huella SHA-256 del certificado: `7f7a779c7df7d659e07222bf30ee5c9e4fd05038f38ba54634a329fcec6d5ac0`
- Firma verificada con el esquema APK Signature Scheme v2

Reconstrucción:

```powershell
cd MCmovilCare
.\gradlew.bat :app:assembleRelease
```

## Control de versiones y planificación

Ramas de trabajo conservadas: `matias/equipos`, `moises/seguimiento-atenciones`, `integracion/ev2`, `integracion/ev3` e `integracion/ev4`. Los commits de esta entrega están en `integracion/ev4` y alternan la autoría entre ambos integrantes, con mensajes en español y compilación verificada antes de cada commit.

Evidencia de Trello: pendiente de enlace hasta completar el inicio de sesión en el navegador.

## Consideraciones

Proyecto con fines exclusivamente académicos. Equipos, diagnósticos, solicitudes, cotizaciones y producto externo son datos ficticios o de demostración.
