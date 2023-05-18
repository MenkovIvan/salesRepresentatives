package org.iamenko1.factory;

import org.iamenko1.dto.StorageDto;
import org.iamenko1.entity.StorageEntity;
import org.springframework.stereotype.Component;

@Component
public class StorageDtoFactory {

    public StorageDto makeStorageDto(StorageEntity entity) {

        return StorageDto.builder()
                .storageId(entity.getStorageId())
                .address(entity.getAddress())
                .name(entity.getName())
                .build();
    }
}
