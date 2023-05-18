package org.iamenko1.factory;

import org.iamenko1.dto.ClientDto;
import org.iamenko1.entity.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientDtoFactory {

    private final UserDtoFactory userDtoFactory;

    public ClientDtoFactory(UserDtoFactory userDtoFactory) {
        this.userDtoFactory = userDtoFactory;
    }

    public ClientDto makeClientDto(ClientEntity entity) {

        return ClientDto.builder()
                .clientId(entity.getClientId())
                .userDto(userDtoFactory.makeUserDtoWithoutPassword(entity.getUserEntity()))
                .numberOfOrders(entity.getNumberOfOrders())
                .createdAt(entity.getCreatedAt())
                .address(entity.getAddress())
                .build();
    }
}
