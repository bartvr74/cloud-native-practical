package com.ezgroceries.shoppinglist.persistence.repository;

import com.ezgroceries.shoppinglist.persistence.entity.ShoppingListEntity;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingListRepository extends CrudRepository<ShoppingListEntity, UUID> {
}
