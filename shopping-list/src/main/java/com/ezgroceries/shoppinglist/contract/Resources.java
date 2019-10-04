package com.ezgroceries.shoppinglist.contract;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resources<T> {

    private List<T> resources;

}
