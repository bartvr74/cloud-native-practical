package com.ezgroceries.cocktail.persistence.entity;

import com.ezgroceries.shoppinglist.persistence.entity.ShoppingListEntity;
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
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "COCKTAIL")
@Data
@EqualsAndHashCode(exclude="shoppingLists")
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

    @Column(name = "THUMB")
    private String thumb;

    @Column(name = "INSTRUCTIONS")
    private String instructions;

    @Column(name = "GLASS")
    private String glass;

    @ManyToMany(mappedBy = "cocktails")
    private Set<ShoppingListEntity> shoppingLists;
}
