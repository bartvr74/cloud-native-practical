package com.ezgroceries.shoppinglist.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CocktailResource {

    private String id;
    private String description;
    private String typeOfGlass;
    private String instruction;
    private String url;
    private List<String> ingredients;

}


