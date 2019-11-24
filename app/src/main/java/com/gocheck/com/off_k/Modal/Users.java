package com.gocheck.com.off_k.Modal;

public class Users {
    private String Name, PhoneNumber, Password, Image, Address;

    public Users(){

    }

    public Users(String name, String phoneNumber, String password, String image, String address) {
        Name = name;
        PhoneNumber = phoneNumber;
        Password = password;
        Image = image;
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
