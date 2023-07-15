package com.delivery.system.products.application.deliveryproducts.rules;

import com.delivery.system.products.application.deliveryproducts.dto.CategoryDTO;
import com.delivery.system.products.application.deliveryproducts.repositories.CategoryRepository;
import com.delivery.system.products.application.deliveryproducts.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryRule {
    private final CategoryRepository repository;

    @Autowired
    public CategoryRule(CategoryRepository repository) {
        this.repository = repository;
    }

    public Boolean saved(CategoryDTO dto){
        Handler handler = new Handler(dto, repository);
        return true;
    }

    public Boolean updated(CategoryDTO dto){
        Handler handler = new Handler(dto, repository);
        return true;
    }


    public class Handler{
        @Autowired
        private CategoryRepository repository;


        public Handler(CategoryDTO dto, CategoryRepository repository) {
            this.repository = repository;
            checkNomeIsNull(dto);
            checkNomeExist(dto);
        }

        private void checkNomeIsNull(CategoryDTO dto){
            if(dto.getName() == null || dto.getName() == ""){
                throw new ResourceNotFoundException("Nome não pode ser nullo ou vazio");
            }
        }

        private void checkNomeExist(CategoryDTO dto){
            boolean result = repository.existsByName(dto.getName());
            if(result){
                throw new ResourceNotFoundException("Este nome já existe na tabela");
            }
        }
    }
}
