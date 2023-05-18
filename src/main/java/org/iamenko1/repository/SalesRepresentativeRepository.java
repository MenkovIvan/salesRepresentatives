package org.iamenko1.repository;

import org.iamenko1.entity.CompanyEntity;
import org.iamenko1.entity.SalesRepresentativeEntity;
import org.iamenko1.entity.TaskEntity;
import org.iamenko1.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface SalesRepresentativeRepository extends JpaRepository<SalesRepresentativeEntity, Long> {

    Optional<SalesRepresentativeEntity> findBySalesRepId(Long salesRepId);

    Stream<SalesRepresentativeEntity> streamAllBy();

    Optional<SalesRepresentativeEntity> findByUserEntity(UserEntity user);

    Stream<SalesRepresentativeEntity> streamAllByTasksIn(Collection<Set<TaskEntity>> tasks);

    Stream<SalesRepresentativeEntity> streamAllByCompany(CompanyEntity company);
}
