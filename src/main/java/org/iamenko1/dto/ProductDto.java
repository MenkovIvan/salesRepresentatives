package org.iamenko1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @JsonProperty("product_id")
    private Long productId;
    private String name;
    @JsonProperty("release_form")
    private String releaseForm;
    private Integer nums;
    private Double price;
    private StorageDto storage;
    private CompanyDto company;
}
