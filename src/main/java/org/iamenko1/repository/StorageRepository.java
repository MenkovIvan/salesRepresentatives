package org.iamenko1.repository;

import org.iamenko1.entity.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface StorageRepository extends JpaRepository<StorageEntity, Long> {

    Optional<StorageEntity> findByStorageId(Long storageId);

    Stream<StorageEntity> streamAllBy();

    Stream<StorageEntity> streamAllByNameStartsWithIgnoreCase(String prefix);
}
