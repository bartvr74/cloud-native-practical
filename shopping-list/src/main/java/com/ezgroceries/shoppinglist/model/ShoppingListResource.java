package com.ezgroceries.shoppinglist.model;

import java.util.UUID;

public class ShoppingListResource {

    private UUID id;
    private String name;

    public ShoppingListResource(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
