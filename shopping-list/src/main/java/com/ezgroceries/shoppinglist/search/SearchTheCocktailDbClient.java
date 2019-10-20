package com.ezgroceries.shoppinglist.search;

import com.ezgroceries.shoppinglist.internal.cocktail.CocktailEntity;
import com.ezgroceries.shoppinglist.internal.cocktail.CocktailRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "cocktailDBClient", url = "https://www.thecocktaildb.com/api/json/v1/1",
        fallback = SearchTheCocktailDbClient.CocktailDBClientFallback.class)
public interface SearchTheCocktailDbClient extends SearchCocktailDbClient {

    @GetMapping(value = "search.php")
    SearchCocktailDbResponse searchCocktails(@RequestParam("s") String search);

    @Component
    class CocktailDBClientFallback implements SearchTheCocktailDbClient {

        private final CocktailRepository cocktailRepository;

        public CocktailDBClientFallback(CocktailRepository cocktailRepository) {
            this.cocktailRepository = cocktailRepository;
        }

        @Override
        public SearchCocktailDbResponse searchCocktails(String search) {
            List<CocktailEntity> cocktailEntities = cocktailRepository.findByNameContainingIgnoreCase(search);

            SearchCocktailDbResponse cocktailDBResponse = new SearchCocktailDbResponse();
            cocktailDBResponse.setDrinks(cocktailEntities.stream().map(cocktailEntity -> {
                SearchCocktailDbResponse.DrinkResource drinkResource = new SearchCocktailDbResponse.DrinkResource();
                drinkResource.setIdDrink(cocktailEntity.getIdDrink());
                drinkResource.setStrDrink(cocktailEntity.getName());
                drinkResource.setStrDrinkThumb(cocktailEntity.getThumb());
                drinkResource.setStrGlass(cocktailEntity.getGlass());
                drinkResource.setStrInstructions(cocktailEntity.getInstructions());
                IngredientUtils.addIngredientsTo(drinkResource, cocktailEntity.getIngredients());

                return drinkResource;
            }).collect(Collectors.toList()));

            return cocktailDBResponse;
        }

    }

}
