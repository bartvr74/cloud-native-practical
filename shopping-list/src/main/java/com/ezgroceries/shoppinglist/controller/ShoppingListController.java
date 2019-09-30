package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.input.CreateShoppingListInput;
import com.ezgroceries.shoppinglist.model.CocktailReference;
import com.ezgroceries.shoppinglist.model.ShoppingListResource;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "application/json")
public class ShoppingListController {

    @PostMapping("/shopping-lists")
    public ResponseEntity<ShoppingListResource> createShoppingList(@RequestBody CreateShoppingListInput createShoppingListInput) {
        ShoppingListResource newShoppingListResource = newShoppingList(createShoppingListInput);
        return new ResponseEntity<>(newShoppingListResource, HttpStatus.CREATED);
    }

    @PostMapping("/shopping-lists/{shoppingListId}/cocktails")
    public ResponseEntity<List<CocktailReference>> addCocktailToShoppingList(@PathVariable(name = "shoppingListId") UUID shoppingListId,
            @RequestBody List<CocktailReference> cocktailReferences) {
        List<CocktailReference> addedCocktails = linkCocktailToShoppingList(shoppingListId, cocktailReferences);
        return new ResponseEntity<>(addedCocktails, HttpStatus.OK);
    }

    private ShoppingListResource newShoppingList(CreateShoppingListInput shoppingListInput) {
        return new ShoppingListResource(
                UUID.randomUUID(),
                shoppingListInput.getName()
        );
    }

    private List<CocktailReference> linkCocktailToShoppingList(UUID shoppingListId,
            List<CocktailReference> cocktailReferences) {
        CocktailReference addedCocktail = new CocktailReference();
        addedCocktail.setCocktailId(cocktailReferences.get(0).getCocktailId());
        return Arrays.asList(addedCocktail);
    }

}
