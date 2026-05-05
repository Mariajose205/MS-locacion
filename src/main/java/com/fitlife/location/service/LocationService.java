package com.fitlife.location.service;

import com.fitlife.location.dto.LocationRequest;
import com.fitlife.location.dto.LocationResponse;
import com.fitlife.location.dto.LocationUpdateRequest;
import com.fitlife.location.entity.Location;
import com.fitlife.location.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    // CRUD Operations
    public LocationResponse crearLocation(LocationRequest request) {
        logger.info("Creando nueva ubicación: {}", request.getNombre());

        // Verificar si ya existe una ubicación con el mismo nombre
        Optional<Location> locationExistente = locationRepository.findByNombre(request.getNombre());
        if (locationExistente.isPresent()) {
            throw new RuntimeException("Ya existe una ubicación con el nombre: " + request.getNombre());
        }

        Location location = new Location();
        location.setNombre(request.getNombre());
        location.setDireccion(request.getDireccion());
        location.setCiudad(request.getCiudad());
        location.setPais(request.getPais());
        location.setCodigoPostal(request.getCodigoPostal());
        location.setLatitud(request.getLatitud());
        location.setLongitud(request.getLongitud());
        location.setTelefono(request.getTelefono());
        location.setEmail(request.getEmail());
        location.setCapacidadMaxima(request.getCapacidadMaxima());
        location.setCapacidadActual(0);
        location.setActivo(true);
        location.setDescripcion(request.getDescripcion());

        Location locationGuardada = locationRepository.save(location);
        logger.info("Ubicación creada exitosamente con ID: {}", locationGuardada.getId());

        return convertToResponse(locationGuardada);
    }

    public LocationResponse obtenerLocationPorId(Long id) {
        logger.info("Buscando ubicación con ID: {}", id);
        
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + id));

        return convertToResponse(location);
    }

    public List<LocationResponse> obtenerTodasLasLocations() {
        logger.info("Obteniendo todas las ubicaciones");
        
        List<Location> locations = locationRepository.findAll();
        return locations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<LocationResponse> obtenerLocationsActivas() {
        logger.info("Obteniendo ubicaciones activas");
        
        List<Location> locations = locationRepository.findByActivoTrue();
        return locations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public LocationResponse actualizarLocation(Long id, LocationUpdateRequest request) {
        logger.info("Actualizando ubicación con ID: {}", id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + id));

        // Actualizar solo los campos que no son nulos
        if (request.getNombre() != null) {
            location.setNombre(request.getNombre());
        }
        if (request.getDireccion() != null) {
            location.setDireccion(request.getDireccion());
        }
        if (request.getCiudad() != null) {
            location.setCiudad(request.getCiudad());
        }
        if (request.getPais() != null) {
            location.setPais(request.getPais());
        }
        if (request.getCodigoPostal() != null) {
            location.setCodigoPostal(request.getCodigoPostal());
        }
        if (request.getLatitud() != null) {
            location.setLatitud(request.getLatitud());
        }
        if (request.getLongitud() != null) {
            location.setLongitud(request.getLongitud());
        }
        if (request.getTelefono() != null) {
            location.setTelefono(request.getTelefono());
        }
        if (request.getEmail() != null) {
            location.setEmail(request.getEmail());
        }
        if (request.getCapacidadMaxima() != null) {
            if (request.getCapacidadMaxima() < location.getCapacidadActual()) {
                throw new RuntimeException("La capacidad máxima no puede ser menor a la capacidad actual");
            }
            location.setCapacidadMaxima(request.getCapacidadMaxima());
        }
        if (request.getDescripcion() != null) {
            location.setDescripcion(request.getDescripcion());
        }
        if (request.getActivo() != null) {
            location.setActivo(request.getActivo());
        }

        Location locationActualizada = locationRepository.save(location);
        logger.info("Ubicación actualizada exitosamente con ID: {}", locationActualizada.getId());

        return convertToResponse(locationActualizada);
    }

    public void eliminarLocation(Long id) {
        logger.info("Eliminando ubicación con ID: {}", id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + id));

        if (location.getCapacidadActual() > 0) {
            throw new RuntimeException("No se puede eliminar una ubicación con personas registradas");
        }

        locationRepository.delete(location);
        logger.info("Ubicación eliminada exitosamente con ID: {}", id);
    }

    public void desactivarLocation(Long id) {
        logger.info("Desactivando ubicación con ID: {}", id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + id));

        location.setActivo(false);
        locationRepository.save(location);
        logger.info("Ubicación desactivada exitosamente con ID: {}", id);
    }

    // Métodos de negocio
    public List<LocationResponse> obtenerLocationsConDisponibilidad() {
        logger.info("Obteniendo ubicaciones con disponibilidad");
        
        List<Location> locations = locationRepository.findLocationsWithAvailability();
        return locations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<LocationResponse> obtenerLocationsConDisponibilidadYCapacidad(Integer capacidadRequerida) {
        logger.info("Obteniendo ubicaciones con disponibilidad y capacidad >= {}", capacidadRequerida);
        
        List<Location> locations = locationRepository.findLocationsWithAvailabilityAndCapacity(capacidadRequerida);
        return locations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void incrementarCapacidadActual(Long id) {
        logger.info("Incrementando capacidad actual de ubicación con ID: {}", id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + id));

        if (!location.getActivo()) {
            throw new RuntimeException("No se puede incrementar capacidad de una ubicación inactiva");
        }

        location.incrementarCapacidadActual();
        locationRepository.save(location);
        logger.info("Capacidad actual incrementada para ubicación con ID: {}", id);
    }

    public void decrementarCapacidadActual(Long id) {
        logger.info("Decrementando capacidad actual de ubicación con ID: {}", id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + id));

        location.decrementarCapacidadActual();
        locationRepository.save(location);
        logger.info("Capacidad actual decrementada para ubicación con ID: {}", id);
    }

    // Métodos de búsqueda
    public List<LocationResponse> buscarLocationsPorCiudad(String ciudad) {
        logger.info("Buscando ubicaciones en ciudad: {}", ciudad);
        
        List<Location> locations = locationRepository.findByCiudad(ciudad);
        return locations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<LocationResponse> buscarLocationsPorPais(String pais) {
        logger.info("Buscando ubicaciones en país: {}", pais);
        
        List<Location> locations = locationRepository.findByPais(pais);
        return locations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<LocationResponse> buscarLocationsPorNombre(String nombre) {
        logger.info("Buscando ubicaciones con nombre que contenga: {}", nombre);
        
        List<Location> locations = locationRepository.findByNombreContainingIgnoreCase(nombre);
        return locations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<LocationResponse> buscarLocationsActivasEnCiudad(String ciudad) {
        logger.info("Buscando ubicaciones activas en ciudad: {}", ciudad);
        
        List<Location> locations = locationRepository.findActiveLocationsInCityWithAvailability(ciudad);
        return locations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<LocationResponse> buscarLocationsPorRangoCapacidad(Integer minima, Integer maxima) {
        logger.info("Buscando ubicaciones con capacidad entre {} y {}", minima, maxima);
        
        List<Location> locations = locationRepository.findByCapacidadMaximaBetween(minima, maxima);
        return locations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<LocationResponse> buscarLocationsPorTermino(String searchTerm) {
        logger.info("Buscando ubicaciones con término: {}", searchTerm);
        
        List<Location> locations = locationRepository.searchActiveLocations(searchTerm);
        return locations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Métodos de estadísticas
    public long contarLocationsActivas() {
        logger.info("Contando ubicaciones activas");
        return locationRepository.countByActivoTrue();
    }

    public long contarLocationsConDisponibilidad() {
        logger.info("Contando ubicaciones con disponibilidad");
        return locationRepository.countLocationsWithAvailability();
    }

    public Double obtenerCapacidadPromedio() {
        logger.info("Calculando capacidad promedio");
        return locationRepository.getAverageCapacity();
    }

    // Métodos auxiliares
    private LocationResponse convertToResponse(Location location) {
        LocationResponse response = new LocationResponse();
        response.setId(location.getId());
        response.setNombre(location.getNombre());
        response.setDireccion(location.getDireccion());
        response.setCiudad(location.getCiudad());
        response.setPais(location.getPais());
        response.setCodigoPostal(location.getCodigoPostal());
        response.setLatitud(location.getLatitud());
        response.setLongitud(location.getLongitud());
        response.setTelefono(location.getTelefono());
        response.setEmail(location.getEmail());
        response.setCapacidadMaxima(location.getCapacidadMaxima());
        response.setCapacidadActual(location.getCapacidadActual());
        response.setActivo(location.getActivo());
        response.setDescripcion(location.getDescripcion());
        response.setFechaCreacion(location.getFechaCreacion());
        response.setFechaActualizacion(location.getFechaActualizacion());
        response.setCapacidadDisponible(location.getCapacidadDisponible());
        response.setDisponible(location.hayDisponibilidad());
        return response;
    }
}
