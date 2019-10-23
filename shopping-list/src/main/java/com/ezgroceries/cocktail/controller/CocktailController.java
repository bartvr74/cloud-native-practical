package com.ezgroceries.cocktail.controller;

import com.ezgroceries.cocktail.dto.CocktailResourceResponse;
import com.ezgroceries.cocktail.dto.ResourcesResponse;
import com.ezgroceries.cocktail.service.internal.CocktailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cocktails", produces = "application/json")
public class CocktailController {

    private CocktailService cocktailService;

    @Autowired
    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @GetMapping
    public ResourcesResponse<CocktailResourceResponse> get(@RequestParam String search) {
        return new ResourcesResponse<>(cocktailService.searchCocktails(search));
    }

}