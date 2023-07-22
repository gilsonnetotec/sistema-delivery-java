package com.delivery.system.products.application.deliveryproducts.rules;

import com.delivery.system.products.application.deliveryproducts.dto.CategoryDTO;
import com.delivery.system.products.application.deliveryproducts.repositories.CategoryRepository;
import com.delivery.system.products.application.deliveryproducts.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryRule {
    private final CategoryRepository repository;
    private static Boolean valid = true;

    @Autowired
    public CategoryRule(CategoryRepository repository) {
        this.repository = repository;
    }

    public CategoryRule saved(CategoryDTO dto){
        Handler handler = new Handler(dto, repository);
        return this;
    }

    public CategoryRule updated(CategoryDTO dto){
        Handler handler = new Handler(dto, repository);
        return this;
    }

    public static Boolean build(){
        return getValid();
    }

    public static Boolean getValid() {
        return valid;
    }

    public static void setValid(Boolean valid) {
        CategoryRule.valid = valid;
    }

    public class Handler{
        @Autowired
        private CategoryRepository repository;

        protected Handler(CategoryDTO dto, CategoryRepository repository) {
            this.repository = repository;
            checkNomeIsNull(dto);
            checkNomeExist(dto);
        }

       protected Handler checkNomeIsNull(CategoryDTO dto){
            if(dto.getName() == null || dto.getName() == ""){
                setValid(false);
                throw new ResourceNotFoundException("Nome não pode ser nullo ou vazio");
            }
            return this;
        }

        protected Handler checkNomeExist(CategoryDTO dto){
            boolean result = repository.existsByName(dto.getName());
            if(result){
                setValid(false);
                throw new ResourceNotFoundException("Este nome já existe na tabela");
            }
            return this;
        }

        public static Boolean validHandler(){
            return getValid();
        }
    }
}
