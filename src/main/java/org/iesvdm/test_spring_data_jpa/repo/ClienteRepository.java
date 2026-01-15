package org.iesvdm.test_spring_data_jpa.repo;

import org.iesvdm.test_spring_data_jpa.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Test 3
    // Crea una clase con record
    // Le mete solo un dato Ling id
    record IdDto(Long id) {};

    // _ (barra baja) por que navegas de cliente a pedido (ya que esta es una lista de clientes)
    public List<IdDto> findDistinctByPedidos_IdIsNotNull();

    // Test 7
    record IdNomApellido1(Long id, String nombre, String apellido1) {};
    List<IdNomApellido1> findByApellido2IsNotNullOrderByApellido1AscApellido2AscNombre();

    @Query("select c.id, c.nombre, c.apellido1 from Cliente c where c.apellido2 is not null order by c.apellido1, c.apellido2, c.nombre")
    List<IdNomApellido1> obteneridNomApell1();



}
