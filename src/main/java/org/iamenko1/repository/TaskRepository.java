package org.iamenko1.repository;

import org.iamenko1.entity.OrderEntity;
import org.iamenko1.entity.RouteEntity;
import org.iamenko1.entity.SalesRepresentativeEntity;
import org.iamenko1.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByTaskId(Long taskId);

    Stream<TaskEntity> streamAllBy();

    Optional<TaskEntity> findByRoute(RouteEntity route);

    Optional<TaskEntity> findByOrder(OrderEntity order);

    Stream<TaskEntity> streamAllBySalesRepresentative(SalesRepresentativeEntity salesRepresentative);

    Stream<TaskEntity> streamAllBySalesRepresentativeAndStatus(SalesRepresentativeEntity salesRepresentative, String status);

    Stream<TaskEntity> streamAllBySalesRepresentativeAndStatusAndRoute(SalesRepresentativeEntity salesRepresentative, String status, RouteEntity route);

}
