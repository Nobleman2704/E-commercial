package com.example.ecommercial.controller.converter;

import com.example.ecommercial.controller.dto.request.ProductCreateAndUpdateRequest;
import com.example.ecommercial.controller.dto.response.ProductGetResponse;
import com.example.ecommercial.dao.ProductCategoryDao;
import com.example.ecommercial.domain.entity.ProductCategoryEntity;
import com.example.ecommercial.domain.entity.ProductEntity;
import com.example.ecommercial.service.category.CategoryService;
import com.example.ecommercial.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductConverter {

    private final ModelMapper modelMapper;
    private final ProductCategoryDao categoryDao;

    public ProductEntity toProductEntity(ProductCreateAndUpdateRequest request){
        ProductEntity product = modelMapper.map(request, ProductEntity.class);
        ProductCategoryEntity category = categoryDao.findById(request.getCategoryId()).get();
        product.setCategories(category);
        return product;
    }

    public ProductGetResponse toProductGetDto(ProductEntity product) {
        return modelMapper.map(product, ProductGetResponse.class);
    }

    public List<ProductGetResponse> toProductGetDto(List<ProductEntity> product) {
        return modelMapper.map(product,
                new TypeToken<List<ProductGetResponse>>() {
                }.getType());
    }
}
