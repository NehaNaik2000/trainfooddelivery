package my.trainfooddelivery.app;



import android.annotation.SuppressLint;
import android.content.Context;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliverypendingAdapter extends RecyclerView.Adapter<DeliverypendingAdapter.ViewHolder> {

    private Context mcontext;
    private List<placedorder>updateDishModellist;
    private  String Userid,Mob,Devname,Orderid;
   private DatabaseReference databaseReference ,data;

    // Interface for callback










    public DeliverypendingAdapter(Context context , List<placedorder> updateDishModelslist,String userid,String mo,String devname,String orderid){

        this.updateDishModellist = updateDishModelslist;
        this.mcontext = context;
        this.Userid=userid;
        this.Mob=mo;
        this.Devname=devname;
        this.Orderid=orderid;


    }


    @NonNull
    @Override
    public DeliverypendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.delivery_pending_item,parent,false);
        return new DeliverypendingAdapter.ViewHolder(view);
    }



    @SuppressLint("SetTextI18n")

    public void onBindViewHolder(@NonNull DeliverypendingAdapter.ViewHolder holder, int position) {

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








        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("PENDING DELIVERY");

                database.child(Orderid).child(Userid).setValue(updateDishModel);

                DatabaseReference dat=FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("shipping");
                Map<String, Object> updateValues = new HashMap<>();
                updateValues.put("DeliveryName", Devname);
                updateValues.put("Deliverymobile", Mob);

                updateValues.put("order",updateDishModel);

                dat.child(updateDishModel.getRestaurant()).child(Userid).setValue(updateValues);

                Toast.makeText(mcontext, "Order accepted", Toast.LENGTH_SHORT).show();

                DatabaseReference deleteplaced= FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("PLACED ORDERS");
                deleteplaced.child(updateDishModel.getRestaurant()).child(updateDishModel.getUserid()).removeValue();

                data = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("assigned delivery");
                data.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot orders: dataSnapshot.getChildren()) {
                            String orderid=orders.getKey();
                            Log.d("orderid", "ID"+orderid);
                            for (DataSnapshot deliverySnapshot : orders.getChildren()) {
                                String key = deliverySnapshot.getKey();
                                Log.d("orderid", "key"+key);
                                assert key != null;
                                if(key.equals(Userid)) {
                                    deliverySnapshot.getRef().removeValue();


                                }


                            }




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });


            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mcontext, "Rejection will be notified to chef ", Toast.LENGTH_SHORT).show();
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
                                if(key.equals(Userid)) {
                                    deliverySnapshot.getRef().removeValue();


                                }


                            }




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });
                DatabaseReference placedOrderRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app")
                        .getReference("PLACED ORDERS")
                        .child(updateDishModel.getRestaurant()).child(updateDishModel.getUserid());

                placedOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            snapshot.getRef().child("Deliveryperson").setValue(false);


                        }
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView etaText, train, Name, mob,rname;
        TextView food,fprice,seatno,coach,payment;
        Button accept,reject;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fprice=itemView.findViewById(R.id.tprice);
            food=itemView.findViewById(R.id.food);
            rname=itemView.findViewById(R.id.hotelname);
            etaText = itemView.findViewById(R.id.ETA);
            mob = itemView.findViewById(R.id.cmobile);
            Name = itemView.findViewById(R.id.customername);
            train = itemView.findViewById(R.id.trainumber);
            seatno=itemView.findViewById(R.id.seat);
            coach=itemView.findViewById(R.id.coach);
            accept=itemView.findViewById(R.id.accept);
            reject=itemView.findViewById(R.id.reject);
            payment=itemView.findViewById(R.id.pay);





        }
    }


}

