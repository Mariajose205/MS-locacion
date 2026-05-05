package com.fitlife.location.controller;

import com.fitlife.location.dto.LocationRequest;
import com.fitlife.location.dto.LocationResponse;
import com.fitlife.location.dto.LocationUpdateRequest;
import com.fitlife.location.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
@Validated
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    // CRUD Operations
    @PostMapping
    public ResponseEntity<LocationResponse> crearLocation(@Valid @RequestBody LocationRequest request) {
        logger.info("Solicitud POST para crear location: {}", request.getNombre());
        try {
            LocationResponse response = locationService.crearLocation(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error al crear location: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> obtenerLocationPorId(@PathVariable Long id) {
        logger.info("Solicitud GET para obtener location con ID: {}", id);
        try {
            LocationResponse response = locationService.obtenerLocationPorId(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error al obtener location con ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<LocationResponse>> obtenerTodasLasLocations() {
        logger.info("Solicitud GET para obtener todas las locations");
        try {
            List<LocationResponse> responses = locationService.obtenerTodasLasLocations();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error al obtener todas las locations: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/activas")
    public ResponseEntity<List<LocationResponse>> obtenerLocationsActivas() {
        logger.info("Solicitud GET para obtener locations activas");
        try {
            List<LocationResponse> responses = locationService.obtenerLocationsActivas();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error al obtener locations activas: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponse> actualizarLocation(
            @PathVariable Long id, 
            @Valid @RequestBody LocationUpdateRequest request) {
        logger.info("Solicitud PUT para actualizar location con ID: {}", id);
        try {
            LocationResponse response = locationService.actualizarLocation(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar location con ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLocation(@PathVariable Long id) {
        logger.info("Solicitud DELETE para eliminar location con ID: {}", id);
        try {
            locationService.eliminarLocation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            logger.error("Error al eliminar location con ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarLocation(@PathVariable Long id) {
        logger.info("Solicitud PATCH para desactivar location con ID: {}", id);
        try {
            locationService.desactivarLocation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            logger.error("Error al desactivar location con ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Métodos de negocio
    @GetMapping("/disponibles")
    public ResponseEntity<List<LocationResponse>> obtenerLocationsConDisponibilidad() {
        logger.info("Solicitud GET para obtener locations con disponibilidad");
        try {
            List<LocationResponse> responses = locationService.obtenerLocationsConDisponibilidad();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error al obtener locations con disponibilidad: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/disponibles/capacidad/{capacidad}")
    public ResponseEntity<List<LocationResponse>> obtenerLocationsConDisponibilidadYCapacidad(
            @PathVariable Integer capacidad) {
        logger.info("Solicitud GET para obtener locations con disponibilidad y capacidad >= {}", capacidad);
        try {
            List<LocationResponse> responses = locationService.obtenerLocationsConDisponibilidadYCapacidad(capacidad);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error al obtener locations con disponibilidad y capacidad: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/incrementar-capacidad")
    public ResponseEntity<Void> incrementarCapacidadActual(@PathVariable Long id) {
        logger.info("Solicitud PATCH para incrementar capacidad actual de location con ID: {}", id);
        try {
            locationService.incrementarCapacidadActual(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            logger.error("Error al incrementar capacidad actual de location con ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/decrementar-capacidad")
    public ResponseEntity<Void> decrementarCapacidadActual(@PathVariable Long id) {
        logger.info("Solicitud PATCH para decrementar capacidad actual de location con ID: {}", id);
        try {
            locationService.decrementarCapacidadActual(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            logger.error("Error al decrementar capacidad actual de location con ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Métodos de búsqueda
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<LocationResponse>> buscarLocationsPorCiudad(@PathVariable String ciudad) {
        logger.info("Solicitud GET para buscar locations en ciudad: {}", ciudad);
        try {
            List<LocationResponse> responses = locationService.buscarLocationsPorCiudad(ciudad);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error al buscar locations en ciudad {}: {}", ciudad, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pais/{pais}")
    public ResponseEntity<List<LocationResponse>> buscarLocationsPorPais(@PathVariable String pais) {
        logger.info("Solicitud GET para buscar locations en país: {}", pais);
        try {
            List<LocationResponse> responses = locationService.buscarLocationsPorPais(pais);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error al buscar locations en país {}: {}", pais, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<LocationResponse>> buscarLocationsPorNombre(@RequestParam String nombre) {
        logger.info("Solicitud GET para buscar locations por nombre: {}", nombre);
        try {
            List<LocationResponse> responses = locationService.buscarLocationsPorNombre(nombre);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error al buscar locations por nombre {}: {}", nombre, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ciudad/{ciudad}/activas-disponibles")
    public ResponseEntity<List<LocationResponse>> buscarLocationsActivasEnCiudad(@PathVariable String ciudad) {
        logger.info("Solicitud GET para buscar locations activas en ciudad: {}", ciudad);
        try {
            List<LocationResponse> responses = locationService.buscarLocationsActivasEnCiudad(ciudad);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error al buscar locations activas en ciudad {}: {}", ciudad, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/capacidad/rango")
    public ResponseEntity<List<LocationResponse>> buscarLocationsPorRangoCapacidad(
            @RequestParam Integer minima, 
            @RequestParam Integer maxima) {
        logger.info("Solicitud GET para buscar locations con capacidad entre {} y {}", minima, maxima);
        try {
            List<LocationResponse> responses = locationService.buscarLocationsPorRangoCapacidad(minima, maxima);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error al buscar locations por rango de capacidad: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/termino")
    public ResponseEntity<List<LocationResponse>> buscarLocationsPorTermino(@RequestParam String termino) {
        logger.info("Solicitud GET para buscar locations por término: {}", termino);
        try {
            List<LocationResponse> responses = locationService.buscarLocationsPorTermino(termino);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error al buscar locations por término {}: {}", termino, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Métodos de estadísticas
    @GetMapping("/estadisticas/activas/count")
    public ResponseEntity<Long> contarLocationsActivas() {
        logger.info("Solicitud GET para contar locations activas");
        try {
            long count = locationService.contarLocationsActivas();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error al contar locations activas: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/estadisticas/disponibles/count")
    public ResponseEntity<Long> contarLocationsConDisponibilidad() {
        logger.info("Solicitud GET para contar locations con disponibilidad");
        try {
            long count = locationService.contarLocationsConDisponibilidad();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error al contar locations con disponibilidad: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/estadisticas/capacidad-promedio")
    public ResponseEntity<Double> obtenerCapacidadPromedio() {
        logger.info("Solicitud GET para obtener capacidad promedio");
        try {
            Double capacidadPromedio = locationService.obtenerCapacidadPromedio();
            return ResponseEntity.ok(capacidadPromedio);
        } catch (Exception e) {
            logger.error("Error al obtener capacidad promedio: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Manejo global de excepciones
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        logger.error("RuntimeException en LocationController: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Exception en LocationController: {}", e.getMessage());
        return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
