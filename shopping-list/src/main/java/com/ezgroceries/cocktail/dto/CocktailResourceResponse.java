package com.ezgroceries.cocktail.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CocktailResourceResponse {

    private UUID id;
    private String description;
    private String typeOfGlass;
    private String instruction;
    private String url;
    private List<String> ingredients;

}


