package com.attendease.models;

public class User {

   private Integer userId;

   private String emailId;

   private String password;

   private String role;


   //default constructor

   public User(){}

   //parametarized constructor

  public User(Integer userId, String emailId , String password , String role){
       this.userId = userId;
       this.emailId = emailId;
       this.password = password;
       this.role = role;
   }

   //getters
   public Integer getUserId(){
       return userId;
   }
   public String getEmailId(){
       return emailId;
   }
   public String getPassword(){
       return password;
   }
   public String getRole(){
       return role;
   }

   //setters
   public void setUserId(Integer userId){
       this.userId = userId;
   }
   public void setEmailId(String emailId){
       this.emailId = emailId;
   }
   public void setPassword(String password){
       this.password = password;
   }

    public void setRole(String role) {
        this.role = role;
    }

}
