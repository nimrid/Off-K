package com.gocheck.com.off_k.Modal;

public class LikedAccom {
    private String pId, Category ,Date, Time,Description,Location,Rent, Contact, Rated, Image;

    public LikedAccom(){}

    public LikedAccom(String pId, String category, String date, String time, String description,
                      String location, String rent, String contact, String rated, String image) {
        this.pId = pId;
        Category = category;
        Date = date;
        Time = time;
        Description = description;
        Location = location;
        Rent = rent;
        Contact = contact;
        Rated = rated;
        Image = image;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getRent() {
        return Rent;
    }

    public void setRent(String rent) {
        Rent = rent;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
