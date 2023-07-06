package my.trainfooddelivery.app.deliveryFoodPanel;

public class LocationData {

    private  double latitude;
    private  double longtitude;
    private long  timestamp;
    private String name;




    public LocationData( double lat, double  longtitude,long timestamp,String name)
        {
            this.latitude=lat;
            this.longtitude=longtitude;
            this.timestamp=timestamp;
            this.name=name;
        }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}



