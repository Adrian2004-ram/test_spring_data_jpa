package org.iesvdm.test_spring_data_jpa.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id --> auto_increment
    @EqualsAndHashCode.Include // Va con el EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private Long id;

    // Con Double tienes que tratar los null si o si
    // Si usas double primitivo no puede ser null, por lo que JPA le asignará 0.0 por defecto
    private double cantidad;

    private LocalDateTime fecha;

    // Lado PROPIETARIO, es el que tiene FK
    @ManyToOne
    private Comercial comercial;

    @ManyToOne
    private Cliente cliente;
    

}
