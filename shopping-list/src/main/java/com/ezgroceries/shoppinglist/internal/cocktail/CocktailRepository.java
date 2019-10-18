package com.ezgroceries.shoppinglist.internal.cocktail;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  CocktailRepository extends CrudRepository<CocktailEntity, String> {
}
