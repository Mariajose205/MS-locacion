package com.fitlife.location.repository;

import com.fitlife.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // Consultas básicas por nombre
    Optional<Location> findByNombre(String nombre);
    List<Location> findByNombreContainingIgnoreCase(String nombre);
    
    // Consultas por ciudad y país
    List<Location> findByCiudad(String ciudad);
    List<Location> findByPais(String pais);
    List<Location> findByCiudadAndPais(String ciudad, String pais);
    
    // Consultas por estado activo
    List<Location> findByActivo(Boolean activo);
    List<Location> findByActivoTrue();
    
    // Consultas por capacidad
    List<Location> findByCapacidadMaximaGreaterThan(Integer capacidadMinima);
    List<Location> findByCapacidadMaximaBetween(Integer capacidadMinima, Integer capacidadMaxima);
    
    // Consultas por disponibilidad
    @Query("SELECT l FROM Location l WHERE l.activo = true AND l.capacidadActual < l.capacidadMaxima")
    List<Location> findLocationsWithAvailability();
    
    @Query("SELECT l FROM Location l WHERE l.activo = true AND l.capacidadActual < l.capacidadMaxima AND l.capacidadMaxima >= :capacidadRequerida")
    List<Location> findLocationsWithAvailabilityAndCapacity(@Param("capacidadRequerida") Integer capacidadRequerida);
    
    // Consultas por coordenadas (búsqueda por proximidad)
    @Query("SELECT l FROM Location l WHERE l.latitud BETWEEN :latMin AND :latMax AND l.longitud BETWEEN :lonMin AND :lonMax AND l.activo = true")
    List<Location> findLocationsByCoordinatesRange(@Param("latMin") Double latMin, @Param("latMax") Double latMax, 
                                                  @Param("lonMin") Double lonMin, @Param("lonMax") Double lonMax);
    
    // Consultas por código postal
    List<Location> findByCodigoPostal(String codigoPostal);
    
    // Consultas combinadas
    @Query("SELECT l FROM Location l WHERE l.activo = true AND l.ciudad = :ciudad AND l.capacidadActual < l.capacidadMaxima")
    List<Location> findActiveLocationsInCityWithAvailability(@Param("ciudad") String ciudad);
    
    @Query("SELECT l FROM Location l WHERE l.activo = true AND l.pais = :pais AND l.capacidadMaxima >= :capacidadRequerida")
    List<Location> findActiveLocationsInCountryWithCapacity(@Param("pais") String pais, @Param("capacidadRequerida") Integer capacidadRequerida);
    
    // Consultas de conteo
    long countByActivo(Boolean activo);
    long countByActivoTrue();
    
    @Query("SELECT COUNT(l) FROM Location l WHERE l.activo = true AND l.capacidadActual < l.capacidadMaxima")
    long countLocationsWithAvailability();
    
    // Consultas para búsqueda por texto
    @Query("SELECT l FROM Location l WHERE l.activo = true AND " +
           "(LOWER(l.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.direccion) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.ciudad) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.pais) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Location> searchActiveLocations(@Param("searchTerm") String searchTerm);
    
    // Consultas para estadísticas
    @Query("SELECT l.ciudad, COUNT(l) FROM Location l WHERE l.activo = true GROUP BY l.ciudad")
    List<Object[]> countLocationsByCity();
    
    @Query("SELECT l.pais, COUNT(l) FROM Location l WHERE l.activo = true GROUP BY l.pais")
    List<Object[]> countLocationsByCountry();
    
    @Query("SELECT AVG(l.capacidadMaxima) FROM Location l WHERE l.activo = true")
    Double getAverageCapacity();
    
    @Query("SELECT l FROM Location l WHERE l.activo = true ORDER BY l.capacidadMaxima DESC")
    List<Location> findActiveLocationsOrderByCapacityDesc();
    
    @Query("SELECT l FROM Location l WHERE l.activo = true ORDER BY l.capacidadMaxima ASC")
    List<Location> findActiveLocationsOrderByCapacityAsc();
}
