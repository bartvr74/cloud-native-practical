package com.ezgroceries.shoppinglist.model;

import java.util.List;
import java.util.UUID;

public class ShoppingListResource {

    private UUID id;
    private String name;
    private List<String> ingredients;

    public ShoppingListResource(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public ShoppingListResource(UUID id, String name, List<String> ingredients) {
        this(id, name);
        this.ingredients = ingredients;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

}
