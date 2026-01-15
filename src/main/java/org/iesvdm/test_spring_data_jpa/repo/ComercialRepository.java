package org.iesvdm.test_spring_data_jpa.repo;

import org.iesvdm.test_spring_data_jpa.domain.Comercial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ComercialRepository extends JpaRepository<Comercial, Long> {

    // Test 5
    record NomApellDto(String nombre, String apellido1, String apellido2) {};
    public List<NomApellDto> findByComisionBetween(BigDecimal cantidad, BigDecimal cantidad2);

    // Test 6

    // Consstruyes una query personalizada con @Query
    // Llamas a Comercial por la entidad, mno por la base de datos
    @Query("select max(c.comision) from Comercial c")
    BigDecimal obtenerMayorComision();

    // En SQL entero
    @Query(value = "select max(c.comision) from comercial c", nativeQuery = true)
    BigDecimal obtenerMayorComicionNativo();


}
