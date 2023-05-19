package org.iamenko1.repository;

import org.iamenko1.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByProductId(Long id);

    Stream<ProductEntity> streamAllBy();

    Stream<ProductEntity> streamAllByNameStartsWithIgnoreCase(String prefix);

    Stream<ProductEntity> streamAllByProductsInBasketIn(Set<OrderBasketEntity> productsInBasket);

    Stream<ProductEntity> streamAllByStorage(StorageEntity storage);

    Stream<ProductEntity> streamAllByCompany(CompanyEntity company);

    Stream<ProductEntity> streamAllByCompanyAndStorage(CompanyEntity company, StorageEntity storage);
}
