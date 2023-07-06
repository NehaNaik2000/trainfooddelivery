package my.trainfooddelivery.app;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;




import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import my.trainfooddelivery.app.ChefFoodPanel.Chef;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChefPendingAdapter extends RecyclerView.Adapter<ChefPendingAdapter.ViewHolder> implements IBaseGpsListener {

    private Context mcontext;
    private List<placedorder> updateDishModellist;
    private static final int PERMISSION_LOCATION = 1;
    private double latitude;
    private DatabaseReference pendingDeliveryRef,placed;
    private double longitude;
    DatabaseReference locationRef, dataa;
    String Area, Code;
    Boolean fulllist = false;
    List<Double> distanceList = new ArrayList<>();
    List<String> names = new ArrayList<>();
    List<String> userIds = new ArrayList<>();
    private String selectedUserId = null;


    public ChefPendingAdapter(Activity context, List<placedorder> updateDishModelslist) {

        this.updateDishModellist = updateDishModelslist;
        this.mcontext = context;


    }


    @NonNull
    public ChefPendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card_view, parent, false);
        return new ChefPendingAdapter.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChefPendingAdapter.ViewHolder holder, int position) {

        final placedorder updateDishModel = updateDishModellist.get(position);

        //holder.distanceSpinner.setEnabled(false);
        holder.finalprice.setText("Grand total" + updateDishModel.getTotalPrice());
        holder.fooddetails.setText("" + updateDishModel.getdishes());

        holder.etaTextView.setText("Eta:" + updateDishModel.geteta());
        holder.trainno.setText("Train no:" + updateDishModel.gettrainno());
        holder.mobile.setText("Mobile: " + updateDishModel.getMobileNo());
        holder.name.setText("Name: " + updateDishModel.getCustomerName());
        String customerId = updateDishModel.getUserid();

        if (updateDishModel.getDeliveryperson()) {
            holder.preparing.setEnabled(false);
            holder.preparing.setText(" Delivey Person Selected");
        } else {
            holder.preparing.setEnabled(true);
        }



        holder.preparing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mcontext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                } else {
                    showLocation();
                }

                Log.e("Notification", "Customer ID onclick: " + customerId);
                sendNotificationToCustomer(customerId, "Order Update", "Your order is being prepared!");

            }


            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestCode == PERMISSION_LOCATION) {
                    if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                        showLocation();
                    } else {
                        Toast.makeText(mcontext, "Permission not granted!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private void showLocation() {
                LocationManager locationManager = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //tv_location.setText("Loading location...");
                    if (ActivityCompat.checkSelfPermission((Activity) mcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission((Activity) mcontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mcontext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();


                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app");
                        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference deliveryRef = database.getReference("Delivery Person location");
                        dataa = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users").child(userid);
                        dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Chef cheff = snapshot.getValue(Chef.class);
                                if (cheff != null) {
                                    Code = cheff.getState();
                                    Area = cheff.getArea();

                                    long twentyMinutesAgo = System.currentTimeMillis() - (1440 * 60 * 1000); // Calculate the timestamp 20 minutes ago

                                    Query query = deliveryRef.child(Code).child(Area).orderByChild("timestamp").startAt(twentyMinutesAgo).endAt(System.currentTimeMillis());


                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.exists()) {
                                                int totalOrders = (int) dataSnapshot.getChildrenCount();
                                                int processedOrders = 0;
                                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                                    String user = userSnapshot.getKey(); // Assuming the user ID is the key of the snapshot
                                                    String name = userSnapshot.child("name").getValue(String.class);
                                                    double longti = userSnapshot.child("longtitude").getValue(Double.class);
                                                    double lat = userSnapshot.child("latitude").getValue(Double.class);
                                                    Log.d("long", "longtitude" + longti);
                                                    Log.d("long", "latitude" + lat);
                                                    calculatedistance(holder, latitude, longitude, lat, longti, totalOrders, ++processedOrders, name, user, updateDishModel);

                                                }


                                            } else {
                                                Toast.makeText(mcontext, "No delivery Person Available Currently", Toast.LENGTH_SHORT).show();

                                            }
                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Handle any errors that occur during the retrieval process
                                        }
                                    });


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }


                        });


                        // Display latitude and longitude in a Toast message
                        String message = "Latitude: " + latitude + ", Longitude: " + longitude;
                        Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mcontext, "Location not available", Toast.LENGTH_SHORT).show();
                    }
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ChefPendingAdapter.this);
            }

        });
    }





    private String getLocationText(Location location) {
        return "Lat: " + location.getLatitude() + "\nLon: " + location.getLongitude();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public int getItemCount() {
        return updateDishModellist.size();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String message = "Latitude: " + latitude + ", Longitude: " + longitude;
        Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();

        // Do something with the latitude and longitude
        String locationText = getLocationText(location);
        Log.d("Location", locationText);


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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView etaTextView, trainno, name, mobile;
        TextView txt,fooddetails,finalprice;
        Button preparing;
        Spinner distanceSpinner;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            finalprice=itemView.findViewById(R.id.finalprice);
            fooddetails=itemView.findViewById(R.id.fooddetails);
            etaTextView = itemView.findViewById(R.id.eta);
            mobile = itemView.findViewById(R.id.customer_mobile);
            name = itemView.findViewById(R.id.customer_name);
            trainno = itemView.findViewById(R.id.trainno);
            preparing = itemView.findViewById(R.id.btn_view_details);
            distanceSpinner = itemView.findViewById(R.id.spinner_distance);
            txt=itemView.findViewById(R.id.txtbox);


        }


    }

    private void calculatedistance(final ViewHolder holder,double startLatitude, double startLongitude, double endLatitude, double endLongitude,int totalOrders,int processedOrders,String name,String userId,placedorder updatedishmodel) {

        new AsyncTask<Void, Void, Double>() {

            @Override
            protected Double doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();
                String url = "https://distance-calculator8.p.rapidapi.com/calc?startLatitude=" + startLatitude +
                        "&startLongitude=" + startLongitude +
                        "&endLatitude=" + endLatitude +
                        "&endLongitude=" + endLongitude;

                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("X-RapidAPI-Key", "bf518fd93bmsh05f2aa3919f65cdp1c7871jsn1aa210d7f25d")
                        .addHeader("X-RapidAPI-Host", "distance-calculator8.p.rapidapi.com")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        // Parse the JSON response
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONObject distanceObject = jsonResponse.getJSONObject("body").getJSONObject("distance");

                        // Extract the kilometers value
                        return distanceObject.getDouble("kilometers");
                    } else {
                        // Handle the unsuccessful response here
                        return null;
                    }
                } catch (IOException | JSONException e) {
                    // Handle the exception here
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Double distance) {
                if (distance != null) {

                    distanceList.add(distance);
                    names.add(name);
                    userIds.add(userId);

                    if (processedOrders == totalOrders) {
                        // Sort the distanceList in ascending order
                        Collections.sort(distanceList);

                        if (!distanceList.isEmpty()) {

                            List<String> spinnerItems = new ArrayList<>();

                            // Create a list of strings containing the name and distance
                            for (int i = 0; i < distanceList.size(); i++) {
                                double dist = distanceList.get(i);
                                String deliveryName = names.get(i);
                                String item = dist +" kms"+ " - " +deliveryName;
                                spinnerItems.add(item);
                            }




                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mcontext, android.R.layout.simple_spinner_item,spinnerItems);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            holder.distanceSpinner.setAdapter(spinnerAdapter);
                            holder.txt.setVisibility(View.VISIBLE);
                            holder.distanceSpinner.setVisibility(View.VISIBLE);
//                            holder.distanceSpinner.performClick();
                            holder.distanceSpinner.setOnItemSelectedListener(null); // Clear the existing selection listener
                            holder.distanceSpinner.setSelection(0);
                            String cardViewId = UUID.randomUUID().toString();

                            holder.distanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                    // Update the node based on the latest selection
                                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app");
                                    pendingDeliveryRef = database.getReference("assigned delivery");
                                    pendingDeliveryRef.child(cardViewId).removeValue();



                                    selectedUserId = userIds.get(position);
                                    // Create a new order detail for the selected user
                                    pendingDeliveryRef.child(cardViewId).child(selectedUserId).setValue(updatedishmodel);

                                    String deliveryName = names.get(position);
                                    String buttonText ="Preparing and Delivery Selected:"+deliveryName;
                                    holder.preparing.setText(buttonText);
                                    holder.preparing.setTextColor(ContextCompat.getColor(mcontext, R.color.green));

                                    DatabaseReference placedOrderRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app")
                                            .getReference("PLACED ORDERS")
                                            .child(updatedishmodel.getRestaurant()).child(updatedishmodel.getUserid());

                                    placedOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                snapshot.getRef().child("Deliveryperson").setValue(true);
                                                sendNotificationToDeliveryPerson(selectedUserId, "New Order!", "Accept or reject the order assigned");
                                                Log.e("Delivery Notification", "notif sent to delivery ID: " + selectedUserId);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Handle the error case
                                        }
                                    });







                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // Handle the case when no item is selected
                                }
                            });


                        }
                    }
                } else {
                    // Handle the error case
                    Log.d("distance", "Error calculating distance");
                }
                // Sort the distanceList in ascending order



                System.out.println("Stored distances:");
                for (double dist : distanceList) {

                    Log.d("distance", "distance: " + dist);
                }
            }


        }.execute();


    }

    private void sendNotificationToCustomer(String customerId, String title, String message) {
        DatabaseReference tokensRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("tokens").child("customer");
        DatabaseReference customerTokenRef = tokensRef.child(customerId);
        customerTokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String token = dataSnapshot.getValue(String.class);
                    Log.d("Notification", "Token: " + token);

                    if (token != null) {
                        Context context = mcontext.getApplicationContext();

                        // Create the notification builder
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "customerchannel")
                                .setSmallIcon(R.drawable.train)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);

                        // Get the notification manager and display the notification
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        notificationManager.notify(0, builder.build());

                        // Save the notification to the customer's notifications node
                        DatabaseReference notificationsRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("notifications").child("customer");
                        DatabaseReference customerNotificationsRef = notificationsRef.child(customerId);
                        String notificationId = customerNotificationsRef.push().getKey();
                        if (notificationId != null) {
                            HashMap<String, Object> notificationData = new HashMap<>();
                            notificationData.put("title", title);
                            notificationData.put("message", message);
                            customerNotificationsRef.child(notificationId).setValue(notificationData);
                        }
                    } else {
                        Toast.makeText(mcontext, "Customer token not found", Toast.LENGTH_SHORT).show();
                        Log.e("Notification", "customer token not found");
                    }
                } else {
                    Toast.makeText(mcontext, "Customer ID not found", Toast.LENGTH_SHORT).show();
                    Log.e("Notification", "customer ID not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the retrieval process
                Log.d("Notification", "Data retrieval canceled: " + databaseError.getMessage());
            }
        });
    }

    private void sendNotificationToDeliveryPerson(String deliveryPersonId, String title, String message) {
        DatabaseReference tokensRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("tokens");
        DatabaseReference deliveryPersonTokenRef = tokensRef.child(deliveryPersonId);
        deliveryPersonTokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String token = dataSnapshot.getValue(String.class);
                    Log.d("Notification", "Delivery Person Token: " + token);

                    if (token != null) {
                        Context context = mcontext.getApplicationContext();

                        // Create the notification builder
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "deliverychannel")
                                .setSmallIcon(R.drawable.train)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);

                        // Get the notification manager and display the notification
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        notificationManager.notify(0, builder.build());

                        // Log the notification details
                        Log.d("Delivery Notification", "delivery person ID in method: " + deliveryPersonId);


                        // Save the notification to the delivery person's notifications node
                        DatabaseReference notificationsRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("notifications");
                        DatabaseReference deliveryPersonNotificationsRef = notificationsRef.child(deliveryPersonId);
                        String notificationId = deliveryPersonNotificationsRef.push().getKey();
                        if (notificationId != null) {
                            HashMap<String, Object> notificationData = new HashMap<>();
                            notificationData.put("title", title);
                            notificationData.put("message", message);
                            deliveryPersonNotificationsRef.child(notificationId).setValue(notificationData);
                        }
                    } else {
                        Toast.makeText(mcontext, "Delivery person token not found", Toast.LENGTH_SHORT).show();
                        Log.e("Notification", "Delivery person token not found");
                    }
                } else {
                    Toast.makeText(mcontext, "Delivery person ID not found", Toast.LENGTH_SHORT).show();
                    Log.e("Notification", "Delivery person ID not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the retrieval process
                Log.e("Notification", "Data retrieval canceled: " + databaseError.getMessage());
            }
        });
    }

}








