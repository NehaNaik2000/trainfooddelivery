package my.trainfooddelivery.app.ChefFoodPanel;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

import my.trainfooddelivery.app.ChefHomeAdapter;
import my.trainfooddelivery.app.CustomerCartAdapter;
import my.trainfooddelivery.app.CustomerHomeAdapter;
import my.trainfooddelivery.app.MainMenu;
import my.trainfooddelivery.app.R;
import my.trainfooddelivery.app.UpdateDishModel;


public class ChefHomeFragment extends Fragment  {
    RecyclerView recyclerView;
    private List<UpdateDishModel> updateDishModelList;
    private ChefHomeAdapter adapter;
    String Code,Area,rname;
    DatabaseReference databaseReference,data;
    private TextView hotelNameTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chef_home,null);

        getActivity().setTitle("Home");
        setHasOptionsMenu(true);

        recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateDishModelList = new ArrayList<>();




        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        data = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users").child(userid);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Chef cheff = snapshot.getValue(Chef.class);
                if (cheff != null) {
                    Code = cheff.getState();
                    Area = cheff.getArea();
                    rname = cheff.getRestaurant();


                }

                Log.d("ChefHomeFragment", "Code: " + Code + ", Area: " + Area + ", rname: " + rname);

                databaseReference = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("FoodDetails").child(Code).child(Area).child(rname);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot restaurantSnapshot : dataSnapshot.getChildren()) {


                                int nullUidCount = 0;
                                UpdateDishModel models = new UpdateDishModel();
                                String randomUID = dataSnapshot.child("RandomUID").getValue(String.class);
                                if (randomUID == null) { // Check if the value is not null
                                    nullUidCount++;
                                    Log.d("DatabaseReference", "nulll uid " + nullUidCount);
                                }
                                models.setRandomUID(restaurantSnapshot.child("RandomUID").getValue(String.class));
                                models.setDishes(restaurantSnapshot.child("Dishes").getValue(String.class));
                                models.setPrice(restaurantSnapshot.child("Price").getValue(String.class));
                                models.setQuantity(restaurantSnapshot.child("Quantity").getValue(String.class));
                                updateDishModelList.add(models);
                            }
                            adapter = new ChefHomeAdapter(getContext(), updateDishModelList);
                            recyclerView.setAdapter(adapter);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }





            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        return v;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.logout,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idd = item.getItemId();
        if(idd == R.id.LOGOUT){
            Logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Logout() {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}