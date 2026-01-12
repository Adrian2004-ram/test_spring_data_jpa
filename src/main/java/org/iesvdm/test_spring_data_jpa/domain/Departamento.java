package org.iesvdm.test_spring_data_jpa.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(
    name = "departamento",
    uniqueConstraints = @UniqueConstraint(name = "uk_departamento_nombre", columnNames = "nombre")
)
public class Departamento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(nullable = false, length = 255)
  private String nombre;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal presupuesto;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal gasto;

  @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  private Set<Empleado> empleados = new HashSet<>();

  // helpers para facilitar gestion de la relacion bidireccional
  public void addEmpleado(Empleado e) {
    empleados.add(e);
    e.setDepartamento(this);
  }

  public void removeEmpleado(Empleado e) {
      empleados.remove(e);
      e.setDepartamento(null);
  }

}
