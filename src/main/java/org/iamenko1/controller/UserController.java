package org.iamenko1.controller;

import lombok.RequiredArgsConstructor;
import org.iamenko1.dto.AckDto;
import org.iamenko1.dto.UserDto;
import org.iamenko1.entity.UserEntity;
import org.iamenko1.exception.BadRequestException;
import org.iamenko1.exception.NotFoundException;
import org.iamenko1.factory.UserDtoFactory;
import org.iamenko1.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional
@RestController("/salesRep")
public class UserController {

    private final UserRepository userRepository;
    private final UserDtoFactory userDtoFactory;

    public static final String FETCH_USER = "/api/users";
    public static final String GET_USER = "/api/users/{user_id}";
    public static final String CREATE_USER = "/api/users";
    public static final String EDIT_USER = "/api/users/{user_id}";
    public static final String DELETE_USER = "/api/users/{user_id}";
    public static final String LOGIN_USER = "/api/users/login";

    /**
     * Получение списка проектов по началу названия
     * example: http://localhost:8080/api/users?prefix_name=menkov
     * @param optionalPrefixName //начало логина пользователя
     * @return если пустой - весь список юзеров, или логины юзеров, которые начинаются с этой строки
     */
    @GetMapping(FETCH_USER)
    public List<UserDto> fetchUsers(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> ! prefixName.trim().isEmpty());

        Stream<UserEntity> userStream = optionalPrefixName
                .map(userRepository::streamAllByLoginStartsWithIgnoreCase)
                .orElseGet(userRepository::streamAllBy);

        return userStream
                .map(userDtoFactory::makeUserDtoWithoutPassword)
                .collect(Collectors.toList());
    }

    /**
     * Получение юзера по id
     * example: http://localhost:8080/api/users/123
     * @param userId - ключ
     * @return юзер
     */
    @GetMapping(GET_USER)
    public UserDto getUser(
            @PathVariable("user_id") Long userId) {

        return userDtoFactory.makeUserDtoWithoutPassword(getUserOrThrowException(userId));
    }

    /**
     * Добавление user
     * example: http://localhost:8080/api/users
     * @param userDto
     * @return userDto
     */
    @PostMapping(CREATE_USER)    //для примера для передачи использутся dto в RequestBody
    public UserDto createUser(@RequestBody UserDto userDto){

        userRepository
                .findByLogin(userDto.getLogin())
                .ifPresent(projectEntity -> {
                    throw new BadRequestException(String.format("Project \"%s\" already exists.", userDto.getLogin()));
                });

        UserEntity userEntity = userRepository.saveAndFlush(
                UserEntity.builder()
                        .login(userDto.getLogin())
                        .password(userDto.getPassword())
                        .type(userDto.getType())
                        .fio(userDto.getFio())
                        .telephoneNumber(userDto.getTelephoneNumber())
                        .build()
        );

        return userDtoFactory.makeUserDtoWithoutPassword(userEntity);
    }

    /**
     * Изменение проекта
     * example: http://localhost:8080/api/users/4?login=Forth Project
     * @param login
     * @param userId
     * @return UserDto
     */
    @PatchMapping(EDIT_USER)     //для примера для передачи использутся поле в запросе + поле в RequestParam
    public UserDto editPatch(
            @PathVariable("user_id") Long userId,
            @RequestParam String login) {

        if (login.trim().isEmpty()) {
            throw new BadRequestException("Name can't be empty");
        }

        UserEntity userEntity = getUserOrThrowException(userId);

        userRepository
                .findByLogin(login)
                .filter(anotherUser -> !Objects.equals(anotherUser.getUserId(), userId))
                .ifPresent(
                        anotherUser -> {
                            throw new BadRequestException(
                                    String.format(
                                            "User \"%s\" already exists.",
                                            login
                                    )
                            );
                        }
                );

        userEntity.setLogin(login);

        userEntity = userRepository.saveAndFlush(userEntity);

        return userDtoFactory.makeUserDtoWithoutPassword(userEntity);
    }

    /**
     * Удаление user
     * DELETE http://localhost:8080/api/users/2
     * @param userId // id user for delete
     * @return AckDto (boolean answer)
     */
    @DeleteMapping(DELETE_USER)
    public AckDto deleteUser(@PathVariable("user_id") Long userId) {

        getUserOrThrowException(userId);

        userRepository
                .deleteById(userId);

        return AckDto.makeDefault(true);
    }

    /**
     * Вход user
     * POST http://localhost:8080/api/users/login
     * @param login логин для входа
     * @param password пароль для входа
     * @return AckDto (boolean answer)
     */
    @PostMapping(LOGIN_USER)
    public AckDto loginUser(@RequestParam String login,
                            @RequestParam String password) {

        userRepository
                .findByLoginAndPassword(login,password)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "User with \"%s\" doesn't exist or password is incorrect",
                                        login
                                )
                        )
                );

        return AckDto.makeDefault(true);
    }

    private UserEntity getUserOrThrowException(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "User with \"%s\" doesn't exist",
                                        userId
                                )
                        )
                );
    }
}
