package com.ezgroceries.shoppinglist.internal.shoppinglist;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingListRepository extends CrudRepository<ShoppingListEntity, UUID> {
}
