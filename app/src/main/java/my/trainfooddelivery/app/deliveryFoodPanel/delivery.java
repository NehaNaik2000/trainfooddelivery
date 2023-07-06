package my.trainfooddelivery.app.deliveryFoodPanel;

public class delivery {

    private String State,Area,FirstName,LastName,MobileNo;

    public delivery(){
    }
    // Press Alt+insert


    public delivery( String state, String area,String FirstName,String LastName,String mo) {

        State = state;
        Area = area;
        FirstName=FirstName;
        LastName=LastName;
        MobileNo=mo;



    }



    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getMobileNo() {
        return MobileNo;
    }
}