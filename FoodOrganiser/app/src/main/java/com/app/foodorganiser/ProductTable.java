package com.app.foodorganiser;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import org.w3c.dom.Text;

@Entity(tableName = "Products")
public class ProductTable {

    public ProductTable(String name, int protein, int carbohydrates, int fats, String code,@Nullable String pathPhoto)
    {
        this.name = name;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
        this.code = code;
        this.pathPhoto = pathPhoto;

    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id",typeAffinity = ColumnInfo.INTEGER)
    private int id;

    @ColumnInfo(name = "Name",typeAffinity = ColumnInfo.TEXT)
    private String name;

    @ColumnInfo(name = "Protein",typeAffinity = ColumnInfo.INTEGER,defaultValue = "0")
    private int protein;

    @ColumnInfo(name = "Carbohydrates",typeAffinity = ColumnInfo.INTEGER, defaultValue = "0")
    private int carbohydrates;

    @ColumnInfo(name = "Fats",typeAffinity = ColumnInfo.INTEGER,defaultValue = "0")
    private int fats;

    // kod w postaci String
    @ColumnInfo(name = "Code",typeAffinity = ColumnInfo.TEXT)
    private String code;

    //przechowuje ściezke do pliku
    @Nullable
    @ColumnInfo(name = "Photo",typeAffinity = ColumnInfo.TEXT)
    private String pathPhoto;


    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public int getFats() {
        return fats;
    }

    public int getProtein() {
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
}
