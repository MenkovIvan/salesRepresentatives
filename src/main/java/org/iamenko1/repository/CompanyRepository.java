package org.iamenko1.repository;

import org.iamenko1.entity.CompanyEntity;
import org.iamenko1.entity.SalesRepresentativeEntity;
import org.iamenko1.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    Optional<CompanyEntity> findByCompanyId(Long companyId);

    Optional<CompanyEntity> findByName(String name);

    Stream<CompanyEntity> streamAllBy();

    Stream<CompanyEntity> streamAllByNameStartsWithIgnoreCase(String prefix);

    Optional<CompanyEntity> streamAllBySalesRepresentativesIn(Collection<Set<SalesRepresentativeEntity>> salesRepresentatives);

    Optional<CompanyEntity> findByUserEntity(UserEntity user);
}
