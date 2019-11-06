package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.dto.CocktailReferenceDto;
import com.ezgroceries.shoppinglist.dto.ShoppingListResourceResponse;
import com.ezgroceries.cocktail.persistence.entity.CocktailEntity;
import com.ezgroceries.cocktail.persistence.repository.CocktailRepository;
import com.ezgroceries.shoppinglist.persistence.entity.ShoppingListEntity;
import com.ezgroceries.shoppinglist.persistence.repository.ShoppingListRepository;
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

    public ShoppingListResourceResponse create(String listName) {
        ShoppingListEntity shoppingListEntity = new ShoppingListEntity();
        shoppingListEntity.setName(listName);
        ShoppingListEntity newShoppingListEntity = shoppingListRepository.save(shoppingListEntity);

        return new ShoppingListResourceResponse(
                newShoppingListEntity.getId(),
                newShoppingListEntity.getName()
        );
    }

    public ShoppingListResourceResponse get(UUID id) {
        ShoppingListResourceResponse shoppingListResourceResponse = null;
        Optional<ShoppingListEntity> shoppingListEntity = shoppingListRepository.findById(id);
        if(shoppingListEntity.isPresent()) {
            List<String> ingredients = getIngredients(shoppingListEntity.get());
            shoppingListResourceResponse = new ShoppingListResourceResponse(
                    shoppingListEntity.get().getId(),
                    shoppingListEntity.get().getName(),
                    ingredients);
        }

        return shoppingListResourceResponse;
    }

    public List<ShoppingListResourceResponse> getAll() {
        Iterable<ShoppingListEntity> shoppingLists = shoppingListRepository.findAll();
        List<ShoppingListResourceResponse> lists = new ArrayList<>();

        for(ShoppingListEntity shoppingList : shoppingLists) {
            List<String> ingredients = getIngredients(shoppingList);
            ShoppingListResourceResponse shoppingListResource =
                new ShoppingListResourceResponse(
                            shoppingList.getId(),
                            shoppingList.getName(),
                            ingredients);
            lists.add(shoppingListResource);
        }

        return lists;
    }

    public List<CocktailReferenceDto> addCocktails(UUID listId, List<CocktailReferenceDto> cocktailRefs) {
        List<CocktailReferenceDto> addedCocktailRefs = new ArrayList<>();
        if(cocktailRefs == null || cocktailRefs.isEmpty())
            return addedCocktailRefs;

        Optional<ShoppingListEntity> shoppingList = shoppingListRepository.findById(listId);
        if(shoppingList.isPresent()) {
            ShoppingListEntity shoppingListEntity = shoppingList.get();
            for(CocktailReferenceDto cocktailRef : cocktailRefs)
                addCocktailToShoppingListEntity(cocktailRef, shoppingListEntity, addedCocktailRefs);
            shoppingListRepository.save(shoppingListEntity);
        }

        return addedCocktailRefs;
    }

    private List<String> getIngredients(ShoppingListEntity shoppingListEntity) {
        List<String> foundIngredients = null;
        Set<CocktailEntity> cocktails = shoppingListEntity.getCocktails();
        if(cocktails != null && !cocktails.isEmpty()) {
            foundIngredients = shoppingListEntity.getCocktails()
                    .stream()
                    .filter(entity -> entity != null)
                    .flatMap(entity -> entity.getIngredients().stream())
                    .distinct()
                    .collect(Collectors.toList());
        }

        return foundIngredients;
    }

    private void addCocktailToShoppingListEntity(CocktailReferenceDto cocktailRef,
            ShoppingListEntity shoppingListEntity, List<CocktailReferenceDto> addedCocktailRefs) {
        // valid cocktail reference ?
        Optional<CocktailEntity> cocktail = cocktailRepository.findById(cocktailRef.getCocktailId());
        if(cocktail.isPresent()) {
            Set<CocktailEntity> cocktails = shoppingListEntity.getCocktails();
            if(cocktails == null) {
                cocktails = new HashSet<>();
            }
            cocktails.add(cocktail.get());
            shoppingListEntity.setCocktails(cocktails);
            addedCocktailRefs.add(cocktailRef);
        }
    }

}
