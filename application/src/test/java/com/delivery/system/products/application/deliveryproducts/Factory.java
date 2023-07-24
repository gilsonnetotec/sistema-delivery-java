package com.delivery.system.products.application.deliveryproducts;

import com.delivery.system.products.application.deliveryproducts.dto.CategoryDTO;
import com.delivery.system.products.application.deliveryproducts.dto.ProductDTO;
import com.delivery.system.products.application.deliveryproducts.entities.Category;
import com.delivery.system.products.application.deliveryproducts.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class Factory {

    public static Category createCategory(){
        Category category = new Category(1L, "Teste de Categoria");
        return category;
    }

    public static CategoryDTO createdCategoryDTO(){
        Category category = createCategory();
        return new CategoryDTO(category);
    }

    public static Product createProduct(){
        Product product = new Product(1L, "Teste de Produto",null,null,null);
        return product;
    }

    public static ProductDTO createdProductDTO(){
        Product product = createProduct();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        categoryDTOList.add(createdCategoryDTO());
        ProductDTO productDTO = new ProductDTO(product);
        productDTO.setCategories(categoryDTOList);
        return productDTO;
    }
}
