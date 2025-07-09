package com.brews.cafeorderingapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.brews.cafeorderingapp.model.Cart;
import com.brews.cafeorderingapp.model.FavouriteDish;
import com.brews.cafeorderingapp.model.ItemModel;
import com.brews.cafeorderingapp.model.MenuModel;
import com.brews.cafeorderingapp.room.Repository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyViewModel extends AndroidViewModel {

    Repository repository;

    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    private final MutableLiveData<Map<String, List<MenuModel>>> menuData = new MutableLiveData<>();

    public LiveData<Map<String, List<MenuModel>>> getMenuData(){
        return menuData;
    }

    public void loadMenuItems(){
        repository.fetchMenu(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, List<MenuModel>> menuMap = new HashMap<>();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String categoryName = dataSnapshot.getKey();
                    List<MenuModel> menuModels = new ArrayList<>();

                    for(DataSnapshot itemSnapshot : dataSnapshot.getChildren()){
                        MenuModel menuModel = itemSnapshot.getValue(MenuModel.class);
                        menuModels.add(menuModel);
                    }

                    menuMap.put(categoryName, menuModels);
                }
                menuData.setValue(menuMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MenuViewModel", "Database error: " + error.getMessage());
            }
        });
    }

    public void insertFavouriteDish(FavouriteDish favouriteDish) {
        repository.insertFavouriteDish(favouriteDish);
    }

    public void deleteFavouriteDish(FavouriteDish favouriteDish) {
        repository.deleteFavouriteDish(favouriteDish);
    }

    public LiveData<Boolean> isDishFavourite(int dishId) {
        return repository.isDishFavourite(dishId);
    }

    public LiveData<List<FavouriteDish>> getAllFavouriteDishes(){
        return repository.getAllFavouriteDishes();
    }
}
