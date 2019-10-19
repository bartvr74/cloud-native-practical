package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.dto.CocktailReference;
import com.ezgroceries.shoppinglist.dto.ShoppingListResource;
import com.ezgroceries.shoppinglist.internal.cocktail.CocktailEntity;
import com.ezgroceries.shoppinglist.internal.cocktail.CocktailRepository;
import com.ezgroceries.shoppinglist.internal.shoppinglist.ShoppingListEntity;
import com.ezgroceries.shoppinglist.internal.shoppinglist.ShoppingListRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingListService {

    private ShoppingListRepository shoppingListRepository;
    private CocktailRepository cocktailRepository;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository,
            CocktailRepository cocktailRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.cocktailRepository = cocktailRepository;
    }

    public ShoppingListResource create(String listName) {
        ShoppingListEntity shoppingListEntity = new ShoppingListEntity();
        shoppingListEntity.setName(listName);
        ShoppingListEntity newShoppingListEntity = shoppingListRepository.save(shoppingListEntity);

        return new ShoppingListResource(
                newShoppingListEntity.getId(),
                newShoppingListEntity.getName()
        );
    }

    public ShoppingListResource get(UUID id) {
        Optional<ShoppingListEntity> shoppingListEntity = shoppingListRepository.findById(id);
        if(shoppingListEntity.isPresent()) {
            List<String> ingredients = getIngredients(shoppingListEntity.get());
            return new ShoppingListResource(
                    shoppingListEntity.get().getId(),
                    shoppingListEntity.get().getName(),
                    ingredients);
        }
        return null;
    }

    public List<ShoppingListResource> getAll() {
        Iterable<ShoppingListEntity> shoppingLists = shoppingListRepository.findAll();
        List<ShoppingListResource> lists = new ArrayList<>();
        for(ShoppingListEntity shoppingList : shoppingLists) {
            List<String> ingredients = getIngredients(shoppingList);
            ShoppingListResource shoppingListResource =
                new ShoppingListResource(
                            shoppingList.getId(),
                            shoppingList.getName(),
                            ingredients);
            lists.add(shoppingListResource);
        }

        return lists;
    }

    public List<CocktailReference> addCocktails(UUID listId, List<CocktailReference> cocktailRefs) {
        List<CocktailReference> addedCocktailReferences = new ArrayList<>();
        Optional<ShoppingListEntity> shoppingList = shoppingListRepository.findById(listId);
        if(shoppingList.isPresent()) {
            if(cocktailRefs != null) {
                for(CocktailReference cocktailRef : cocktailRefs) {
                    Optional<CocktailEntity> cocktail = cocktailRepository.findById(cocktailRef.getCocktailId());
                    if(cocktail.isPresent()) {
                        ShoppingListEntity shoppingListEntity = shoppingList.get();
                        Set<CocktailEntity> cocktails = shoppingListEntity.getCocktails();
                        if(cocktails == null) {
                            cocktails = new HashSet<>();
                        }
                        cocktails.add(cocktail.get());
                        shoppingListEntity.setCocktails(cocktails);
                        shoppingListRepository.save(shoppingListEntity);

                        CocktailReference addedCocktailReference = new CocktailReference();
                        addedCocktailReference.setCocktailId(cocktail.get().getId());
                        addedCocktailReferences.add(addedCocktailReference);
                    }
                }
            }
        }

        return addedCocktailReferences;
    }

    private List<String> getIngredients(ShoppingListEntity shoppingListEntity) {
        Set<CocktailEntity> cocktails = shoppingListEntity.getCocktails();
        if(cocktails != null) {
            List<String> ingredients = shoppingListEntity.getCocktails().stream()
                .filter(entity -> entity != null)
                .flatMap(entity->entity.getIngredients().stream())
                .distinct()
                .collect(Collectors.toList());
            return ingredients;
        }
        return null;
    }

}
