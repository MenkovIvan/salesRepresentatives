package org.iamenko1.factory;

import org.iamenko1.dto.UserDto;
import org.iamenko1.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDtoFactory {

    public UserDto makeUserDtoWithoutPassword(UserEntity entity) {

        return  UserDto.builder()
                .userId(entity.getUserId())
                .login(entity.getLogin())
                .fio(entity.getFio())
                .type(entity.getType())
                .telephoneNumber(entity.getTelephoneNumber())
                .build();
    }
}
