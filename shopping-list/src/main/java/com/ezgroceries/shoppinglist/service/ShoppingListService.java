package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.dto.CocktailReference;
import com.ezgroceries.shoppinglist.dto.ShoppingListResource;
import com.ezgroceries.shoppinglist.internal.shoppinglist.ShoppingListEntity;
import com.ezgroceries.shoppinglist.internal.shoppinglist.ShoppingListRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingListService {

    private ShoppingListRepository shoppingListRepository;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
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
            return new ShoppingListResource(
                    shoppingListEntity.get().getId(),
                    shoppingListEntity.get().getName(),
                    null); // todo
        }
        return null;
    }

    public List<ShoppingListResource> getAll() {
        Iterable<ShoppingListEntity> shoppingLists = shoppingListRepository.findAll();
        List<ShoppingListResource> lists = new ArrayList<>();
        for(ShoppingListEntity shoppingList : shoppingLists) {
            ShoppingListResource shoppingListResource =
                    new ShoppingListResource(
                            shoppingList.getId(),
                            shoppingList.getName(),
                            null); // todo
            lists.add(shoppingListResource);

        }

        return lists;
    }

    public List<CocktailReference> addCocktails(UUID listId, List<CocktailReference> cocktailIds) {
        CocktailReference addedCocktailReference = new CocktailReference();
        addedCocktailReference.setCocktailId(cocktailIds.get(0).getCocktailId());
        return Arrays.asList(addedCocktailReference);
    }



}
