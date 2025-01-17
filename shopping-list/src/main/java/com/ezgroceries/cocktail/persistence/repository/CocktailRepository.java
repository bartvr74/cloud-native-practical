package com.ezgroceries.cocktail.persistence.repository;

import com.ezgroceries.cocktail.persistence.entity.CocktailEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CocktailRepository extends CrudRepository<CocktailEntity, UUID> {

    @Query(value = "SELECT c FROM CocktailEntity c WHERE c.idDrink IN :ids")
    List<CocktailEntity> findByIdDrinkIn(@Param("ids") List<String> ids);

    List<CocktailEntity> findByNameContainingIgnoreCase(String search);

}
