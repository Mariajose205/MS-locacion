# Fitlife MS-location

Microservicio de gestión de ubicaciones para FitLife. Este servicio maneja la información de gimnasios, sedes y ubicaciones físicas.

## Características

- **Gestión de Ubicaciones**: CRUD completo para ubicaciones de gimnasios
- **Información de Sedes**: Datos de dirección, coordenadas, horarios
- **Estados de Ubicación**: Activa, Inactiva, En mantenimiento
- **REST API**: Endpoints completos para gestión de ubicaciones
- **Unit Testing**: Pruebas unitarias con JUnit y Mockito

## Tecnologías

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL 8.0
- Maven
- Docker

## Endpoints

### Gestión de Ubicaciones
- `POST /locations` - Crear nueva ubicación
- `GET /locations` - Obtener todas las ubicaciones
- `GET /locations/{id}` - Obtener ubicación por ID
- `PUT /locations/{id}` - Actualizar ubicación
- `DELETE /locations/{id}` - Eliminar ubicación
- `GET /locations/activas` - Obtener ubicaciones activas
- `POST /locations/{id}/desactivar` - Desactivar ubicación

## Configuración

### Variables de Entorno

```env
SPRING_DATASOURCE_URL=jdbc:mysql://mysql-locations:3306/fitlife_locations_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

## Desarrollo

### Compilar el proyecto
```bash
mvn clean package
```

### Ejecutar pruebas
```bash
mvn test
```

### Ejecutar localmente
```bash
mvn spring-boot:run
```

## Docker

### Construir imagen
```bash
docker build -t fitlife-location:latest .
```

### Ejecutar contenedor
```bash
docker run -p 8082:8082 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/fitlife_locations_db \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=root \
  fitlife-location:latest
```

## GitHub Actions

Este repositorio utiliza GitHub Actions para CI/CD:

- **Build**: Compila el proyecto con Maven
- **Test**: Ejecuta pruebas unitarias
- **Docker Build**: Construye la imagen Docker
- **Docker Push**: Sube la imagen a Docker Hub

## Contribución

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## Licencia

Este proyecto es parte de FitLife Gym Management System.
