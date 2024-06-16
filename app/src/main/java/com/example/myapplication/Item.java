package com.example.myapplication;

public class Item {

    private Long id;
    private String name;
    private String distance;
    private String latitude;
    private String longitude;
    private String adress;


    public Item(String name, String distance, Long id, String latitude, String longitude) {
        this.name = name;
        this.distance = distance;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getParseDistance() {
        String number = this.distance.replaceAll("[^0-9]", ""); // Remove non-numeric characters
        return Integer.parseInt(number);
    }
}
