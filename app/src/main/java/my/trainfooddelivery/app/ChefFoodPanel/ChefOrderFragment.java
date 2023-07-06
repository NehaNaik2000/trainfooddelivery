package my.trainfooddelivery.app.ChefFoodPanel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import my.trainfooddelivery.app.ChefShippingAdapter;
import my.trainfooddelivery.app.DeliverypendingAdapter;
import my.trainfooddelivery.app.R;
import my.trainfooddelivery.app.ShippingAdapter;
import my.trainfooddelivery.app.deliveryFoodPanel.delivery;
import my.trainfooddelivery.app.placedorder;


public class ChefOrderFragment extends Fragment {

    private String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference databaseReference;
    private String name,delname,number,resarea;
    private List<placedorder> updateDishModelList;
    private ChefShippingAdapter adapter;
    private RecyclerView re;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chef_orders,null);
        getActivity().setTitle("Orders for shipping");
        re = v.findViewById(R.id.recyc);
        re.setHasFixedSize(true);
        re.setLayoutManager(new LinearLayoutManager(getContext()));
        updateDishModelList = new ArrayList<>();


        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Chef dev = snapshot.getValue(Chef.class);
                if (dev != null) {
                    name=dev.getRestaurant();
                    resarea=dev.getArea();

                    Log.d("mobile of del","del");



                } else {
                    Log.d("error", "no fetch");

                }
                DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("shipping").child(name);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                             delname=orderSnapshot.child("DeliveryName").getValue(String.class);
                            number=orderSnapshot.child("Deliverymobile").getValue(String.class);


                            if (orderSnapshot.child("order").exists()) {
                                DataSnapshot orderDataSnapshot = orderSnapshot.child("order");

                                // Retrieve the order details
                                placedorder models = new placedorder();
                                models.setCustomerName(orderDataSnapshot.child("customerName").getValue(String.class));
                                models.setdishes(orderDataSnapshot.child("dishes").getValue(String.class));
                                models.setTotalPrice(orderDataSnapshot.child("totalPrice").getValue(String.class));
                                models.seteta(orderDataSnapshot.child("eta").getValue(String.class));
                                models.setRestaurant(orderDataSnapshot.child("restaurant").getValue(String.class));
                                models.setMobileNo(orderDataSnapshot.child("mobileNo").getValue(String.class));
                                models.setSeatNumber(orderDataSnapshot.child("seatNumber").getValue(String.class));
                                models.settraino(orderDataSnapshot.child("trainno").getValue(String.class));
                                models.setCoach(orderDataSnapshot.child("coach").getValue(String.class));
                                models.setUserid(orderDataSnapshot.child("userid").getValue(String.class));
                                models.setPayment(orderDataSnapshot.child("payment").getValue(String.class));

                                updateDishModelList.add(models);


                            }
                        }

                        // After iterating over the data, you can update your adapter or perform other operations
                        adapter = new ChefShippingAdapter(getContext(), updateDishModelList,delname,number,resarea);
                        re.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
                    }
                });














            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }





});
 return v;
    }
}

