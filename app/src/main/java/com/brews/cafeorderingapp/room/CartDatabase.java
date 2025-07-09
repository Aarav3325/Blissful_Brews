package com.brews.cafeorderingapp.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.brews.cafeorderingapp.model.Cart;
import com.brews.cafeorderingapp.model.FavouriteDish;

@Database(entities = {Cart.class, FavouriteDish.class}, version = 6)
public abstract class CartDatabase extends RoomDatabase {
    public static CartDatabase dbInstance;

    public static synchronized CartDatabase getDbInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = Room.databaseBuilder(context.getApplicationContext(), CartDatabase.class, "cart_dv").fallbackToDestructiveMigration().build();
        }

        return dbInstance;
    }

    public abstract CartDAO getCartDAO();
}
