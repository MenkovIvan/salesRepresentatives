package org.iamenko1.controller;

import lombok.RequiredArgsConstructor;
import org.iamenko1.dto.AckDto;
import org.iamenko1.dto.ClientDto;
import org.iamenko1.entity.ClientEntity;
import org.iamenko1.entity.UserEntity;
import org.iamenko1.exception.BadRequestException;
import org.iamenko1.exception.NotFoundException;
import org.iamenko1.factory.ClientDtoFactory;
import org.iamenko1.repository.ClientRepository;
import org.iamenko1.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional
@RestController
public class ClientController {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientDtoFactory clientDtoFactory;

    public static final String FETCH_CLIENT = "/api/clients";
    public static final String GET_CLIENT = "/api/clients/{client_id}";
    public static final String CREATE_CLIENT = "/api/clients";
    public static final String EDIT_CLIENT = "/api/clients/{client_id}";
    public static final String DELETE_CLIENT = "/api/clients/{client_id}";

    /**
     * Получение списка складов по началу названия
     * example: http://localhost:8080/api/clients?prefix_address=Moscow
     * @param optionalPrefixAddress //начало адреса
     * @return если пустой - весь список маршрутов, или название маршрута с этой строки
     */
    @GetMapping(FETCH_CLIENT)
    public List<ClientDto> fetchClient(
            @RequestParam(value = "prefix_address", required = false) Optional<String> optionalPrefixAddress) {

        optionalPrefixAddress = optionalPrefixAddress.filter(prefixName -> ! prefixName.trim().isEmpty());

        Stream<ClientEntity> userStream = optionalPrefixAddress
                .map(clientRepository::streamAllByAddressStartsWithIgnoreCase)
                .orElseGet(clientRepository::streamAllBy);

        return userStream
                .map(clientDtoFactory::makeClientDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение клиента по id
     * example: http://localhost:8080/api/clients/123
     * @param client_id - ключ
     * @return склад
     */
    @GetMapping(GET_CLIENT)
    public ClientDto getClient(
            @PathVariable("client_id") Long client_id) {

        return clientDtoFactory.makeClientDto(getClientOrThrowException(client_id));
    }

    /**
     * Добавление клиента
     * example: http://localhost:8080/api/clients
     * @param clientDto
     * @return ClientDto
     */
    @PostMapping(CREATE_CLIENT)    //для примера для передачи использутся dto в RequestBody
    public ClientDto createClient(@RequestBody ClientDto clientDto){

        UserEntity user = userRepository
                .findById(clientDto.getUserDto().getUserId())
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "User with \"%s\" doesn't exist",
                                        clientDto.getUserDto().getUserId()
                                )
                        )
                );

        ClientEntity clientEntity = clientRepository.saveAndFlush(
                ClientEntity.builder()
                        .address(clientDto.getAddress())
                        .numberOfOrders(clientDto.getNumberOfOrders())
                        .userEntity(user)
                        .build()
        );

        return clientDtoFactory.makeClientDto(clientEntity);
    }

    /**
     * Изменение адреса клиента
     * example: http://localhost:8080/api/clients/4
     * client_id - id
     * @param ClientDto - два поля
     * @return UserDto
     */
    @PatchMapping(EDIT_CLIENT)     //для примера для передачи использутся поле в запросе + поле в RequestParam
    public ClientDto editClient(
            @PathVariable("client_id") Long client_id,
            @RequestBody ClientDto ClientDto) {

        if (ClientDto.getAddress().trim().isEmpty()) {
            throw new BadRequestException("Fields can't be empty");
        }

        ClientEntity clientEntity = getClientOrThrowException(client_id);

        clientEntity.setAddress(ClientDto.getAddress());

        clientEntity = clientRepository.saveAndFlush(clientEntity);

        return clientDtoFactory.makeClientDto(clientEntity);
    }

    /**
     * Удаление user
     * DELETE http://localhost:8080/api/clients/2
     * @param client_id // id Client for delete
     * @return AckDto (boolean answer)
     */
    @DeleteMapping(DELETE_CLIENT)
    public AckDto deleteClient(@PathVariable("client_id") Long client_id) {

        getClientOrThrowException(client_id);

        clientRepository
                .deleteById(client_id);

        return AckDto.makeDefault(true);
    }

    private ClientEntity getClientOrThrowException(Long client_id) {
        return clientRepository
                .findByClientId(client_id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Client with \"%s\" doesn't exist",
                                        client_id
                                )
                        )
                );
    }
}
