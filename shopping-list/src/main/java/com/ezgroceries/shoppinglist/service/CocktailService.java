package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.dto.CocktailResource;
import com.ezgroceries.shoppinglist.internal.cocktail.CocktailEntity;
import com.ezgroceries.shoppinglist.internal.cocktail.CocktailRepository;
import com.ezgroceries.shoppinglist.search.IngredientUtils;
import com.ezgroceries.shoppinglist.search.SearchCocktailDbClient;
import com.ezgroceries.shoppinglist.search.SearchCocktailDbResponse;
import com.ezgroceries.shoppinglist.search.SearchCocktailDbResponse.DrinkResource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CocktailService {

    private SearchCocktailDbClient searchClient;
    private CocktailRepository cocktailRepository;

    @Autowired
    public CocktailService(@Qualifier("defaultSearch") SearchCocktailDbClient searchClient, CocktailRepository cocktailRepository) {
        this.searchClient = searchClient;
        this.cocktailRepository = cocktailRepository;
    }

    public List<CocktailResource> searchCocktails(String search) {
        SearchCocktailDbResponse cocktailDbResponse = searchClient.searchCocktails(search);
        List<DrinkResource> drinkResources = cocktailDbResponse.getDrinks();
        if(drinkResources != null && !drinkResources.isEmpty())
            return mergeCocktails(drinkResources);
        else
            return new ArrayList<>();
    }

    public List<CocktailResource> mergeCocktails(List<DrinkResource> drinks) {
        //Get all the idDrink attributes
        List<String> ids = drinks.stream().map(DrinkResource::getIdDrink).collect(Collectors.toList());

        //Get all the ones we already have from our DB, use a Map for convenient lookup
        Map<String, CocktailEntity> existingEntityMap = cocktailRepository.findByIdDrinkIn(ids).stream().collect(Collectors.toMap(CocktailEntity::getIdDrink, o -> o, (o, o2) -> o));

        //Stream over all the drinks, map them to the existing ones, persist a new one if not existing
        Map<String, CocktailEntity> allEntityMap = drinks.stream().map(drinkResource -> {
            CocktailEntity cocktailEntity = existingEntityMap.get(drinkResource.getIdDrink());
            if (cocktailEntity == null) {
                CocktailEntity newCocktailEntity = new CocktailEntity();
                newCocktailEntity.setIdDrink(drinkResource.getIdDrink());
                newCocktailEntity.setName(drinkResource.getStrDrink());
                newCocktailEntity.setIngredients(new HashSet<>(IngredientUtils.getIngredientsFrom(drinkResource)));
                newCocktailEntity.setThumb(drinkResource.getStrDrinkThumb());
                newCocktailEntity.setGlass(drinkResource.getStrGlass());
                newCocktailEntity.setInstructions(drinkResource.getStrInstructions());
                cocktailEntity = cocktailRepository.save(newCocktailEntity);
            }
            return cocktailEntity;
        }).collect(Collectors.toMap(CocktailEntity::getIdDrink, o -> o, (o, o2) -> o));

        //Merge drinks and our entities, transform to CocktailResource instances
        return mergeAndTransform(drinks, allEntityMap);
    }

    private List<CocktailResource> mergeAndTransform(List<DrinkResource> drinks,
            Map<String, CocktailEntity> allEntityMap) {
        return drinks.stream()
                .map(drinkResource -> new CocktailResource(allEntityMap.get(drinkResource.getIdDrink()).getId(),
                        drinkResource.getStrDrink(), drinkResource.getStrGlass(), drinkResource.getStrInstructions(),
                        drinkResource.getStrDrinkThumb(), IngredientUtils.getIngredientsFrom(drinkResource)))
                .collect(Collectors.toList());
    }

}
