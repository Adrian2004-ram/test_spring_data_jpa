package org.iesvdm.test_spring_data_jpa.domain;

public class Comercial {

    /*
    * Comercial <-- <1:n> --> Pedido
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
    * normalmente sin estado, para evitar duplicación y mantener la lógica principal más limpia.
        - Propósito: proporcionar operaciones comunes (formateo, conversiones, validaciones, utilidades de fechas, etc.).
        - Características: normalmente métodos estáticos o instancias ligeras, sin lógica de negocio compleja.
        - Ejemplo en entidades: métodos helper tipo addPedido / removePedido que mantienen la consistencia de una relación
          bidireccional (añaden/remueven elementos y actualizan la referencia inversa).
        - Buenas prácticas: responsabilidad única, testables, evitar dependencias fuertes y no mezclar con lógica de negocio.
    *
    * // Helper methods en Comercial:
    * public void addPedido(Pedido pedido) {
    *    this.pedidos.add(pedido);
    *    pedido.setComercial(this);
    * }
    * public void removePedido(Pedido pedido) {
    *   this.pedidos.remove(pedido);
    *   pedido.setComercial(null);
    * }
    *
    *
    *
    */



}
