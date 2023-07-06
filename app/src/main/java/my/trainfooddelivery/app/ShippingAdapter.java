package my.trainfooddelivery.app;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import my.trainfooddelivery.app.customerFoodPanel.CustomerCartFragment;

public class ShippingAdapter extends RecyclerView.Adapter<ShippingAdapter.ViewHolder> {
    private Context mcontext;
    private DatabaseReference data;
    private List<placedorder> updateDishModellist;
   private String orderid;
   private String restaurantName;

    public ShippingAdapter(Context context, List<placedorder> updateDishModelList,String order) {
        this.mcontext=context;
        this.updateDishModellist=updateDishModelList;
       this.orderid=order;
    }

    @NonNull
    @Override
    public ShippingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.shipping,parent,false);
        return new ShippingAdapter.ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull ShippingAdapter.ViewHolder holder, int position) {

        final placedorder updateDishModel = updateDishModellist.get(position);

        holder.fprice.setText("Grand total"+updateDishModel.getTotalPrice());
        holder.food.setText(""+updateDishModel.getdishes());

        holder.etaText.setText("Eta:" + updateDishModel.geteta());
        holder.train.setText("Train no:" + updateDishModel.gettrainno());
        holder.mob.setText("Mobile: " + updateDishModel.getMobileNo());
        holder.Name.setText("Name: " + updateDishModel.getCustomerName());
        holder.coach.setText("Coach: " + updateDishModel.getCoach());
        holder.rname.setText("Restaurant: " + updateDishModel.getRestaurant());
        holder.seatno.setText("SeatNo: " + updateDishModel.getSeatNumber());
        holder.payment.setText("Payment mode:"+updateDishModel.getPayment());

        holder.delivered.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.delivered.setBackgroundTintList(ContextCompat.getColorStateList(mcontext, R.color.Red));
                }
                data= FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("PENDING DELIVERY");
                data.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot remove:snapshot.getChildren())
                        {

                             String key =remove.getKey();
                                    if(key.equals(orderid))
                                  {
                                      remove.getRef().removeValue();
                                  }

                        }

                        // Retrieve the mobile number from the ViewHolder
                        String mobileNumber = updateDishModel.getMobileNo();
                        Log.e("Notification","cust mobile from holder" + mobileNumber);
                        // Get a reference to the "users" node in the database
                        DatabaseReference usersRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");
                        // Query the users node to find the matching user ID based on the mobile number
                        usersRef.orderByChild("MobileNo").equalTo(mobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    // Fetch the matching user ID
                                    String CustomeruserID = userSnapshot.getKey();
                                    Log.e("Notification","cust id from db" + CustomeruserID);

                                    sendNotificationToCustomer(CustomeruserID,"Your order has been delivered!");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("Notification Error","Error finding matching mobile no");
                            }
                        });

                        // Get the restaurant name from the current order
                        restaurantName = updateDishModel.getRestaurant();
                       //  Find the chef's user ID based on the restaurant name
                        DatabaseReference chefRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");
                        chefRef.orderByChild("Restaurant").equalTo(restaurantName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot chefSnapshot : dataSnapshot.getChildren()) {
                                    String chefID = chefSnapshot.getKey();
                                    Log.e("Notification","chef id from db" + chefID);

                                    sendNotificationToChef(chefID,"Order delivered successfully!");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                 // Handle error
                            }
                        });

                        String DeliveryuserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        // Send notification to the current delivery person
                        sendNotificationToDeliveryPerson( DeliveryuserID,"Order delivered successfully!");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return updateDishModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView etaText, train, Name, mob,rname;
        TextView food,fprice,seatno,coach,payment;
        Button delivered;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fprice=itemView.findViewById(R.id.money);
            food=itemView.findViewById(R.id.foods);
            rname=itemView.findViewById(R.id.hotel);
            etaText = itemView.findViewById(R.id.ETa);
            mob = itemView.findViewById(R.id.cusmobile);
            Name = itemView.findViewById(R.id.cusname);
            train = itemView.findViewById(R.id.trainos);
            seatno=itemView.findViewById(R.id.seaT);
            coach=itemView.findViewById(R.id.coacH);
            delivered=itemView.findViewById(R.id.delivered);

            payment=itemView.findViewById(R.id.payment);

        }
    }

    private void sendNotificationToCustomer(String userID, String message) {

        DatabaseReference tokensRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("tokens").child("customer");
        DatabaseReference customerTokenRef = tokensRef.child(userID);
        customerTokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String token = dataSnapshot.getValue(String.class);
                    Log.d("Notification", "Customer Token: " + token);

                    if (token != null) {
                        Context context = mcontext.getApplicationContext();

                        // Create the notification builder
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "customerchannel")
                                .setSmallIcon(R.drawable.train)
                                .setContentTitle("Order Delivered!")
                                .setContentText(message)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);

                        // Get the notification manager and display the notification
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        notificationManager.notify(0, builder.build());

                        // Save the notification to the customer's notifications node
                        DatabaseReference notificationsRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("notifications").child("customer");
                        DatabaseReference customerNotificationsRef = notificationsRef.child(userID);
                        String notificationId = customerNotificationsRef.push().getKey();
                        if (notificationId != null) {
                            HashMap<String, Object> notificationData = new HashMap<>();
                            notificationData.put("title","Order Delivered!");
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
                // Empty implementation of onCancelled
            }
        });
    }

    private void sendNotificationToDeliveryPerson(String DeliveryuserID, String message) {

        DatabaseReference tokensRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("tokens").child("delivery");
        DatabaseReference DeliveryTokenRef = tokensRef.child(DeliveryuserID);
        DeliveryTokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String token = dataSnapshot.getValue(String.class);
                    Log.d("Notification", "Delivery Token: " + token);

                    if (token != null) {
                        Context context = mcontext.getApplicationContext();

                        // Create the notification builder
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "deliverychannel")
                                .setSmallIcon(R.drawable.train)
                                .setContentTitle("Order Delivered!")
                                .setContentText(message)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);

                        // Get the notification manager and display the notification
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        notificationManager.notify(0, builder.build());

                    } else {
                        Toast.makeText(mcontext, "Delivery token not found", Toast.LENGTH_SHORT).show();
                        Log.e("Notification", "Delivery token not found");
                    }
                } else {
                    Toast.makeText(mcontext, "Delivery ID not found", Toast.LENGTH_SHORT).show();
                    Log.e("Notification", "Delivery ID not found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Empty implementation of onCancelled
            }
        });
    }

    private void sendNotificationToChef(String userID, String message) {
        DatabaseReference tokensRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("tokens").child("chef");
        DatabaseReference chefTokenRef = tokensRef.child(userID);
        chefTokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String token = dataSnapshot.getValue(String.class);
                    Log.d("Notification", "Chef Token: " + token);

                    if (token != null) {
                        Context context = mcontext.getApplicationContext();

                        // Create the notification builder
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "chefchannel")
                                .setSmallIcon(R.drawable.train)
                                .setContentTitle("Order Delivered!")
                                .setContentText(message)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);

                        // Get the notification manager and display the notification
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        notificationManager.notify(0, builder.build());

                    } else {
                        Toast.makeText(mcontext, "Chef token not found", Toast.LENGTH_SHORT).show();
                        Log.e("Notification", "Chef token not found");
                    }
                } else {
                    Toast.makeText(mcontext, "Chef ID not found", Toast.LENGTH_SHORT).show();
                    Log.e("Notification", "Chef ID not found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Empty implementation of onCancelled
            }
        });
    }

}
