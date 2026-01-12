package org.iesvdm.test_spring_data_jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.iesvdm.test_spring_data_jpa.domain.Departamento;
import org.iesvdm.test_spring_data_jpa.domain.Empleado;
import org.iesvdm.test_spring_data_jpa.repo.DepartamentoRepository;
import org.iesvdm.test_spring_data_jpa.repo.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class EmpleadoDepartamentoJpaTests {


  @Autowired EntityManager em;
  @Autowired
  EmpleadoRepository empleadoRepo;
  @Autowired
  DepartamentoRepository departamentoRepo;

  // ----------------------
  // 1) Inserción básica
  // ----------------------

  @Test
  void crea_un_empleado_asociado_a_un_departamento_existente() {
    Departamento d = new Departamento();
    d.setNombre("Informática");
    d.setPresupuesto(new BigDecimal("100000.00"));
    d.setGasto(new BigDecimal("0.00"));
    d = departamentoRepo.saveAndFlush(d);

    Empleado e = new Empleado();
    e.setNombre("José");
    e.setApellido1("Martín");
    e.setApellido2("Tejero");
    e.setNif("74862213X");
    e.setDepartamento(d);
    e = empleadoRepo.saveAndFlush(e);

    Empleado recargado = empleadoRepo.findById(e.getId()).orElseThrow();

    assertThat(recargado.getId()).isNotNull();
    assertThat(recargado.getDepartamentoId()).isEqualTo(d.getId());
  }

  // ----------------------
  // 2) Consultas 1 tabla
  // ----------------------

  @Test
  void lista_primer_apellido_de_todos_los_empleados() {
    List<String> apellidos = empleadoRepo.findAllApellido1();
    assertThat(apellidos).hasSize(13);
  }

  @Test
  void lista_primer_apellido_distinct() {
    List<String> apellidos = empleadoRepo.findDistinctApellido1();
    assertThat(apellidos).hasSize(11);
  }

  @Test
  void lista_nombre_y_apellidos() {
    List<Object[]> rows = em.createQuery(
            "select e.nombre, e.apellido1, e.apellido2 from Empleado e",
            Object[].class
        )
        .getResultList();

    assertThat(rows).hasSize(13);
  }

  @Test
  void lista_departamento_id_de_empleados_y_distinct() {
    assertThat(empleadoRepo.findAllDepartamentoIds()).hasSize(13);
    assertThat(empleadoRepo.findDistinctDepartamentoIds()).hasSize(6); // 5 deptos + null
  }

  @Test
  void lista_nombre_completo_en_mayusculas() {
    List<String> nombres = em.createQuery(
            "select upper(concat(e.nombre, ' ', e.apellido1, ' ', coalesce(e.apellido2, ''))) from Empleado e",
            String.class
        )
        .getResultList();

    assertThat(nombres).hasSize(13);
    assertThat(nombres.get(0)).isNotBlank();
  }

  @Test
  void lista_nombre_completo_en_minusculas() {
    List<String> nombres = em.createQuery(
            "select lower(concat(e.nombre, ' ', e.apellido1, ' ', coalesce(e.apellido2, ''))) from Empleado e",
            String.class
        )
        .getResultList();

    assertThat(nombres).hasSize(13);
  }

  @Test
  void separa_dni_y_letra_del_nif() {
    List<Object[]> rows = em.createQuery(
            "select e.nombre, substring(e.nif, 1, 8), substring(e.nif, 9, 1) from Empleado e",
            Object[].class
        )
        .getResultList();

    assertThat(rows).hasSize(13);
    // ejemplo rápido: el NIF tiene 9 caracteres
    assertThat(((String) rows.get(0)[1]).length()).isEqualTo(8);
    assertThat(((String) rows.get(0)[2]).length()).isEqualTo(1);
  }

  @Test
  void presupuesto_disponible_por_departamento() {
    List<Object[]> rows = em.createQuery(
            "select d.nombre, (d.presupuesto - d.gasto) from Departamento d",
            Object[].class
        )
        .getResultList();

    assertThat(rows).hasSize(7);
  }

  @Test
  void presupuesto_disponible_ordenado_asc() {
    List<Object[]> rows = em.createQuery(
            "select d.nombre, (d.presupuesto - d.gasto) from Departamento d order by (d.presupuesto - d.gasto) asc",
            Object[].class
        )
        .getResultList();

    assertThat(rows).hasSize(7);
    assertThat(rows.get(0)[0]).isEqualTo("I+D");
  }

  @Test
  void departamentos_ordenados_por_nombre() {
    List<String> asc = em.createQuery("select d.nombre from Departamento d order by d.nombre asc", String.class)
        .getResultList();
    List<String> desc = em.createQuery("select d.nombre from Departamento d order by d.nombre desc", String.class)
        .getResultList();

    assertThat(asc).hasSize(7);
    assertThat(desc).hasSize(7);
    assertThat(asc.get(0)).isEqualTo("Contabilidad");
    assertThat(desc.get(0)).isEqualTo("Sistemas");
  }

  @Test
  void empleados_ordenados_por_apellidos_y_nombre() {
    List<Object[]> rows = em.createQuery(
            "select e.apellido1, e.apellido2, e.nombre from Empleado e " +
                "order by e.apellido1 asc, " +
                "case when e.apellido2 is null then 0 else 1 end asc, e.apellido2 asc, e.nombre asc",
            Object[].class
        )
        .getResultList();

    assertThat(rows).hasSize(13);
  }

  @Test
  void top3_departamentos_mayor_y_menor_presupuesto() {
    assertThat(departamentoRepo.findTop3ByOrderByPresupuestoDesc())
        .extracting(Departamento::getNombre)
        .containsExactly("I+D", "Recursos Humanos", "Sistemas");

    assertThat(departamentoRepo.findTop3ByOrderByPresupuestoAsc())
        .extracting(Departamento::getNombre)
        .contains("Proyectos", "Publicidad", "Contabilidad");
  }

  @Test
  void top2_departamentos_mayor_y_menor_gasto() {
    assertThat(departamentoRepo.findTop2ByOrderByGastoDesc())
        .extracting(Departamento::getNombre)
        .containsExactly("I+D", "Recursos Humanos");

    assertThat(departamentoRepo.findTop2ByOrderByGastoAsc())
        .extracting(Departamento::getNombre)
        .containsExactly("Proyectos", "Publicidad");
  }

  @Test
  void paginacion_5_filas_desde_la_3a_incluida() {
    TypedQuery<Empleado> q = em.createQuery("select e from Empleado e order by e.id", Empleado.class);
    q.setFirstResult(2); // 0-based => 3a fila
    q.setMaxResults(5);

    List<Empleado> rows = q.getResultList();
    assertThat(rows).hasSize(5);
    assertThat(rows.get(0).getId()).isEqualTo(3L);
  }

  @Test
  void filtros_por_presupuesto_y_gasto() {
    assertThat(departamentoRepo.findByPresupuestoGreaterThanEqual(new BigDecimal("150000"))).hasSize(3);
    assertThat(departamentoRepo.findByGastoLessThan(new BigDecimal("5000"))).hasSize(3);

    // 100000..200000 sin BETWEEN (equivalente)
    List<Departamento> entre = em.createQuery(
            "select d from Departamento d where d.presupuesto >= :min and d.presupuesto <= :max",
            Departamento.class
        )
        .setParameter("min", new BigDecimal("100000"))
        .setParameter("max", new BigDecimal("200000"))
        .getResultList();
    assertThat(entre).hasSize(3);

    // NOT BETWEEN
    assertThat(departamentoRepo.findByPresupuestoNotBetween(new BigDecimal("100000"), new BigDecimal("200000")))
        .hasSize(4);

    // BETWEEN
    assertThat(departamentoRepo.findByPresupuestoBetween(new BigDecimal("100000"), new BigDecimal("200000")))
        .hasSize(3);
  }

  @Test
  void comparaciones_entre_gasto_y_presupuesto() {
    assertThat(departamentoRepo.findWhereGastoGreaterThanPresupuesto())
        .extracting(Departamento::getNombre)
        .containsExactlyInAnyOrder("I+D", "Publicidad");

    assertThat(departamentoRepo.findWhereGastoLessThanPresupuesto()).hasSize(4);
  }

  @Test
  void empleados_con_apellido2_null_lopez_y_diaz_o_moreno() {
    assertThat(empleadoRepo.findByApellido2IsNull()).hasSize(2);
    assertThat(empleadoRepo.findByApellido2("López")).hasSize(1);

    // "Díaz o Moreno" sin IN (OR)
    List<Empleado> or = em.createQuery(
            "select e from Empleado e where e.apellido2 = 'Díaz' or e.apellido2 = 'Moreno'",
            Empleado.class
        ).getResultList();
    assertThat(or).hasSize(2);

    // con IN
    assertThat(empleadoRepo.findByApellido2In(List.of("Díaz", "Moreno"))).hasSize(2);
  }

  @Test
  void empleados_por_departamento_id() {
    assertThat(empleadoRepo.findByDepartamento_Id(3L)).hasSize(2);
    assertThat(empleadoRepo.findByDepartamento_IdIn(List.of(2L, 4L, 5L))).hasSize(6);
  }

  // ----------------------
  // 3) Consultas multitabla
  // ----------------------

  @Test
  void empleados_con_su_departamento_incluso_si_no_tienen() {
    List<Empleado> rows = empleadoRepo.findAllWithDepartamento();
    assertThat(rows).hasSize(13);
  }

  @Test
  void empleados_con_departamento_ordenado_por_nombre_depto_y_apellidos() {
    List<Empleado> rows = em.createQuery(
            "select e from Empleado e left join fetch e.departamento d " +
                "order by " +
                "case when d.nombre is null then 1 else 0 end asc, d.nombre asc, " +
                "e.apellido1 asc, " +
                "case when e.apellido2 is null then 0 else 1 end asc, e.apellido2 asc, " +
                "e.nombre asc",
            Empleado.class
        ).getResultList();

    assertThat(rows).hasSize(13);
  }

  @Test
  void departamentos_que_tienen_empleados() {
    assertThat(departamentoRepo.findConEmpleados()).hasSize(5);
  }

  @Test
  void departamentos_con_empleados_y_presupuesto_actual() {
    List<Object[]> rows = em.createQuery(
            "select distinct d.id, d.nombre, (d.presupuesto - d.gasto) " +
                "from Departamento d join d.empleados e",
            Object[].class
        ).getResultList();

    assertThat(rows).hasSize(5);
  }

  @Test
  void departamento_de_empleado_por_nif_y_por_nombre_completo() {
    String dept1 = em.createQuery(
            "select d.nombre from Departamento d join d.empleados e where e.nif = :nif",
            String.class
        )
        .setParameter("nif", "38382980M")
        .getSingleResult();

    assertThat(dept1).isEqualTo("Desarrollo");

    String dept2 = em.createQuery(
            "select d.nombre from Departamento d join d.empleados e " +
                "where e.nombre = :nombre and e.apellido1 = :a1 and e.apellido2 = :a2",
            String.class
        )
        .setParameter("nombre", "Pepe")
        .setParameter("a1", "Ruiz")
        .setParameter("a2", "Santana")
        .getSingleResult();

    assertThat(dept2).isEqualTo("Recursos Humanos");
  }

  @Test
  void empleados_de_departamento_por_nombre() {
    List<Empleado> id = em.createQuery(
            "select e from Empleado e join e.departamento d where d.nombre = :nombre order by e.nombre",
            Empleado.class
        )
        .setParameter("nombre", "I+D")
        .getResultList();

    assertThat(id).hasSize(2);
  }

  @Test
  void nombres_empleados_en_deptos_con_presupuesto_entre_100k_y_200k() {
    List<String> nombres = em.createQuery(
            "select concat(e.nombre, ' ', e.apellido1, ' ', coalesce(e.apellido2, '')) " +
                "from Empleado e join e.departamento d " +
                "where d.presupuesto between :min and :max",
            String.class
        )
        .setParameter("min", new BigDecimal("100000"))
        .setParameter("max", new BigDecimal("200000"))
        .getResultList();

    assertThat(nombres).hasSize(7);
  }

  @Test
  void departamentos_donde_existe_empleado_con_apellido2_null_distinct() {
    List<String> deptos = em.createQuery(
            "select distinct d.nombre from Departamento d join d.empleados e where e.apellido2 is null",
            String.class
        ).getResultList();

    assertThat(deptos).containsExactlyInAnyOrder("Sistemas", "Desarrollo");
  }

  // ----------------------
  // 4) Composición externa
  // ----------------------

  @Test
  void empleados_sin_departamento() {
    assertThat(empleadoRepo.findByDepartamentoIsNull()).hasSize(2);
  }

  @Test
  void departamentos_sin_empleados() {
    assertThat(departamentoRepo.findSinEmpleados())
        .extracting(Departamento::getNombre)
        .containsExactlyInAnyOrder("Proyectos", "Publicidad");
  }

  // ----------------------
  // 5) Consultas resumen
  // ----------------------

  @Test
  void agregados_basicos() {
    BigDecimal sum = em.createQuery("select sum(d.presupuesto) from Departamento d", BigDecimal.class)
        .getSingleResult();
    BigDecimal min = em.createQuery("select min(d.presupuesto) from Departamento d", BigDecimal.class)
        .getSingleResult();
    BigDecimal max = em.createQuery("select max(d.presupuesto) from Departamento d", BigDecimal.class)
        .getSingleResult();
    Long totalEmpleados = em.createQuery("select count(e) from Empleado e", Long.class)
        .getSingleResult();
    Long empleadosApellido2Null = em.createQuery("select count(e) from Empleado e where e.apellido2 is null", Long.class)
        .getSingleResult();

    assertThat(sum).isEqualByComparingTo(new BigDecimal("1035000.00"));
    assertThat(min).isEqualByComparingTo(new BigDecimal("0.00"));
    assertThat(max).isEqualByComparingTo(new BigDecimal("375000.00"));
    assertThat(totalEmpleados).isEqualTo(13);
    assertThat(empleadosApellido2Null).isEqualTo(2);
  }

  @Test
  void empleados_por_departamento_group_by_y_having() {
    // (equivalente al getRawMany del spec con leftJoin)
    List<Object[]> rows = em.createQuery(
            "select d.nombre, count(e) from Empleado e left join e.departamento d " +
                "group by d.id, d.nombre " +
                "order by case when d.nombre is null then 0 else 1 end asc, d.nombre asc",
            Object[].class
        ).getResultList();

    // incluye el grupo NULL (empleados sin depto)
    assertThat(rows).hasSize(6);

    // HAVING > 2
    List<Object[]> having = em.createQuery(
            "select d.nombre, count(e) from Empleado e join e.departamento d group by d.id, d.nombre having count(e) > 2 order by d.nombre",
            Object[].class
        ).getResultList();

    assertThat(having)
        .extracting(r -> (String) r[0])
        .containsExactly("Desarrollo", "Sistemas");
  }

  @Test
  void empleados_por_departamento_con_case_sin_depto() {
    List<Object[]> rows = em.createQuery(
            "select case when d.nombre is null then 'SIN_DEPTO.' else d.nombre end, count(e) " +
                "from Empleado e left join e.departamento d " +
                "group by d.id, d.nombre " +
                "order by 1",
            Object[].class
        ).getResultList();

    assertThat(rows).hasSize(6);
    assertThat(rows)
        .extracting(r -> (String) r[0])
        .contains("SIN_DEPTO.");
  }

  // ----------------------
  // 6) Subconsultas
  // ----------------------

  public static record EmpleadoDto(Long id, String nif, String nombre, String apellido1, String apellido2, Long departamentoId) {}

  @Test
  void subquery_empleados_del_departamento_sistemas_sin_join() {
    List<EmpleadoDto> rows = em.createQuery(
            "select new org.iesvdm.test_spring_data_jpa.EmpleadoDepartamentoJpaTests$EmpleadoDto(" +
                "e.id, e.nif, e.nombre, e.apellido1, e.apellido2, e.departamento.id" +
                ") " +
                "from Empleado e " +
                "where e.departamento.id in (select d.id from Departamento d where d.nombre = :nombre) " +
                "order by e.id",
            EmpleadoDto.class
        )
        .setParameter("nombre", "Sistemas")
        .getResultList();

    assertThat(rows).containsExactly(
        new EmpleadoDto(2L, "Y5575632D", "Adela", "Salas", "Díaz", 2L),
        new EmpleadoDto(7L, "80576669X", "Pilar", "Ruiz", null, 2L),
        new EmpleadoDto(9L, "56399183D", "Juan", "Gómez", "López", 2L)
    );
  }

  @Test
  void subquery_departamento_con_mayor_y_menor_presupuesto() {
    Departamento max = em.createQuery(
            "select d from Departamento d where d.presupuesto = (select max(d2.presupuesto) from Departamento d2)",
            Departamento.class
        ).getSingleResult();

    assertThat(max.getNombre()).isEqualTo("I+D");
    assertThat(max.getPresupuesto()).isEqualByComparingTo(new BigDecimal("375000.00"));

    List<Departamento> mins = em.createQuery(
            "select d from Departamento d where d.presupuesto = (select min(d2.presupuesto) from Departamento d2) order by d.id",
            Departamento.class
        ).getResultList();

    assertThat(mins).extracting(Departamento::getNombre)
        .containsExactly("Proyectos", "Publicidad");
  }
}
