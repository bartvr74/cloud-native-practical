package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.model.CocktailDbResponse;
import com.ezgroceries.shoppinglist.model.CocktailDbResponse.DrinkResource;
import com.ezgroceries.shoppinglist.contract.CocktailResource;
import com.ezgroceries.shoppinglist.repository.CocktailDbClient;
import io.micrometer.core.instrument.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrewCocktailService {

    private CocktailDbClient cocktailDbClient;

    @Autowired
    public BrewCocktailService(CocktailDbClient cocktailDbClient) {
        this.cocktailDbClient = cocktailDbClient;
    }

    public List<CocktailResource> searchCocktails(String search) {
        CocktailDbResponse cocktailDbResponse = cocktailDbClient.searchCocktails(search);
        List<DrinkResource> drinkResources = cocktailDbResponse.getDrinks();
        List<CocktailResource> cocktailResources = new ArrayList<>();
        for(DrinkResource drinkResource : drinkResources) {
            CocktailResource cocktailResource = new CocktailResource();
            cocktailResource.setId(UUID.randomUUID());
            cocktailResource.setDescription(drinkResource.getStrDrink());
            cocktailResource.setInstruction(drinkResource.getStrInstructions());
            cocktailResource.setUrl(drinkResource.getStrDrinkThumb());
            cocktailResource.setTypeOfGlass(drinkResource.getStrGlass());

            // why did db cocktail not return a list of ordered ingredients :-) ? alt is reflection.
            List<String> ingredients = new ArrayList<>();
            addIngredient(ingredients, drinkResource.getStrIngredient1());
            addIngredient(ingredients, drinkResource.getStrIngredient2());
            addIngredient(ingredients, drinkResource.getStrIngredient3());
            addIngredient(ingredients, drinkResource.getStrIngredient4());
            addIngredient(ingredients, drinkResource.getStrIngredient5());
            addIngredient(ingredients, drinkResource.getStrIngredient6());
            addIngredient(ingredients, drinkResource.getStrIngredient7());
            addIngredient(ingredients, drinkResource.getStrIngredient8());
            addIngredient(ingredients, drinkResource.getStrIngredient9());
            addIngredient(ingredients, drinkResource.getStrIngredient10());
            addIngredient(ingredients, drinkResource.getStrIngredient11());
            addIngredient(ingredients, drinkResource.getStrIngredient12());
            addIngredient(ingredients, drinkResource.getStrIngredient13());
            addIngredient(ingredients, drinkResource.getStrIngredient14());
            addIngredient(ingredients, drinkResource.getStrIngredient15());
            cocktailResource.setIngredients(ingredients);

            cocktailResources.add(cocktailResource);
        }

        return cocktailResources;
    }

    private void addIngredient(List<String> ingredients, String ingredient) {
        if(StringUtils.isNotEmpty(ingredient)) {
            ingredients.add(ingredient);
        }
    }

}
