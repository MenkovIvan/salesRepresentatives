package org.iamenko1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    @JsonProperty("client_id")
    private Long clientId;
    @JsonProperty("created_at")
    private Instant createdAt;
    private String address;
    @JsonProperty("number_of_orders")
    private Integer numberOfOrders;
    @JsonProperty("user")
    private UserDto userDto;
}
