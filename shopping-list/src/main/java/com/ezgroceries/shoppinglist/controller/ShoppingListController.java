package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.model.ShoppingListInput;
import com.ezgroceries.shoppinglist.model.ShoppingListResource;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/shopping-lists", produces = "application/json")
public class ShoppingListController {

    @PostMapping
    public ResponseEntity<ShoppingListResource> createShoppingList(@RequestBody ShoppingListInput shoppingListInput) {
        ShoppingListResource newShoppingListResource = createNewDummyShoppingList(shoppingListInput);
        return new ResponseEntity<>(newShoppingListResource, HttpStatus.CREATED);
    }

    private ShoppingListResource createNewDummyShoppingList(ShoppingListInput shoppingListInput) {
        return new ShoppingListResource(
                UUID.randomUUID(),
                shoppingListInput.getName()
        );
    }

}
