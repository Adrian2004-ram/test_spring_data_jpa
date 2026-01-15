package org.iesvdm.test_spring_data_jpa.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

// Indica que solo se incluirán en equals y hashCode los campos anotados con @EqualsAndHashCode.Include
// Esto permite un control más fino sobre qué campos se utilizan para estas operaciones,
// evitando problemas comunes como incluir colecciones que pueden causar ciclos o afectar el rendimiento.
//
// Compara el id(en este caso) para determinar la igualdad y el código hash del objeto.
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
public class Comercial {

    /*
    * Comercial <-- <1:n> --> Pedido
    *
    * -----------------------------------
    *
    * @Entity
    * -- Comercial:
    * @OneToMany(mappedBy="comercial")
    * Set<Pedido> pedidos;
    *
    * @Entity
    * -- Pedido (Lado Propietario):
    * @ManyToOne
    * @JoinColumn(name="comercial_id")
    * Comercial comercial;
    *
    * -----------------------------------
    * Un Helper es una función o clase de utilidad que agrupa operaciones auxiliares y reutilizables,
      normalmente sin estado, para evitar duplicación y mantener la lógica principal más limpia.
        - Propósito: proporcionar operaciones comunes (formateo, conversiones, validaciones, utilidades de fechas, etc.).
        - Características: normalmente métodos estáticos o instancias ligeras, sin lógica de negocio compleja.
        - Ejemplo en entidades: métodos helper tipo addPedido / removePedido que mantienen la consistencia de una relación
          bidireccional (añaden/remueven elementos y actualizan la referencia inversa).
        - Buenas prácticas: responsabilidad única, testables, evitar dependencias fuertes y no mezclar con lógica de negocio.
    * -----------------------------------
    * // Helper methods en Comercial:
    * public void addPedido(Pedido pedido) {
    *    this.pedidos.add(pedido);
    *    pedido.setComercial(this);
    * }
    * public void removePedido(Pedido pedido) {
    *   this.pedidos.remove(pedido);
    *   pedido.setComercial(null);
    * }
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id --> auto_increment
    @EqualsAndHashCode.Include // Va con el EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private Long id;

    // @Column annotations to define column properties of DataBAse
    @Column(length = 100)
    private String nombre;

    @Column(length = 100)
    private String apellido1;

    @Column(length = 100)
    private String apellido2;

    private String ciudad;

    @Column(precision = 10, scale = 2)
    private BigDecimal comision;

    // Lado INVERSO, el que no tiene FK, el que tiene mappedBy
    @OneToMany(mappedBy = "comercial") // mappedBy apunta al atributo de la clase Pedido que referencia a Comercial
    @Builder.Default
    @ToString.Exclude // Evita llamada recursiva infinita en toString()
    // Siempre llamar en collections
    private Set<Pedido> pedidos = new HashSet<>();


}
