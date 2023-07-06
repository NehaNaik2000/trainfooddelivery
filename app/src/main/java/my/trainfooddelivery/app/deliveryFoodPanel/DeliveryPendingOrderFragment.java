package my.trainfooddelivery.app.deliveryFoodPanel;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import my.trainfooddelivery.app.CustomerHomeAdapter;
import my.trainfooddelivery.app.DeliverypendingAdapter;
import my.trainfooddelivery.app.UpdateDishModel;
import my.trainfooddelivery.app.customerFoodPanel.CustomerCartFragment;
import my.trainfooddelivery.app.deliveryFoodPanel.delivery;
import my.trainfooddelivery.app.deliveryFoodPanel.LocationData;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import my.trainfooddelivery.app.ChefFoodPanel.Chef;
import my.trainfooddelivery.app.IBaseGpsListener;
import my.trainfooddelivery.app.MainMenu;
import my.trainfooddelivery.app.R;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import my.trainfooddelivery.app.MainMenu;
import my.trainfooddelivery.app.R;
import my.trainfooddelivery.app.placedorder;


public class DeliveryPendingOrderFragment extends Fragment implements IBaseGpsListener {
    private  Button available;
    private static final int PERMISSION_LOCATION = 1;
    DatabaseReference locationRef,dataa,databaseReference;
    private String mob ,name,State,Area,devname,userid;
    long timestamp;
    RecyclerView recyclerView;
    private List<placedorder> updateDishModelList;
    private DeliverypendingAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_deliverypendingorder,container,false);
        getActivity().setTitle("Pending Orders");
        setHasOptionsMenu(true);
        available=v.findViewById(R.id.available);
        recyclerView = v.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateDishModelList = new ArrayList<>();

        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
                    requestLocationPermission();
                } else {
                    showLocation();
                }

                // Get the current user's ID
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // Save the token to the database
                FirebaseMessaging.getInstance().getToken()
                        .addOnSuccessListener(token -> {
                            // Create a reference to the "tokens" node in the Firebase Realtime Database
                            DatabaseReference tokensRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                    .getReference("tokens").child("delivery");

                            // Create a new child node under "tokens" with the token as the value
                            tokensRef.child(userID).setValue(token);

//                            // Send floating notification to the user
//                            sendFloatingNotification();

                            // Send a real-time notification to customers
                            sendRealTimeNotification(userID,"Ready for delivery!", "Your status has been changed to available!");

                        })
                        .addOnFailureListener(e -> {
                            // Handle token retrieval failure
                        });




            }
        });


         userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataa = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users").child(userid);
        dataa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                delivery dev = snapshot.getValue(delivery.class);
                if (dev != null) {
                    String first=dev.getFirstName();
                    String last=dev.getLastName();
                    mob=dev.getMobileNo();
                    devname=first+" "+last;
                    Log.d("mobile of del","del"+mob);



                } else {
                    Log.d("error", "no fetch");

                }

                databaseReference = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("assigned delivery");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot orders: dataSnapshot.getChildren()) {
                            String orderid=orders.getKey();
                            Log.d("orderid", "ID"+orderid);
                            for (DataSnapshot deliverySnapshot : orders.getChildren()) {
                                String key = deliverySnapshot.getKey();
                                Log.d("orderid", "key"+key);
                                assert key != null;
                                if(key.equals(userid)) {
                                    placedorder models = new placedorder();
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
                                    adapter = new DeliverypendingAdapter(getContext(),updateDishModelList,userid,mob,devname,orderid);

                                    recyclerView.setAdapter(adapter);

                                }


                            }




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
    }
    private void showLocation() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            Toast.makeText(requireContext(), "Enable GPS!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    private String getLocationText(Location location) {
        return "Lat: " + location.getLatitude() + "\nLon: " + location.getLongitude();
    }

    private void saveLocationToFirebase(double latitude, double longitude) {
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataa = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users").child(userid);
        dataa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                delivery del = snapshot.getValue(delivery.class);
                if (del != null) {
                    State = del.getState();
                    Area = del.getArea();
                    String Fname=del.getFirstName();
                    String Lname=del.getLastName();
                    name=Fname+" "+Lname;
                    Log.d("state","state"+State);
                    Log.d("area","area"+Area);
                    Log.d("name","name"+name);


                }
                else
                {
                    Log.e("error","error");
                }
                timestamp=System.currentTimeMillis();

                locationRef=FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Delivery Person location");


                // Create a new Location object
                LocationData locationData = new LocationData(latitude, longitude,timestamp,name);

                // Save the location to Firebase
                locationRef.child(State).child(Area).child(userid).setValue(locationData);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void onLocationChanged(Location location) {
        saveLocationToFirebase(location.getLatitude(), location.getLongitude());

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

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

    private void saveTokenToFirebase(String token) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference tokensRef = FirebaseDatabase.getInstance().getReference("tokens");
        tokensRef.child(userId).setValue(token);
    }

    private void sendRealTimeNotification(String userID, String title, String message) {
        Context context = getContext();

        // Create a reference to the "notifications" node in the Firebase Realtime Database
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("notifications").child("delivery").child(userID);

        // Create a new child node under "notifications" with the notification data
        CustomerCartFragment.MyNotification notification = new CustomerCartFragment.MyNotification(title, message);
        notificationsRef.push().setValue(notification);

        // Create a notification channel
        createNotificationChannel();

        // Display the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "deliverychannel")
                .setSmallIcon(R.drawable.train)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the notification channel with a unique ID and name
            String channelId = "deliverychannel";
            CharSequence channelName = "Delivery Channel";
            String channelDescription = "Delivery Notifications";

            // Define the importance level and vibration pattern
            int importance = NotificationManager.IMPORTANCE_HIGH;
            long[] pattern = {0, 1000, 500, 1000};

            // Create the notification channel
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);
            channel.enableVibration(true);
            channel.setVibrationPattern(pattern);

            // Register the channel with the system
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}


