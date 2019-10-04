package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.contract.CreateShoppingList;
import com.ezgroceries.shoppinglist.model.CocktailReference;
import com.ezgroceries.shoppinglist.model.ShoppingListResource;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "application/json")
public class ShoppingListController {

    // todo dummy data for mobile team
    // todo replace with services / repositories later on similar to cocktail controller

    @PostMapping("/shopping-lists")
    public ResponseEntity<ShoppingListResource> createShoppingList(
            @RequestBody CreateShoppingList createShoppingList) {
        ShoppingListResource newShoppingList = newShoppingList(createShoppingList);
        return new ResponseEntity<>(newShoppingList, HttpStatus.CREATED);
    }

    @PostMapping("/shopping-lists/{shoppingListId}/cocktails")
    public ResponseEntity<List<CocktailReference>> addCocktailReferences(
            @PathVariable(name = "shoppingListId") UUID shoppingListId,
            @RequestBody List<CocktailReference> cocktailReferences) {
        List<CocktailReference> addedCocktailReferences =
                addCocktailReferencesToShoppingList(shoppingListId, cocktailReferences);
        return new ResponseEntity<>(addedCocktailReferences, HttpStatus.OK);
    }

    @GetMapping("/shopping-lists/{shoppingListId}")
    public ShoppingListResource findShoppingList(@PathVariable(name = "shoppingListId") UUID shoppingListId) {
        return getShoppingList(shoppingListId);
    }

    @GetMapping("/shopping-lists")
    public List<ShoppingListResource> getShoppingLists() {
        return getAllShoppingList();
    }

    private ShoppingListResource newShoppingList(CreateShoppingList shoppingListInput) {
        return new ShoppingListResource(
                UUID.randomUUID(),
                shoppingListInput.getName()
        );
    }

    private ShoppingListResource getShoppingList(UUID shoppingListId) {
        return new ShoppingListResource(
                UUID.fromString(shoppingListId.toString()), "My shop list",
                Arrays.asList("Tequila", "Triple sec", "Lime juice", "Salt"));
    }

    private List<CocktailReference> addCocktailReferencesToShoppingList(UUID shoppingListId,
            List<CocktailReference> cocktailReferences) {
        CocktailReference addedCocktail = new CocktailReference();
        addedCocktail.setCocktailId(cocktailReferences.get(0).getCocktailId());
        return Arrays.asList(addedCocktail);
    }

    private List<ShoppingListResource> getAllShoppingList() {
        return Arrays.asList(
                getShoppingList(UUID.randomUUID()),
                getShoppingList(UUID.randomUUID()),
                getShoppingList(UUID.randomUUID())
        );
    }

}
