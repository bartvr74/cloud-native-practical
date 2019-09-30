package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.input.CreateShoppingListInput;
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

    @GetMapping("/shopping-lists/{shoppingListId}")
    public ShoppingListResource get(@PathVariable(name = "shoppingListId") UUID shoppingListId) {
        return getShoppingList(shoppingListId);
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

    private ShoppingListResource getShoppingList(UUID shoppingListId) {
        return new ShoppingListResource(
                        UUID.fromString("23b3d85a-3928-41c0-a533-6538a71e17c4"), "My shop list",
                        Arrays.asList("Tequila", "Triple sec", "Lime juice", "Salt"));
    }

}
