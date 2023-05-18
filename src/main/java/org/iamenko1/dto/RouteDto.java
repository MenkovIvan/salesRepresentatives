package org.iamenko1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto {

    @JsonProperty("route_id")
    private Long routeId;
    private String name;
    @JsonProperty("total_length")
    private String totalLength;
    private Date time;
    @JsonProperty("num_of_storages")
    private Integer numOfStorages;
}
