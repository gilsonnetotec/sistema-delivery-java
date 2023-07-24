package com.delivery.system.products.application.deliveryproducts.rules;

import com.delivery.system.products.application.deliveryproducts.dto.ProductDTO;
import com.delivery.system.products.application.deliveryproducts.repositories.ProductRepository;
import com.delivery.system.products.application.deliveryproducts.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductRule {
    private final ProductRepository repository;
    private static Boolean valid = true;

    @Autowired
    public ProductRule(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductRule saved(ProductDTO dto){
        Handler handler = new Handler(dto, repository);
        return this;
    }

    public ProductRule updated(ProductDTO dto){
        Handler handler = new Handler(dto, repository);
        return this;
    }


    public static Boolean build(){
        return getValid();
    }

    public static Boolean getValid() {
        return valid;
    }

    public static void setValid(Boolean valid) { ProductRule.valid = valid; }

        public class Handler{
        @Autowired
        private ProductRepository repository;

        protected  Handler(ProductDTO dto, ProductRepository repository){
            this.repository = repository;
            checkNomeIsNull(dto);
            checkNomeExist(dto);
        }

        protected Handler checkNomeIsNull(ProductDTO dto){
            if(dto.getName() == null || dto.getName() == ""){
                setValid(false);
                throw new ResourceNotFoundException("Nome não pode ser nullo ou vazio");
            }
            return this;
        }

        protected Handler checkNomeExist(ProductDTO dto){
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
