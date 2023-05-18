package org.iamenko1.repository;

import org.iamenko1.entity.ClientEntity;
import org.iamenko1.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

        Optional<ClientEntity> findByClientId(Long clientId);

        Stream<ClientEntity> streamAllByAddressStartsWithIgnoreCase(String address);

        Stream<ClientEntity> streamAllBy();

        Optional<ClientEntity> findByUserEntity(UserEntity user);
}
