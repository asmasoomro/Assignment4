package com.example.assignment4;

public class CurrentUser {
    private static CurrentUser user;
    private User currentUser;

    private CurrentUser(){
    }

    public static CurrentUser getInstance(){
        if(user ==null){
            user = new CurrentUser();
        }
        return user;
    }
    public User getCurrentUser(){
        return currentUser;
    }
    public void setCurrentUser(User currentUser){

        this.currentUser=currentUser;
    }
}