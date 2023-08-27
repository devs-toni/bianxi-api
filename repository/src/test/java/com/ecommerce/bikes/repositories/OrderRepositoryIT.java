package com.ecommerce.bikes.repositories;

import com.ecommerce.bikes.DockerConfiguration;
import com.ecommerce.bikes.domain.Like;
import com.ecommerce.bikes.entities.OrderEntity;
import com.ecommerce.bikes.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderRepositoryIT extends DockerConfiguration {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void should_return_order_by_id() {
        OrderEntity order = orderRepository.findById(1L).get();

        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals(order.getAddress(), order.getAddress());
        assertEquals(order.getPrice(), order.getPrice());
    }

    @Test
    public void should_return_all_orders_by_user_id() {
        List<OrderEntity> orders = orderRepository.findAllByUserId(2L);

        assertEquals(1, orders.size());
    }

    private static OrderEntity order =  new OrderEntity(
            1L,
            "C/Muro n3",
            563.25f,
            emptyList()
    );
}
