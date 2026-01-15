package org.iesvdm.test_spring_data_jpa.repo;

import org.iesvdm.test_spring_data_jpa.domain.Pedido;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
// - Define que tu interfaz hereda de JpaRepository, una librería de Spring Data JPA.
// - Spring genera automáticamente métodos CRUD (findAll, save, delete…) sin que tú los programes.
// - Pedido es la entidad que gestionará el repositorio.
// - Long es el tipo de la clave primaria de esa entidad.
// - Al extender JpaRepository, tu repositorio queda listo para usarse con inyección de dependencias.

    // Method Query
    // La consulta se resuerve sobre la query derivada del nombre del método
    public List<Pedido> findAllByOrderByFechaDesc();

    // Test 2
    public List<Pedido> findTop2ByOrderByCantidadDesc();

    List<Pedido> findByFechaBetweenAndCantidadGreaterThan(LocalDateTime inicio, LocalDateTime fin, double cantidad);



}
