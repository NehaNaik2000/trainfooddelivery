package my.trainfooddelivery.app.customerFoodPanel;

import android.os.Bundle;
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

import my.trainfooddelivery.app.R;
import my.trainfooddelivery.app.Receipt;
import my.trainfooddelivery.app.ReceiptAdapter;
import my.trainfooddelivery.app.placedorder;

public class CustomerTrackFragment extends Fragment {
    RecyclerView listView;
    List<placedorder> receiptList;
    String receiptNumber;
    String name;
    String number,area;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customertrack, null);
        getActivity().setTitle("Track");
        listView = v.findViewById(R.id.cycle_menu);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        receiptList = new ArrayList<>();

        String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference receiptRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("receipt").child(userid);

            receiptRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot receiptSnapshot : dataSnapshot.getChildren()) {
                        receiptNumber = receiptSnapshot.getKey();
                        name = receiptSnapshot.child("details").child("name").getValue(String.class);
                         number = receiptSnapshot.child("details").child("number").getValue(String.class);
                         area=receiptSnapshot.child("details").child("area").getValue(String.class);

                        if (receiptSnapshot.child("details").child("order").exists()) {
                            DataSnapshot orderDataSnapshot =receiptSnapshot.child("details").child("order");

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

                            receiptList.add(models);


                        }

                    }

                    // Pass the receiptList to your ListView adapter for display
                    ReceiptAdapter adapter = new ReceiptAdapter(getContext(), receiptList,name,number,receiptNumber,area);
                    listView.setAdapter(adapter);
                }



                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error
                }
            });


        return v;
    }

}

