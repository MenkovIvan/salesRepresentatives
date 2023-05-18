package org.iamenko1.factory;

import org.iamenko1.dto.RouteDto;
import org.iamenko1.entity.RouteEntity;
import org.springframework.stereotype.Component;

@Component
public class RouteDtoFactory {

    public RouteDto makeRouteDto(RouteEntity entity) {

        return RouteDto.builder()
                .routeId(entity.getRouteId())
                .name(entity.getName())
                .time(entity.getTime())
                .totalLength(entity.getTotalLength())
                .numOfStorages(entity.getNumOfStorages())
                .build();
    }
}
