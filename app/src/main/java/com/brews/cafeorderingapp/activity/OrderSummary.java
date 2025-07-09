package com.brews.cafeorderingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brews.cafeorderingapp.R;
import com.brews.cafeorderingapp.adapter.SummaryAdapter;
import com.brews.cafeorderingapp.model.Cart;
import com.brews.cafeorderingapp.model.FirebaseCart;
import com.brews.cafeorderingapp.model.FirebaseOrder;
import com.brews.cafeorderingapp.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class OrderSummary extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String firebaseUser = firebaseAuth.getCurrentUser().getUid();
    ArrayList<Cart> cartArrayList = new ArrayList<>();
    TextView orderID, orderTotal;
    ArrayList<FirebaseOrder> summaryArrayList;
    ArrayList<FirebaseOrder> temp;
    DatabaseReference reference;
    Double itemTotal, taxes;
    int platformFee;
    TextView tvItemTotal, tvTaxes, tvPlatformFee, tvPaid, tvOrderDate, tvPhoneNumber;
    private RecyclerView rvSummary;
    private SummaryAdapter summaryAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_summary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        orderID = findViewById(R.id.orderId);
        orderTotal = findViewById(R.id.orderTotal);
        Button homeRedirect = findViewById(R.id.homeRedirect);

//        itemTotal = getIntent().getDoubleExtra("itemTotal", 0);
//        taxes = getIntent().getDoubleExtra("taxes", 0);
//        platformFee = getIntent().getIntExtra("platformFee", 0);

        summaryArrayList = new ArrayList<>();
        temp = new ArrayList<>();

        tvItemTotal = findViewById(R.id.itemTotalPrice);
        tvTaxes = findViewById(R.id.taxes);
        tvPlatformFee = findViewById(R.id.platformFeeOrder);
        tvPaid = findViewById(R.id.paid);
        tvOrderDate = findViewById(R.id.orderDate);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);


        String uniqueKey = getIntent().getStringExtra("uniqueKey");

        String orderId = getIntent().getStringExtra("orderID");

        firebaseDatabase = FirebaseDatabase.getInstance();
        //reference = firebaseDatabase.getReference("Orders").child(firebaseUser).child(uniqueKey);
        databaseReference = firebaseDatabase.getReference("Orders").child(firebaseUser).child(orderId);
        DatabaseReference totalRef = firebaseDatabase.getReference().child("Orders").child(firebaseUser).child(orderId);

        ArrayList<FirebaseCart> firebaseCartArrayList = new ArrayList<>();

        totalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Order firebaseOrder = snapshot.getValue(Order.class);
                    assert firebaseOrder != null;

                        double total = firebaseOrder.getTotal();

                        ArrayList<FirebaseOrder> orderArrayList = firebaseOrder.getFirebaseOrders();

                        long itemTotal = 0;

                        for (FirebaseOrder order : orderArrayList){
                            itemTotal = itemTotal + order.getItemPrice();
                        }
                        int id = firebaseOrder.getOrderId();

                        orderID.setText("#" + id);

//                Integer a = total;
//                double d = a.doubleValue()* 500;

                        double cgst = (double) (itemTotal * 9) / 100;
                        double sgst = (double) (itemTotal * 9) / 100;
                        double totalGST = cgst + sgst;
                        int platformFee = 1;
                        double grandTotal = itemTotal + totalGST + platformFee;

                        tvItemTotal.setText("₹" + itemTotal);
                        tvTaxes.setText("₹" + totalGST);
                        tvPaid.setText("Paid Using : Cash (₹" + total + ")");
                        tvPlatformFee.setText("₹" + platformFee);
                        tvOrderDate.setText("" + new Date());
                        orderTotal.setText("₹" + grandTotal);
                    }


                //double total = snapshot.child("total").getValue(Double.class);


                //tvPhoneNumber.setText("" + phone);

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rvSummary = findViewById(R.id.rvSummary);
        rvSummary.setLayoutManager(new LinearLayoutManager(this));


        homeRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderSummary.this, NewMenu.class);
                intent.putExtra("uniqueKey", uniqueKey);
                startActivity(intent);
            }
        });


        //Fetch order details from Firebase RTDB
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                summaryArrayList.clear();

                    Order order = snapshot.getValue(Order.class);

                    assert order != null;
                   // if(order.getStatus() != 5){
                        ArrayList<FirebaseOrder> temp = order.getFirebaseOrders();

                        summaryArrayList.addAll(temp);
                    //}

                summaryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        summaryAdapter = new SummaryAdapter(summaryArrayList);
        rvSummary.setAdapter(summaryAdapter);


    }


}