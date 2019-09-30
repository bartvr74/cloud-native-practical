package com.ezgroceries.shoppinglist.model;

import java.util.List;

public class Resources<T> {

    private List<T> resources;

    public Resources(List<T> resources) {
        this.resources = resources;
    }

    public List<T> getResources() {
        return resources;
    }

}
