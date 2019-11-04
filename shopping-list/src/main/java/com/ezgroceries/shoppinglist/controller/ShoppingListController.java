package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.dto.CocktailReferenceDto;
import com.ezgroceries.shoppinglist.dto.CreateShoppingListRequest;
import com.ezgroceries.shoppinglist.dto.ShoppingListResourceResponse;
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
@RequestMapping(value="/shopping-lists", produces = "application/json")
public class ShoppingListController {

    private ShoppingListService shoppingListService;

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @PostMapping
    public ResponseEntity<ShoppingListResourceResponse> createShoppingList(@RequestBody CreateShoppingListRequest createShoppingList) {
        ShoppingListResourceResponse newShoppingList = shoppingListService.create(createShoppingList.getName());
        return new ResponseEntity<>(newShoppingList, HttpStatus.CREATED);
    }

    @PostMapping("/{shoppingListId}/cocktails")
    public ResponseEntity<List<CocktailReferenceDto>> addCocktailReferences(@PathVariable(name = "shoppingListId")
            UUID shoppingListId, @RequestBody List<CocktailReferenceDto> cocktailReferences) {
        List<CocktailReferenceDto> addedCocktailReferences =
                shoppingListService.addCocktails(shoppingListId, cocktailReferences);
        return new ResponseEntity<>(addedCocktailReferences, HttpStatus.OK);
    }

    @GetMapping("/{shoppingListId}")
    public ShoppingListResourceResponse findShoppingList(@PathVariable(name = "shoppingListId") UUID shoppingListId) {
        return shoppingListService.get(shoppingListId);
    }

    @GetMapping
    public List<ShoppingListResourceResponse> getShoppingLists() {
        return shoppingListService.getAll();
    }

}
