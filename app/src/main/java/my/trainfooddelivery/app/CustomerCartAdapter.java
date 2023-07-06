package my.trainfooddelivery.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomerCartAdapter extends RecyclerView.Adapter<CustomerCartAdapter.ViewHolder> {

    private List<UpdateDishModel> cartList;
    private Context context;
    private boolean isOrderEnabled = false;
    private  String userId;


    public CustomerCartAdapter(Context context, List<UpdateDishModel> cartList, String userid) {
        this.cartList = cartList;
        this.context = context;
        this.userId = userid;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_cart, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UpdateDishModel cart = cartList.get(position);
        holder.imageres.setImageResource(R.drawable.paneer_butter_masala);

// load dish image using Glide
        Glide.with(context)
                .load(cart.getRandomUID())
                .into(holder.imageres);
        holder.dishNameTextView.setText(cart.getDishes());
        holder.quantityTextView.setText(cart.getQuantity());
        holder.priceTextView.setText(cart.getPrice()+" RS");
        holder.restaurantTextView.setText(cart.getRestaurant());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date etaDate = null;
        try {
            etaDate = sdf.parse(cart.getEta());
            Log.d("eta","cart eta from database"+cart.getEta());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar etaCal = Calendar.getInstance();
        etaCal.setTime(etaDate);
        int etaMinutes = etaCal.get(Calendar.HOUR_OF_DAY) * 60 + etaCal.get(Calendar.MINUTE);

        // Get current time in minutes
        Calendar now = Calendar.getInstance();
        int currentMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);

        // Check time difference
        int timeDiff = etaMinutes - currentMinutes;
        Log.d("time","timediff"+timeDiff);
        if (timeDiff > 60) {
            // Display check availability button and set to green
            holder.checkAvailabilityBtn.setVisibility(View.VISIBLE);
            holder.checkAvailabilityBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkAvailabilityBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
//                    cart.setCheckedAvailability(true);
                    boolean isAvailable = true;


                           cart.setAvailability(isAvailable);
                           String res =cart.getRestaurant();
                      DatabaseReference placedRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("AVAILABLE ORDERS").child(res).child(userId);
                    placedRef.child(cart.getDishes()).setValue(cart);
                }
            });
            int buttonColor = ((ColorDrawable)holder.checkAvailabilityBtn.getBackground()).getColor();
            if (buttonColor == Color.BLUE) {
                Toast.makeText(context, "Error: check availability for all items ", Toast.LENGTH_SHORT).show();



            }

        }
        else  {
            holder.checkAvailabilityBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.Red));

            String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.d("uid","user"+useridd);
            Log.d("uid","user"+cart.getDishes());
            DatabaseReference dishRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ORDERS").child(useridd).child(cart.getDishes());

            dishRef.removeValue();


        }

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dishNameTextView;
        private TextView quantityTextView;
        private TextView priceTextView;
        private TextView restaurantTextView;
        ImageView imageres;

Button checkAvailabilityBtn;


        public ViewHolder(View itemView) {
            super(itemView);
            dishNameTextView = itemView.findViewById(R.id.txt_product_name);
            quantityTextView = itemView.findViewById(R.id.txt_quantity);
            priceTextView = itemView.findViewById(R.id.txt_price);
            restaurantTextView=itemView.findViewById(R.id.restaurant);
            imageres=itemView.findViewById(R.id.img_product);
            checkAvailabilityBtn=itemView.findViewById(R.id.check);
//            placeorder=itemView.findViewById(R.id.place_order);


        }
    }



}
