package com.gocheck.com.off_k.Modal;

public class Accommodation {
    private String pId, Category, Date, Time, PropertyName, Description, ImageUri, Location, Rent, Contact, ProductStatus;

    public Accommodation(){

    }

    public Accommodation(String pId, String category, String date, String time, String propertyName,
                         String description, String imageUri, String location, String rent, String contact, String productStatus) {
        this.pId = pId;
        Category = category;
        Date = date;
        Time = time;
        PropertyName = propertyName;
        Description = description;
        ImageUri = imageUri;
        Location = location;
        Rent = rent;
        Contact = contact;
        ProductStatus = productStatus;
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

    public String getPropertyName() {
        return PropertyName;
    }

    public void setPropertyName(String propertyName) {
        PropertyName = propertyName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
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

    public String getProductStatus() {
        return ProductStatus;
    }

    public void setProductStatus(String productStatus) {
        ProductStatus = productStatus;
    }
}
