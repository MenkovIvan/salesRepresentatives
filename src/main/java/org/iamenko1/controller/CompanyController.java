package org.iamenko1.controller;

import lombok.RequiredArgsConstructor;
import org.iamenko1.dto.AckDto;
import org.iamenko1.dto.CompanyDto;
import org.iamenko1.entity.CompanyEntity;
import org.iamenko1.entity.UserEntity;
import org.iamenko1.exception.BadRequestException;
import org.iamenko1.exception.NotFoundException;
import org.iamenko1.factory.CompanyDtoFactory;
import org.iamenko1.repository.CompanyRepository;
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
@RestController
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyDtoFactory companyDtoFactory;

    public static final String FETCH_COMPANY = "/api/companies";
    public static final String GET_COMPANY = "/api/companies/{company_id}";
    public static final String CREATE_COMPANY = "/api/companies";
    public static final String EDIT_COMPANY = "/api/companies/{company_id}";
    public static final String DELETE_COMPANY = "/api/companies/{company_id}";

    /**
     * Получение списка складов по началу названия
     * example: http://localhost:8080/api/companies?prefix_name=menkov
     * @param optionalPrefixName //начало названия склада
     * @return если пустой - весь список маршрутов, или название маршрута с этой строки
     */
    @GetMapping(FETCH_COMPANY)
    public List<CompanyDto> fetchStoragess(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> ! prefixName.trim().isEmpty());

        Stream<CompanyEntity> userStream = optionalPrefixName
                .map(companyRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(companyRepository::streamAllBy);

        return userStream
                .map(companyDtoFactory::makeCompanyDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение склада по id
     * example: http://localhost:8080/api/companies/123
     * @param company_id - ключ
     * @return склад
     */
    @GetMapping(GET_COMPANY)
    public CompanyDto getCompany(
            @PathVariable("company_id") Long company_id) {

        return companyDtoFactory.makeCompanyDto(getCompanyOrThrowException(company_id));
    }

    /**
     * Добавление склада
     * example: http://localhost:8080/api/companies
     * @param
     * @return CompanyDto
     */
    @PostMapping(CREATE_COMPANY)    //для примера для передачи использутся dto в RequestBody
    public CompanyDto createCompany(@RequestBody CompanyDto companyDto){

        companyRepository
                .findByName(companyDto.getName())
                .ifPresent(projectEntity -> {
                    throw new BadRequestException(String.format("Company \"%s\" already exists.", companyDto.getName()));
                });
        UserEntity user = userRepository
                .findById(companyDto.getOwnerId())
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "User with \"%s\" doesn't exist",
                                        companyDto.getOwnerId()
                                )
                        )
                );

        CompanyEntity companyEntity = companyRepository.saveAndFlush(
                CompanyEntity.builder()
                        .name(companyDto.getName())
                        .companyId(companyDto.getCompanyId())
                        .numOfOrders(companyDto.getNumOfOrders())
                        .inn(companyDto.getInn())
                        .userEntity(user)
                        .build()
        );

        return companyDtoFactory.makeCompanyDto(companyEntity);
    }

    /**
     * Изменение склада
     * example: http://localhost:8080/api/companies/4?login=Forth
     * company_id - id
     * @param CompanyDto - два поля
     * @return UserDto
     */
    @PatchMapping(EDIT_COMPANY)     //для примера для передачи использутся поле в запросе + поле в RequestParam
    public CompanyDto editCOMPANY(
            @PathVariable("company_id") Long company_id,
            @RequestBody CompanyDto CompanyDto) {

        if (CompanyDto.getName().trim().isEmpty()) {
            throw new BadRequestException("Fields can't be empty");
        }

        CompanyEntity CompanyEntity = getCompanyOrThrowException(company_id);

        companyRepository
                .findByName(CompanyDto.getName())
                .filter(anotherCompany -> !Objects.equals(anotherCompany.getCompanyId(), company_id))
                .ifPresent(
                        anotherUser -> {
                            throw new BadRequestException(
                                    String.format(
                                            "COMPANY \"%s\" already exists.",
                                            CompanyDto.getName()
                                    )
                            );
                        }
                );

        CompanyEntity.setName(CompanyDto.getName());

        CompanyEntity = companyRepository.saveAndFlush(CompanyEntity);

        return companyDtoFactory.makeCompanyDto(CompanyEntity);
    }

    /**
     * Удаление user
     * DELETE http://localhost:8080/api/companies/2
     * @param company_id // id company for delete
     * @return AckDto (boolean answer)
     */
    @DeleteMapping(DELETE_COMPANY)
    public AckDto deleteUser(@PathVariable("company_id") Long company_id) {

        getCompanyOrThrowException(company_id);

        companyRepository
                .deleteById(company_id);

        return AckDto.makeDefault(true);
    }

    private CompanyEntity getCompanyOrThrowException(Long company_id) {
        return companyRepository
                .findByCompanyId(company_id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Company with \"%s\" doesn't exist",
                                        company_id
                                )
                        )
                );
    }
}
