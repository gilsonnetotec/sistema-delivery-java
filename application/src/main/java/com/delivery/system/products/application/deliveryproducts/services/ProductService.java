package com.delivery.system.products.application.deliveryproducts.services;

import com.delivery.system.products.application.deliveryproducts.dto.ProductDTO;
import com.delivery.system.products.application.deliveryproducts.entities.Category;
import com.delivery.system.products.application.deliveryproducts.entities.Product;
import com.delivery.system.products.application.deliveryproducts.repositories.CategoryRepository;
import com.delivery.system.products.application.deliveryproducts.repositories.ProductRepository;
import com.delivery.system.products.application.deliveryproducts.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(PageRequest pageRequest, String categoryIds){
        Set<Category> categories = convertCategoryIdsToSet(categoryIds);
        Page<Product> page = repository.findByCategoriesIn(categories,pageRequest);
        return page.map(product -> new ProductDTO(product));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Optional<Product> obj = repository.findById(id);
        Product  entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada"));
        return new ProductDTO(entity, entity.getCategories());
    }

    private Set<Category> convertCategoryIdsToSet(String categoryIds) {
        Set<Category> categories = new HashSet<>();
        String[] categoryIdArray = categoryIds.split(",");

        for (String categoryId : categoryIdArray) {
            Long id = Long.parseLong(categoryId.trim());
            categoryRepository.findById(id).ifPresent(categories::add);
        }

        return categories;
    }

}
