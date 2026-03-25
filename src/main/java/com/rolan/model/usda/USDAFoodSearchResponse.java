package com.rolan.model.usda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class USDAFoodSearchResponse {

    private List<USDAFood> foods;
    private int totalHits;
}
