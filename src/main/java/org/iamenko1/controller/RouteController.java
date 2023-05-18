package org.iamenko1.controller;

import lombok.RequiredArgsConstructor;
import org.iamenko1.dto.AckDto;
import org.iamenko1.dto.RouteDto;
import org.iamenko1.entity.RouteEntity;
import org.iamenko1.exception.BadRequestException;
import org.iamenko1.exception.NotFoundException;
import org.iamenko1.factory.RouteDtoFactory;
import org.iamenko1.repository.RouteRepository;
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
public class RouteController {

    private final RouteRepository routeRepository;
    private final RouteDtoFactory routeDtoFactory;

    public static final String FETCH_ROUTE = "/api/routes";
    public static final String GET_ROUTE = "/api/routes/{route_id}";
    public static final String CREATE_ROUTE = "/api/routes";
    public static final String EDIT_ROUTE = "/api/routes/{route_id}";
    public static final String DELETE_ROUTE = "/api/routes/{route_id}";

    /**
     * Получение списка складов по началу названия
     * example: http://localhost:8080/api/routes?prefix_name=menkov
     * @param optionalPrefixName //начало названия склада
     * @return если пустой - весь список маршрутов, или название маршрута с этой строки
     */
    @GetMapping(FETCH_ROUTE)
    public List<RouteDto> fetchStoragess(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> ! prefixName.trim().isEmpty());

        Stream<RouteEntity> userStream = optionalPrefixName
                .map(routeRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(routeRepository::streamAllBy);

        return userStream
                .map(routeDtoFactory::makeRouteDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение склада по id
     * example: http://localhost:8080/api/routes/123
     * @param route_id - ключ
     * @return склад
     */
    @GetMapping(GET_ROUTE)
    public RouteDto getUser(
            @PathVariable("route_id") Long route_id) {

        return routeDtoFactory.makeRouteDto(getRouteOrThrowException(route_id));
    }

    /**
     * Добавление склада
     * example: http://localhost:8080/api/routes
     * @param
     * @return RouteDto
     */
    @PostMapping(CREATE_ROUTE)    //для примера для передачи использутся dto в RequestBody
    public RouteDto createUser(@RequestBody RouteDto routeDto){

        routeRepository
                .findByName(routeDto.getName())
                .ifPresent(projectEntity -> {
                    throw new BadRequestException(String.format("Storage \"%s\" already exists.", routeDto.getName()));
                });

        RouteEntity routeEntity = routeRepository.saveAndFlush(
                RouteEntity.builder()
                        .name(routeDto.getName())
                        .routeId(routeDto.getRouteId())
                        .numOfStorages(routeDto.getNumOfStorages())
                        .totalLength(routeDto.getTotalLength())
                        .time(routeDto.getTime())
                        .build()
        );

        return routeDtoFactory.makeRouteDto(routeEntity);
    }

    /**
     * Изменение склада
     * example: http://localhost:8080/api/routes/4?login=Forth
     * route_id - id
     * @param routeDto - два поля
     * @return UserDto
     */
    @PatchMapping(EDIT_ROUTE)     //для примера для передачи использутся поле в запросе + поле в RequestParam
    public RouteDto editRoute(
            @PathVariable("route_id") Long route_id,
            @RequestBody RouteDto routeDto) {

        if (routeDto.getName().trim().isEmpty() && routeDto.getTotalLength().trim().isEmpty()) {
            throw new BadRequestException("Fields can't be empty");
        }

        RouteEntity routeEntity = getRouteOrThrowException(route_id);

        routeRepository
                .findByName(routeDto.getName())
                .filter(anotherRoute -> !Objects.equals(anotherRoute.getRouteId(), route_id))
                .ifPresent(
                        anotherUser -> {
                            throw new BadRequestException(
                                    String.format(
                                            "Route \"%s\" already exists.",
                                            routeDto.getName()
                                    )
                            );
                        }
                );

        routeEntity.setName(routeDto.getName());
        routeEntity.setTotalLength(routeDto.getTotalLength());

        routeEntity = routeRepository.saveAndFlush(routeEntity);

        return routeDtoFactory.makeRouteDto(routeEntity);
    }

    /**
     * Удаление user
     * DELETE http://localhost:8080/api/routes/2
     * @param route_id // id storage for delete
     * @return AckDto (boolean answer)
     */
    @DeleteMapping(DELETE_ROUTE)
    public AckDto deleteUser(@PathVariable("route_id") Long route_id) {

        getRouteOrThrowException(route_id);

        routeRepository
                .deleteById(route_id);

        return AckDto.makeDefault(true);
    }

    private RouteEntity getRouteOrThrowException(Long route_id) {
        return routeRepository
                .findByRouteId(route_id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Route with \"%s\" doesn't exist",
                                        route_id
                                )
                        )
                );
    }
}
