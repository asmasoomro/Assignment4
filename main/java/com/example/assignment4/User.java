package com.example.assignment4;

public class User {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String address;
    private int purchaseCounter;
    private static User instance;


    public User(){

    }
    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public User(String firstName, String lastName, String password, String email,String address, int purchaseCounter) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.purchaseCounter=purchaseCounter;
    }


    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPurchaseCounter() {return purchaseCounter;}

    public void setPurchaseCounter(int purchaseCounter) {
        this.purchaseCounter= purchaseCounter;
    }

}
