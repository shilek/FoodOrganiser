package com.app.foodorganiser;

import androidx.annotation.Nullable;

public class Product {
    String name, code, pathPhoto;
    int protein, carbohydrates, fats;

    public Product(String name, int protein, int carbohydrates, int fats, String code, @Nullable String pathPhoto) {
        this.name = name;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
        this.code = code;
        this.pathPhoto = pathPhoto;
    }

}
