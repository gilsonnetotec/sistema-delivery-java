package com.delivery.system.products.application.deliveryproducts;

import com.delivery.system.products.application.deliveryproducts.dto.CategoryDTO;
import com.delivery.system.products.application.deliveryproducts.entities.Category;

public class Factory {

    public static Category createCategory(){
        Category category = new Category(1L, "Teste de Categoria");
        return category;
    }

    public static CategoryDTO createdCategoryDTO(){
        Category category = createCategory();
        return new CategoryDTO(category);
    }
}
