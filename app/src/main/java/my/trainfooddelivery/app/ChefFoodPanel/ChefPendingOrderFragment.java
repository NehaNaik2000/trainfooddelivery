package my.trainfooddelivery.app.ChefFoodPanel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.trainfooddelivery.app.ChefPendingAdapter;
import my.trainfooddelivery.app.CustomerHomeAdapter;
import my.trainfooddelivery.app.Order;
import my.trainfooddelivery.app.R;
import my.trainfooddelivery.app.UpdateDishModel;
import my.trainfooddelivery.app.placedorder;

public class ChefPendingOrderFragment extends Fragment {
    private List<placedorder> orderList;
    List<placedorder> matchingOrderList = new ArrayList<>();
    List<placedorder> separateOrderList = new ArrayList<>();
    private ChefPendingAdapter adapter, separateAdapter;
    RecyclerView recyclerView, separateRecyclerView;
    private Button backButton; // Reference to the "Back" button
    private String rname,phonenumber;
    private DatabaseReference database, data; // Reference to the "Placed Orders" node in Firebase

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        View v = inflater.inflate(R.layout.fragment_chef_pendingorders, null);
        getActivity().setTitle("Pending orders");
        recyclerView = v.findViewById(R.id.card_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        separateRecyclerView = v.findViewById(R.id.recyclerView2);
        separateRecyclerView.setHasFixedSize(true);
        separateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList = new ArrayList<>();
        backButton = v.findViewById(R.id.back_button);

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        data = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users").child(userid);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Chef cheff = snapshot.getValue(Chef.class);
                assert cheff != null;
                rname = cheff.getRestaurant();
                phonenumber=cheff.getMobile();

                Log.d("onDataChange: ", "Failed to read value." + rname);
                database = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("PLACED ORDERS");

                Query orderQuery = database.child(rname);

                orderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Create a map to store dishes with the same ETA and train number
                        Map<String, List<placedorder>> dishMap = new HashMap<>();

                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            String userid =orderSnapshot.getKey();
                            String name = orderSnapshot.child("CustomerName").getValue(String.class);
                            String mobile = orderSnapshot.child("MobileNo").getValue(String.class);
                            String SeatNumber=orderSnapshot.child("SeatNumber").getValue(String.class);
                            String Coach=orderSnapshot.child("Coach").getValue(String.class);
                            String payment=orderSnapshot.child("Payment").getValue((String.class));
                            String trainNo = "";
                            String eta = "";
                            Boolean delivery=orderSnapshot.child("Deliveryperson").getValue((Boolean.class));


                            for (DataSnapshot dishSnapshot : orderSnapshot.getChildren()) {
                                if (!dishSnapshot.getKey().equals("CustomerName") && !dishSnapshot.getKey().equals("MobileNo")&&!dishSnapshot.getKey().equals("Coach") && !dishSnapshot.getKey().equals("SeatNumber")&&!dishSnapshot.getKey().equals("Payment")&&!dishSnapshot.getKey().equals("Deliveryperson")) {
                                    String dishName = dishSnapshot.getKey();
                                    String price = dishSnapshot.child("price").getValue(String.class);
                                    String quantity = dishSnapshot.child("quantity").getValue(String.class);
                                    trainNo = dishSnapshot.child("trainno").getValue(String.class);
                                    eta = dishSnapshot.child("eta").getValue(String.class);


                                    // Generate a key by concatenating ETA and train number
                                    String dishKey = eta + "_" + trainNo;

                                    // Check if the dish key already exists in the dish map
                                    if (dishMap.containsKey(dishKey)) {
                                        // Add the dish to the existing list with the same key
                                        List<placedorder> existingDishList = dishMap.get(dishKey);
                                        placedorder dish = new placedorder( dishName,  price, quantity, eta, name,mobile, trainNo,price,SeatNumber,Coach,rname,phonenumber,userid,payment,delivery);
                                        existingDishList.add(dish);
                                    } else {
                                        // Create a new list for the dish key and add the dish
                                        List<placedorder> newDishList = new ArrayList<>();
                                        placedorder dish = new  placedorder( dishName,  price, quantity, eta, name,mobile, trainNo,price,SeatNumber,Coach,rname,phonenumber,userid,payment,delivery);
                                        newDishList.add(dish);
                                        dishMap.put(dishKey, newDishList);
                                    }
                                }
                            }
                        }

                        // Iterate over the dish map entries to create grouped orders
                        for (Map.Entry<String, List<placedorder>> entry : dishMap.entrySet()) {
                            List<placedorder> existingDishList = entry.getValue();

                            // Create a single order object for each group
                            placedorder order = new placedorder();
                            order.setCustomerName(existingDishList.get(0).getCustomerName());
                            order.setMobileNo(existingDishList.get(0).getMobileNo());
                            order.setDishList(existingDishList);
                            order.setdishes(getDishNames(existingDishList));
                            double totalPrice = getTotalPrice(existingDishList);
                            order.setTotalPrice(String.valueOf(totalPrice));
                            order.settraino(existingDishList.get(0).gettrainno());
                            order.seteta(existingDishList.get(0).geteta());
                            order.setSeatNumber(existingDishList.get(0).getSeatNumber());
                            order.setCoach(existingDishList.get(0).getCoach());
                            order.setRestaurant(existingDishList.get(0).getRestaurant());
                            order.setPhone(existingDishList.get(0).getPhone());
                            order.setUserid(existingDishList.get(0).getUserid());
                            order.setPayment(existingDishList.get(0).getPayment());
                            order.setDeliveryperson(existingDishList.get(0).getDeliveryperson());

                            matchingOrderList.add(order);

                            Log.d("OrderDetails", "Customer Name: " + order.getCustomerName());
                            Log.d("OrderDetails", "Mobile No: " + order.getMobileNo());
                            Log.d("OrderDetails", "Dishes: " + order.getdishes());
                            Log.d("OrderDetails", "Train No: " + order.gettrainno());
                            Log.d("OrderDetails", "ETA: " + order.geteta());
                            Log.d("OrderDetails", "hotel: " + order.getRestaurant());
                            Log.d("OrderDetails", "phonel: " + order.getPhone());
                            Log.d("OrderDetails", "userid: " + order.getUserid());



                        }

                        // Set up the adapter and recyclerView for matchingOrderList
                        adapter = new ChefPendingAdapter(getActivity(), matchingOrderList);
                        recyclerView.setAdapter(adapter);

                        // Set up the adapter and recyclerView for separateOrderList
                        separateAdapter = new ChefPendingAdapter( getActivity(),separateOrderList);
                        separateRecyclerView.setAdapter(separateAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the cancellation/error event
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("onDataChange: ", "Failed to read value.", error.toException());
            }
        });

        return v;
    }

    private String getDishNames(List<placedorder> dishList) {
        StringBuilder dishNamesBuilder = new StringBuilder();

        for (placedorder dish : dishList) {
            String dishName = dish.getdishes();
            String price = dish.getprice();
            String quantity = dish.getquantity();
            dishNamesBuilder.append(dishName).append(" (Price: ").append(price).append(", Quantity: ")
                    .append(quantity)
                    .append(")").append(", ");
        }

        String dishNames = dishNamesBuilder.toString().trim();
        if (dishNames.endsWith(",")) {
            dishNames = dishNames.substring(0, dishNames.length() - 1);
        }

        return dishNames;
    }

    private double getTotalPrice(List<placedorder> orderList) {
        double totalPrice = 0;

        for (placedorder order : orderList) {
            for (placedorder dish : order.getDishList()) {
                double price = Double.parseDouble(dish.getprice());
                totalPrice += price;
            }
        }

        return totalPrice;
    }

}
