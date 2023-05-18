package org.iamenko1.controller;

import lombok.RequiredArgsConstructor;
import org.iamenko1.dto.AckDto;
import org.iamenko1.dto.StorageDto;
import org.iamenko1.entity.StorageEntity;
import org.iamenko1.entity.UserEntity;
import org.iamenko1.exception.BadRequestException;
import org.iamenko1.exception.NotFoundException;
import org.iamenko1.factory.StorageDtoFactory;
import org.iamenko1.repository.StorageRepository;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional
@RestController
public class StorageController {

    private final StorageRepository storageRepository;
    private final StorageDtoFactory storageDtoFactory;

    public static final String FETCH_STORAGE = "/api/storages";
    public static final String GET_STORAGE = "/api/storages/{storage_id}";
    public static final String CREATE_STORAGE = "/api/storages";
    public static final String EDIT_STORAGE = "/api/storages/{storage_id}";
    public static final String DELETE_STORAGE = "/api/storages/{storage_id}";

    /**
     * Получение списка складов по началу названия
     * example: http://localhost:8080/salesRep/api/storages?prefix_name=menkov
     * @param optionalPrefixName //начало названия склада
     * @return если пустой - весь список складов, или название складов с этой строки
     */
    @GetMapping(FETCH_STORAGE)
    public List<StorageDto> fetchStoragess(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> ! prefixName.trim().isEmpty());

        Stream<StorageEntity> userStream = optionalPrefixName
                .map(storageRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(storageRepository::streamAllBy);

        return userStream
                .map(storageDtoFactory::makeStorageDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение склада по id
     * example: http://localhost:8080/salesRep/api/storages/123
     * @param storageId - ключ
     * @return склад
     */
    @GetMapping(GET_STORAGE)
    public StorageDto getUser(
            @PathVariable("storage_id") Long storageId) {

        return storageDtoFactory.makeStorageDto(getStorageOrThrowException(storageId));
    }

    /**
     * Добавление склада
     * example: http://localhost:8080/salesRep/api/storages
     * @param
     * @return storageDto
     */
    @PostMapping(CREATE_STORAGE)    //для примера для передачи использутся dto в RequestBody
    public StorageDto createUser(@RequestBody StorageDto storageDto){

        storageRepository
                .findByName(storageDto.getName())
                .ifPresent(projectEntity -> {
                    throw new BadRequestException(String.format("Storage \"%s\" already exists.", storageDto.getName()));
                });

        StorageEntity storageEntity = storageRepository.saveAndFlush(
                StorageEntity.builder()
                        .name(storageDto.getName())
                        .address(storageDto.getAddress())
                        .build()
        );

        return storageDtoFactory.makeStorageDto(storageEntity);
    }

    /**
     * Изменение склада
     * example: http://localhost:8080/salesRep/api/users/4?login=Forth
     * @param login
     * @param userId
     * @return UserDto
     */
    @PatchMapping(EDIT_STORAGE)     //для примера для передачи использутся поле в запросе + поле в RequestParam
    public StorageDto editStorage(
            @PathVariable("storage_id") Long storage_id,
            @RequestParam String name,
            @RequestParam String address) {

        if (name.trim().isEmpty() && address.trim().isEmpty()) {
            throw new BadRequestException("Fields can't be empty");
        }

        StorageEntity storageEntity = getStorageOrThrowException(storage_id);

        storageRepository
                .findByName(name)
                .filter(anotherUser -> !Objects.equals(anotherUser.getStorageId(), storage_id))
                .ifPresent(
                        anotherUser -> {
                            throw new BadRequestException(
                                    String.format(
                                            "Storage \"%s\" already exists.",
                                            name
                                    )
                            );
                        }
                );

        storageEntity.setName(name);
        storageEntity.setAddress(address);

        storageEntity = storageRepository.saveAndFlush(storageEntity);

        return storageDtoFactory.makeStorageDto(storageEntity);
    }

    /**
     * Удаление user
     * DELETE http://localhost:8080/salesRep/api/storages/2
     * @param storage_id // id storage for delete
     * @return AckDto (boolean answer)
     */
    @DeleteMapping(DELETE_STORAGE)
    public AckDto deleteUser(@PathVariable("storage_id") Long storage_id) {

        getStorageOrThrowException(storage_id);

        storageRepository
                .deleteById(storage_id);

        return AckDto.makeDefault(true);
    }

    private StorageEntity getStorageOrThrowException(Long storageId) {
        return storageRepository
                .findById(storageId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Storage with \"%s\" doesn't exist",
                                        storageId
                                )
                        )
                );
    }
}
