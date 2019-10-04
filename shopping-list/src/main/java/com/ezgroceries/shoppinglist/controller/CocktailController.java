package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.contract.CocktailResource;
import com.ezgroceries.shoppinglist.contract.Resources;
import com.ezgroceries.shoppinglist.service.BrewCocktailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cocktails", produces = "application/json")
public class CocktailController {

    private BrewCocktailService brewCocktailService;

    @Autowired
    public CocktailController(BrewCocktailService brewCocktailService) {
        this.brewCocktailService = brewCocktailService;
    }

    @GetMapping
    public Resources<CocktailResource> get(@RequestParam String search) {
        return new Resources<>(brewCocktailService.searchCocktails(search));
    }

}