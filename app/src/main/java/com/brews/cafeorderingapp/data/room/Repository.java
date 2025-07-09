package com.brews.cafeorderingapp.room;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.brews.cafeorderingapp.model.Cart;
import com.brews.cafeorderingapp.model.FavouriteDish;
import com.brews.cafeorderingapp.model.ItemModel;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final CartDAO cartDAO;
    public MutableLiveData<List<ItemModel>> itemModelMutableLiveData = new MutableLiveData<>();
    ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
    ArrayList<ItemModel> cartArrayList = new ArrayList<>();
    MutableLiveData<List<ItemModel>> cartMutableLiveData = new MutableLiveData<>();
    ExecutorService executorService;
    Handler handler;
    DatabaseReference databaseReference;

    public Repository(Application application) {
        CartDatabase cartDatabase = CartDatabase.getDbInstance(application);
        this.cartDAO = cartDatabase.getCartDAO();


        databaseReference = FirebaseDatabase.getInstance().getReference("newmenu");

        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

//    public MutableLiveData<List<ItemModel>> getAllItems(){
//        itemModelMutableLiveData.setValue(itemModelArrayList);
//        return itemModelMutableLiveData;
//    }


    public void insertFavouriteDish(FavouriteDish favouriteDish) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.insertFavouriteDish(favouriteDish);
            }
        });
    }

    public void deleteFavouriteDish(FavouriteDish favouriteDish) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.deleteFavouriteDish(favouriteDish);
            }
        });
    }

    public LiveData<Boolean> isDishFavourite(int dishId) {
        return Transformations.map(
                cartDAO.isDishFavourite(dishId),
                value -> value != null && value == 1
        );
    }

    public LiveData<List<FavouriteDish>> getAllFavouriteDishes(){
        return cartDAO.getAllFavouriteDishes();
    }

    public void addItem(Cart cart) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.addItemToCart(cart);
            }
        });
    }

    public void deleteItem(Cart cart) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                cartDAO.removeItemFromCart(cart);
            }
        });
    }

    public void updateItem(int count, String itemName) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.updateCartItem(count, itemName);
            }
        });
    }

    public LiveData<List<Cart>> getCartItems() {
        return cartDAO.getAllCartItems();
    }

    public void fetchMenu(ValueEventListener listener){
        databaseReference.addValueEventListener(listener);
    }
}
