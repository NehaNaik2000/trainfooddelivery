package my.trainfooddelivery.app.ChefFoodPanel;

public class Chef {
    private String Area,  ConfirmPassword, Emailid, Restaurant, House, Lname, Mobile, Password, Postcode, State;

    // Press Alt+Insert


    public Chef(String area, String confirmPassword, String emailid, String rest, String house, String lname, String mobile, String password, String postcode, String state) {
        this.Area = area;

        ConfirmPassword = confirmPassword;
        Emailid = emailid;
        Restaurant = rest;
        House = house;
        Lname = lname;
        Mobile = mobile;
        Password = password;
        Postcode = postcode;
        State = state;
    }
    public Chef() {
    }

    public String getArea() {
        return Area;
    }


    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public String getEmailid() {
        return Emailid;
    }

    public String getRestaurant() {
        return Restaurant;
    }

    public String getHouse() {
        return House;
    }

    public String getLname() {
        return Lname;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getPassword() {
        return Password;
    }

    public String getPostcode() {
        return Postcode;
    }

    public String getState() {
        return State;
    }

}
