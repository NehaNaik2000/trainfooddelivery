package my.trainfooddelivery.app;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class placedorder {
        private String dishes;
        private String price;
        private String quantity;
        private String eta;
        private String trainno;
        private String CustomerName;
        private String MobileNo;
        private String Totalprice;
    private String SeatNumber;
    private String Coach;
    private String Restaurant;
    private String Phone;
    private String Userid;
         private List<placedorder>DishList;
         private  String Payment;
    private  Boolean Deliveryperson;
    private String customerToken;
    private String deliveryPersonToken;


        public placedorder() {
            // Default constructor required for calls to DataSnapshot.getValue(Order.class)
        }

        public placedorder(String dishName, String price, String quantity, String eta,String name,String mobile, String trainno,String totalprice,String seatnumber,
     String coach,String rname,String phone,String userid,String pay,Boolean person) {
            this.dishes = dishName;
            this.price = price;
            this.quantity = quantity;
            this.eta = eta;
            this.trainno = trainno;
            this.CustomerName=name;
            this.MobileNo=mobile;
            this.Totalprice=totalprice;
            this.DishList = new ArrayList<>();
            this.DishList.add(this);
            this.SeatNumber=seatnumber;
            this.Coach=coach;
            this.Restaurant=rname;
            this.Phone=phone;
            this.Userid=userid;
            this.Payment=pay;
            this.Deliveryperson=person;
        }

        public void setprice(String price )
        {
            this.price=price;
        }

        public String getprice()
        {
            return price;
        }
        public void seteta(String eta) {
            this.eta = eta;
        }

        public void setquantity(String quantity) {
            this.quantity = quantity;
        }

        public void settraino(String trainno) {
            this.trainno = trainno;
        }

        public String getdishes() {
            return dishes;
        }

        public void setdishes(String dishes) {
            this.dishes = dishes;
        }

        public String geteta() {
            return eta;
        }

        public String getquantity() {
            return quantity;
        }

        public String gettrainno() {
            return trainno;
        }

        public String getCustomerName(){
            return CustomerName;
        }
         public String getMobileNo()
         {
             return MobileNo;
         }

        public void setMobileNo(String mobileNo) {
            MobileNo = mobileNo;
        }

        public void setCustomerName(String customerName) {
            CustomerName = customerName;
        }

        public void setDishList(List<placedorder> existingDishList) {
        }

        public List<placedorder>getDishList()
        {
            return DishList;
        }


    public void setTotalPrice(String totalprice) {
            Totalprice=totalprice;
    }

    public String getTotalPrice() {
        return Totalprice;
    }

    public String getCoach() {
        return Coach;
    }

    public String getSeatNumber() {
        return SeatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        SeatNumber = seatNumber;
    }

    public void setCoach(String coach) {
        Coach = coach;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setRestaurant(String restaurant) {
        Restaurant = restaurant;
    }

    public String getRestaurant() {
        return Restaurant;
    }

    public String getPhone() {
        return Phone;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getUserid() {
        return Userid;
    }

    public String getPayment()
    {
        return Payment;
    }

    public void setPayment(String payment) {
        this.Payment = payment;
    }

    public void setDeliveryperson(Boolean deliveryperson) {
        Deliveryperson = deliveryperson;
    }

    public Boolean getDeliveryperson() {
        return Deliveryperson;
    }

//    public Context getCustomerToken() { return customerToken; }
//
//    public void setCustomerToken(String customerToken) { this.customerToken = customerToken; }
//
//    public String getDeliveryPersonToken() {
//        return deliveryPersonToken;
//    }
//
//    public void setDeliveryPersonToken(String deliveryPersonToken) {
//        this.deliveryPersonToken = deliveryPersonToken;
//    }
}


