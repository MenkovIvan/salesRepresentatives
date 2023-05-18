package org.iamenko1.repository;

import org.iamenko1.entity.CompanyEntity;
import org.iamenko1.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(Long userId);

    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByLoginAndPassword(String login, String password);

    Stream<UserEntity> streamAllBy();

    Stream<UserEntity> streamAllByFioStartsWithIgnoreCase(String prefix);
    Stream<UserEntity> streamAllByLoginStartsWithIgnoreCase(String prefix);

}
