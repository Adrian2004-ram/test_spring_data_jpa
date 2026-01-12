package org.iesvdm.test_spring_data_jpa.repo;


import org.iesvdm.test_spring_data_jpa.domain.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

  List<Departamento> findTop3ByOrderByPresupuestoDesc();

  List<Departamento> findTop3ByOrderByPresupuestoAsc();

  List<Departamento> findTop2ByOrderByGastoDesc();

  List<Departamento> findTop2ByOrderByGastoAsc();

  List<Departamento> findByPresupuestoGreaterThanEqual(BigDecimal min);

  List<Departamento> findByGastoLessThan(BigDecimal max);

  List<Departamento> findByPresupuestoBetween(BigDecimal min, BigDecimal max);

  @Query("select d from Departamento d where d.presupuesto < :min or d.presupuesto > :max")
  List<Departamento> findByPresupuestoNotBetween(BigDecimal min, BigDecimal max);

  @Query("select d from Departamento d where d.gasto > d.presupuesto")
  List<Departamento> findWhereGastoGreaterThanPresupuesto();

  @Query("select d from Departamento d where d.gasto < d.presupuesto")
  List<Departamento> findWhereGastoLessThanPresupuesto();

  @Query("select distinct d from Departamento d join d.empleados e")
  List<Departamento> findConEmpleados();

  @Query("select distinct d from Departamento d left join d.empleados e where e.id is null")
  List<Departamento> findSinEmpleados();
}
