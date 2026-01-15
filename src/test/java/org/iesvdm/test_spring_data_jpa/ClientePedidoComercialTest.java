package org.iesvdm.test_spring_data_jpa;

import org.iesvdm.test_spring_data_jpa.domain.Pedido;
import org.iesvdm.test_spring_data_jpa.repo.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ClientePedidoComercialTest {

    @Autowired
    PedidoRepository pedidoRepository;

    /*
    * Consultas sobre una tabla
    * Devuelve un listado con todos los pedidos que se han realizado. Los pedidos
    * deben estar ordenados por la fecha de realización, mostrando en primer lugar
    * los pedidos más recientes.
    *
    * */

    @Test
    void test1() {

        List<Pedido> pedidos= pedidoRepository.findAllByOrderByFechaDesc();

        System.out.println(pedidos);

    }

    @Test
    void test2() {

        List<Pedido> pedidos= pedidoRepository.findTop2ByOrderByCantidadDesc();

        pedidos.forEach(System.out::println);

    }

}
