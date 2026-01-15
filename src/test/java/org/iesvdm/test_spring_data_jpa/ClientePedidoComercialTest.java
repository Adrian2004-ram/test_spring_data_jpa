package org.iesvdm.test_spring_data_jpa;

import jakarta.persistence.EntityManager;
import org.iesvdm.test_spring_data_jpa.domain.Cliente;
import org.iesvdm.test_spring_data_jpa.domain.Comercial;
import org.iesvdm.test_spring_data_jpa.domain.Pedido;
import org.iesvdm.test_spring_data_jpa.repo.ClienteRepository;
import org.iesvdm.test_spring_data_jpa.repo.ComercialRepository;
import org.iesvdm.test_spring_data_jpa.repo.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class ClientePedidoComercialTest {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ComercialRepository comercialRepository;

    @Autowired
    EntityManager entityManager;


//    Consultas sobre una tabla
//    Devuelve un listado con todos los pedidos que se han realizado. Los pedidos
//    deben estar ordenados por la fecha de realización, mostrando en primer lugar
//    los pedidos más recientes.


    @Test
    void test1() {

        List<Pedido> pedidos= pedidoRepository.findAllByOrderByFechaDesc();

        System.out.println(pedidos);

    }

//    Devuelve todos los datos de los dos pedidos de mayor valor

    @Test
    void test2() {

        List<Pedido> pedidos= pedidoRepository.findTop2ByOrderByCantidadDesc();

        pedidos.forEach(System.out::println);

    }

//    Devuelve un listado con los identificadores de los clientes que han realizado algún pedido. Tenga en cuenta que no debe mostrar identificadores que estén repetidos.

    @Test
    void test3() {

        List<ClienteRepository.IdDto> ids = clienteRepository.findDistinctByPedidos_IdIsNotNull();

        ids.forEach(System.out::println);

    }

//    Devuelve un listado de todos los pedidos que se realizaron durante el año 2017, cuya cantidad total sea superior a 500€.

    @Test
    void test4() {

        List<Pedido> pedidos= pedidoRepository.findByFechaBetweenAndCantidadGreaterThan(LocalDateTime.of(2017, 1, 1, 0, 0), LocalDateTime.of(2017, 12, 31, 23, 59, 59), 500);

        pedidos.forEach(System.out::println);

    }

//    Devuelve un listado con el nombre y los apellidos de los comerciales que tienen una comisión entre 0.05 y 0.11.

    @Test
    void test5() {

        List<ComercialRepository.NomApellDto> comerciales= comercialRepository.findByComisionBetween(new BigDecimal("0.05"), new BigDecimal("0.11"));

        comerciales.forEach(System.out::println);

    }

//    Devuelve el valor de la comisión de mayor valor que existe en la tabla comercial.

    @Test
    void test6() {

        BigDecimal cantidad = comercialRepository.obtenerMayorComision();

        System.out.println("----------------------------------------Cantidad mayor: " + cantidad);

    }

//    Devuelve el identificador, nombre y primer apellido de aquellos clientes cuyo segundo apellido no es NULL. El listado deberá estar ordenado alfabéticamente por apellidos y nombre.

    @Test
    void  test7() {

        // Funcion creada en ClienteRepository
        List<ClienteRepository.IdNomApellido1> list = clienteRepository.findByApellido2IsNotNullOrderByApellido1AscApellido2AscNombre();
        list.forEach(System.out::println);

        // Consulta con @Query en ClienteRepository
        List<ClienteRepository.IdNomApellido1> list2 = clienteRepository.obteneridNomApell1();
        list2.forEach(System.out::println);

        // Consulta con EntityManager
        List<ClienteRepository.IdNomApellido1> list3 = entityManager.createQuery("""
                select c.id, c.nombre, c.apellido1 
                from Cliente c 
                order by c.apellido1, c.apellido2, c.nombre
                """, ClienteRepository.IdNomApellido1.class).getResultList();
        list3.forEach(System.out::println);

    }

//    Devuelve un listado de los nombres de los clientes que empiezan por A y terminan por n y también los nombres que empiezan por P. El listado deberá estar ordenado alfabéticamente.

//    Devuelve un listado de los nombres de los clientes que no empiezan por A. El listado deberá estar ordenado alfabéticamente.

//    Devuelve un listado con los nombres de los comerciales que terminan por el o o. Tenga en cuenta que se deberán eliminar los nombres repetidos.

//    Consultas multitabla (Composición interna)
//    Devuelve un listado con el identificador, nombre y los apellidos de todos los clientes que han realizado algún pedido. El listado debe estar ordenado alfabéticamente y se deben eliminar los elementos repetidos.

//    Devuelve un listado que muestre todos los pedidos que ha realizado cada cliente. El resultado debe mostrar todos los datos de los pedidos y del cliente. El listado debe mostrar los datos de los clientes ordenados alfabéticamente.

//    Devuelve un listado que muestre todos los pedidos en los que ha participado un comercial. El resultado debe mostrar todos los datos de los pedidos y de los comerciales. El listado debe mostrar los datos de los comerciales ordenados alfabéticamente.

//    Devuelve un listado que muestre todos los clientes, con todos los pedidos que han realizado y con los datos de los comerciales asociados a cada pedido.

//    Devuelve un listado de todos los clientes que realizaron un pedido durante el año 2017, cuya cantidad esté entre 300 € y 1000 €.

//    Devuelve el nombre y los apellidos de todos los comerciales que ha participado en algún pedido realizado por María Santana Moreno.

//    Devuelve el nombre de todos los clientes que han realizado algún pedido con el comercial Daniel Sáez Vega.

//    Consultas multitabla (Composición externa)
//    Devuelve un listado con todos los clientes junto con los datos de los pedidos que han realizado. Este listado también debe incluir los clientes que no han realizado ningún pedido. El listado debe estar ordenado alfabéticamente por el primer apellido, segundo apellido y nombre de los clientes.

//    Devuelve un listado con todos los comerciales junto con los datos de los pedidos que han realizado. Este listado también debe incluir los comerciales que no han realizado ningún pedido. El listado debe estar ordenado alfabéticamente por el primer apellido, segundo apellido y nombre de los comerciales.

//    Devuelve un listado que solamente muestre los clientes que no han realizado ningún pedido.

//    Devuelve un listado que solamente muestre los comerciales que no han realizado ningún pedido.

//    Devuelve un listado con los clientes que no han realizado ningún pedido y de los comerciales que no han participado en ningún pedido. Ordene el listado alfabéticamente por los apellidos y el nombre. En en listado deberá diferenciar de algún modo los clientes y los comerciales.

//            Consultas resumen
//    Calcula la cantidad total que suman todos los pedidos que aparecen en la tabla pedido.

//    Calcula la cantidad media de todos los pedidos que aparecen en la tabla pedido.

//    Calcula el número total de comerciales distintos que aparecen en la tabla pedido.

//    Calcula el número total de clientes que aparecen en la tabla cliente.

//    Calcula cuál es la mayor cantidad que aparece en la tabla pedido.

//    Calcula cuál es la menor cantidad que aparece en la tabla pedido.

//    Calcula cuál es el valor máximo de categoría para cada una de las ciudades que aparece en la tabla cliente.

//    Calcula cuál es el máximo valor de los pedidos realizados durante el mismo día para cada uno de los clientes. Es decir, el mismo cliente puede haber realizado varios pedidos de diferentes cantidades el mismo día. Se pide que se calcule cuál es el pedido de máximo valor para cada uno de los días en los que un cliente ha realizado un pedido. Muestra el identificador del cliente, nombre, apellidos, la fecha y el valor de la cantidad.

//    Calcula cuál es el máximo valor de los pedidos realizados durante el mismo día para cada uno de los clientes, teniendo en cuenta que sólo queremos mostrar aquellos pedidos que superen la cantidad de 2000 €.

//    Calcula el máximo valor de los pedidos realizados para cada uno de los comerciales durante la fecha 2016-08-17. Muestra el identificador del comercial, nombre, apellidos y total.

//    Devuelve un listado con el identificador de cliente, nombre y apellidos y el número total de pedidos que ha realizado cada uno de clientes. Tenga en cuenta que pueden existir clientes que no han realizado ningún pedido. Estos clientes también deben aparecer en el listado indicando que el número de pedidos realizados es 0.

//    Devuelve un listado con el identificador de cliente, nombre y apellidos y el número total de pedidos que ha realizado cada uno de clientes durante el año 2017.

//    Devuelve un listado que muestre el identificador de cliente, nombre, primer apellido y el valor de la máxima cantidad del pedido realizado por cada uno de los clientes. El resultado debe mostrar aquellos clientes que no han realizado ningún pedido indicando que la máxima cantidad de sus pedidos realizados es 0. Puede hacer uso de la función IFNULL.

//    Devuelve cuál ha sido el pedido de máximo valor que se ha realizado cada año.

//    Devuelve el número total de pedidos que se han realizado cada año.

//    Subconsultas
//    Devuelve un listado con todos los pedidos que ha realizado Adela Salas Díaz. (Sin utilizar INNER JOIN).

//    Devuelve el número de pedidos en los que ha participado el comercial Daniel Sáez Vega. (Sin utilizar INNER JOIN)

//    Devuelve los datos del cliente que realizó el pedido más caro en el año 2019. (Sin utilizar INNER JOIN)

//    Devuelve la fecha y la cantidad del pedido de menor valor realizado por el cliente Pepe Ruiz Santana.

//    Devuelve un listado con los datos de los clientes y los pedidos, de todos los clientes que han realizado un pedido durante el año 2017 con un valor mayor o igual al valor medio de los pedidos realizados durante ese mismo año.

//    Subconsultas con ALL y ANY
//    Devuelve el pedido más caro que existe en la tabla pedido si hacer uso de MAX, ORDER BY ni LIMIT.

//    Devuelve un listado de los clientes que no han realizado ningún pedido. (Utilizando ANY o ALL).

//    Devuelve un listado de los comerciales que no han realizado ningún pedido. (Utilizando ANY o ALL).

//    Subconsultas con IN y NOT IN
//    Devuelve un listado de los clientes que no han realizado ningún pedido. (Utilizando IN o NOT IN).

//    Devuelve un listado de los comerciales que no han realizado ningún pedido. (Utilizando IN o NOT IN).

//    Subconsultas con EXISTS y NOT EXISTS
//    Devuelve un listado de los clientes que no han realizado ningún pedido. (Utilizando EXISTS o NOT EXISTS).

//    Devuelve un listado de los comerciales que no han realizado ningún pedido. (Utilizando EXISTS o NOT EXISTS).


}
