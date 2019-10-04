package com.ezgroceries.shoppinglist.repository;

import com.ezgroceries.shoppinglist.model.CocktailDbResponse;

public interface CocktailDbClient {

    CocktailDbResponse searchCocktails(String search);

}
