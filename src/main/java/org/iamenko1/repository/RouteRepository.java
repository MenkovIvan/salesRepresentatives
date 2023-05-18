package org.iamenko1.repository;

import org.iamenko1.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface RouteRepository extends JpaRepository<RouteEntity, Long> {

    Optional<RouteEntity> findByRouteId(Long routeId);

    Optional<RouteEntity> findByName(String name);

    Stream<RouteEntity> streamAllBy();

    Stream<RouteEntity> streamAllByNameStartsWithIgnoreCase(String prefix);
}
