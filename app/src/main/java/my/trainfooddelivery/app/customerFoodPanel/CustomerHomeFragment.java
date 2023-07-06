
package my.trainfooddelivery.app.customerFoodPanel;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.util.pool.FactoryPools;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import my.trainfooddelivery.app.ChefFoodPanel.Chef;
import my.trainfooddelivery.app.ChefFoodPanel.FoodDetails;
import my.trainfooddelivery.app.CustomerHomeAdapter;
import my.trainfooddelivery.app.MainMenu;
import my.trainfooddelivery.app.R;
import my.trainfooddelivery.app.UpdateDishModel;

public class CustomerHomeFragment extends Fragment {



    RecyclerView recyclerView;
    private List<UpdateDishModel> updateDishModelList;
    private CustomerHomeAdapter adapter;
    String State, City, Area;
    DatabaseReference databaseReference;
    String selectedStationName,trainno;
    String selectedStateCode,selectedstationeta;
    TextInputLayout state ,area;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customerhome, null);
        getActivity().setTitle("Home");
        recyclerView = v.findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateDishModelList = new ArrayList<>();
        Bundle bundle = getArguments();
        selectedStationName = bundle.getString("selectedStationName");
        selectedStateCode = bundle.getString("selectedStateCode");
        selectedstationeta=bundle.getString("etatime");
        trainno=bundle.getString("trainno");
        TextView state = v.findViewById(R.id.state_code_text_view);
        state.setText(bundle.getString("etatime"));

        TextView area = v.findViewById(R.id.station_name_text_view);
        area.setText(bundle.getString("selectedStateCode"));



        databaseReference = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("FoodDetails").child(selectedStateCode).child(selectedStationName);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot restaurantSnapshot : dataSnapshot.getChildren()) {
                     for(DataSnapshot dish:restaurantSnapshot.getChildren()) {

                         int nullUidCount = 0;
                         UpdateDishModel models = new UpdateDishModel();
                         String randomUID = dish.child("RandomUID").getValue(String.class);
                         models.setRandomUID(dish.child("RandomUID").getValue(String.class));
                         models.setDishes(dish.child("Dishes").getValue(String.class));
                         models.setPrice(dish.child("Price").getValue(String.class));
                         models.setQuantity(dish.child("Quantity").getValue(String.class));
                         models.setRestaurant(dish.child("ImageURL").getValue(String.class));
                         updateDishModelList.add(models);

                         adapter = new CustomerHomeAdapter(getContext(), updateDishModelList, selectedstationeta, trainno);
                         recyclerView.setAdapter(adapter);
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











