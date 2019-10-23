package com.ezgroceries.shoppinglist.dto;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShoppingListResourceResponse {

    private UUID id;
    private String name;
    private List<String> ingredients;

    public ShoppingListResourceResponse(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public ShoppingListResourceResponse(UUID id, String name, List<String> ingredients) {
        this(id, name);
        this.ingredients = ingredients;
    }

}
