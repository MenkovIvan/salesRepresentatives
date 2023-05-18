package org.iamenko1.repository;

import org.iamenko1.entity.ClientEntity;
import org.iamenko1.entity.OrderBasketEntity;
import org.iamenko1.entity.OrderEntity;
import org.iamenko1.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderId(Long orderId);

    Stream<OrderEntity> streamAllBy();

    Optional<OrderEntity> findByClient(ClientEntity client);

    Optional<OrderEntity> findByTask(TaskEntity task);

    Stream<OrderEntity> findAllByProductsInBasketIn(Collection<Set<OrderBasketEntity>> productsInBasket);
}
