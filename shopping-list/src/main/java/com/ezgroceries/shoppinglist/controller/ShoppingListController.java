package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.contract.CocktailReference;
import com.ezgroceries.shoppinglist.contract.CreateShoppingList;
import com.ezgroceries.shoppinglist.contract.ShoppingListResource;
import com.ezgroceries.shoppinglist.service.ShoppingListService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
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

    private ShoppingListService shoppingListService;

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @PostMapping("/shopping-lists")
    public ResponseEntity<ShoppingListResource> createShoppingList(@RequestBody CreateShoppingList createShoppingList) {
        ShoppingListResource newShoppingList = shoppingListService.create(createShoppingList.getName());
        return new ResponseEntity<>(newShoppingList, HttpStatus.CREATED);
    }

    @PostMapping("/shopping-lists/{shoppingListId}/cocktails")
    public ResponseEntity<List<CocktailReference>> addCocktailReferences(@PathVariable(name = "shoppingListId")
            UUID shoppingListId, @RequestBody List<CocktailReference> cocktailReferences) {
        List<CocktailReference> addedCocktailReferences =
                shoppingListService.addCocktails(shoppingListId, cocktailReferences);
        return new ResponseEntity<>(addedCocktailReferences, HttpStatus.OK);
    }

    @GetMapping("/shopping-lists/{shoppingListId}")
    public ShoppingListResource findShoppingList(@PathVariable(name = "shoppingListId") UUID shoppingListId) {
        return shoppingListService.get(shoppingListId);
    }

    @GetMapping("/shopping-lists")
    public List<ShoppingListResource> getShoppingLists() {
        return shoppingListService.lists();
    }

}
