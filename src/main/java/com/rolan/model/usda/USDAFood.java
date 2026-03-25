package com.rolan.model.usda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class USDAFood {

    private Long fdcId;
    private String description;
    private List<USDANutrient> foodNutrients;
}
