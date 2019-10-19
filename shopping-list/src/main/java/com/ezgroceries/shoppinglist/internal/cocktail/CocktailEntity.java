package com.ezgroceries.shoppinglist.internal.cocktail;

import com.ezgroceries.shoppinglist.internal.shoppinglist.ShoppingListEntity;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "COCKTAIL")
@Data
public class CocktailEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private UUID id;

    @Column(name = "ID_DRINK")
    private String idDrink;

    @Column(name = "NAME")
    private String name;

    @Convert(converter = StringSetConverter.class)
    @Column(name = "INGREDIENTS")
    private Set<String> ingredients;

    @ManyToMany(mappedBy = "cocktails")
    private Set<ShoppingListEntity> shoppingLists;
}
