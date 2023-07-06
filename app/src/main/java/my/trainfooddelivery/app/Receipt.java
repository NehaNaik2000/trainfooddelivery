package my.trainfooddelivery.app;

public class Receipt {
    private String receiptNumber;
    private String name;
    private String number;
    private String coach;
    private String customerName;
    private String dishes;
    private String eta;
    private String mobileNo;
    private String payment;
    private String restaurant;
    private String seatNumber;
    private String totalPrice;
    private String trainNo;
    private String userId;

    public Receipt() {
        // Default constructor required for Firebase
    }

    public Receipt(String receiptNumber, String name, String number, String coach, String customerName,
                   String dishes, String eta, String mobileNo, String payment, String restaurant,
                   String seatNumber, String totalPrice, String trainNo, String userId) {
        this.receiptNumber = receiptNumber;
        this.name = name;
        this.number = number;
        this.coach = coach;
        this.customerName = customerName;
        this.dishes = dishes;
        this.eta = eta;
        this.mobileNo = mobileNo;
        this.payment = payment;
        this.restaurant = restaurant;
        this.seatNumber = seatNumber;
        this.totalPrice = totalPrice;
        this.trainNo = trainNo;
        this.userId = userId;
    }

    // Getters and setters for all fields

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDishes() {
        return dishes;
    }

    public void setDishes(String dishes) {
        this.dishes = dishes;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

