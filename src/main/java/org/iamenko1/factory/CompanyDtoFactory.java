package org.iamenko1.factory;

import org.iamenko1.dto.CompanyDto;
import org.iamenko1.entity.CompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyDtoFactory {
    
    public CompanyDto makeCompanyDto(CompanyEntity entity) {

        return CompanyDto.builder()
                .companyId(entity.getCompanyId())
                .inn(entity.getInn())
                .numOfOrders(entity.getNumOfOrders())
                .name(entity.getName())
                .ownerId(entity.getUserEntity().getUserId())
                .build();
    }
}
