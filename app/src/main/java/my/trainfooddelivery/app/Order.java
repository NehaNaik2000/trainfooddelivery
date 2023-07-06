package my.trainfooddelivery.app;

public class Order {
    private String dishName;
    private String price;
    private String quantity;
    private long timestamp;
    private String ImageURL;
    private String mEta;
    private String trainno;
    private String restaurant;


    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String dishName, String price, String quantity,long timestamp,String image,String restaurant,String mEta,String trainno) {
        this.dishName = dishName;
        this.price = price;
        this.quantity = quantity;
        this.timestamp=timestamp;
        this.ImageURL=image;
        this.restaurant=restaurant;
        this.mEta=mEta;
        this.trainno=trainno;
    }

    public String getDishName() {
        return dishName;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public long getTimestamp(){return timestamp;}

    public String getImageURL(){return ImageURL;}

    public String getRestaurant(){return restaurant;}

    public String  getmEta(){return mEta;}

    public String getTrainno() {
        return trainno;
    }
}

