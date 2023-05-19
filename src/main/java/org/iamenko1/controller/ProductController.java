package org.iamenko1.controller;

import lombok.RequiredArgsConstructor;
import org.iamenko1.dto.AckDto;
import org.iamenko1.dto.ProductDto;
import org.iamenko1.entity.CompanyEntity;
import org.iamenko1.entity.ProductEntity;
import org.iamenko1.entity.StorageEntity;
import org.iamenko1.entity.UserEntity;
import org.iamenko1.exception.BadRequestException;
import org.iamenko1.exception.NotFoundException;
import org.iamenko1.factory.ProductDtoFactory;
import org.iamenko1.repository.*;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional
@RestController
public class ProductController {
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final StorageRepository storageRepository;
    private final ProductDtoFactory productDtoFactory;

    public static final String FETCH_PRODUCT = "/api/products";
    public static final String GET_PRODUCT = "/api/products/{product_id}";
    public static final String CREATE_PRODUCT = "/api/products";
    public static final String EDIT_PRODUCT = "/api/products/{product_id}";
    public static final String DELETE_PRODUCT = "/api/products/{product_id}";

    /**
     * Получение списка product по началу названия
     * example: http://localhost:8080/api/products?prefix_address=Moscow
     * @param optionalPrefixName //начало названия
     * @return если пустой - весь список products, или название product с этой строки
     */
    @GetMapping(FETCH_PRODUCT)
    public List<ProductDto> fetchProduct(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> ! prefixName.trim().isEmpty());

        Stream<ProductEntity> userStream = optionalPrefixName
                .map(productRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(productRepository::streamAllBy);

        return userStream
                .map(productDtoFactory::makeProductDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение product по id
     * example: http://localhost:8080/api/products/123
     * @param product_id - ключ
     * @return продукт
     */
    @GetMapping(GET_PRODUCT)
    public ProductDto getProduct(
            @PathVariable("product_id") Long product_id) {

        return productDtoFactory.makeProductDto(getProductOrThrowException(product_id));
    }

    /**
     * Добавление product
     * example: http://localhost:8080/api/products
     * @param productDto
     * @return ProductDto
     */
    @PostMapping(CREATE_PRODUCT)    //для примера для передачи использутся dto в RequestBody
    public ProductDto createProduct(@RequestBody ProductDto productDto){

        CompanyEntity company = companyRepository
                .findById(productDto.getCompany().getCompanyId())
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Company with \"%s\" doesn't exist",
                                        productDto.getCompany().getCompanyId()
                                )
                        )
                );
        StorageEntity storage = storageRepository
                .findById(productDto.getStorage().getStorageId())
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Storage with \"%s\" doesn't exist",
                                        productDto.getStorage().getStorageId()
                                )
                        )
                );

        ProductEntity productEntity = productRepository.saveAndFlush(
                ProductEntity.builder()
                        .company(company)
                        .storage(storage)
                        .name(productDto.getName())
                        .nums(productDto.getNums())
                        .price(productDto.getPrice())
                        .releaseForm(productDto.getReleaseForm())
                        .build()
        );

        return productDtoFactory.makeProductDto(productEntity);
    }

    /**
     * Изменение названия product
     * example: http://localhost:8080/api/products/4
     * product_id - id
     * @param productDto - два поля
     * @return ProductDto
     */
    @PatchMapping(EDIT_PRODUCT)     //для примера для передачи использутся поле в запросе + поле в RequestParam
    public ProductDto editProduct(
            @PathVariable("product_id") Long product_id,
            @RequestBody ProductDto productDto) {

        if (productDto.getName().trim().isEmpty() && productDto.getPrice()!=null && productDto.getNums()!=null) {
            throw new BadRequestException("Fields can't be empty");
        }

        ProductEntity productEntity = getProductOrThrowException(product_id);

        productEntity.setName(productDto.getName());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setNums(productDto.getNums());

        productEntity = productRepository.saveAndFlush(productEntity);

        return productDtoFactory.makeProductDto(productEntity);
    }

    /**
     * Удаление product
     * DELETE http://localhost:8080/api/products/2
     * @param product_id // id Product for delete
     * @return AckDto (boolean answer)
     */
    @DeleteMapping(DELETE_PRODUCT)
    public AckDto deleteProduct(@PathVariable("product_id") Long product_id) {

        getProductOrThrowException(product_id);

        productRepository
                .deleteById(product_id);

        return AckDto.makeDefault(true);
    }

    private ProductEntity getProductOrThrowException(Long product_id) {
        return productRepository
                .findByProductId(product_id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Product with \"%s\" doesn't exist",
                                        product_id
                                )
                        )
                );
    }
}
