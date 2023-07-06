package my.trainfooddelivery.app.deliveryFoodPanel;

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

import my.trainfooddelivery.app.CustomerCartAdapter;
import my.trainfooddelivery.app.DeliverypendingAdapter;
import my.trainfooddelivery.app.R;
import my.trainfooddelivery.app.ShippingAdapter;
import my.trainfooddelivery.app.placedorder;


public class DeliveryShipOrderFragment extends Fragment {
    private DatabaseReference pending,data;
    private List<placedorder> updateDishModelList;
    private ShippingAdapter adapter;
    private RecyclerView recy;
    String userid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deliveryshiporder, null);
        getActivity().setTitle("Ship Orders");
        userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("key","key"+userid);

        recy = v.findViewById(R.id.recycleviews);
        recy.setHasFixedSize(true);
        recy.setLayoutManager(new LinearLayoutManager(getContext()));
        updateDishModelList = new ArrayList<>();

        pending= FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("PENDING DELIVERY");
        pending.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ship: snapshot.getChildren()) {
                    String orderid=ship.getKey();
                    for (DataSnapshot deliverySnapshot : ship.getChildren()) {
                        String key =deliverySnapshot.getKey();
                        Log.d("key","key"+key);
                        assert key != null;
                        if(key.equals(userid)) {
                            placedorder models = new placedorder();
                            Log.d("key","inside");
                            models.setCustomerName(deliverySnapshot.child("customerName").getValue(String.class));
                            models.setdishes(deliverySnapshot.child("dishes").getValue(String.class));
                            models.setTotalPrice(deliverySnapshot.child("totalPrice").getValue(String.class));
                            models.seteta(deliverySnapshot.child("eta").getValue(String.class));
                            models.setRestaurant(deliverySnapshot.child("restaurant").getValue(String.class));
                            models.setMobileNo(deliverySnapshot.child("mobileNo").getValue(String.class));
                            models.setSeatNumber(deliverySnapshot.child("seatNumber").getValue(String.class));
                            models.settraino(deliverySnapshot.child("trainno").getValue(String.class));
                            models.setCoach(deliverySnapshot.child("coach").getValue(String.class));
                            models.setUserid(deliverySnapshot.child("userid").getValue(String.class));
                            models.setPayment(deliverySnapshot.child("payment").getValue(String.class));
                            updateDishModelList.add(models);
                            adapter = new ShippingAdapter(getContext(),updateDishModelList,orderid);

                            recy.setAdapter(adapter);

                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }


}

