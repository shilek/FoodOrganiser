package com.app.foodorganiser.entity;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProductTable {

    public ProductTable(int id, String name, int protein, int carbohydrates, int fats, String code, @Nullable String pathPhoto) {
        this.id = id;
        this.name = name;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
        this.code = code;
        this.pathPhoto = pathPhoto;
    }

    private int id;
    private String name;
    private double protein;
    private double carbohydrates;
    private double fats;
    private String code;
    @Nullable
    private String pathPhoto;

    private static String validate(String product) {
        if(product.contains("{") && product.contains("}") && product.contains(":") && product.contains("=")) {
            return product.substring(product.indexOf("{")+1,product.indexOf("}"));
        }
        else
            return null;
    }

    private static String getVariableValue(String string) {
        return string.substring(string.indexOf("=")+1);
    }

    public static ProductTable toObject(String product) {
        String valid;
        if(( valid = validate(product)) == null) {
            return null;
        }
        String [] values = valid.split(":");
        try{
            if(values.length==7) {
                return new ProductTable(Integer.parseInt(getVariableValue(values[0])), getVariableValue(values[1]),
                        Integer.parseInt(getVariableValue(values[2])),Integer.parseInt(getVariableValue(values[3])),
                        Integer.parseInt(getVariableValue(values[4])), getVariableValue(values[5]), getVariableValue(values[6]));
            }
            else if(values.length==6) {
                return new ProductTable(Integer.parseInt(getVariableValue(values[0])), getVariableValue(values[1]),
                        Integer.parseInt(getVariableValue(values[2])),Integer.parseInt(getVariableValue(values[3])),
                        Integer.parseInt(getVariableValue(values[4])), getVariableValue(values[5]),null);
            }
        }
        catch (NumberFormatException e) {
            return null;
        }
        return null;
    }

    public static List<ProductTable> toObject(List<String> list)
    {
        List<ProductTable> productTable = new ArrayList<>();
        ProductTable temp;
        for(int i = 0; i < list.size(); i++)
        {
            temp = ProductTable.toObject(list.get(i));
            if(temp != null) {
                productTable.add(temp);
            }
        }
        return productTable;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public double getFats() {
        return fats;
    }

    public double getProtein() {
        return protein;
    }

    @Nullable
    public String getPathPhoto() {
        return pathPhoto;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPathPhoto(@Nullable String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    @Override
    public String toString() {
        return name + '\n' +
                "Proteins: " + protein +
                ", Carbohydrates: " + carbohydrates +
                ", Fats: " + fats;
    }
}
