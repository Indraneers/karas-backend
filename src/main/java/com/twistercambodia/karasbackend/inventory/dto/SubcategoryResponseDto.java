package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Subcategory;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class SubcategoryResponseDto {
    private String id;
    private String name;
    private CategoryDto category;
    private List<ProductDto> products;
    private int productCount;

    public SubcategoryResponseDto() {}

    public SubcategoryResponseDto(Subcategory subcategory) {
        ModelMapper mapper = new ModelMapper();
        this.id = subcategory.getId();
        this.name = subcategory.getName();
        this.category = new CategoryDto(subcategory.getCategory());
        this.products = subcategory.getProducts().stream()
                .map((p) -> mapper.map(p, ProductDto.class))
                .collect(Collectors.toList());
        this.productCount = subcategory.getProductCount();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
