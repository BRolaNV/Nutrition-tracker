package com.rolan.model.usda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class USDANutrient {

    private Integer nutrientId;
    private String nutrientName;
    private Double value;

}
