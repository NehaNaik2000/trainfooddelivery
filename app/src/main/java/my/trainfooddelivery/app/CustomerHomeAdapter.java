package my.trainfooddelivery.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import my.trainfooddelivery.app.R;
import my.trainfooddelivery.app.UpdateDishModel;
import my.trainfooddelivery.app.customerFoodPanel.CustomerCartFragment;

public class CustomerHomeAdapter extends RecyclerView.Adapter<CustomerHomeAdapter.ViewHolder> {

    private Context mcontext;
    private List<UpdateDishModel>updateDishModellist;
    private String mEta;
    private String trainno;







    public CustomerHomeAdapter(Context context , List<UpdateDishModel>updateDishModelslist,String eta,String trainno){

        this.updateDishModellist = updateDishModelslist;
        this.mcontext = context;
        this.mEta=eta;
        this.trainno=trainno;

    }


    @NonNull
    @Override
    public CustomerHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customer_menudish,parent,false);
        return new CustomerHomeAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomerHomeAdapter.ViewHolder holder, int position) {

        final UpdateDishModel updateDishModel = updateDishModellist.get(position);
        // set placeholder image
        holder.imageView.setImageResource(R.drawable.paneer_butter_masala);

// load dish image using Glide
        Glide.with(mcontext)
                .load(updateDishModel.getRandomUID())
                .into(holder.imageView);

        holder.Dishname.setText(""+updateDishModel.getDishes());
        holder.Price.setText("Price: "+updateDishModel.getPrice()+"Rs");
        holder.Restaurant.setText(""+updateDishModel.getRestaurant());
        holder.fquantity.setText("Qno: "+updateDishModel.getQuantity());
        holder.etaTextView.setText(mcontext.getString(R.string.eta_format, mEta));


        ArrayList<Integer> numbers = new ArrayList();
        for(int i = 1; i <= 10; i++){
            numbers.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(mcontext,android.R.layout.simple_spinner_dropdown_item,numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.quantity.setAdapter(adapter);

        holder.quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Here you can get the selected item and do something with it
                int selectedQuantity = (int) adapterView.getItemAtPosition(i);
                Log.d("SpinnerSelection", "Selected Quantity: " + selectedQuantity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
        holder.cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cartButton.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.green));
                int selectedQuantity = holder.quantity.getSelectedItemPosition() + 1;
                Log.d("selected","select"+selectedQuantity);
                DatabaseReference database = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ORDERS");
//                String quan=updateDishModel.getQuantity();
                String res=updateDishModel.getRestaurant();
                String price=updateDishModel.getPrice();
                Log.d("res", "name of res: " + res);
                int iprice=Integer.parseInt(price);
//
                long timestamp=System.currentTimeMillis();
                int totaldishamt=selectedQuantity*iprice;
                String totaldishamtStr = String.valueOf(totaldishamt);
                String selectedquanStr = String.valueOf(selectedQuantity);
                Order order = new Order(updateDishModel.getDishes(), totaldishamtStr,selectedquanStr,timestamp,updateDishModel.getRandomUID(),updateDishModel.getRestaurant(),mEta,trainno);

                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
                database.child(useridd).child(updateDishModel.getDishes()).setValue(order);
            }
        });



    }

    @Override
    public int getItemCount() {
        return updateDishModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView etaTextView;
        ImageView imageView;
        TextView Dishname,Price,Restaurant,fquantity;
        ImageButton cartButton;
        Spinner quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Restaurant=itemView.findViewById(R.id.restaurant_name);
            imageView = itemView.findViewById(R.id.menu_image);
            Dishname = itemView.findViewById(R.id.dishname);
            Price = itemView.findViewById(R.id.price);
            cartButton = itemView.findViewById(R.id.cart_button);
            quantity=itemView.findViewById(R.id.orderquantity);
            fquantity=itemView.findViewById(R.id.fixedquantity);
            etaTextView=itemView.findViewById(R.id.time);

            // Define click listener for orderButton



        }
    }


}

