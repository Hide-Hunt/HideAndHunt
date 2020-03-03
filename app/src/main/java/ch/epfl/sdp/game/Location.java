package ch.epfl.sdp.game;

public class Location {
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double distanceTo(Location other) {
        return Math.abs(Math.sqrt(
                Math.pow(this.latitude - other.latitude, 2) + Math.pow(this.longitude - other.longitude, 2)
        ));
    }
}
