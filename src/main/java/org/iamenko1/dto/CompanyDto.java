package org.iamenko1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private Long companyId;
    private String name;
    private String inn;
    @JsonProperty("num_of_orders")
    private Integer numOfOrders;
    @JsonProperty("owner_id")
    private Long ownerId;
}
