package my.trainfooddelivery.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.trainfooddelivery.app.R;
import my.trainfooddelivery.app.Receipt;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {
    private Context context;
    private List<placedorder> receiptList;
    private String name,number,renum,Area;

    public ReceiptAdapter(Context context, List<placedorder> receiptList,String na,String num,String re,String area) {
        this.context = context;
        this.receiptList = receiptList;
        this.name=na;
        this.number=num;
        this.renum=re;
        this.Area=area;
    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_receipt, parent, false);
        return new ReceiptViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {
        placedorder receipt = receiptList.get(position);

        holder.txtReceiptNumber.setText("Receipt ID: "+renum);
        holder.txtName.setText("Delivery Name: " + name);
        holder.txtNumber.setText("Delivery Number: " + number);
        holder.txtCustomerName.setText("Customer Name: " + receipt.getCustomerName());
        holder.txtMobileNo.setText("Customer Mobile Number: " + receipt.getMobileNo());
        holder.txtUserId.setText("Area: "+Area);
        holder.txtCoach.setText("Coach: " + receipt.getCoach());
        holder.txtSeatNumber.setText("Seat Number: " + receipt.getSeatNumber());
        holder.txtTrainNo.setText("Train Number: " + receipt.gettrainno());
        holder.txtEta.setText("ETA: " + receipt.geteta());
        holder.txtRestaurant.setText("Restaurant: " + receipt.getRestaurant());
        holder.txtDishes.setText("Dishes: " + receipt.getdishes());
        holder.txtTotalPrice.setText("Total Price: " + receipt.getTotalPrice());
        holder.txtPayment.setText("Payment: " + receipt.getPayment());



    }

    @Override
    public int getItemCount() {
        return receiptList.size();
    }

    public static class ReceiptViewHolder extends RecyclerView.ViewHolder {
        public TextView txtReceiptNumber;
        public TextView txtName;
        public TextView txtNumber;
        public TextView txtCoach;
        public TextView txtCustomerName;
        public TextView txtDishes;
        public TextView txtEta;
        public TextView txtMobileNo;
        public TextView txtPayment;
        public TextView txtRestaurant;
        public TextView txtSeatNumber;
        public TextView txtTotalPrice;
        public TextView txtTrainNo;
        public TextView txtUserId;

        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReceiptNumber = itemView.findViewById(R.id.txtReceiptNumber);
            txtName = itemView.findViewById(R.id.txtName);
            txtNumber = itemView.findViewById(R.id.txtNumber);
            txtCoach = itemView.findViewById(R.id.txtCoach);
            txtCustomerName = itemView.findViewById(R.id.txtCustomerName);
            txtDishes = itemView.findViewById(R.id.txtDishes);
            txtEta = itemView.findViewById(R.id.txtEta);
            txtMobileNo = itemView.findViewById(R.id.txtMobileNo);
            txtPayment = itemView.findViewById(R.id.txtPayment);
            txtRestaurant = itemView.findViewById(R.id.txtRestaurant);
            txtSeatNumber = itemView.findViewById(R.id.txtSeatNumber);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            txtTrainNo = itemView.findViewById(R.id.txtTrainNo);
            txtUserId = itemView.findViewById(R.id.txtUserId);
        }
    }
}



