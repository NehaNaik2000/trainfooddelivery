package my.trainfooddelivery.app;

public class UpdateDishModel {

    String Dishes,RandomUID,Description,Quantity,Price,ImageURL,Restaurant,eta,name,mobile,trainno;
   private boolean isCheckedAvailability;

    // Press Alt+insert
      public UpdateDishModel()
      {

      }
    public UpdateDishModel(String  dish,String desc,String randomuid,String quantity,String price,String image,String restaurant,String eta,boolean ischecked,String name, String mobile,String trainno){
        Dishes=dish;
        RandomUID=randomuid;
        Description=desc;
        Quantity=quantity;
        Price=price;
        ImageURL=image;
        Restaurant=restaurant;
        eta=eta;
        isCheckedAvailability=ischecked;
        name=name;
        mobile=mobile;
        trainno=trainno;
    }

    public String getDishes() {
        return Dishes;
    }

    public void setDishes(String dishes) {
        Dishes = dishes;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public void setRestaurant(String restaurant){  Restaurant=restaurant;}

    public String getRestaurant(){ return Restaurant ;}

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getEta() {
        return eta;
    }

    public boolean getAvailability() {
        return isCheckedAvailability;
    }

    public void setAvailability(boolean isCheckedAvailability) {
        this.isCheckedAvailability = isCheckedAvailability;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrainno() {
        return trainno;
    }

    public void setTrainno(String trainno) {
        this.trainno = trainno;
    }
}
