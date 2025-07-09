package com.brews.cafeorderingapp.model;

import java.util.ArrayList;

public class Order {
    int orderId;
    double total;
    String timestamp;


    int status;
    // 0 - not accepted
    // 1 - accepted
    // 2 - being prepared
    // 3 - ready to serve/ ready for pick up
    // 4 - order completed

    ArrayList<FirebaseOrder> firebaseOrders;

    public Order(int orderId, double total, ArrayList<FirebaseOrder> firebaseOrders, String timestamp) {
        this.orderId = orderId;
        this.total = total;
        this.firebaseOrders = firebaseOrders;
        this.timestamp = timestamp;
        this.status = 0;
    }

    public Order() {

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<FirebaseOrder> getFirebaseOrders() {
        return firebaseOrders;
    }

    public void setFirebaseOrders(ArrayList<FirebaseOrder> firebaseOrders) {
        this.firebaseOrders = firebaseOrders;
    }
}

