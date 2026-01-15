package org.iesvdm.test_spring_data_jpa.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 100)
    private String nombre;

    @Column(length = 100)
    private String apellido1;

    @Column(length = 100)
    private String apellido2;

    private String ciudad;

    private Integer categoria;

    @OneToMany(mappedBy = "cliente")
    @Builder.Default
    @ToString.Exclude
    private Set<Pedido> pedidos = new HashSet<>();

}
