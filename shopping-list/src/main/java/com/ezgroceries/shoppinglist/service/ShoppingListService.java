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
        Optional<ShoppingListEntity> shoppingListEntity = shoppingListRepository.findById(id);
        if(shoppingListEntity.isPresent()) {
            List<String> ingredients = getIngredients(shoppingListEntity.get());
            return new ShoppingListResourceResponse(
                    shoppingListEntity.get().getId(),
                    shoppingListEntity.get().getName(),
                    ingredients);
        }
        return null;
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
        List<CocktailReferenceDto> addedCocktailReferences = new ArrayList<>();
        Optional<ShoppingListEntity> shoppingList = shoppingListRepository.findById(listId);
        if(shoppingList.isPresent()) {
            if(cocktailRefs != null) {
                for(CocktailReferenceDto cocktailRef : cocktailRefs) {
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

                        CocktailReferenceDto addedCocktailReference = new CocktailReferenceDto();
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
        if(cocktails == null) {
            return null;
        } else {
            List<String> ingredients = shoppingListEntity.getCocktails()
                    .stream()
                    .filter(entity -> entity != null)
                    .flatMap(entity -> entity.getIngredients().stream())
                    .distinct()
                    .collect(Collectors.toList());
            return ingredients;
        }
    }

}
