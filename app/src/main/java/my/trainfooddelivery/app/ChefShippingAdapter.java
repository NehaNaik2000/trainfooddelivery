package my.trainfooddelivery.app;



import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChefShippingAdapter extends RecyclerView.Adapter<ChefShippingAdapter.ViewHolder> {
    private Context mcontext;
    private DatabaseReference data;
    private List<placedorder> updateDishModellist;
    private String Name,Number,Area;

    public ChefShippingAdapter(Context context, List<placedorder> updateDishModelList,String name,String number,String area) {
        this.mcontext=context;
        this.updateDishModellist=updateDishModelList;
        this.Name=name;
        this.Number=number;
        this.Area=area;
    }

    @NonNull
    @Override
    public ChefShippingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.chefship,parent,false);
        return new ChefShippingAdapter.ViewHolder(view);

    }




    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull ChefShippingAdapter.ViewHolder holder, int position) {

        final placedorder updateDishModel = updateDishModellist.get(position);

        holder.fprice.setText("Grand total"+updateDishModel.getTotalPrice());
        holder.food.setText(""+updateDishModel.getdishes());

        holder.etaText.setText("Eta:" + updateDishModel.geteta());
        holder.train.setText("Train no:" + updateDishModel.gettrainno());
        holder.mob.setText("Delivery Mobile: " +Number);
        holder.Name.setText("Customer Name: " + updateDishModel.getCustomerName());
        holder.coach.setText("Coach: " + updateDishModel.getCoach());
        holder.rname.setText("Delivery: " +Name);
        holder.seatno.setText("SeatNo: " + updateDishModel.getSeatNumber());
        holder.payment.setText("Payment mode:"+updateDishModel.getPayment());

        holder.ship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.ship.setBackgroundTintList(ContextCompat.getColorStateList(mcontext, R.color.Red));
                }

                DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("shipping").child(updateDishModel.getRestaurant());

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            if (orderSnapshot.child("DeliveryName").getValue(String.class).equals(Name) &&
                                    orderSnapshot.child("Deliverymobile").getValue(String.class).equals(Number)) {
                                orderSnapshot.getRef().removeValue();
                                createReceipt(updateDishModel,Name,Number,Area);

                                // Send notification to the customer
                               // sendNotificationToCustomer(updateDishModel.getCustomerToken(), "Your order is ready!");

                                // Send notification to the delivery person
                                //sendNotificationToDeliveryPerson(updateDishModel.getDeliveryPersonToken(), "Order ready for delivery!");

                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
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
        Button ship;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fprice=itemView.findViewById(R.id.Money);
            food=itemView.findViewById(R.id.foodies);
            rname=itemView.findViewById(R.id.Hotel);
            etaText = itemView.findViewById(R.id.eTa);
            mob = itemView.findViewById(R.id.cusmobiles);
            Name = itemView.findViewById(R.id.cusnames);
            train = itemView.findViewById(R.id.trai);
            seatno=itemView.findViewById(R.id.seAT);
            coach=itemView.findViewById(R.id.coaCH);
            ship=itemView.findViewById(R.id.sent_shipping);

            payment=itemView.findViewById(R.id.Payment);

        }
    }

    private void createReceipt(placedorder order,String name,String number,String area) {
        DatabaseReference receiptRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("receipt");

        // Generate a unique receipt number
        String receiptNumber = receiptRef.push().getKey();
        HashMap<String, Object> receiptDetails = new HashMap<>();
        receiptDetails.put("order", order);
        receiptDetails.put("name", name);
        receiptDetails.put("number", number);
        receiptDetails.put("area",area);





        // Store the details under the receipt node
        receiptRef.child(order.getUserid()).child(receiptNumber).child("details").setValue(receiptDetails);
    }

    private void sendNotificationToCustomer(String customerToken, String message) {


    }

    private void sendNotificationToDeliveryPerson(String deliveryPersonToken, String message) {

    }

}









