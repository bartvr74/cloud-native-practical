package com.ezgroceries.shoppinglist.controller;

import com.ezgroceries.shoppinglist.dto.CocktailResource;
import com.ezgroceries.shoppinglist.dto.Resources;
import com.ezgroceries.shoppinglist.service.CocktailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cocktails", produces = "application/json")
public class CocktailController {

    private CocktailService brewCocktailService;

    @Autowired
    public CocktailController(CocktailService brewCocktailService) {
        this.brewCocktailService = brewCocktailService;
    }

    @GetMapping
    public Resources<CocktailResource> get(@RequestParam String search) {
        return new Resources<>(brewCocktailService.searchCocktails(search));
    }

}