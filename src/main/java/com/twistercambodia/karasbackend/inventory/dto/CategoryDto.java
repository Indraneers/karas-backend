package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Category;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class CategoryDto {
    private String id;
    private String name;
    private List<SubcategoryRequestDto> subcategories;
    private int subcategoryCount;
    private String img;
    private String color;

    public CategoryDto() {}

    public CategoryDto(Category category) {
        ModelMapper mapper = new ModelMapper();
        this.id = category.getId();;
        this.name = category.getName();
        this.subcategories =
                category.getSubcategories() != null
                ?
                category
                    .getSubcategories()
                    .stream()
                    .map(sc -> mapper.map(sc, SubcategoryRequestDto.class))
                    .toList()
                :
                new ArrayList<>();
        this.subcategoryCount = category.getSubcategoryCount();
        this.img = category.getImg();
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

    public List<SubcategoryRequestDto> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<SubcategoryRequestDto> subcategories) {
        this.subcategories = subcategories;
    }

    public int getSubcategoryCount() {
        return subcategoryCount;
    }

    public void setSubcategoryCount(int subcategoryCount) {
        this.subcategoryCount = subcategoryCount;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
