package com.delivery.system.products.application.deliveryproducts.dto;

import com.delivery.system.products.application.deliveryproducts.entities.Category;

import java.io.Serializable;

public class CategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    CategoryDTO(){}

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category category){
        this.id = category.getId();
        this.name = category.getName();
    }

    public Long getId() {
        return id;
    }//

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
