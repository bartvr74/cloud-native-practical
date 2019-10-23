package com.ezgroceries.cocktail.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourcesResponse<T> {

    private List<T> resources;

}
