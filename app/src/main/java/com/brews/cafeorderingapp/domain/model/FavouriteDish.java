package com.brews.cafeorderingapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_dishes")
public class FavouriteDish {
    String dishName;
    String dishImage;
    int dishPrice;
    String dishDescription;
    @PrimaryKey(autoGenerate = true)
    int dishId;

    public FavouriteDish(String dishName, String dishImage, int dishPrice, String dishDescription, int dishId) {
        this.dishName = dishName;
        this.dishImage = dishImage;
        this.dishPrice = dishPrice;
        this.dishDescription = dishDescription;
        this.dishId = dishId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public FavouriteDish() {
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishImage() {
        return dishImage;
    }

    public void setDishImage(String dishImage) {
        this.dishImage = dishImage;
    }

    public int getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(int dishPrice) {
        this.dishPrice = dishPrice;
    }


    public String getDishDescription() {
        return dishDescription;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }
}
