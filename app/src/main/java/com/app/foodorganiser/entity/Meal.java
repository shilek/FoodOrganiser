package com.app.foodorganiser.entity;

import com.app.foodorganiser.MainActivity;

public class Meal {
    String mealName, mealMainIngr, mealDescription, productsIDs;

    public Meal() {}

    public Meal(String mealName, String mealMainIngr, String mealDescription) {
        this.mealName = mealName;
        this.mealMainIngr = mealMainIngr;
        this.mealDescription = mealDescription;
    }

    public Meal(String mealName, String mealMainIngr, String mealDescription, String products) {
        this.mealName = mealName;
        this.mealMainIngr = mealMainIngr;
        this.mealDescription = mealDescription;
        this.productsIDs = products;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealMainIngr() {
        return mealMainIngr;
    }

    public void setMealMainIngr(String mealMainIngr) {
        this.mealMainIngr = mealMainIngr;
    }

    public String getMealDescription() {
        return mealDescription;
    }

    public void setMealDescription(String mealDescription) {
        this.mealDescription = mealDescription;
    }

    public String[] getProductsIDs() {
        String[] prodIDs = null;
        if(productsIDs != null) prodIDs = productsIDs.split(",");
        return prodIDs;
    }

    public void setProductsIDs(String productsIDs) {
        this.productsIDs = productsIDs;
    }

    @Override
    public String toString() {
        int proteins = 0, carbos = 0, fats = 0;

        if(productsIDs != null){
            String[] prodIDs = productsIDs.split(",");
            for(String id : prodIDs){
                for (ProductTable p : MainActivity.allProductsList) {
                    if(p.getId() == Integer.parseInt(id)){
                        proteins += p.getProtein();
                        carbos += p.getCarbohydrates();
                        fats += p.getFats();
                    }
                }
            }
        }

        return mealName + "\n" + mealMainIngr + "\nProteins: "+ proteins + "  |  Carbos: " + carbos + "  |  Fats: " + fats;
    }
}
