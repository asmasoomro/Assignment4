package com.example.assignment4;

import com.google.firebase.database.Exclude;

public class Upload {

    private static Upload instance;
    private String name;
    private String imageUrl;
    private String category;
    private String manufacturer;
    private String stock;
    private String itemKey;
    private String price;
    private String originalItemKey;
    private String quantity;

    public Upload(){}

    public static Upload getInstance() {
        if (instance == null) {
            instance = new Upload();
        }
        return instance;
    }

    public Upload(String name, String imageUrl,String category,String manufacturer, String stock,String price){
        if(name.trim().equals("")){
            name="Empty";
        }

        this.name=name;
        this.imageUrl=imageUrl;
        this.manufacturer=manufacturer;
        this.category=category;
        this.stock=stock;
        this.price =price;
    }

    public Upload(String name, String imageUrl, String category, String manufacturer, String stock, String price , String quantity) {
        this.name=name;
        this.imageUrl=imageUrl;
        this.manufacturer=manufacturer;
        this.category=category;
        this.stock=stock;
        this.price =price;
        this.quantity=quantity;
    }


    public String getName(){
        return name;

    }
    public void setName(String name){
        this.name=name;
    }  public String getQuantity(){
        return quantity;

    }
    public void setQuantity(String quantity){
        this.quantity=quantity;
    }

    public String getImageUrl(){
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {

        this.imageUrl=imageUrl;
    }
    public String getManufacturer(){
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer=manufacturer;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(String category) {
        this.category=category;
    }
    public String getStock(){
        return stock;
    }
    public void setStock(String stock) {
        this.stock=stock;
    }

    public String getPrice(){
        return price;
    }
    public void setPrice(String price){
        this.price=price;
    }
    @Exclude
    public String getKey(){
        return itemKey;
    }
    @Exclude
    public void setKey(String Key) {
        this.itemKey=Key;
    }
    public String getOriginalItemKey() {
        return originalItemKey;
    }

    public void setOriginalItemKey(String originalItemKey) {
        this.originalItemKey = originalItemKey;
    }



}