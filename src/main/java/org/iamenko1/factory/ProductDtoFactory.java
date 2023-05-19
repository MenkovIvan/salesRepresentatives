package org.iamenko1.factory;

import org.iamenko1.dto.ProductDto;
import org.iamenko1.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoFactory {

    private final CompanyDtoFactory companyDtoFactory;
    private final StorageDtoFactory storageDtoFactory;

    public ProductDtoFactory(CompanyDtoFactory companyDtoFactory, StorageDtoFactory storageDtoFactory) {
        this.companyDtoFactory = companyDtoFactory;
        this.storageDtoFactory = storageDtoFactory;
    }

    public ProductDto makeProductDto(ProductEntity entity) {

        return ProductDto.builder()
                .productId(entity.getProductId())
                .name(entity.getName())
                .releaseForm(entity.getReleaseForm())
                .price(entity.getPrice())
                .nums(entity.getNums())
                .company(companyDtoFactory.makeCompanyDto(entity.getCompany()))
                .storage(storageDtoFactory.makeStorageDto(entity.getStorage()))
                .build();
    }
}
