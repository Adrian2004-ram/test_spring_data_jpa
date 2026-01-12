package org.iesvdm.test_spring_data_jpa.repo;

import org.iesvdm.test_spring_data_jpa.domain.Empleado;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

  @Query("select e.apellido1 from Empleado e")
  List<String> findAllApellido1();

  @Query("select distinct e.apellido1 from Empleado e")
  List<String> findDistinctApellido1();

  @Query("select e.departamento.id from Empleado e")
  List<Long> findAllDepartamentoIds();

  @Query("select distinct e.departamento.id from Empleado e")
  List<Long> findDistinctDepartamentoIds();

  List<Empleado> findByApellido2IsNull();

  List<Empleado> findByApellido2(String apellido2);

  List<Empleado> findByApellido2In(Collection<String> apellidos2);

  List<Empleado> findByDepartamento_Id(Long departamentoId);

  List<Empleado> findByDepartamento_IdIn(Collection<Long> departamentoIds);

  List<Empleado> findByDepartamentoIsNull();

  @EntityGraph(attributePaths = "departamento")
  @Query("select e from Empleado e")
  List<Empleado> findAllWithDepartamento();
}
