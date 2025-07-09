package com.brews.cafeorderingapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.brews.cafeorderingapp.model.Cart;
import com.brews.cafeorderingapp.model.FavouriteDish;

import java.util.List;

@Dao
public interface CartDAO {

    @Insert
    void addItemToCart(Cart cart);

    @Delete
    void removeItemFromCart(Cart cart);

    @Query("UPDATE cart_table SET cartItemCount=:count WHERE cartItemName=:itemName")
    void updateCartItem(int count, String itemName);

    @Query("SELECT * FROM cart_table")
    LiveData<List<Cart>> getAllCartItems();

    @Insert
    void insertFavouriteDish(FavouriteDish favouriteDish);

    @Delete
    void deleteFavouriteDish(FavouriteDish favouriteDish);

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_dishes WHERE dishId = :dishId)")
    LiveData<Integer> isDishFavourite(int dishId);

    @Query("SELECT * FROM favourite_dishes")
    LiveData<List<FavouriteDish>> getAllFavouriteDishes();
}
