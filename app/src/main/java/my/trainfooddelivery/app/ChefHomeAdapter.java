package my.trainfooddelivery.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChefHomeAdapter extends RecyclerView.Adapter<ChefHomeAdapter.ViewHolder> {

    private Context mcontext;
    private List<UpdateDishModel> updateDishModellist;








    public ChefHomeAdapter(Context context , List<UpdateDishModel>updateDishModelslist){

        this.updateDishModellist = updateDishModelslist;
        this.mcontext = context;


    }


    @NonNull
    public ChefHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chefhomeitem, parent, false);
        return new ChefHomeAdapter.ViewHolder(view);
    }




    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChefHomeAdapter.ViewHolder holder, int position) {

        final UpdateDishModel updateDishModel = updateDishModellist.get(position);


        // set placeholder image
        holder.imageView.setImageResource(R.drawable.paneer_butter_masala);

// load dish image using Glide
        Glide.with(mcontext)
                .load(updateDishModel.getRandomUID())
                .into(holder.imageView);

        holder.Dishname.setText(""+updateDishModel.getDishes());
        holder.Price.setText("Price: "+updateDishModel.getPrice()+"Rs");
        holder.quantity.setText("Qno: "+updateDishModel.getQuantity());







    }

    @Override
    public int getItemCount() {
        return updateDishModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView Dishname,Price,quantity;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Dishname = itemView.findViewById(R.id.dishname);
            Price = itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.fixedquantity);
            imageView=itemView.findViewById(R.id.menu_image);






        }
    }


}