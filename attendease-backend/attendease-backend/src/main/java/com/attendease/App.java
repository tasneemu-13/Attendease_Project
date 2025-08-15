package com.attendease;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.attendease.controllers.AdminController;

import java.sql.Connection;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class App {
    public static void main(String[] args) {
           try{
               Connection conn = com.attendease.utils.DatabaseConnection.getConnection();
               System.out.println("Connection Established!"+(conn!=null));
               AdminController controller = new AdminController(conn);
               controller.adminMenu();
               System.out.println("Successfull run");

           }catch(Exception e){
               e.printStackTrace();
           }
           String password = "admin123";
           String hashedPassword = BCrypt.withDefaults().hashToString(12,password.toCharArray());
           System.out.println("The BCrypt hash for 'admin123' is:"+hashedPassword);


    }
}