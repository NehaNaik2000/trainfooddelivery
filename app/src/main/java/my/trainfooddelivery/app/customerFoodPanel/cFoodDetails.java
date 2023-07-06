package my.trainfooddelivery.app.customerFoodPanel;

public class cFoodDetails {
    public String Chefid,RandomUID,ImageURL;
    // Alt+insert

    public cFoodDetails(String randomUID, String chefid,String imageURL) {
        ImageURL = imageURL;
        RandomUID = randomUID;
        Chefid = chefid;
    }
}
