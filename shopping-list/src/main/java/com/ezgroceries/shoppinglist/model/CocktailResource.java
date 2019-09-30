package com.ezgroceries.shoppinglist.model;

import java.util.List;
import java.util.UUID;

public class CocktailResource {

    private UUID id;
    private String description;
    private String typeOfGlass;
    private String instruction;
    private String url;
    private List<String> ingredients;

    public CocktailResource(UUID id, String description, String typeOfGlass, String instruction, String url,
            List<String> ingredients) {
        this.id = id;
        this.description = description;
        this.typeOfGlass = typeOfGlass;
        this.instruction = instruction;
        this.url = url;
        this.ingredients = ingredients;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTypeOfGlass() {
        return typeOfGlass;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

}


