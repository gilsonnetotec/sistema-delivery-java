package com.delivery.system.products.application.deliveryproducts.rules;

import com.delivery.system.products.application.deliveryproducts.dto.CategoryDTO;
import com.delivery.system.products.application.deliveryproducts.repositories.CategoryRepository;
import com.delivery.system.products.application.deliveryproducts.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryRule {
    private final CategoryRepository repository;
    private Boolean valid = true;

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

    public Boolean biuld(){
        return valid;
    }


    public class Handler{
        @Autowired
        private CategoryRepository repository;


        protected Handler(CategoryDTO dto, CategoryRepository repository) {
            this.repository = repository;
            checkNomeIsNull(dto);
            checkNomeExist(dto);
        }

       protected void checkNomeIsNull(CategoryDTO dto){
            if(dto.getName() == null || dto.getName() == ""){
                valid = false;
                throw new ResourceNotFoundException("Nome não pode ser nullo ou vazio");
            }
        }

        protected void checkNomeExist(CategoryDTO dto){
            boolean result = repository.existsByName(dto.getName());
            if(result){
                valid = false;
                throw new ResourceNotFoundException("Este nome já existe na tabela");
            }
        }
    }
}
